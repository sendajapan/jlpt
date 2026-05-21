<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class VocabBg extends Model
{
    protected $table = 'vocab_bgs';

    protected $primaryKey = 'vocab_bg_id';

    public $timestamps = false;

    protected $fillable = [
        'vocab_bg_path',
        'vocab_bg_sort',
    ];

    protected $casts = [
        'vocab_bg_sort' => 'integer',
    ];

    public function categories(): HasMany
    {
        return $this->hasMany(VocabCategory::class, 'category_bg_path', 'vocab_bg_id');
    }

    public function subcategories(): HasMany
    {
        return $this->hasMany(VocabSubcategory::class, 'icon_thumbnail_bg', 'vocab_bg_id');
    }

    public function vocabularies(): HasMany
    {
        return $this->hasMany(Vocabulary::class, 'image_thumbnail_bg', 'vocab_bg_id');
    }
}
