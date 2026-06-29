<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Kanji;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class KanjiController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $kanjis = Kanji::query()
            ->when($request->filled('search'), fn ($q) => $q->where(fn ($w) => $w
                ->where('kanji', 'like', '%'.$request->string('search').'%')
                ->orWhere('translate', 'like', '%'.$request->string('search').'%')
                ->orWhere('meanings', 'like', '%'.$request->string('search').'%')
            ))
            ->when($request->filled('jlpt'), fn ($q) => $q->where('jlpt', $request->string('jlpt')))
            ->when($request->filled('level'), fn ($q) => $q->where('level', $request->integer('level')))
            ->orderBy('id')
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
