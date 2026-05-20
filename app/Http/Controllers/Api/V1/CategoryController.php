<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\VocabCategory;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Storage;
use OpenApi\Attributes as OA;

class CategoryController extends Controller
{
    #[OA\Get(
        path: '/api/v1/categories',
        tags: ['Public'],
        summary: 'List all vocab categories',
        responses: [new OA\Response(response: 200, description: 'OK')]
    )]
    public function index(): JsonResponse
    {
        $categories = VocabCategory::query()
            ->orderBy('sort_order')
            ->orderBy('name_en')
            ->get()
            ->map(fn (VocabCategory $c) => [
                'id'                  => $c->id,
                'name_en'             => $c->name_en,
                'name_jp'             => $c->name_jp,
                'name_romaji'         => $c->name_romaji,
                'icon_url'            => $this->url($c->icon_path),
                'icon_thumbnail_url'  => $this->url($c->icon_thumbnail_path),
                'category_bg_path'    => $c->category_bg_path,
                'audio_url'           => $this->url($c->audio_path),
                'sort_order'          => $c->sort_order,
                'is_premium'          => $c->is_premium,
            ]);

        return response()->json(['data' => $categories]);
    }

    private function url(?string $path): ?string
    {
        return $path ? Storage::disk('public')->url($path) : null;
    }
}
