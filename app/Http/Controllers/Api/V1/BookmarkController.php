<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Vocabulary;
use App\Services\BookmarkService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use OpenApi\Attributes as OA;

class BookmarkController extends Controller
{
    public function __construct(private BookmarkService $bookmarks) {}

    #[OA\Get(
        path: '/api/v1/bookmarks',
        tags: ['Bookmarks'],
        summary: "List the authenticated user's bookmarked vocab words (paginated)",
        security: [['sanctum' => []]],
        responses: [new OA\Response(response: 200, description: 'OK')]
    )]
    public function index(Request $request): JsonResponse
    {
        return response()->json($this->bookmarks->list($request->user()));
    }

    #[OA\Post(
        path: '/api/v1/bookmarks/{vocabulary}',
        tags: ['Bookmarks'],
        summary: 'Add a vocab word to bookmarks',
        security: [['sanctum' => []]],
        parameters: [new OA\Parameter(name: 'vocabulary', in: 'path', required: true, schema: new OA\Schema(type: 'integer'))],
        responses: [
            new OA\Response(response: 201, description: 'Added'),
            new OA\Response(response: 404, description: 'Vocab not found'),
        ]
    )]
    public function store(Request $request, Vocabulary $vocabulary): JsonResponse
    {
        $this->bookmarks->add($request->user(), $vocabulary);

        return response()->json(['message' => 'Added to bookmarks.'], 201);
    }

    #[OA\Delete(
        path: '/api/v1/bookmarks/{vocabulary}',
        tags: ['Bookmarks'],
        summary: 'Remove a vocab word from bookmarks',
        security: [['sanctum' => []]],
        parameters: [new OA\Parameter(name: 'vocabulary', in: 'path', required: true, schema: new OA\Schema(type: 'integer'))],
        responses: [new OA\Response(response: 200, description: 'Removed')]
    )]
    public function destroy(Request $request, Vocabulary $vocabulary): JsonResponse
    {
        $this->bookmarks->remove($request->user(), $vocabulary);

        return response()->json(['message' => 'Removed from bookmarks.']);
    }
}
