<?php

namespace App\Providers;

use App\Models\AppUser;
use App\Observers\AppUserObserver;
use Illuminate\Support\ServiceProvider;

class AppServiceProvider extends ServiceProvider
{
    public function register(): void {}

    public function boot(): void
    {
        AppUser::observe(AppUserObserver::class);
    }
}
