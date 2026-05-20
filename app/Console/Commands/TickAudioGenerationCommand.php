<?php

namespace App\Console\Commands;

use App\Jobs\GenerateNextVocabAudioJob;
use App\Models\AudioSetting;
use Illuminate\Console\Command;

class TickAudioGenerationCommand extends Command
{
    protected $signature = 'audio:tick';

    protected $description = 'Dispatch a single audio generation job if automation is running';

    public function handle(): int
    {
        $setting = AudioSetting::current();

        if (!$setting->is_running) {
            $this->info('Audio automation is stopped.');
            return self::SUCCESS;
        }

        GenerateNextVocabAudioJob::dispatch();
        $this->info('Audio generation job dispatched.');

        return self::SUCCESS;
    }
}
