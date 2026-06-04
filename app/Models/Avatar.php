<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Avatar extends Model
{
    use HasFactory;

    protected $fillable = [
        'name',
        'image_path',
        'coin_price',
        'sort_order',
        'is_active',
    ];

    protected function casts(): array
    {
        return [
            'coin_price' => 'integer',
            'sort_order' => 'integer',
            'is_active' => 'boolean',
        ];
    }

    public function purchasers(): BelongsToMany
    {
        return $this->belongsToMany(AppUser::class, 'avatar_purchases', 'avatar_id', 'app_user_id')
            ->withTimestamps();
    }
}
