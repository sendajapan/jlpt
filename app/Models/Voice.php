<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Storage;

class Voice extends Model
{
    protected $table = 'voices';

    protected $fillable = [
        'name',
        'voice_id',
        'language',
        'gender',
        'description',
        'reference_path',
        'settings',
        'is_default',
        'sort_order',
    ];

    protected $casts = [
        'settings'   => 'array',
        'is_default' => 'boolean',
        'sort_order' => 'integer',
    ];

    public function referenceAbsolutePath(): ?string
    {
        return $this->reference_path
            ? Storage::disk('public')->path($this->reference_path)
            : null;
    }

    public static function default(): ?self
    {
        return static::where('is_default', true)->first() ?? static::orderBy('sort_order')->first();
    }
}
