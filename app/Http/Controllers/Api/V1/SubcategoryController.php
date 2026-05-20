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
        tags: ['Public'],
        summary: 'List subcategories (optionally filtered by category_id)',
        parameters: [new OA\Parameter(name: 'category_id', in: 'query', required: false, schema: new OA\Schema(type: 'integer'))],
        responses: [new OA\Response(response: 200, description: 'OK')]
    )]
    public function index(Request $request): JsonResponse
    {
        $subcategories = VocabSubcategory::query()
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
