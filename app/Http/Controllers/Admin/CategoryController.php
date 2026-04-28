<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreCategoryRequest;
use App\Http\Requests\Admin\UpdateCategoryRequest;
use App\Models\Category;
use App\Services\CategoryService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;
use Mckenziearts\Notify\Exceptions\InvalidNotificationException;

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

    /**
     * @throws InvalidNotificationException
     */
    public function store(StoreCategoryRequest $request): RedirectResponse
    {
        $data = $request->validated();
        $data['icon_path'] = $this->storeFile($request, 'icon_path', 'categories/icons');
        $data['audio_path'] = $this->storeFile($request, 'audio_path', 'categories/audio');

        $this->service->create($data);

        notify()->success()->title('Category created successfully.')->send();

        return redirect()->route('admin.categories.index');
    }

    public function edit(Category $category): View
    {
        return view('admin.categories.edit', compact('category'));
    }

    public function update(UpdateCategoryRequest $request, Category $category): RedirectResponse
    {
        $data = $request->validated();
        $data['icon_path'] = $this->replaceFile($request, 'icon_path', 'categories/icons', $category->icon_path);
        $data['audio_path'] = $this->replaceFile($request, 'audio_path', 'categories/audio', $category->audio_path);

        $this->service->update($category, $data);

        notify()->success()->title('Category updated successfully.')->send();

        return redirect()->route('admin.categories.index');
    }

    /**
     * @throws InvalidNotificationException
     */
    public function updateIcon(Request $request, Category $category): RedirectResponse
    {
        $request->validate(['icon_path' => ['required', 'image', 'max:2048']]);
        $this->deleteFile($category->icon_path);
        $category->update(['icon_path' => $request->file('icon_path')->store('categories/icons', 'public')]);

        notify()->success()->title('Icon updated.')->send();

        return back();
    }

    public function destroy(Category $category): RedirectResponse
    {
        $this->deleteFile($category->icon_path);
        $this->deleteFile($category->audio_path);
        $this->service->delete($category);

        notify()->success()->title('Category deleted.')->send();

        return redirect()->route('admin.categories.index');
    }
}
