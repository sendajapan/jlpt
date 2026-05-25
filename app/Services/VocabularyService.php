<?php

namespace App\Services;

use App\Models\VocabBg;
use App\Models\VocabCategory;
use App\Models\VocabSubcategory;
use App\Models\Vocabulary;
use App\Models\Voice;
use B7s\FluentVox\FluentVox;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Pagination\LengthAwarePaginator;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Str;

class VocabularyService
{
    public function getAll(array $filters = [], int $perPage = 50): LengthAwarePaginator
    {
        return Vocabulary::query()
            ->with('subcategory.category')
            ->leftJoin('vocab_bgs', 'vocab_bgs.vocab_bg_id', '=', 'image_thumbnail_bg')
            ->when($filters['search'] ?? null, fn ($q, $s) => $this->applySearchFilter($q, $s))
            ->when($filters['subcategory_id'] ?? null, fn ($q, $v) => $q->where('vocab_subcategory_id', $v))
            ->when($filters['category_id'] ?? null, fn ($q, $v) => $q->whereHas('subcategory', fn ($s) => $s->where('vocab_category_id', $v)))
            ->when(isset($filters['is_approved']) && $filters['is_approved'] !== '', fn ($q) => $q->where('is_approved', $filters['is_approved']))
            ->when(isset($filters['image_path']) && $filters['image_path'] === 'images', fn ($q) => $this->applyHasImageFilter($q))
            ->when(isset($filters['image_path']) && $filters['image_path'] === 'pending', fn ($q) => $this->applyPendingImageFilter($q))
            ->when(isset($filters['audio_flag']) && $filters['audio_flag'] === 'pending', fn ($q) => $this->applyPendingAudioFilter($q))
            ->when(isset($filters['audio_flag']) && $filters['audio_flag'] === 'complete', fn ($q) => $this->applyCompleteAudioFilter($q))
            ->orderBy('sort_order')
            ->orderBy('word_jp')
            //->tap(fn($q) => dd($q->toRawSql()))
            ->paginate($perPage)
            ->withQueryString();
    }

    private function applySearchFilter($query, string $search)
    {
        return $query
            ->where('word_jp', 'like', "%{$search}%")
            ->orWhere('word_romaji', 'like', "%{$search}%")
            ->orWhere('word_en', 'like', "%{$search}%");
    }

    private function applyHasImageFilter($query)
    {
        return $query->whereNot('image_path', '');
    }

    private function applyPendingImageFilter($query)
    {
        return $query->whereNull('image_path');
    }

    private function applyPendingAudioFilter($query)
    {
        return $query
            ->whereAny(['sentence_audio_en_reviewed', 'sentence_audio_jp_reviewed', 'audio_en_reviewed'], '0')
            ->whereNotNull(['sentence_audio_en', 'sentence_audio_jp', 'audio_en']);
    }

    private function applyCompleteAudioFilter($query)
    {
        return $query
            ->where('sentence_audio_en_reviewed', '1')
            ->where('audio_jp_reviewed', '1')
            ->where('sentence_audio_jp_reviewed', '1')
            ->where('audio_en_reviewed', '1');
    }

    public function getAllCategories(): Collection
    {
        return VocabCategory::orderBy('name_en')->get();
    }

    public function getAllSubcategories(): Collection
    {
        return VocabSubcategory::with('category')->orderBy('name_en')->get();
    }

    public function getAllVocabBg(): Collection
    {
        return VocabBg::orderBy('vocab_bg_sort')->get();
    }

    public function create(array $data): Vocabulary
    {
        return Vocabulary::create($data);
    }

    public function update(Vocabulary $vocabulary, array $data): Vocabulary
    {
        $vocabulary->update($data);

        return $vocabulary;
    }

    public function delete(Vocabulary $vocabulary): void
    {
        $vocabulary->delete();
    }

