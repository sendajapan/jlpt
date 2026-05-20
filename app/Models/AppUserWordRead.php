<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class AppUserWordRead extends Model
{
    protected $fillable = ['app_user_id', 'vocab_id', 'read_count', 'last_read_at'];

    protected $casts = [
        'last_read_at' => 'datetime',
        'read_count' => 'integer',
    ];

    public function user(): BelongsTo
    {
        return $this->belongsTo(AppUser::class, 'app_user_id');
    }

    public function vocabulary(): BelongsTo
    {
        return $this->belongsTo(Vocabulary::class, 'vocab_id');
    }
}
