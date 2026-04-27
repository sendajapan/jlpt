<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreSubcategoryRequest;
use App\Http\Requests\Admin\UpdateSubcategoryRequest;
use App\Models\Subcategory;
use App\Services\SubcategoryService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use Illuminate\View\View;

class SubcategoryController extends Controller
{
    public function __construct(private SubcategoryService $service) {}

    public function index(Request $request): View
    {
        $subcategories = $this->service->getAll($request->input('search'));
        return view('admin.subcategories.index', compact('subcategories'));
    }

    public function create(): View
    {
        $categories = $this->service->getAllCategories();
        return view('admin.subcategories.create', compact('categories'));
    }

    public function store(StoreSubcategoryRequest $request): RedirectResponse
    {
        $data = $request->validated();
        $data['icon_path']  = $this->storeFile($request, 'icon_path',  'subcategories/icons');
        $data['audio_path'] = $this->storeFile($request, 'audio_path', 'subcategories/audio');

        $this->service->create($data);
        return redirect()->route('admin.subcategories.index')
            ->with('success', 'Subcategory created successfully.');
    }

    public function edit(Subcategory $subcategory): View
    {
        $categories = $this->service->getAllCategories();
        return view('admin.subcategories.edit', compact('subcategory', 'categories'));
    }

    public function update(UpdateSubcategoryRequest $request, Subcategory $subcategory): RedirectResponse
    {
        $data = $request->validated();

        if ($request->hasFile('icon_path')) {
            $this->deleteFile($subcategory->icon_path);
            $data['icon_path'] = $this->storeFile($request, 'icon_path', 'subcategories/icons');
        } else {
            unset($data['icon_path']);
        }

        if ($request->hasFile('audio_path')) {
            $this->deleteFile($subcategory->audio_path);
            $data['audio_path'] = $this->storeFile($request, 'audio_path', 'subcategories/audio');
        } else {
            unset($data['audio_path']);
        }

        $this->service->update($subcategory, $data);
        return redirect()->route('admin.subcategories.index')
            ->with('success', 'Subcategory updated successfully.');
    }

    public function destroy(Subcategory $subcategory): RedirectResponse
    {
        $this->deleteFile($subcategory->icon_path);
        $this->deleteFile($subcategory->audio_path);
        $this->service->delete($subcategory);
        return redirect()->route('admin.subcategories.index')
            ->with('success', 'Subcategory deleted successfully.');
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
