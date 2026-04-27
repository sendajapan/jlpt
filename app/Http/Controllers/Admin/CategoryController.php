<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreCategoryRequest;
use App\Http\Requests\Admin\UpdateCategoryRequest;
use App\Models\Category;
use App\Services\CategoryService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use Illuminate\View\View;

class CategoryController extends Controller
{
    public function __construct(private CategoryService $service) {}

    public function index(Request $request): View
    {
        $categories = $this->service->getAll($request->input('search'));
        return view('admin.categories.index', compact('categories'));
    }

    public function create(): View
    {
        return view('admin.categories.create');
    }

    public function store(StoreCategoryRequest $request): RedirectResponse
    {
        $data = $request->validated();
        $data['icon_path']  = $this->storeFile($request, 'icon_path',  'categories/icons');
        $data['audio_path'] = $this->storeFile($request, 'audio_path', 'categories/audio');

        $this->service->create($data);
        return redirect()->route('admin.categories.index')
            ->with('success', 'Category created successfully.');
    }

    public function edit(Category $category): View
    {
        return view('admin.categories.edit', compact('category'));
    }

    public function update(UpdateCategoryRequest $request, Category $category): RedirectResponse
    {
        $data = $request->validated();

        if ($request->hasFile('icon_path')) {
            $this->deleteFile($category->icon_path);
            $data['icon_path'] = $this->storeFile($request, 'icon_path', 'categories/icons');
        } else {
            unset($data['icon_path']);
        }

        if ($request->hasFile('audio_path')) {
            $this->deleteFile($category->audio_path);
            $data['audio_path'] = $this->storeFile($request, 'audio_path', 'categories/audio');
        } else {
            unset($data['audio_path']);
        }

        $this->service->update($category, $data);
        return redirect()->route('admin.categories.index')
            ->with('success', 'Category updated successfully.');
    }

    public function destroy(Category $category): RedirectResponse
    {
        $this->deleteFile($category->icon_path);
        $this->deleteFile($category->audio_path);
        $this->service->delete($category);
        return redirect()->route('admin.categories.index')
            ->with('success', 'Category deleted successfully.');
    }

    private function storeFile(Request $request, string $field, string $folder): ?string
    {
        if (!$request->hasFile($field)) return null;
        return $request->file($field)->store($folder, 'public');
    }

    private function deleteFile(?string $path): void
    {
        if ($path) Storage::disk('public')->delete($path);
    }
}
