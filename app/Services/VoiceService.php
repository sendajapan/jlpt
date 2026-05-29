<?php

namespace App\Services;

use App\Models\Voice;
use Illuminate\Pagination\LengthAwarePaginator;

class VoiceService
{
    public function getAll(?string $search = null, int $perPage = 100): LengthAwarePaginator
    {
        return Voice::query()
            ->when($search, fn ($q) => $q->where('name', 'like', "%{$search}%")
                ->orWhere('language', 'like', "%{$search}%"))
            ->orderBy('sort_order')
            ->orderBy('name')
            ->paginate($perPage)
            ->withQueryString();
    }

    public function create(array $data): Voice
    {
        if (! empty($data['is_default'])) {
            Voice::query()->update(['is_default' => false]);
        }
        return Voice::create($data);
    }

    public function update(Voice $voice, array $data): Voice
    {
        if (! empty($data['is_default'])) {
            Voice::query()->where('id', '!=', $voice->id)->update(['is_default' => false]);
        }
        $voice->update($data);
        return $voice;
    }

    public function toggleDefault(Voice $voice): Voice
    {
        if ($voice->is_default) {
            $voice->update(['is_default' => false]);
            return $voice;
        }

        Voice::query()->where('id', '!=', $voice->id)->update(['is_default' => false]);
        $voice->update(['is_default' => true]);

        return $voice;
    }

    public function delete(Voice $voice): void
    {
        $voice->delete();
    }
}
