<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class VocabSubcategory extends Model
{
    protected $table = 'vocab_subcategories';

    protected $fillable = [
        'vocab_category_id',
        'name_en',
        'name_jp',
        'name_romaji',
        'icon_path',
        'icon_thumbnail_path',
        'icon_thumbnail_bg',
        'audio_path',
        'sort_order',
        'is_premium',
        'coin_price',
    ];

    protected $casts = [
        'is_premium' => 'boolean',
        'coin_price' => 'integer',
        'sort_order' => 'integer',
    ];

    public function category(): BelongsTo
    {
        return $this->belongsTo(VocabCategory::class, 'vocab_category_id');
    }

    public function background(): BelongsTo
    {
        return $this->belongsTo(VocabBg::class, 'icon_thumbnail_bg', 'vocab_bg_id');
    }

    public function vocabularies(): HasMany
    {
        return $this->hasMany(Vocabulary::class, 'vocab_subcategory_id');
    }
}
