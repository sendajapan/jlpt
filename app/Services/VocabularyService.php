<?php

namespace App\Services;

use App\Models\VocabCategory;
use App\Models\VocabSubcategory;
use App\Models\Vocabulary;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Pagination\LengthAwarePaginator;

class VocabularyService
{
    public function getAll(array $filters = [], int $perPage = 100): LengthAwarePaginator
    {
        return Vocabulary::query()
            ->with('subcategory.category')
            ->when($filters['search'] ?? null, fn ($q, $s) => $q->where('word_jp', 'like', "%{$s}%")
                ->orWhere('word_romaji', 'like', "%{$s}%")
                ->orWhere('word_en', 'like', "%{$s}%"))
            ->when($filters['subcategory_id'] ?? null, fn ($q, $v) => $q->where('vocab_subcategory_id', $v))
            ->when($filters['category_id'] ?? null, fn ($q, $v) => $q->whereHas('subcategory', fn ($s) => $s->where('vocab_category_id', $v)))
            ->when(isset($filters['is_approved']) && $filters['is_approved'] !== '', fn ($q) => $q->where('is_approved', $filters['is_approved']))
            ->when(isset($filters['image_path']) && $filters['image_path'] === 'images', fn ($q) => $q->whereNot('image_path', ''))
            ->when(isset($filters['image_path']) && $filters['image_path'] === 'pending', fn ($q) => $q->where('image_path', NULL))
            ->orderBy('sort_order')
            ->orderBy('word_jp')
            ->paginate($perPage)
            ->withQueryString();
    }

    public function getAllCategories(): Collection
    {
        return VocabCategory::orderBy('name_en')->get();
    }

    public function getAllSubcategories(): Collection
    {
        return VocabSubcategory::with('category')->orderBy('name_en')->get();
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
