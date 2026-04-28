<?php

namespace App\Services;

use App\Models\Category;
use App\Models\Subcategory;
use App\Models\Vocabulary;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Pagination\LengthAwarePaginator;

class VocabularyService
{
    public function getAll(array $filters = [], int $perPage = 15): LengthAwarePaginator
    {
        return Vocabulary::query()
            ->with('subcategory.category')
            ->when($filters['search'] ?? null, fn ($q, $s) => $q->where('word_jp', 'like', "%{$s}%")
                ->orWhere('word_romaji', 'like', "%{$s}%")
                ->orWhere('word_en', 'like', "%{$s}%"))
            ->when($filters['subcategory_id'] ?? null, fn ($q, $v) => $q->where('subcategory_id', $v))
            ->when($filters['category_id'] ?? null, fn ($q, $v) => $q->whereHas('subcategory', fn ($s) => $s->where('category_id', $v)))
            ->when(isset($filters['is_approved']) && $filters['is_approved'] !== '', fn ($q) => $q->where('is_approved', $filters['is_approved']))
            ->orderBy('sort_order')
            ->orderBy('word_jp')
            ->paginate($perPage)
            ->withQueryString();
    }

    public function getAllCategories(): Collection
    {
        return Category::orderBy('name_en')->get();
    }

    public function getAllSubcategories(): Collection
    {
        return Subcategory::with('category')->orderBy('name_en')->get();
    }

    public function create(array $data): Vocabulary
    {
        return Vocabulary::create($data);
    }

    public function update(Vocabulary $vocabulary, array $data): Vocabulary
    {
        $vocabulary->update($data);
        return $vocabulary;
    }

    public function delete(Vocabulary $vocabulary): void
    {
        $vocabulary->delete();
    }
}
