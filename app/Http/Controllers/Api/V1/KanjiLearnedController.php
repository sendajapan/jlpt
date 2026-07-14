<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Kanji;
use App\Services\KanjiService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class KanjiLearnedController extends Controller
{
    public function __construct(private KanjiService $service) {}

    public function index(Request $request): JsonResponse
    {
        return response()->json([
            'data' => $this->service->learnedKanjiIds($request->user()),
        ]);
    }

    public function store(Request $request, Kanji $kanji): JsonResponse
    {
        $firstTime = $this->service->markLearned($request->user(), $kanji);

        return response()->json([
            'learned' => true,
            'first_time' => $firstTime,
            'coins' => (int) $request->user()->refresh()->coins,
        ], 201);
    }
}
