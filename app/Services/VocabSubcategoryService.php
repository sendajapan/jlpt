<?php

namespace App\Services;

use App\Models\VocabCategory;
use App\Models\VocabSubcategory;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Pagination\LengthAwarePaginator;

class VocabSubcategoryService
{
    public function getAll(?string $search = null, int $perPage = 15): LengthAwarePaginator
    {
        return VocabSubcategory::query()
            ->with('category')
            ->when($search, fn ($q) => $q->where('name_en', 'like', "%{$search}%")
                ->orWhere('name_jp', 'like', "%{$search}%"))
            ->orderBy('sort_order')
            ->orderBy('name_en')
            ->paginate($perPage)
            ->withQueryString();
    }

    public function getAllCategories(): Collection
    {
        return VocabCategory::orderBy('name_en')->get();
    }

    public function create(array $data): VocabSubcategory
    {
        return VocabSubcategory::create($data);
    }

    public function update(VocabSubcategory $vocabSubcategory, array $data): VocabSubcategory
    {
        $vocabSubcategory->update($data);
        return $vocabSubcategory;
    }

    public function delete(VocabSubcategory $vocabSubcategory): void
    {
        $vocabSubcategory->delete();
    }
}
