<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Kana;
use App\Services\KanaService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class KanaLearnedController extends Controller
{
    public function __construct(private KanaService $service) {}

    public function index(Request $request): JsonResponse
    {
        return response()->json([
            'data' => $this->service->learnedKanaIds($request->user()),
        ]);
    }

    public function store(Request $request, Kana $kana): JsonResponse
    {
        $firstTime = $this->service->markLearned($request->user(), $kana);

        return response()->json([
            'learned' => true,
            'first_time' => $firstTime,
            'coins' => (int) $request->user()->refresh()->coins,
        ], 201);
    }
}
