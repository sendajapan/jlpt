<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class VocabBg extends Model
{

    public function category(): BelongsTo
    {
        return $this->belongsTo(VocabCategory::class, 'vocab_category_id');
    }

    public function vocabularies(): HasMany
    {
        return $this->hasMany(Vocabulary::class, 'vocab_subcategory_id');
    }
}
