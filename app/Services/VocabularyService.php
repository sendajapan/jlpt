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
    public function getAll(array $filters = [], int $perPage = 100): LengthAwarePaginator
    {
        return Vocabulary::query()
            ->with('subcategory.category')
            ->leftJoin('vocab_bgs', 'vocab_bgs.vocab_bg_id', '=', 'image_thumbnail_bg')
            ->when($filters['search'] ?? null, fn ($q, $s) => $q->where('word_jp', 'like', "%{$s}%")
                ->orWhere('word_romaji', 'like', "%{$s}%")
                ->orWhere('word_en', 'like', "%{$s}%"))
            ->when($filters['subcategory_id'] ?? null, fn ($q, $v) => $q->where('vocab_subcategory_id', $v))
            ->when($filters['category_id'] ?? null, fn ($q, $v) => $q->whereHas('subcategory', fn ($s) => $s->where('vocab_category_id', $v)))
            ->when(isset($filters['is_approved']) && $filters['is_approved'] !== '', fn ($q) => $q->where('is_approved', $filters['is_approved']))
            ->when(isset($filters['image_path']) && $filters['image_path'] === 'images', fn ($q) => $q->whereNot('image_path', ''))
            ->when(isset($filters['image_path']) && $filters['image_path'] === 'pending', fn ($q) => $q->where('image_path', null))
            ->orderBy('sort_order')
            ->orderBy('word_jp')
            ->paginate($perPage)
            ->withQueryString();
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

        if (isset($settings['cfg_weight'])) {
            $tts = $tts->cfgWeight((float) $settings['cfg_weight']);
        } else {
            $tts = $tts->slow();
        }

        if (isset($settings['temperature'])) {
            $tts = $tts->temperature((float) $settings['temperature']);
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
        $storagePath = 'vocab/words/audio/' . Str::uuid() . '.wav';

        Storage::disk('public')->put($storagePath, file_get_contents($result->getPath()));
        @unlink($result->getPath());

        if ($vocabulary->$field) {
            Storage::disk('public')->delete($vocabulary->$field);
        }

        $vocabulary->update([$field => $storagePath]);

        return $storagePath;
    }
}
