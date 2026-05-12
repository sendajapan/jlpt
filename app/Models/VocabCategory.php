<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class VocabCategory extends Model
{
    protected $table = 'vocab_categories';

    protected $fillable = [
        'name_en',
        'name_jp',
        'name_romaji',
        'icon_path',
        'icon_thumbnail_path',
        'category_bg_path',
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
        return $this->hasMany(VocabSubcategory::class, 'vocab_category_id');
    }
}
