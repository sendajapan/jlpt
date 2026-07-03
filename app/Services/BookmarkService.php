<?php

namespace App\Services;

use App\Models\AppUser;
use App\Models\Vocabulary;

class BookmarkService
{
    public function add(AppUser $user, Vocabulary $vocab): void
    {
        $user->bookmarks()->syncWithoutDetaching([$vocab->id]);
    }

    public function remove(AppUser $user, Vocabulary $vocab): void
    {
        $user->bookmarks()->detach($vocab->id);
    }

    public function list(AppUser $user, int $perPage = 20)
    {
        return $user->bookmarks()
            ->orderByPivot('created_at', 'desc')
            ->paginate($perPage);
    }
}
