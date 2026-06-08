<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Vocabulary extends Model
{
    protected $table = 'vocab_words';

    protected $fillable = [
        'vocab_subcategory_id',
        'word_jp',
        'audio_jp',
        'sentence_jp',
        'sentence_audio_jp',
        'word_romaji',
        'sentence_romaji',
        'word_en',
        'audio_en',
        'sentence_en',
        'sentence_audio_en',
        'image_path',
        'image_thumbnail_path',
        'image_thumbnail_bg',
        'sort_order',
        'is_premium',
        'coin_price',
        'is_approved',
        'level',
        'voice_id',
        'audio_jp_reviewed',
        'audio_en_reviewed',
        'sentence_audio_jp_reviewed',
        'sentence_audio_en_reviewed',
    ];

    protected $casts = [
        'sort_order' => 'integer',
        'is_premium' => 'boolean',
        'coin_price' => 'integer',
        'is_approved' => 'boolean',
        'audio_jp_reviewed' => 'boolean',
        'audio_en_reviewed' => 'boolean',
        'sentence_audio_jp_reviewed' => 'boolean',
        'sentence_audio_en_reviewed' => 'boolean',
    ];

    public function subcategory(): BelongsTo
    {
        return $this->belongsTo(VocabSubcategory::class, 'vocab_subcategory_id');
    }

    public function background(): BelongsTo
    {
        return $this->belongsTo(VocabBg::class, 'image_thumbnail_bg', 'vocab_bg_id');
    }

    public function voice(): BelongsTo
    {
        return $this->belongsTo(Voice::class);
    }
}
