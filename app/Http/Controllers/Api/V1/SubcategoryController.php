<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\VocabSubcategory;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use OpenApi\Attributes as OA;

class SubcategoryController extends Controller
{
    #[OA\Get(
        path: '/api/v1/subcategories',
        summary: 'List subcategories, optionally filtered by category',
        tags: ['Public'],
        parameters: [
            new OA\Parameter(
                name: 'category_id',
                in: 'query',
                required: false,
                description: 'Filter by parent category ID',
                schema: new OA\Schema(type: 'integer', example: 1)
            ),
        ],
        responses: [
            new OA\Response(
                response: 200,
                description: 'List of subcategories ordered by sort_order then name_en',
                content: new OA\JsonContent(
                    properties: [
                        new OA\Property(
                            property: 'data',
                            type: 'array',
                            items: new OA\Items(ref: '#/components/schemas/VocabSubcategory')
                        ),
                    ]
                )
            ),
        ]
    )]
    public function index(Request $request): JsonResponse
    {
        $subcategories = VocabSubcategory::query()
            ->with('background')
            ->when($request->filled('category_id'), fn ($q) => $q->where('vocab_category_id', $request->integer('category_id')))
            ->orderBy('sort_order')
            ->orderBy('name_en')
            ->get()
            ->map(fn (VocabSubcategory $s) => [
                'id'                   => $s->id,
                'vocab_category_id'    => $s->vocab_category_id,
                'name_en'              => $s->name_en,
                'name_jp'              => $s->name_jp,
                'name_romaji'          => $s->name_romaji,
                'icon_url'             => $this->url($s->icon_path),
                'icon_thumbnail_url'   => $this->url($s->icon_thumbnail_path),
                'icon_thumbnail_bg'    => $s->icon_thumbnail_bg,
                'bg_url'               => $s->background ? asset($s->background->vocab_bg_path) : null,
                'audio_url'            => $this->url($s->audio_path),
                'sort_order'           => $s->sort_order,
                'is_premium'           => $s->is_premium,
            ]);

        return response()->json(['data' => $subcategories]);
    }

    private function url(?string $path): ?string
    {
        return $path ? Storage::disk('public')->url($path) : null;
    }
}
