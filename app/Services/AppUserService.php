<?php

namespace App\Services;

use App\Models\AppUser;
use Illuminate\Pagination\LengthAwarePaginator;

class AppUserService
{
    public function getAll(
        ?string $search = null,
        ?string $status = null,
        ?string $provider = null,
        int $perPage = 30,
    ): LengthAwarePaginator {
        return AppUser::query()
            ->when($search, fn ($q) => $q->where(fn ($q) => $q
                ->where('username', 'like', "%{$search}%")
                ->orWhere('email', 'like', "%{$search}%")
                ->orWhere('full_name', 'like', "%{$search}%")))
            ->when($status === 'banned', fn ($q) => $q->where('is_banned', true))
            ->when($status === 'verified', fn ($q) => $q->where('is_verified', true)->where('is_banned', false))
            ->when($status === 'unverified', fn ($q) => $q->where('is_verified', false)->where('is_banned', false))
            ->when($provider, fn ($q) => $q->where('login_provider', $provider))
            ->latest()
            ->paginate($perPage)
            ->withQueryString();
    }

    public function update(AppUser $appUser, array $data): AppUser
    {
        $appUser->update($data);
        return $appUser;
    }
}
