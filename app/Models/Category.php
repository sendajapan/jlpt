<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Category extends Model
{
    protected $fillable = [
        'name_en',
        'name_jp',
        'name_romaji',
        'icon_path',
        'audio_path',
        'sort_order',
        'is_premium',
    ];

    protected $casts = [
        'is_premium' => 'boolean',
        'sort_order' => 'integer',
    ];

    public function subcategories(): HasMany
    {
        return $this->hasMany(Subcategory::class);
    }
}
