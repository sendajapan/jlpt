<?php

namespace App\Services;

use App\Models\AppUser;
use App\Models\Vocabulary;

class FavoriteService
{
    public function add(AppUser $user, Vocabulary $vocab): void
    {
        $user->favorites()->syncWithoutDetaching([$vocab->id]);
    }

    public function remove(AppUser $user, Vocabulary $vocab): void
    {
        $user->favorites()->detach($vocab->id);
    }

    public function list(AppUser $user, int $perPage = 20)
    {
        return $user->favorites()
            ->orderByPivot('created_at', 'desc')
            ->paginate($perPage);
    }
}
