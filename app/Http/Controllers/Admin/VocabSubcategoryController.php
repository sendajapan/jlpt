<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreVocabSubcategoryRequest;
use App\Http\Requests\Admin\UpdateVocabSubcategoryRequest;
use App\Models\VocabSubcategory;
use App\Services\VocabSubcategoryService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class VocabSubcategoryController extends Controller
{
    public function __construct(private VocabSubcategoryService $service) {}

    public function index(Request $request): View
    {
        $subcategories = $this->service->getAll(
            $request->input('search'),
            $request->input('category') ? (int) $request->input('category') : null,
            $request->input('type'),
        );
        $categories = $this->service->getAllCategories();

        return view('admin.vocab.subcategories.index', compact('subcategories', 'categories'));
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
        $subcategory = $vocabSubcategory;
        $vocab_bg = $this->service->getAllVocabBg();
        $categories = $this->service->getAllCategories();

        return view('admin.vocab.subcategories.edit', compact('subcategory', 'categories', 'vocab_bg'));
    }

    public function update(UpdateVocabSubcategoryRequest $request, VocabSubcategory $vocabSubcategory): RedirectResponse
    {
        $data = $request->validated();
        $data['icon_path']           = $this->replaceImage($request, 'icon_path', 'vocab/subcategories/icons', $vocabSubcategory->icon_path);
        $data['icon_thumbnail_path'] = $this->replaceThumbnail($request->file('icon_path'), 'vocab/subcategories/icons', $vocabSubcategory->icon_thumbnail_path);
        $data['audio_path']          = $this->replaceFile($request, 'audio_path', 'vocab/subcategories/audio', $vocabSubcategory->audio_path);
        $data['icon_thumbnail_bg']  = $request->icon_thumbnail_bg;

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

    public function reorder(Request $request): JsonResponse
    {
        $ids        = $request->input('ids', []);
        $categoryId = (int) $request->input('category_id');

        foreach ($ids as $index => $id) {
            VocabSubcategory::where('id', $id)
                ->where('vocab_category_id', $categoryId)
                ->update(['sort_order' => $index + 1]);
        }

        return response()->json(['ok' => true]);
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
