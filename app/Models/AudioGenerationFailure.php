<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class AudioGenerationFailure extends Model
{
    protected $table = 'audio_generation_failures';

    protected $fillable = [
        'vocab_id',
        'field',
        'attempts',
        'last_error',
        'last_attempt_at',
    ];

    protected $casts = [
        'attempts' => 'integer',
        'last_attempt_at' => 'datetime',
    ];

    public const MAX_ATTEMPTS = 3;

    public function vocabulary(): BelongsTo
    {
        return $this->belongsTo(Vocabulary::class, 'vocab_id');
    }
}
