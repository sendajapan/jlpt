<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class AvatarPurchase extends Model
{
    protected $fillable = [
        'app_user_id',
        'avatar_id',
    ];

    public function user(): BelongsTo
    {
        return $this->belongsTo(AppUser::class, 'app_user_id');
    }

    public function avatar(): BelongsTo
    {
        return $this->belongsTo(Avatar::class);
    }
}
