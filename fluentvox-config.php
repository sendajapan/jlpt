<?php

return [
    'python_path' => __DIR__ . '/bin/python-throttled.sh',
    'models_path' => null,
    'default_model' => 'chatterbox',
    'device' => 'auto',
    'output_path' => __DIR__ . '/storage/app/fluentvox-tmp',
    'audio_format' => 'wav',
    'sample_rate' => 24000,
    'defaults' => [
        'exaggeration' => 0.5,
        'temperature' => 0.6,
        'cfg_weight' => 0.5,
        'seed' => 0,
    ],
    'pytorch' => [
        'torch' => '2.6.0',
        'torchaudio' => '2.6.0',
        'torchvision' => '0.21.0',
    ],
    'timeout' => 600,
    'verbose' => false,
];
