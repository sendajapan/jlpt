<?php

namespace App\Services;

use App\Models\Avatar;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Pagination\LengthAwarePaginator;

class AvatarService
{
    public function all(?string $search = null, int $perPage = 50): LengthAwarePaginator
    {
        return Avatar::query()
            ->when($search, fn ($q) => $q->where('name', 'like', "%{$search}%"))
            ->orderBy('sort_order')
            ->orderBy('name')
            ->paginate($perPage)
            ->withQueryString();
    }

    public function allActive(): Collection
    {
        return Avatar::where('is_active', true)
            ->orderBy('sort_order')
            ->orderBy('name')
            ->get();
    }

    public function randomFree(): ?Avatar
    {
        return Avatar::where('coin_price', 0)
            ->where('is_active', true)
            ->inRandomOrder()
            ->first();
    }

    public function create(array $data): Avatar
    {
        return Avatar::create($data);
    }

    public function update(Avatar $avatar, array $data): Avatar
    {
        $avatar->update($data);

        return $avatar;
    }

    public function delete(Avatar $avatar): void
    {
        $avatar->delete();
    }
}
