<?php

$file = "uploads/audio.mp3"; // original file

if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    $start = floatval($_POST['start']);
    $end = floatval($_POST['end']);

    $tmpFile = $file . ".tmp.mp3";

    $cmd = sprintf(
        'ffmpeg -y -i %s -ss %s -to %s -c copy %s 2>&1',
        escapeshellarg($file),
        escapeshellarg($start),
        escapeshellarg($end),
        escapeshellarg($tmpFile)
    );

    exec($cmd, $output, $result);

    if ($result === 0 && file_exists($tmpFile)) {

        unlink($file);
        rename($tmpFile, $file);

        echo json_encode([
            'success' => true
        ]);
    } else {
        echo json_encode([
            'success' => false,
            'error' => implode("\n", $output)
        ]);
    }

    exit;
}
?>

<!DOCTYPE html>
<html>
<head>
    <script src="https://unpkg.com/wavesurfer.js"></script>
    <script src="https://unpkg.com/wavesurfer.js@7/dist/plugins/regions.min.js"></script>
</head>
<body>

<h3>Audio Cropper</h3>

<div id="waveform"></div>

<br>

<button id="play">Play/Pause</button>
<button id="save">Crop & Save</button>

<script>

    const regions = WaveSurfer.Regions.create();

    const ws = WaveSurfer.create({
        container: '#waveform',
        waveColor: '#999',
        progressColor: '#444',
        height: 150,
        url: 'uploads/audio.mp3',
        plugins: [regions]
    });

    let selectedRegion = null;

    ws.on('ready', () => {

        selectedRegion = ws.addRegion({
            start: 0,
            end: 5,
            color: 'rgba(0,123,255,0.3)',
            resize: true,
            drag: true
        });

    });

    document.getElementById('play').onclick = () => {
        ws.playPause();
    };

    document.getElementById('save').onclick = () => {

        if (!selectedRegion) {
            alert('Select an area first');
            return;
        }

        let formData = new FormData();

        formData.append('start', selectedRegion.start);
        formData.append('end', selectedRegion.end);

        fetch(window.location.href, {
            method: 'POST',
            body: formData
        })
            .then(r => r.json())
            .then(data => {

                if (data.success) {
                    alert('File cropped and saved');
                    location.reload();
                } else {
                    alert(data.error);
                }

            });
    };

</script>

</body>
</html>
