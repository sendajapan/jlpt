<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Kanji;
use App\Services\KanjiService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class KanjiController extends Controller
{
    public function __construct(private KanjiService $service) {}

    public function index(Request $request): JsonResponse
    {
        $request->validate([
            'jlpt' => ['sometimes', 'in:N5,N4,N3,N2,N1'],
            'strokes' => ['sometimes', 'integer', 'min:1'],
            'page' => ['sometimes', 'integer', 'min:1'],
            'per_page' => ['sometimes', 'integer', 'min:1', 'max:200'],
        ]);

        $user = $request->user('app_users');
        $learnedIds = $user ? collect($this->service->learnedKanjiIds($user))->flip() : collect();

        $filters = $request->only(['search', 'jlpt', 'level', 'strokes', 'is_premium']);
        $page = $this->service->getAll($filters)->paginate((int) $request->input('per_page', 60));

        return response()->json([
            'data' => collect($page->items())
                ->map(fn (Kanji $kanji) => $this->formatKanji($kanji, $learnedIds->has($kanji->id))),
            'meta' => [
                'current_page' => $page->currentPage(),
                'last_page' => $page->lastPage(),
                'per_page' => $page->perPage(),
                'total' => $page->total(),
            ],
        ]);
    }

    public function show(Request $request, Kanji $kanji): JsonResponse
    {
        $user = $request->user('app_users');
        $learned = $user ? $user->learnedKanjis()->whereKey($kanji->id)->exists() : false;

        $words = collect([$kanji->vocab])
            ->filter()
            ->map(fn ($word) => [
                'id' => $word->id,
                'word_jp' => $word->word_jp,
                'word_romaji' => $word->word_romaji,
                'word_en' => $word->word_en,
                'audio_jp_url' => $this->url($word->audio_jp),
                'image_thumbnail_url' => $this->url($word->image_thumbnail_path),
                'image_thumbnail_bg' => $word->image_thumbnail_bg,
            ])
            ->values();

        return response()->json([
            'data' => $this->formatKanji($kanji, $learned) + ['example_words' => $words],
        ]);
    }

    public function strokes(): JsonResponse
    {
        return response()->json(['data' => $this->service->strokeCounts()]);
    }

    private function formatKanji(Kanji $kanji, bool $learned): array
    {
        return [
            'id' => $kanji->id,
            'kanji' => $kanji->kanji,
            'strokes' => $kanji->strokes,
            'grade' => $kanji->grade,
            'freq' => $kanji->freq,
            'jlpt' => $kanji->jlpt,
            'translate' => $kanji->translate,
            'meanings' => $kanji->meanings,
            'readings_on' => $kanji->readings_on,
            'readings_kun' => $kanji->readings_kun,
            'level' => $kanji->level,
            'radicals' => $kanji->radicals,
            'vocab_id' => $kanji->vocab_id,
            'is_premium' => $kanji->is_premium,
            'is_learned' => $learned,
        ];
    }

    private function url(?string $path): ?string
    {
        return $path ? Storage::disk('public')->url($path) : null;
    }
}