    public function generateAudio(Vocabulary $vocabulary, string $field, ?int $voiceId): string
    {
        $textMap = [
            'audio_en' => $vocabulary->word_en,
            'audio_jp' => $vocabulary->word_jp,
            'sentence_audio_en' => $vocabulary->sentence_en,
            'sentence_audio_jp' => $vocabulary->sentence_jp,
        ];

        $text = $textMap[$field] ?? null;

        if (empty($text)) {
            throw new \InvalidArgumentException('No text available for this field.');
        }

        // Chatterbox is trained on sentences. Bare single words like "earmuffs" or
        // "イヤーマフ" make the model think the utterance is unfinished, so it
        // hallucinates an extra word at the end. Wrapping the word so it looks
        // like a complete sentence (English: "word.", Japanese: "「word」。")
        // gives the model a clear stop signal. Brackets/periods are not spoken.
        $isSingleWord = $field === 'audio_en';
        $isSingleWordJp = $field === 'audio_jp';

        if ($isSingleWord) {
            $clean = rtrim($text, " .。!?！？「」\"'");
            $text = $isSingleWordJp ? "「{$clean}」。" : "{$clean}.";
        }elseif ($isSingleWordJp) {
            $clean = rtrim($text, " .。!?！？「」\"'");
            $text = $isSingleWordJp ? "「{$clean}」。" : "{$clean}.";
        }

        $voice = $voiceId ? Voice::find($voiceId) : Voice::default();

        if ($voice) {
            $vocabulary->update(['voice_id' => $voice->id]);
        }

        $isJapanese = in_array($field, ['audio_jp', 'sentence_audio_jp']);
        $voiceRef = $voice?->referenceAbsolutePath();
        $settings = $voice?->settings ?? [];

        $tts = FluentVox::make()->text($text);

        if ($voiceRef && file_exists($voiceRef)) {
            $tts = $tts->voiceFrom($voiceRef);
        }

        if (isset($settings['exaggeration'])) {
            $tts = $tts->exaggeration((float) $settings['exaggeration']);
        }

        // ->slow() stretches generation time, which makes any trailing
        // hallucination more audible. Skip it for single words.
        if (! $isSingleWord && ! isset($settings['cfg_weight'])) {
            $tts = $tts->slow();
        }

        // For single words the model needs a stricter leash so it sticks to
        // the text. The "single_word_*" values on the voice config supply
        // that stricter setting. If they are not set, fall back to the
        // voice's base temperature/cfg_weight.
        $temperature = isset($settings['temperature']) ? (float) $settings['temperature'] : null;
        $cfgWeight = isset($settings['cfg_weight']) ? (float) $settings['cfg_weight'] : null;

        if ($isSingleWord) {
            if (isset($settings['single_word_temperature'])) {
                $temperature = (float) $settings['single_word_temperature'];
            }
            if (isset($settings['single_word_cfg_weight'])) {
                $cfgWeight = (float) $settings['single_word_cfg_weight'];
            }
        }

        if ($temperature !== null) {
            $tts = $tts->temperature($temperature);
        }

        if ($cfgWeight !== null) {
            $tts = $tts->cfgWeight($cfgWeight);
        }

        if (isset($settings['seed'])) {
            $tts = $tts->seed((int) $settings['seed']);
        }

        $model = $settings['model'] ?? null;

        if ($isJapanese || $model === 'chatterbox-multilingual') {
            $tts = $tts->multilingual();
        } elseif ($model === 'chatterbox-turbo') {
            $tts = $tts->turbo();
        } elseif ($model === 'chatterbox') {
            $tts = $tts->standard();
        }

        if ($isJapanese) {
            $tts = $tts->japanese();
        }

        $result = $tts->generate();

        // Chatterbox always pads its output to a minimum length. When the
        // spoken word ends earlier than that, the model fills the rest with
        // low-level noise / instrumental-sounding garbage. Trim it off before
        // saving, unless the voice config explicitly disables trimming.
        $wavBytes = file_get_contents($result->getPath());

        $storagePath = 'vocab/words/audio/'.Str::uuid().'.wav';

        if (($settings['trim_trailing_noise'] ?? true)) {
            $wavBytes = $this->trimTrailingNoise($wavBytes);
        }

        Storage::disk('public')->put($storagePath, $wavBytes);

        // Converting existing generated wav audio to mp3
        $tts->convertAudio(
            Storage::disk('public')->path($storagePath),
            Storage::disk('public')->path(str_replace('.wav', '.mp3', $storagePath)),
            'mp3',
            [
                'bitrate' => 64,
            ]
        );

        // deleting original wav file from storage
        @unlink(Storage::disk('public')->path($storagePath));

        $storagePath = str_replace('.wav', '.mp3', $storagePath);

        if ($vocabulary->$field) {
            Storage::disk('public')->delete($vocabulary->$field);
        }

        $vocabulary->update([$field => $storagePath]);

        return $storagePath;
    }

