<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Kanji extends Model
{
    protected $fillable = [
        'kanji',
        'strokes',
        'grade',
        'freq',
        'jlpt',
        'translate',
        'meanings',
        'readings_on',
        'readings_kun',
        'level',
        'radicals',
        'vocab_id',
        'is_premium',
    ];

    protected $casts = [
        'strokes' => 'integer',
        'grade' => 'integer',
        'freq' => 'integer',
        'level' => 'integer',
        'is_premium' => 'boolean',
    ];

    public function vocab(): BelongsTo
    {
        return $this->belongsTo(Vocabulary::class, 'vocab_id');
    }
}
