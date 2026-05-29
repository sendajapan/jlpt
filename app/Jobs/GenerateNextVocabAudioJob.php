<?php

namespace App\Jobs;

use App\Models\AudioSetting;
use App\Services\AudioAutomationService;
use App\Services\VocabularyService;
use Illuminate\Bus\Queueable;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Queue\Middleware\WithoutOverlapping;
use Illuminate\Queue\SerializesModels;
use Illuminate\Support\Facades\Log;

class GenerateNextVocabAudioJob implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    public int $timeout = 600;
    public int $tries = 1;

    public function middleware(): array
    {
        return [(new WithoutOverlapping('vocab-audio-generation'))->expireAfter(1800)];
    }

    public function handle(
        AudioAutomationService $automation,
        VocabularyService $vocabService
    ): void {
        $setting = AudioSetting::current();

        if (!$setting->is_running) {
            return;
        }

        $next = $automation->findNextPending();

        if (!$next) {
            $setting->update(['last_run_at' => now()]);
            return;
        }

        /** @var \App\Models\Vocabulary $vocab */
        $vocab = $next['vocab'];
        $field = $next['field'];
        $voiceId = $vocab->voice_id;

        try {
            $vocabService->generateAudio($vocab, $field, $voiceId);

            $automation->clearFailure($vocab->id, $field);

            $setting->update([
                'generated_count' => $setting->generated_count + 1,
                'last_run_at' => now(),
                'last_error' => null,
            ]);
        } catch (\Throwable $e) {
            Log::warning('Audio generation failed', [
                'vocab_id' => $vocab->id,
                'field' => $field,
                'error' => $e->getMessage(),
            ]);

            $automation->recordFailure($vocab->id, $field, $e->getMessage());

            $setting->update([
                'last_run_at' => now(),
                'last_error' => "Vocab #{$vocab->id} {$field}: " . $e->getMessage(),
            ]);
        }
    }
}