    // Cuts the silent/noisy padding the TTS engine leaves at the end of a clip.
    //
    // How it works:
    //   1. Parse the WAV header to find the audio samples. Supports both 16-bit
    //      PCM and 32-bit float (Chatterbox writes float32).
    //   2. Scan samples from the END of the file backward, looking for the last
    //      point where the audio is actually loud enough to be speech (above the
    //      noise threshold) and stays loud for a short window (so a single click
    //      is not mistaken for the end of the word).
    //   3. Keep a small "tail" after that point so the natural release of the
    //      voice is not clipped, then apply a brief fade-out so the cut does not
    //      cause an audible pop.
    //   4. Rewrite the WAV with the shortened data and corrected size fields.
    //
    // Knobs if results need tuning:
    //   $threshold  -> raise to cut more aggressively, lower to keep more audio
    //   $tailMs     -> extra silence kept after the last detected speech sample
    //   $fadeMs     -> length of the fade-out applied at the very end
    private function trimTrailingNoise(string $wav): string
    {
        if (strlen($wav) < 44 || substr($wav, 0, 4) !== 'RIFF' || substr($wav, 8, 4) !== 'WAVE') {
            return $wav;
        }

        $offset = 12;
        $sampleRate = 24000;
        $channels = 1;
        $bitsPerSample = 16;
        $audioFormat = 1;
        $dataOffset = null;
        $dataSize = null;

        while ($offset + 8 <= strlen($wav)) {
            $chunkId = substr($wav, $offset, 4);
            $chunkSize = unpack('V', substr($wav, $offset + 4, 4))[1];
            if ($chunkId === 'fmt ') {
                $audioFormat = unpack('v', substr($wav, $offset + 8, 2))[1];
                $channels = unpack('v', substr($wav, $offset + 10, 2))[1];
                $sampleRate = unpack('V', substr($wav, $offset + 12, 4))[1];
                $bitsPerSample = unpack('v', substr($wav, $offset + 22, 2))[1];
            } elseif ($chunkId === 'data') {
                $dataOffset = $offset + 8;
                $dataSize = $chunkSize;
                break;
            }
            $offset += 8 + $chunkSize + ($chunkSize % 2);
        }

        $isFloat32 = ($audioFormat === 3 && $bitsPerSample === 32);
        $isPcm16 = ($audioFormat === 1 && $bitsPerSample === 16);

        if ($dataOffset === null || (! $isFloat32 && ! $isPcm16)) {
            return $wav;
        }

        $bytesPerSample = ($bitsPerSample / 8) * $channels;
        $totalSamples = intdiv($dataSize, $bytesPerSample);
        $pcm = substr($wav, $dataOffset, $dataSize);

        $readSample = $isFloat32
            ? fn (int $i) => unpack('g', substr($pcm, $i * $bytesPerSample, 4))[1]
            : fn (int $i) => unpack('s', substr($pcm, $i * $bytesPerSample, 2))[1] / 32768.0;

        $threshold = 0.018;
        $windowMs = 30;
        $windowSize = max(1, (int) ($sampleRate * $windowMs / 1000));
        $requiredHits = max(1, (int) ($windowSize * 0.2));

        $lastVoiced = -1;
        for ($i = $totalSamples - 1; $i >= 0; $i--) {
            if (abs($readSample($i)) >= $threshold) {
                $hits = 1;
                $from = max(0, $i - $windowSize + 1);
                for ($j = $i - 1; $j >= $from; $j--) {
                    if (abs($readSample($j)) >= $threshold) {
                        $hits++;
                    }
                    if ($hits >= $requiredHits) {
                        break;
                    }
                }
                if ($hits >= $requiredHits) {
                    $lastVoiced = $i;
                    break;
                }
            }
        }

        if ($lastVoiced < 0) {
            return $wav;
        }

        $tailMs = 120;
        $tailSamples = (int) ($sampleRate * $tailMs / 1000);
        $keepSamples = min($totalSamples, $lastVoiced + 1 + $tailSamples);
        $fadeMs = 40;
        $fadeSamples = min($tailSamples, (int) ($sampleRate * $fadeMs / 1000));

        $newPcm = substr($pcm, 0, $keepSamples * $bytesPerSample);

        if ($fadeSamples > 0 && $keepSamples >= $fadeSamples) {
            $fadeStart = $keepSamples - $fadeSamples;
            for ($i = 0; $i < $fadeSamples; $i++) {
                $idx = ($fadeStart + $i) * $bytesPerSample;
                $gain = 1 - ($i / $fadeSamples);
                if ($isFloat32) {
                    $sample = unpack('g', substr($newPcm, $idx, 4))[1];
                    $newPcm = substr_replace($newPcm, pack('g', $sample * $gain), $idx, 4);
                } else {
                    $sample = unpack('s', substr($newPcm, $idx, 2))[1];
                    $newPcm = substr_replace($newPcm, pack('s', (int) ($sample * $gain)), $idx, 2);
                }
            }
        }

        $newDataSize = strlen($newPcm);
        $header = substr($wav, 0, $dataOffset - 8);
        $rebuilt = $header.'data'.pack('V', $newDataSize).$newPcm;
        $rebuilt = substr_replace($rebuilt, pack('V', strlen($rebuilt) - 8), 4, 4);

        return $rebuilt;
    }
}
