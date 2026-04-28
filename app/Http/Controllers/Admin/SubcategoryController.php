<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreSubcategoryRequest;
use App\Http\Requests\Admin\UpdateSubcategoryRequest;
use App\Models\Subcategory;
use App\Services\SubcategoryService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
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
        $data['icon_path'] = $this->storeFile($request, 'icon_path', 'subcategories/icons');
        $data['audio_path'] = $this->storeFile($request, 'audio_path', 'subcategories/audio');

        $this->service->create($data);

        notify()->success()->title('Subcategory created successfully.')->send();

        return redirect()->route('admin.subcategories.index');
    }

    public function edit(Subcategory $subcategory): View
    {
        $categories = $this->service->getAllCategories();

        return view('admin.subcategories.edit', compact('subcategory', 'categories'));
    }

    public function update(UpdateSubcategoryRequest $request, Subcategory $subcategory): RedirectResponse
    {
        $data = $request->validated();
        $data['icon_path'] = $this->replaceFile($request, 'icon_path', 'subcategories/icons', $subcategory->icon_path);
        $data['audio_path'] = $this->replaceFile($request, 'audio_path', 'subcategories/audio', $subcategory->audio_path);

        $this->service->update($subcategory, $data);

        notify()->success()->title('Subcategory updated successfully.')->send();

        return redirect()->route('admin.subcategories.index');
    }

    public function updateIcon(Request $request, Subcategory $subcategory): RedirectResponse
    {
        $request->validate(['icon_path' => ['required', 'image', 'max:2048']]);
        $this->deleteFile($subcategory->icon_path);
        $subcategory->update(['icon_path' => $request->file('icon_path')->store('subcategories/icons', 'public')]);

        notify()->success()->title('Icon updated.')->send();

        return back();
    }

    public function destroy(Subcategory $subcategory): RedirectResponse
    {
        $this->deleteFile($subcategory->icon_path);
        $this->deleteFile($subcategory->audio_path);
        $this->service->delete($subcategory);

        notify()->success()->title('Subcategory deleted.')->send();

        return redirect()->route('admin.subcategories.index');
    }
}
