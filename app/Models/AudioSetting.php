<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class AudioSetting extends Model
{
    protected $table = 'audio_settings';

    protected $fillable = [
        'is_running',
        'generated_count',
        'last_run_at',
        'last_error',
    ];

    protected $casts = [
        'is_running' => 'boolean',
        'last_run_at' => 'datetime',
    ];

    public static function current(): self
    {
        return static::firstOrCreate(['id' => 1], [
            'is_running' => false,
            'generated_count' => 0,
        ]);
    }
}
