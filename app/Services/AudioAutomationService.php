<?php

namespace App\Services;

use App\Models\AudioGenerationFailure;
use App\Models\Vocabulary;
use Illuminate\Support\Facades\DB;

class AudioAutomationService
{
    public const FIELDS = [
        'audio_jp' => 'word_jp',
        'audio_en' => 'word_en',
        'sentence_audio_jp' => 'sentence_jp',
        'sentence_audio_en' => 'sentence_en',
    ];

    public function findNextPending(): ?array
    {
        foreach (self::FIELDS as $audioField => $textField) {
            $vocab = Vocabulary::query()
                ->where('is_approved', true)
                ->whereNull($audioField)
                ->whereNotNull($textField)
                ->where($textField, '!=', '')
                ->whereNotExists(function ($q) use ($audioField) {
                    $q->select(DB::raw(1))
                      ->from('audio_generation_failures')
                      ->whereColumn('audio_generation_failures.vocab_id', 'vocab_words.id')
                      ->where('audio_generation_failures.field', $audioField)
                      ->where('audio_generation_failures.attempts', '>=', AudioGenerationFailure::MAX_ATTEMPTS);
                })
                ->orderBy('id')
                ->first();

            if ($vocab) {
                return ['vocab' => $vocab, 'field' => $audioField];
            }
        }

        return null;
    }

    public function pendingCounts(): array
    {
        $counts = [];
        foreach (self::FIELDS as $audioField => $textField) {
            $counts[$audioField] = Vocabulary::query()
                ->where('is_approved', true)
                ->whereNull($audioField)
                ->whereNotNull($textField)
                ->where($textField, '!=', '')
                ->count();
        }
        return $counts;
    }

    public function recordFailure(int $vocabId, string $field, string $error): void
    {
        $failure = AudioGenerationFailure::firstOrNew([
            'vocab_id' => $vocabId,
            'field' => $field,
        ]);

        $failure->attempts = ($failure->attempts ?? 0) + 1;
        $failure->last_error = $error;
        $failure->last_attempt_at = now();
        $failure->save();
    }

    public function clearFailure(int $vocabId, string $field): void
    {
        AudioGenerationFailure::where('vocab_id', $vocabId)
            ->where('field', $field)
            ->delete();
    }
}
