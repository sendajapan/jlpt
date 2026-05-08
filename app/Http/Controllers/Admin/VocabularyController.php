<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreVocabularyRequest;
use App\Http\Requests\Admin\UpdateVocabularyRequest;
use App\Models\Vocabulary;
use App\Services\VocabularyService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Str;
use Illuminate\View\View;

class VocabularyController extends Controller
{
    public function __construct(private VocabularyService $service) {}

    public function index(Request $request): View
    {
        $filters = $request->only(['search', 'category_id', 'subcategory_id', 'is_approved', 'image_path']);
        $vocabularies = $this->service->getAll($filters);
        $categories = $this->service->getAllCategories();
        $subcategories = $this->service->getAllSubcategories();

        return view('admin.vocab.words.index', compact('vocabularies', 'categories', 'subcategories'));
    }

    public function create(): View
    {
        $categories    = $this->service->getAllCategories();
        $subcategories = $this->service->getAllSubcategories();

        return view('admin.vocab.words.create', compact('categories', 'subcategories'));
    }

    public function store(StoreVocabularyRequest $request): RedirectResponse
    {
        $data = $request->validated();
        $data['audio_jp']              = $this->storeFile($request, 'audio_jp', 'vocab/words/audio');
        $data['audio_en']              = $this->storeFile($request, 'audio_en', 'vocab/words/audio');
        $data['sentence_audio_jp']     = $this->storeFile($request, 'sentence_audio_jp', 'vocab/words/audio');
        $data['sentence_audio_en']     = $this->storeFile($request, 'sentence_audio_en', 'vocab/words/audio');
        $data['image_path']            = $this->storeImage($request, 'image_path', 'vocab/words/images');
        $data['image_thumbnail_path']  = $this->storeThumbnail($request->file('image_path'), 'vocab/words/thumbnails');

        $this->service->create($data);

        notify()->success()->title('Vocabulary entry created successfully.')->send();

        return redirect()->route('admin.vocab.words.index');
    }

    public function edit(Vocabulary $vocabulary): View
    {
        $categories    = $this->service->getAllCategories();
        $subcategories = $this->service->getAllSubcategories();
        $vocab_bg = $this->service->getAllVocabBg();
        
        return view('admin.vocab.words.edit', compact('vocabulary', 'categories', 'subcategories', 'vocab_bg'));
    }

    public function update(UpdateVocabularyRequest $request, Vocabulary $vocabulary): RedirectResponse
    {

        $data = $request->validated();
        $data['audio_jp']              = $this->replaceFile($request, 'audio_jp', 'vocab/words/audio', $vocabulary->audio_jp);
        $data['audio_en']              = $this->replaceFile($request, 'audio_en', 'vocab/words/audio', $vocabulary->audio_en);
        $data['sentence_audio_jp']     = $this->replaceFile($request, 'sentence_audio_jp', 'vocab/words/audio', $vocabulary->sentence_audio_jp);
        $data['sentence_audio_en']     = $this->replaceFile($request, 'sentence_audio_en', 'vocab/words/audio', $vocabulary->sentence_audio_en);
        $data['image_path']            = $this->replaceImage($request, 'image_path', 'vocab/words/images', $vocabulary->image_path);
        $data['image_thumbnail_path']  = $this->replaceThumbnail($request->file('image_path'), 'vocab/words/thumbnails', $vocabulary->image_thumbnail_path);
        $data['image_thumbnail_bg']  = $request->image_thumbnail_bg;

        $this->service->update($vocabulary, $data);

        notify()->success()->title('Vocabulary entry updated successfully.')->send();

        return redirect()->route('admin.vocab.words.index');
    }

    public function reorder(Request $request): JsonResponse
    {
        $ids           = $request->input('ids', []);
        $subcategoryId = (int) $request->input('subcategory_id');

        foreach ($ids as $index => $id) {
            Vocabulary::where('id', $id)
                ->where('vocab_subcategory_id', $subcategoryId)
                ->update(['sort_order' => $index + 1]);
        }

        return response()->json(['ok' => true]);
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
            'image_path'           => $this->saveImageFile($request->file('image_path'), 'vocab/words/images'),
            'image_thumbnail_path' => $this->storeThumbnail($request->file('image_path'), 'vocab/words/thumbnails'),
        ]);

        notify()->success()->title('Image updated.')->send();

        return back();
    }

    public function generateAudio(Request $request, Vocabulary $vocabulary): JsonResponse
    {
        $field = $request->validate(['field' => ['required', 'in:audio_en,audio_jp,sentence_audio_en,sentence_audio_jp']])['field'];

        $textMap = [
            'audio_en'          => $vocabulary->word_en,
            'audio_jp'          => $vocabulary->word_jp,
            'sentence_audio_en' => $vocabulary->sentence_en,
            'sentence_audio_jp' => $vocabulary->sentence_jp,
        ];

        $text = $textMap[$field];

        if (empty($text)) {
            return response()->json(['error' => 'No text available.'], 422);
        }

        $result = \B7s\FluentVox\FluentVox::make()->text($text)->generate();

        $storagePath = 'vocab/words/audio/' . Str::uuid() . '.wav';
        Storage::disk('public')->put($storagePath, file_get_contents($result->getPath()));

        $this->deleteFile($vocabulary->$field);

        $vocabulary->update([$field => $storagePath]);

        return response()->json(['url' => Storage::disk('public')->url($storagePath)]);
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

        return redirect()->route('admin.vocab.words.index');
    }
}
