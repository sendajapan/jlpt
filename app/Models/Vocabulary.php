<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Vocabulary extends Model
{
    protected $fillable = [
        'subcategory_id',
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
        'sort_order',
        'is_premium',
        'is_approved',
    ];

    protected $casts = [
        'sort_order'  => 'integer',
        'is_premium'  => 'boolean',
        'is_approved' => 'boolean',
    ];

    public function subcategory(): BelongsTo
    {
        return $this->belongsTo(Subcategory::class);
    }
}
