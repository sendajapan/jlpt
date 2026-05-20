<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Vocabulary;
use App\Services\WordReadService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class WordReadController extends Controller
{
    public function __construct(private WordReadService $reads)
    {
    }

    public function index(Request $request): JsonResponse
    {
        return response()->json(
            $request->user()->reads()->with('vocabulary')->orderByDesc('last_read_at')->paginate(20)
        );
    }

    public function store(Request $request, Vocabulary $vocabulary): JsonResponse
    {
        $read = $this->reads->markRead($request->user(), $vocabulary);

        return response()->json([
            'read' => $read,
            'coins' => (int) $request->user()->refresh()->coins,
        ], 201);
    }
}
