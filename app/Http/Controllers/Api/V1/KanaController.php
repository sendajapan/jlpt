<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Kana;
use App\Models\Vocabulary;
use App\Services\KanaService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class KanaController extends Controller
{
    public function __construct(private KanaService $service) {}

    public function index(Request $request): JsonResponse
    {
        $validated = $request->validate([
            'script' => 'required|in:hiragana,katakana',
        ]);

        $user = $request->user('app_users');
        $learnedIds = $user ? collect($this->service->learnedKanaIds($user))->flip() : collect();

        $kanas = $this->service->getByScript($validated['script'])
            ->map(fn (Kana $kana) => $this->formatKana($kana, $learnedIds->has($kana->id)));

        return response()->json(['data' => $kanas]);
    }

    public function show(Request $request, Kana $kana): JsonResponse
    {
        $user = $request->user('app_users');
        $learned = $user ? $user->learnedKanas()->whereKey($kana->id)->exists() : false;

        $words = $this->service->exampleWords($kana)
            ->map(fn (Vocabulary $word) => [
                'id' => $word->id,
                'word_jp' => $word->word_jp,
                'word_romaji' => $word->word_romaji,
                'word_en' => $word->word_en,
                'audio_jp_url' => $this->url($word->audio_jp),
                'image_thumbnail_url' => $this->url($word->image_thumbnail_path),
                'image_thumbnail_bg' => $word->image_thumbnail_bg,
            ]);

        return response()->json([
            'data' => $this->formatKana($kana, $learned) + ['example_words' => $words],
        ]);
    }

    private function formatKana(Kana $kana, bool $learned): array
    {
        return [
            'id' => $kana->id,
            'script' => $kana->script,
            'kana' => $kana->kana,
            'romaji' => $kana->romaji,
            'sound_url' => $this->url($kana->sound_url),
            'section' => $kana->section,
            'position' => $kana->position,
            'is_learned' => $learned,
        ];
    }

    private function url(?string $path): ?string
    {
        return $path ? Storage::disk('public')->url($path) : null;
    }
}
