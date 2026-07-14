<?php

namespace App\Services;

use App\Models\AppUser;
use App\Models\Kanji;
use Illuminate\Database\Eloquent\Builder;
use Illuminate\Support\Collection;
use Illuminate\Support\Facades\DB;

class KanjiService
{
    public function __construct(private CoinService $coins) {}

    public function getAll(array $filters = []): Builder
    {
        return Kanji::query()
            ->when($filters['search'] ?? null, fn ($q, $s) => $q->where(fn ($w) => $w
                ->where('kanji', 'like', "%{$s}%")
                ->orWhere('translate', 'like', "%{$s}%")
                ->orWhere('meanings', 'like', "%{$s}%")
            ))
            ->when($filters['jlpt'] ?? null, fn ($q, $v) => $q->where('jlpt', $v))
            ->when($filters['level'] ?? null, fn ($q, $v) => $q->where('level', $v))
            ->when($filters['strokes'] ?? null, fn ($q, $v) => $q->where('strokes', $v)->whereNotNull('jlpt'))
            ->when(isset($filters['is_premium']) && $filters['is_premium'] !== '', fn ($q) => $q->where('is_premium', $filters['is_premium']))
            ->orderBy('id');
    }

    public function strokeCounts(): Collection
    {
        return Kanji::whereNotNull('jlpt')
            ->selectRaw('strokes, count(*) as count')
            ->groupBy('strokes')
            ->orderBy('strokes')
            ->get()
            ->map(fn (Kanji $row) => ['strokes' => $row->strokes, 'count' => (int) $row->count]);
    }

    public function learnedKanjiIds(AppUser $user): array
    {
        return $user->learnedKanjis()->pluck('kanjis.id')->all();
    }

    public function markLearned(AppUser $user, Kanji $kanji): bool
    {
        return DB::transaction(function () use ($user, $kanji) {
            $result = $user->learnedKanjis()->syncWithoutDetaching([$kanji->id]);
            $firstTime = count($result['attached']) > 0;

            $reward = (int) config('services.app_rewards.coins_per_kanji_learned', 10);
            if ($firstTime && $reward > 0) {
                $this->coins->award($user, $reward, 'kanji_learned', $kanji);
            }

            return $firstTime;
        });
    }

    public function create(array $data): Kanji
    {
        return Kanji::create($data);
    }

    public function update(Kanji $kanji, array $data): Kanji
    {
        $kanji->update($data);

        return $kanji;
    }

    public function delete(Kanji $kanji): void
    {
        $kanji->delete();
    }
}
