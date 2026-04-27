<?php

namespace App\Services;

use App\Models\Subcategory;
use App\Models\Vocabulary;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Pagination\LengthAwarePaginator;

class VocabularyService
{
    public function getAll(?string $search = null, int $perPage = 15): LengthAwarePaginator
    {
        return Vocabulary::query()
            ->with('subcategory.category')
            ->when($search, fn ($q) => $q->where('word_jp', 'like', "%{$search}%")
                ->orWhere('word_romaji', 'like', "%{$search}%")
                ->orWhere('meaning_en', 'like', "%{$search}%"))
            ->orderBy('sort_order')
            ->orderBy('word_jp')
            ->paginate($perPage)
            ->withQueryString();
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
