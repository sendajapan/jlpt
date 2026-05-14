<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\VocabSubcategory;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class SubcategoryController extends Controller
{
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
