<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreVocabularyRequest;
use App\Http\Requests\Admin\UpdateVocabularyRequest;
use App\Models\Vocabulary;
use App\Services\VocabularyService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class VocabularyController extends Controller
{
    public function __construct(private VocabularyService $service) {}

    public function index(Request $request): View
    {
        $filters = $request->only(['search', 'category_id', 'subcategory_id', 'is_approved']);
        $vocabularies = $this->service->getAll($filters);
        $categories = $this->service->getAllCategories();
        $subcategories = $this->service->getAllSubcategories();

        return view('admin.vocabularies.index', compact('vocabularies', 'categories', 'subcategories'));
    }

    public function create(): View
    {
        $subcategories = $this->service->getAllSubcategories();

        return view('admin.vocabularies.create', compact('subcategories'));
    }

    public function store(StoreVocabularyRequest $request): RedirectResponse
    {
        $data = $request->validated();
        $data['audio_jp']              = $this->storeFile($request, 'audio_jp', 'vocabularies/audio');
        $data['audio_en']              = $this->storeFile($request, 'audio_en', 'vocabularies/audio');
        $data['sentence_audio_jp']     = $this->storeFile($request, 'sentence_audio_jp', 'vocabularies/audio');
        $data['sentence_audio_en']     = $this->storeFile($request, 'sentence_audio_en', 'vocabularies/audio');
        $data['image_path']            = $this->storeFile($request, 'image_path', 'vocabularies/images');
        $data['image_thumbnail_path']  = $this->storeThumbnail($request->file('image_path'), 'vocabularies/thumbnails');

        $this->service->create($data);

        notify()->success()->title('Vocabulary entry created successfully.')->send();

        return redirect()->route('admin.vocabularies.index');
    }

    public function edit(Vocabulary $vocabulary): View
    {
        $subcategories = $this->service->getAllSubcategories();

        return view('admin.vocabularies.edit', compact('vocabulary', 'subcategories'));
    }

    public function update(UpdateVocabularyRequest $request, Vocabulary $vocabulary): RedirectResponse
    {
        $data = $request->validated();
        $data['audio_jp']              = $this->replaceFile($request, 'audio_jp', 'vocabularies/audio', $vocabulary->audio_jp);
        $data['audio_en']              = $this->replaceFile($request, 'audio_en', 'vocabularies/audio', $vocabulary->audio_en);
        $data['sentence_audio_jp']     = $this->replaceFile($request, 'sentence_audio_jp', 'vocabularies/audio', $vocabulary->sentence_audio_jp);
        $data['sentence_audio_en']     = $this->replaceFile($request, 'sentence_audio_en', 'vocabularies/audio', $vocabulary->sentence_audio_en);
        $data['image_path']            = $this->replaceFile($request, 'image_path', 'vocabularies/images', $vocabulary->image_path);
        $data['image_thumbnail_path']  = $this->replaceThumbnail($request->file('image_path'), 'vocabularies/thumbnails', $vocabulary->image_thumbnail_path);

        $this->service->update($vocabulary, $data);

        notify()->success()->title('Vocabulary entry updated successfully.')->send();

        return redirect()->route('admin.vocabularies.index');
    }

    public function toggleApproved(Vocabulary $vocabulary): RedirectResponse
    {
        $vocabulary->update(['is_approved' => ! $vocabulary->is_approved]);

        return back();
    }

    public function updateImage(Request $request, Vocabulary $vocabulary): RedirectResponse
    {
        $request->validate(['image_path' => ['required', 'image', 'max:2048']]);
        $this->deleteFile($vocabulary->image_path);
        $this->deleteFile($vocabulary->image_thumbnail_path);
        $vocabulary->update([
            'image_path'           => $request->file('image_path')->store('vocabularies/images', 'public'),
            'image_thumbnail_path' => $this->storeThumbnail($request->file('image_path'), 'vocabularies/thumbnails'),
        ]);

        notify()->success()->title('Image updated.')->send();

        return back();
    }

    public function destroy(Vocabulary $vocabulary): RedirectResponse
    {
        $this->deleteFile($vocabulary->audio_jp);
        $this->deleteFile($vocabulary->audio_en);
        $this->deleteFile($vocabulary->sentence_audio_jp);
        $this->deleteFile($vocabulary->sentence_audio_en);
        $this->deleteFile($vocabulary->image_path);
        $this->deleteFile($vocabulary->image_thumbnail_path);
        $this->service->delete($vocabulary);

        notify()->success()->title('Vocabulary entry deleted.')->send();

        return redirect()->route('admin.vocabularies.index');
    }
}
