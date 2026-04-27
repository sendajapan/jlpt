<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Vocabulary extends Model
{
    protected $fillable = [
        'subcategory_id',
        'word_jp',
        'word_romaji',
        'meaning_en',
        'audio_path',
        'image_path',
        'example_sentence_jp',
        'example_sentence_en',
        'jlpt_level',
        'sort_order',
    ];

    protected $casts = [
        'sort_order' => 'integer',
    ];

    public function subcategory(): BelongsTo
    {
        return $this->belongsTo(Subcategory::class);
    }
}
