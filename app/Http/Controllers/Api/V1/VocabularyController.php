<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Vocabulary;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use OpenApi\Attributes as OA;

class VocabularyController extends Controller
{
    #[OA\Get(
        path: '/api/v1/vocabularies',
        summary: 'List approved vocab words with optional search and filters',
        tags: ['Public'],
        parameters: [
            new OA\Parameter(
                name: 'search',
                in: 'query',
                required: false,
                description: 'Search across word_jp, word_romaji, and word_en',
                schema: new OA\Schema(type: 'string', example: 'neko')
            ),
            new OA\Parameter(
                name: 'category_id',
                in: 'query',
                required: false,
                description: 'Filter words that belong to subcategories of this category',
                schema: new OA\Schema(type: 'integer', example: 1)
            ),
            new OA\Parameter(
                name: 'subcategory_id',
                in: 'query',
                required: false,
                description: 'Filter words that belong to this subcategory',
                schema: new OA\Schema(type: 'integer', example: 3)
            ),
        ],
        responses: [
            new OA\Response(
                response: 200,
                description: 'List of approved vocab words ordered by sort_order then word_jp',
                content: new OA\JsonContent(
                    properties: [
                        new OA\Property(
                            property: 'data',
                            type: 'array',
                            items: new OA\Items(ref: '#/components/schemas/VocabWord')
                        ),
                    ]
                )
            ),
        ]
    )]
    public function index(Request $request): JsonResponse
    {
        $search = $request->string('search')->toString();

        $vocabularies = Vocabulary::query()
            ->where('is_approved', true)
            ->when($request->filled('subcategory_id'), fn ($q) => $q->where('vocab_subcategory_id', $request->integer('subcategory_id')))
            ->when($request->filled('category_id'), fn ($q) => $q->whereHas('subcategory', fn ($s) => $s->where('vocab_category_id', $request->integer('category_id'))))
            ->when($search !== '', fn ($q) => $q->where(fn ($w) => $w
                ->where('word_jp', 'like', "%{$search}%")
                ->orWhere('word_romaji', 'like', "%{$search}%")
                ->orWhere('word_en', 'like', "%{$search}%")))
            ->with('background')
            ->orderBy('sort_order')
            ->orderBy('word_jp')
            ->get()
            ->map(fn (Vocabulary $v) => [
                'id'                   => $v->id,
                'vocab_subcategory_id' => $v->vocab_subcategory_id,
                'word_jp'              => $v->word_jp,
                'word_romaji'          => $v->word_romaji,
                'word_en'              => $v->word_en,
                'sentence_jp'          => $v->sentence_jp,
                'sentence_romaji'      => $v->sentence_romaji,
                'sentence_en'          => $v->sentence_en,
                'audio_jp_url'         => $this->url($v->audio_jp),
                'audio_en_url'         => $this->url($v->audio_en),
                'sentence_audio_jp_url'=> $this->url($v->sentence_audio_jp),
                'sentence_audio_en_url'=> $this->url($v->sentence_audio_en),
                'image_url'            => $this->url($v->image_path),
                'image_thumbnail_url'  => $this->url($v->image_thumbnail_path),
                'image_thumbnail_bg'   => $v->image_thumbnail_bg,
                'bg_url'               => $v->background ? asset($v->background->vocab_bg_path) : null,
                'sort_order'           => $v->sort_order,
                'is_premium'           => $v->is_premium,
            ]);

        return response()->json(['data' => $vocabularies]);
    }

    private function url(?string $path): ?string
    {
        return $path ? Storage::disk('public')->url($path) : null;
    }
}
