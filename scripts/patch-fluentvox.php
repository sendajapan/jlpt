<?php

$modelManager = __DIR__ . '/../vendor/b7s/fluentvox/src/Support/ModelManager.php';
$fluentVox    = __DIR__ . '/../vendor/b7s/fluentvox/src/FluentVox.php';

if (file_exists($modelManager)) {
    $content = file_get_contents($modelManager);
    $changed = false;

    $oldLoad = "    def cpu_torch_load(f, map_location=None, *args, **kwargs):\n        # Always force map_location to CPU, overriding any None or CUDA values\n        map_location = 'cpu'\n        return original_torch_load(f, map_location=map_location, *args, **kwargs)";
    $newLoad = "    def cpu_torch_load(f, map_location=None, *args, **kwargs):\n        # Always force map_location to CPU, overriding any None or CUDA values\n        map_location = 'cpu'\n        # PyTorch 2.6+ changed weights_only default to True which breaks older checkpoints\n        kwargs['weights_only'] = False\n        return original_torch_load(f, map_location=map_location, *args, **kwargs)";

    if (str_contains($content, $oldLoad)) {
        $content = str_replace($oldLoad, $newLoad, $content);
        $changed = true;
    }

    // {$model->value} appears literally in the PHP source heredoc
    $oldMatch = '        model_repo = match ("{$model->value}") {' . "\n"
              . '            "chatterbox-multilingual" => "ResembleAI/chatterbox",' . "\n"
              . '            default => "resemble-ai/{$model->value}",' . "\n"
              . '        }';
    $newMatch = '        model_repo = "ResembleAI/chatterbox" if "{$model->value}" == "chatterbox-multilingual" else "resemble-ai/{$model->value}"';

    if (str_contains($content, $oldMatch)) {
        $content = str_replace($oldMatch, $newMatch, $content);
        $changed = true;
    }

    if ($changed) {
        file_put_contents($modelManager, $content);
        echo "Patched: ModelManager.php\n";
    } else {
        echo "ModelManager.php already patched — skipping.\n";
    }
}

if (file_exists($fluentVox)) {
    $content = file_get_contents($fluentVox);

    $needle      = "torch.load = _patched_torch_load\n\n{\$import}";
    $replacement = "torch.load = _patched_torch_load\n\n# Patch perth watermarker: PerthImplicitWatermarker is None on CPU-only systems\nimport perth\nif perth.PerthImplicitWatermarker is None:\n    perth.PerthImplicitWatermarker = perth.DummyWatermarker\n\n{\$import}";

    if (str_contains($content, $needle)) {
        file_put_contents($fluentVox, str_replace($needle, $replacement, $content));
        echo "Patched: FluentVox.php\n";
    } else {
        echo "FluentVox.php already patched — skipping.\n";
    }
}
