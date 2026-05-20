<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Vocabulary;
use App\Services\FavoriteService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class FavoriteController extends Controller
{
    public function __construct(private FavoriteService $favorites)
    {
    }

    public function index(Request $request): JsonResponse
    {
        return response()->json($this->favorites->list($request->user()));
    }

    public function store(Request $request, Vocabulary $vocabulary): JsonResponse
    {
        $this->favorites->add($request->user(), $vocabulary);

        return response()->json(['message' => 'Added to favorites.'], 201);
    }

    public function destroy(Request $request, Vocabulary $vocabulary): JsonResponse
    {
        $this->favorites->remove($request->user(), $vocabulary);

        return response()->json(['message' => 'Removed from favorites.']);
    }
}
