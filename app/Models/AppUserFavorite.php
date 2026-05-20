<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class AppUserFavorite extends Model
{
    protected $fillable = ['app_user_id', 'vocab_id'];

    public function user(): BelongsTo
    {
        return $this->belongsTo(AppUser::class, 'app_user_id');
    }

    public function vocabulary(): BelongsTo
    {
        return $this->belongsTo(Vocabulary::class, 'vocab_id');
    }
}
