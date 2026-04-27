<?php

namespace App\Services;

use App\Models\Category;
use App\Models\Subcategory;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Pagination\LengthAwarePaginator;

class SubcategoryService
{
    public function getAll(?string $search = null, int $perPage = 15): LengthAwarePaginator
    {
        return Subcategory::query()
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
        return Category::orderBy('name_en')->get();
    }

    public function create(array $data): Subcategory
    {
        return Subcategory::create($data);
    }

    public function update(Subcategory $subcategory, array $data): Subcategory
    {
        $subcategory->update($data);
        return $subcategory;
    }

    public function delete(Subcategory $subcategory): void
    {
        $subcategory->delete();
    }
}
