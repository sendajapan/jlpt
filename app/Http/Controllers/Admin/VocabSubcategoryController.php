<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreVocabSubcategoryRequest;
use App\Http\Requests\Admin\UpdateVocabSubcategoryRequest;
use App\Models\VocabSubcategory;
use App\Services\VocabSubcategoryService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class VocabSubcategoryController extends Controller
{
    public function __construct(private VocabSubcategoryService $service) {}

    public function index(Request $request): View
    {
        $subcategories = $this->service->getAll($request->input('search'));

        return view('admin.vocab.subcategories.index', compact('subcategories'));
    }

    public function create(): View
    {
        $categories = $this->service->getAllCategories();

        return view('admin.vocab.subcategories.create', compact('categories'));
    }

    public function store(StoreVocabSubcategoryRequest $request): RedirectResponse
    {
        $data = $request->validated();
        $data['icon_path']           = $this->storeImage($request, 'icon_path', 'vocab/subcategories/icons');
        $data['icon_thumbnail_path'] = $this->storeThumbnail($request->file('icon_path'), 'vocab/subcategories/icons');
        $data['audio_path']          = $this->storeFile($request, 'audio_path', 'vocab/subcategories/audio');

        $this->service->create($data);

        notify()->success()->title('Subcategory created successfully.')->send();

        return redirect()->route('admin.vocab.subcategories.index');
    }

    public function edit(VocabSubcategory $vocabSubcategory): View
    {
        $categories = $this->service->getAllCategories();

        return view('admin.vocab.subcategories.edit', ['subcategory' => $vocabSubcategory, 'categories' => $categories]);
    }

    public function update(UpdateVocabSubcategoryRequest $request, VocabSubcategory $vocabSubcategory): RedirectResponse
    {
        $data = $request->validated();
        $data['icon_path']           = $this->replaceImage($request, 'icon_path', 'vocab/subcategories/icons', $vocabSubcategory->icon_path);
        $data['icon_thumbnail_path'] = $this->replaceThumbnail($request->file('icon_path'), 'vocab/subcategories/icons', $vocabSubcategory->icon_thumbnail_path);
        $data['audio_path']          = $this->replaceFile($request, 'audio_path', 'vocab/subcategories/audio', $vocabSubcategory->audio_path);

        $this->service->update($vocabSubcategory, $data);

        notify()->success()->title('Subcategory updated successfully.')->send();

        return redirect()->route('admin.vocab.subcategories.index');
    }

    public function updateIcon(Request $request, VocabSubcategory $vocabSubcategory): RedirectResponse
    {
        $request->validate(['icon_path' => ['required', 'image', 'max:2048']]);
        $this->deleteFile($vocabSubcategory->icon_path);
        $this->deleteFile($vocabSubcategory->icon_thumbnail_path);

        $file = $request->file('icon_path');
        $vocabSubcategory->update([
            'icon_path'           => $this->saveImageFile($file, 'vocab/subcategories/icons'),
            'icon_thumbnail_path' => $this->storeThumbnail($file, 'vocab/subcategories/icons'),
        ]);

        notify()->success()->title('Icon updated.')->send();

        return back();
    }

    public function destroy(VocabSubcategory $vocabSubcategory): RedirectResponse
    {
        $this->deleteFile($vocabSubcategory->icon_path);
        $this->deleteFile($vocabSubcategory->icon_thumbnail_path);
        $this->deleteFile($vocabSubcategory->audio_path);
        $this->service->delete($vocabSubcategory);

        notify()->success()->title('Subcategory deleted.')->send();

        return redirect()->route('admin.vocab.subcategories.index');
    }
}
