<?php

namespace Database\Seeders;

use App\Models\Voice;
use Illuminate\Database\Seeder;

class VoiceSeeder extends Seeder
{
    public function run(): void
    {
        $voices = [
            [
                'name'        => 'Hana — Calm Narrator',
                'language'    => 'ja',
                'gender'      => 'female',
                'description' => 'Soft, calm female voice ideal for vocabulary readings and slow-paced study material.',
                'settings'    => [
                    'model'        => 'chatterbox-multilingual',
                    'exaggeration' => 0.3,
                    'cfg_weight'   => 0.3,
                    'temperature'  => 0.6,
                    'seed'         => 42,
                ],
                'is_default'  => true,
            ],
            [
                'name'        => 'Sora — Natural Conversational',
                'language'    => 'ja',
                'gender'      => 'female',
                'description' => 'Balanced everyday female voice for conversational example sentences.',
                'settings'    => [
                    'model'        => 'chatterbox-multilingual',
                    'exaggeration' => 0.5,
                    'cfg_weight'   => 0.5,
                    'temperature'  => 0.8,
                    'seed'         => 0,
                ],
                'is_default'  => false,
            ],
            [
                'name'        => 'Ren — Expressive Male',
                'language'    => 'ja',
                'gender'      => 'male',
                'description' => 'Energetic male voice with stronger emotional delivery for dialogue practice.',
                'settings'    => [
                    'model'        => 'chatterbox-multilingual',
                    'exaggeration' => 0.7,
                    'cfg_weight'   => 0.6,
                    'temperature'  => 0.9,
                    'seed'         => 123,
                ],
                'is_default'  => false,
            ],
            [
                'name'        => 'Kaito — Fast Male',
                'language'    => 'ja',
                'gender'      => 'male',
                'description' => 'Quick-paced male voice for native-speed listening practice.',
                'settings'    => [
                    'model'        => 'chatterbox-multilingual',
                    'exaggeration' => 0.5,
                    'cfg_weight'   => 0.7,
                    'temperature'  => 0.8,
                    'seed'         => 0,
                ],
                'is_default'  => false,
            ],
            [
                'name'        => 'Yui — Dramatic Storyteller',
                'language'    => 'ja',
                'gender'      => 'female',
                'description' => 'Theatrical female voice for story passages and emphatic readings.',
                'settings'    => [
                    'model'        => 'chatterbox-multilingual',
                    'exaggeration' => 1.0,
                    'cfg_weight'   => 0.4,
                    'temperature'  => 1.2,
                    'seed'         => 7,
                ],
                'is_default'  => false,
            ],
            [
                'name'        => 'Emma — English Reader',
                'language'    => 'en',
                'gender'      => 'female',
                'description' => 'Neutral English female voice for translations and meanings.',
                'settings'    => [
                    'model'        => 'chatterbox',
                    'exaggeration' => 0.4,
                    'cfg_weight'   => 0.5,
                    'temperature'  => 0.7,
                    'seed'         => 0,
                ],
                'is_default'  => false,
            ],
        ];

        foreach ($voices as $voice) {
            Voice::updateOrCreate(['name' => $voice['name']], $voice);
        }
    }
}
