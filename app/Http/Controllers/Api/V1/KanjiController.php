<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Kanji;
use App\Services\KanjiService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class KanjiController extends Controller
{
    public function __construct(private KanjiService $service) {}

    public function index(Request $request): JsonResponse
    {
        $filters = $request->only(['search', 'jlpt', 'level', 'is_premium']);

        $kanjis = $this->service->getAll($filters)
            ->get()
            ->map(fn (Kanji $k) => [
                'id' => $k->id,
                'kanji' => $k->kanji,
                'strokes' => $k->strokes,
                'grade' => $k->grade,
                'freq' => $k->freq,
                'jlpt' => $k->jlpt,
                'translate' => $k->translate,
                'meanings' => $k->meanings,
                'readings_on' => $k->readings_on,
                'readings_kun' => $k->readings_kun,
                'level' => $k->level,
                'radicals' => $k->radicals,
                'vocab_id' => $k->vocab_id,
                'is_premium' => $k->is_premium,
            ]);

        return response()->json(['data' => $kanjis]);
    }
}
