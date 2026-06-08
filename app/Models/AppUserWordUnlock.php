<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class AppUserWordUnlock extends Model
{
    protected $fillable = [
        'app_user_id',
        'vocab_id',
        'unlock_type',
        'coins_spent',
    ];

    protected $casts = [
        'coins_spent' => 'integer',
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
