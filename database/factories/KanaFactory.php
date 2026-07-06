<?php

namespace Database\Factories;

use App\Models\Kana;
use Illuminate\Database\Eloquent\Factories\Factory;

class KanaFactory extends Factory
{
    protected $model = Kana::class;

    public function definition(): array
    {
        return [
            'script' => 'hiragana',
            'kana' => 'あ',
            'romaji' => 'a',
            'section' => 'basic',
            'position' => fake()->unique()->numberBetween(1, 1000),
        ];
    }

    public function katakana(): static
    {
        return $this->state(['script' => 'katakana', 'kana' => 'ア']);
    }
}
