<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\MorphTo;

class AppUserCategoryUnlock extends Model
{
    protected $fillable = [
        'app_user_id',
        'unlockable_type',
        'unlockable_id',
        'coins_spent',
    ];

    protected $casts = [
        'coins_spent' => 'integer',
    ];

    public function user(): BelongsTo
    {
        return $this->belongsTo(AppUser::class, 'app_user_id');
    }

    public function unlockable(): MorphTo
    {
        return $this->morphTo();
    }
}
