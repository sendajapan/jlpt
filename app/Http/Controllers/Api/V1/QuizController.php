<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\AppUser;
use App\Models\AppUserCategoryUnlock;
use App\Models\AppUserWordUnlock;
use App\Models\VocabCategory;
use App\Models\VocabSubcategory;
use App\Models\Vocabulary;
use App\Services\CoinService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Collection;
use Illuminate\Support\Facades\Storage;

class QuizController extends Controller
{
    public function __construct(private readonly CoinService $coins) {}

    public function generate(Request $request): JsonResponse
    {
        $user = $request->user('app_users');

        [$unlockedWordIds, $categoryUnlocks] = $this->loadUserUnlocks($user);

        $words = Vocabulary::query()
            ->where('is_approved', true)
            ->with(['subcategory.category'])
            ->inRandomOrder()
            ->get()
            ->filter(fn (Vocabulary $v) => ! $this->isLocked($v, $user, $unlockedWordIds, $categoryUnlocks))
            ->take(10)
            ->values();

        if ($words->count() < 4) {
            return response()->json(['data' => []]);
        }

        $questions = $words->map(fn (Vocabulary $word) => $this->buildQuestion($word, $words));

        return response()->json(['data' => $questions]);
    }

    public function complete(Request $request): JsonResponse
    {
        $data = $request->validate([
            'score' => 'required|integer|min:0|max:10',
            'total' => 'required|integer|min:1|max:10',
        ]);

        $user = $request->user('app_users');
        $coinsEarned = $data['score'] * 10;
        $xpEarned = $data['score'] * 5;

        $this->coins->award($user, $coinsEarned, 'quiz_completion');

        $user->newQuery()->whereKey($user->id)->increment('xp', $xpEarned);
        $user->refresh();

        return response()->json([
            'coins_earned' => $coinsEarned,
            'xp_earned' => $xpEarned,
            'total_coins' => $user->coins,
            'total_xp' => $user->xp,
        ]);
    }

    private function buildQuestion(Vocabulary $word, Collection $pool): array
    {
        $type = $this->pickType($word);
        $correctAnswer = $type === 'text' ? $word->word_en : ($word->word_jp ?: $word->word_romaji);

        $distractors = $pool
            ->where('id', '!=', $word->id)
            ->shuffle()
            ->take(3)
            ->map(fn (Vocabulary $w) => $type === 'text' ? $w->word_en : ($w->word_jp ?: $w->word_romaji))
            ->values()
            ->all();

        $options = collect([$correctAnswer, ...$distractors])->shuffle()->values()->all();

        return [
            'type' => $type,
            'word' => [
                'id' => $word->id,
                'word_jp' => $word->word_jp,
                'word_romaji' => $word->word_romaji,
                'word_en' => $word->word_en,
                'image_url' => $this->url($word->image_path),
                'audio_jp_url' => $this->url($word->audio_jp),
                'audio_en_url' => $this->url($word->audio_en),
                'sentence_audio_jp_url' => $this->url($word->sentence_audio_jp),
                'sentence_audio_en_url' => $this->url($word->sentence_audio_en),
            ],
            'correct_answer' => $correctAnswer,
            'options' => $options,
        ];
    }

    private function pickType(Vocabulary $word): string
    {
        $types = ['text'];
        if ($word->image_path) {
            $types[] = 'image';
        }
        if ($word->audio_jp) {
            $types[] = 'audio';
        }

        shuffle($types);

        return $types[0];
    }

    private function loadUserUnlocks(?AppUser $user): array
    {
        if (! $user) {
            return [collect(), collect()];
        }

        $unlockedWordIds = AppUserWordUnlock::where('app_user_id', $user->id)->pluck('vocab_id')->flip();
        $categoryUnlocks = AppUserCategoryUnlock::where('app_user_id', $user->id)->get();

        return [$unlockedWordIds, $categoryUnlocks];
    }

    private function isLocked(Vocabulary $v, ?AppUser $user, Collection $unlockedWordIds, Collection $categoryUnlocks): bool
    {
        if (! $user) {
            return $v->is_premium
                || ($v->subcategory?->is_premium ?? false)
                || ($v->subcategory?->category?->is_premium ?? false);
        }

        if ($unlockedWordIds->has($v->id)) {
            return false;
        }

        return ! $categoryUnlocks->contains(function (AppUserCategoryUnlock $u) use ($v) {
            if ($u->unlockable_type === VocabSubcategory::class && $u->unlockable_id === $v->vocab_subcategory_id) {
                return true;
            }

            if ($u->unlockable_type === VocabCategory::class && $u->unlockable_id === ($v->subcategory?->vocab_category_id)) {
                return true;
            }

            return false;
        });
    }

    private function url(?string $path): ?string
    {
        return $path ? Storage::disk('public')->url($path) : null;
    }
}
