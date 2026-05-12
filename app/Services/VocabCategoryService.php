<?php

namespace App\Services;

use App\Models\VocabCategory;
use App\Models\VocabBg;
use Illuminate\Pagination\LengthAwarePaginator;
use Illuminate\Database\Eloquent\Collection;

class VocabCategoryService
{
    public function getAll(?string $search = null, int $perPage = 100): LengthAwarePaginator
    {
        return VocabCategory::query()
            ->leftJoin('vocab_bgs', 'vocab_bgs.vocab_bg_id', '=', 'category_bg_path')
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
    public function getAllVocabBg(): Collection
    {
        return VocabBg::orderBy('vocab_bg_sort')->get();
    }

    public function delete(VocabCategory $vocabCategory): void
    {
        $vocabCategory->delete();
    }
}
