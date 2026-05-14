<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\Vocabulary;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class VocabularyController extends Controller
{
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
