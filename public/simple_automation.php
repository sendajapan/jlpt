<?php

$host = "127.0.0.1";
$user = "sendajapan1";
$pass = "sulaiman007";
$db   = "jlpt_db";

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$result = $conn->query("SELECT concat('https://pathlingo.scholarlyapps.com/admin/vocab/words/',id,'/generate-audio?field=audio_en&voice=',voice_id)  FROM `vocab_words` WHERE 1 AND audio_en is NULL
UNION
SELECT concat('https://pathlingo.scholarlyapps.com/admin/vocab/words/',id,'/generate-audio?field=audio_jp&voice=',voice_id)  FROM `vocab_words` WHERE 1 AND audio_jp is NULL
UNION
SELECT concat('https://pathlingo.scholarlyapps.com/admin/vocab/words/',id,'/generate-audio?field=sentence_audio_en&voice=',voice_id)  FROM `vocab_words` WHERE 1 AND sentence_audio_en is NULL
UNION
SELECT concat('https://pathlingo.scholarlyapps.com/admin/vocab/words/',id,'/generate-audio?field=sentence_audio_jp&voice=',voice_id)  FROM `vocab_words` WHERE 1 AND sentence_audio_jp is NULL;
");

$urls_to_add = [];

while ($row = $result->fetch_array()) {
    $conn->query("INSERT INTO `urls` (`url`) VALUES (".$row[0].");");
}
// DELETE URL
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['delete_id'])) {

    $id = (int) $_POST['delete_id'];

    $stmt = $conn->prepare("DELETE FROM urls WHERE id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();

    echo "deleted";
    exit;
}

// GET URLS
$result = $conn->query("SELECT id, url FROM urls ORDER BY id ASC");

$urls = [];

while ($row = $result->fetch_assoc()) {

    $urls[] = [
        'id'  => $row['id'],
        'url' => $row['url']
    ];
}

?>

<!DOCTYPE html>
<html>
<head>
    <title>Parallel URL Executor</title>

    <style>

        body {
            font-family: Arial;
            padding: 20px;
        }

        .running {
            color: blue;
        }

        .success {
            color: green;
        }

        .error {
            color: red;
        }

    </style>
</head>
<body>

<h2>Executing URLs In Parallel Every 20 Seconds.</h2>

<div id="log"></div>

<script>

    const urls = <?php echo json_encode($urls); ?>;

    const log = document.getElementById('log');

    function addLog(message, className = '') {

        const p = document.createElement('p');

        p.className = className;

        p.innerHTML = message;

        log.appendChild(p);
    }

    async function deleteUrl(id) {

        await fetch(window.location.href, {

            method: 'POST',

            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },

            body: 'delete_id=' + encodeURIComponent(id)
        });
    }

    async function executeUrl(item, index) {

        try {

            addLog(`[${index + 1}] Running: ${item.url}`, 'running');

            // Execute URL silently
            const response = await fetch(item.url, {
                method: 'GET',
                cache: 'no-cache'
            });

            const text = await response.text();

            console.log(text);

            // Remove from database
            await deleteUrl(item.id);

            addLog(`[${index + 1}] Completed & Deleted`, 'success');

        } catch (error) {

            console.error(error);

            addLog(`[${index + 1}] Failed: ${item.url}`, 'error');
        }
    }

    // RUN ALL URLS IN PARALLEL
    function startParallelExecution() {

        urls.forEach((item, index) => {

            // Start each request with delay
            setTimeout(() => {

                executeUrl(item, index);

            }, index * 20000); // 20 seconds gap between starting each URL
        });
    }

    startParallelExecution();

</script>

</body>
</html>
