<?php

$filename = $_GET['audio'];
$folder = 'https://pathlingo.scholarlyapps.com/storage/vocab/words/audio/';
$path = '/var/www/html/jlpt-laravel-vue/storage/app/public/vocab/words/audio/';

if($filename!=''){}else{
        'No file Found';
        exit;
    }

$file = $path.$filename;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    header('Content-Type: application/json');

    $start = isset($_POST['start']) ? floatval($_POST['start']) : 0;
    $end   = isset($_POST['end']) ? floatval($_POST['end']) : 0;

    if ($end <= $start) {
        echo json_encode([
            'success' => false,
            'message' => 'Invalid selection'
        ]);
        exit;
    }

    $tmpFile = "audio_tmp.mp3";

    $cmd = sprintf(
        'ffmpeg -y -i %s -ss %s -to %s -c:a libmp3lame -q:a 2 %s 2>&1',
        escapeshellarg($file),
        escapeshellarg($start),
        escapeshellarg($end),
        escapeshellarg($tmpFile)
    );

    exec($cmd, $output, $returnCode);

    if ($returnCode !== 0) {

        echo json_encode([
            'success' => false,
            'message' => implode("\n", $output)
        ]);
        exit;
    }

    unlink($file);
    rename($tmpFile, $file);

    echo json_encode([
        'success' => true
    ]);
    exit;
}
?>

<!DOCTYPE html>
<html>
<head>

    <meta charset="utf-8">

    <title>Audio Cropper</title>

    <script src="https://unpkg.com/wavesurfer.js@7"></script>
    <script src="https://unpkg.com/wavesurfer.js@7/dist/plugins/regions.min.js"></script>

    <style>

        body{
            font-family:Arial;
            margin:30px;
        }

        #waveform{
            width:100%;
            border:1px solid #ccc;
            margin-bottom:20px;
        }

        button{
            padding:10px 20px;
            margin-right:10px;
            cursor:pointer;
        }

    </style>

</head>

<body>

<h2>MP3 Waveform Cropper</h2>

<div id="waveform"></div>

<button id="playBtn">Play / Pause</button>
<button id="saveBtn">Crop & Save</button>

<script>

    let selectedRegion = null;

    const regionsPlugin = WaveSurfer.Regions.create();

    const wavesurfer = WaveSurfer.create({
        container: '#waveform',
        waveColor: '#999',
        progressColor: '#333',
        height: 150,
        url: '<?=$file?>?' + Date.now(),
        plugins: [regionsPlugin]
    });

    wavesurfer.on('ready', () => {

        regionsPlugin.enableDragSelection({
            color: 'rgba(0,123,255,0.3)'
        });

        selectedRegion = regionsPlugin.addRegion({
            start: 0,
            end: Math.min(5, wavesurfer.getDuration()),
            color: 'rgba(0,123,255,0.3)',
            drag: true,
            resize: true
        });

    });

    regionsPlugin.on('region-created', region => {

        regionsPlugin.getRegions().forEach(r => {

            if (r.id !== region.id) {
                r.remove();
            }

        });

        selectedRegion = region;
    });

    regionsPlugin.on('region-updated', region => {
        selectedRegion = region;
    });

    regionsPlugin.on('region-clicked', region => {
        selectedRegion = region;
    });

    document.getElementById('playBtn').addEventListener('click', () => {
        wavesurfer.playPause();
    });

    document.getElementById('saveBtn').addEventListener('click', () => {

        if (!selectedRegion) {
            alert('Please select a crop area');
            return;
        }

        const fd = new FormData();

        fd.append('start', selectedRegion.start);
        fd.append('end', selectedRegion.end);

        fetch('', {
            method: 'POST',
            body: fd
        })
            .then(res => res.json())
            .then(data => {

                if (data.success) {

                    alert('Audio saved successfully');

                    location.reload();

                } else {

                    alert(data.message);
                }

            })
            .catch(err => {

                console.error(err);
                alert('Error saving audio');

            });

    });

</script>

</body>
</html>
