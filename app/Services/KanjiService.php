<?php

namespace App\Services;

use App\Models\Kanji;
use Illuminate\Database\Eloquent\Builder;

class KanjiService
{
    public function getAll(array $filters = []): Builder
    {
        return Kanji::query()
            ->when($filters['search'] ?? null, fn ($q, $s) => $q->where(fn ($w) => $w
                ->where('kanji', 'like', "%{$s}%")
                ->orWhere('translate', 'like', "%{$s}%")
                ->orWhere('meanings', 'like', "%{$s}%")
            ))
            ->when($filters['jlpt'] ?? null, fn ($q, $v) => $q->where('jlpt', $v))
            ->when($filters['level'] ?? null, fn ($q, $v) => $q->where('level', $v))
            ->when(isset($filters['is_premium']) && $filters['is_premium'] !== '', fn ($q) => $q->where('is_premium', $filters['is_premium']))
            ->orderBy('id');
    }

    public function create(array $data): Kanji
    {
        return Kanji::create($data);
    }

    public function update(Kanji $kanji, array $data): Kanji
    {
        $kanji->update($data);

        return $kanji;
    }

    public function delete(Kanji $kanji): void
    {
        $kanji->delete();
    }
}
