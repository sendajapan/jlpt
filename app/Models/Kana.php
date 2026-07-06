<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Kana extends Model
{
    use HasFactory;

    protected $fillable = [
        'script',
        'kana',
        'romaji',
        'sound_url',
        'section',
        'position',
    ];

    protected $casts = [
        'position' => 'integer',
    ];

    public function learners(): BelongsToMany
    {
        return $this->belongsToMany(AppUser::class, 'app_user_kana_learned', 'kana_id', 'app_user_id')
            ->withTimestamps();
    }
}
