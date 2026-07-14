<?php

namespace Database\Factories;

use App\Models\Kanji;
use Illuminate\Database\Eloquent\Factories\Factory;

class KanjiFactory extends Factory
{
    protected $model = Kanji::class;

    public function definition(): array
    {
        return [
            'kanji' => '日',
            'strokes' => 4,
            'grade' => 1,
            'freq' => 1,
            'jlpt' => 'N5',
            'translate' => 'Sun',
            'meanings' => 'Day, Sun',
            'readings_on' => 'にち, じつ',
            'readings_kun' => 'ひ, か',
            'level' => 1,
        ];
    }
}
