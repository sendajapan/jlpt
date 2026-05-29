<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Vocabulary;
use App\Services\WordReadService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use OpenApi\Attributes as OA;

class WordReadController extends Controller
{
    public function __construct(private WordReadService $reads)
    {
    }

    #[OA\Get(
        path: '/api/v1/me/reads',
        tags: ['Reads'],
        summary: 'List vocab words the authenticated user has marked read (paginated)',
        security: [['sanctum' => []]],
        responses: [new OA\Response(response: 200, description: 'OK')]
    )]
    public function index(Request $request): JsonResponse
    {
        return response()->json(
            $request->user()->reads()->with('vocabulary')->orderByDesc('last_read_at')->paginate(20)
        );
    }

    #[OA\Post(
        path: '/api/v1/words/{vocabulary}/read',
        tags: ['Reads'],
        summary: 'Mark a vocab word as read (first read awards coins)',
        security: [['sanctum' => []]],
        parameters: [new OA\Parameter(name: 'vocabulary', in: 'path', required: true, schema: new OA\Schema(type: 'integer'))],
        responses: [
            new OA\Response(
                response: 201,
                description: 'Marked read',
                content: new OA\JsonContent(
                    properties: [
                        new OA\Property(property: 'read', type: 'object'),
                        new OA\Property(property: 'coins', type: 'integer'),
                    ]
                )
            ),
        ]
    )]
    public function store(Request $request, Vocabulary $vocabulary): JsonResponse
    {
        $read = $this->reads->markRead($request->user(), $vocabulary);

        return response()->json([
            'read' => $read,
            'coins' => (int) $request->user()->refresh()->coins,
        ], 201);
    }
}
