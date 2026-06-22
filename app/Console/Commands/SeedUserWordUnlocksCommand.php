<?php

namespace App\Console\Commands;

use App\Models\AppUser;
use App\Services\WordUnlockService;
use Illuminate\Console\Attributes\Description;
use Illuminate\Console\Attributes\Signature;
use Illuminate\Console\Command;

#[Signature('app:seed-user-word-unlocks')]
#[Description('Seed free word unlock records for all existing users (idempotent)')]
class SeedUserWordUnlocksCommand extends Command
{
    public function handle(WordUnlockService $unlocks): void
    {
        $total = AppUser::count();
        $this->info("Seeding free word unlocks for {$total} users...");

        AppUser::query()->lazyById(100)->each(function (AppUser $user) use ($unlocks) {
            $unlocks->seedFreeWordsForUser($user);
            $this->output->write('.');
        });

        $this->newLine();
        $this->info('Done.');
    }
}
