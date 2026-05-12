<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreVocabCategoryRequest;
use App\Http\Requests\Admin\UpdateVocabCategoryRequest;
use App\Models\VocabCategory;
use App\Services\VocabCategoryService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class VocabCategoryController extends Controller
{
    public function __construct(private VocabCategoryService $service) {}

    public function index(Request $request): View
    {
        $categories = $this->service->getAll($request->input('search'));

        return view('admin.vocab.categories.index', compact('categories'));
    }

    public function create(): View
    {
        return view('admin.vocab.categories.create');
    }

    public function store(StoreVocabCategoryRequest $request): RedirectResponse
    {
        $data = $request->validated();
        $data['icon_path']           = $this->storeImage($request, 'icon_path', 'vocab/categories/icons');
        $data['icon_thumbnail_path'] = $this->storeThumbnail($request->file('icon_path'), 'vocab/categories/icons');
        $data['audio_path']          = $this->storeFile($request, 'audio_path', 'vocab/categories/audio');

        $this->service->create($data);

        notify()->success()->title('Category created successfully.')->send();

        return redirect()->route('admin.vocab.categories.index');
    }

    public function edit(VocabCategory $vocabCategory): View
    {
        $category = $vocabCategory;
        $vocab_bg = $this->service->getAllVocabBg();
        return view('admin.vocab.categories.edit', compact('category', 'vocab_bg'));
    }

    public function update(UpdateVocabCategoryRequest $request, VocabCategory $vocabCategory): RedirectResponse
    {
        $data = $request->validated();
        $data['icon_path']           = $this->replaceImage($request, 'icon_path', 'vocab/categories/icons', $vocabCategory->icon_path);
        $data['icon_thumbnail_path'] = $this->replaceThumbnail($request->file('icon_path'), 'vocab/categories/icons', $vocabCategory->icon_thumbnail_path);
        $data['audio_path']          = $this->replaceFile($request, 'audio_path', 'vocab/categories/audio', $vocabCategory->audio_path);
        $data['category_bg_path']  = $request->category_bg_path;

        $this->service->update($vocabCategory, $data);

        notify()->success()->title('Category updated successfully.')->send();

        return redirect()->route('admin.vocab.categories.index');
    }

    public function updateIcon(Request $request, VocabCategory $vocabCategory): RedirectResponse
    {
        $request->validate(['icon_path' => ['required', 'image', 'max:2048']]);
        $this->deleteFile($vocabCategory->icon_path);
        $this->deleteFile($vocabCategory->icon_thumbnail_path);

        $file = $request->file('icon_path');
        $vocabCategory->update([
            'icon_path'           => $this->saveImageFile($file, 'vocab/categories/icons'),
            'icon_thumbnail_path' => $this->storeThumbnail($file, 'vocab/categories/icons'),
        ]);

        notify()->success()->title('Icon updated.')->send();

        return back();
    }

    public function reorder(Request $request): JsonResponse
    {
        $ids    = $request->input('ids', []);
        $offset = (int) $request->input('offset', 0);

        foreach ($ids as $index => $id) {
            VocabCategory::where('id', $id)->update(['sort_order' => $offset + $index + 1]);
        }

        return response()->json(['ok' => true]);
    }

    public function destroy(VocabCategory $vocabCategory): RedirectResponse
    {
        $this->deleteFile($vocabCategory->icon_path);
        $this->deleteFile($vocabCategory->icon_thumbnail_path);
        $this->deleteFile($vocabCategory->audio_path);
        $this->service->delete($vocabCategory);

        notify()->success()->title('Category deleted.')->send();

        return redirect()->route('admin.vocab.categories.index');
    }
}
