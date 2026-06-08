<?php

namespace App\Observers;

use App\Models\AppUser;
use App\Services\WordUnlockService;

class AppUserObserver
{
    public function __construct(private WordUnlockService $unlocks) {}

    public function created(AppUser $appUser): void
    {
        $this->unlocks->seedFreeWordsForUser($appUser);
    }
}
