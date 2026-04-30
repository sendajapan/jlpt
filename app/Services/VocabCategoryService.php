<?php

namespace App\Services;

use App\Models\VocabCategory;
use Illuminate\Pagination\LengthAwarePaginator;

class VocabCategoryService
{
    public function getAll(?string $search = null, int $perPage = 100): LengthAwarePaginator
    {
        return VocabCategory::query()
            ->when($search, fn ($q) => $q->where('name_en', 'like', "%{$search}%")
                ->orWhere('name_jp', 'like', "%{$search}%"))
            ->orderBy('sort_order')
            ->orderBy('name_en')
            ->paginate($perPage)
            ->withQueryString();
    }

    public function create(array $data): VocabCategory
    {
        return VocabCategory::create($data);
    }

    public function update(VocabCategory $vocabCategory, array $data): VocabCategory
    {
        $vocabCategory->update($data);
        return $vocabCategory;
    }

    public function delete(VocabCategory $vocabCategory): void
    {
        $vocabCategory->delete();
    }
}
