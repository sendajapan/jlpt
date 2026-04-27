<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreVocabularyRequest;
use App\Http\Requests\Admin\UpdateVocabularyRequest;
use App\Models\Vocabulary;
use App\Services\VocabularyService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use Illuminate\View\View;

class VocabularyController extends Controller
{
    public function __construct(private VocabularyService $service) {}

    public function index(Request $request): View
    {
        $vocabularies = $this->service->getAll($request->input('search'));
        return view('admin.vocabularies.index', compact('vocabularies'));
    }

    public function create(): View
    {
        $subcategories = $this->service->getAllSubcategories();
        return view('admin.vocabularies.create', compact('subcategories'));
    }

    public function store(StoreVocabularyRequest $request): RedirectResponse
    {
        $data = $request->validated();
        $data['audio_path'] = $this->storeFile($request, 'audio_path', 'vocabularies/audio');
        $data['image_path'] = $this->storeFile($request, 'image_path', 'vocabularies/images');

        $this->service->create($data);
        return redirect()->route('admin.vocabularies.index')
            ->with('success', 'Vocabulary entry created successfully.');
    }

    public function edit(Vocabulary $vocabulary): View
    {
        $subcategories = $this->service->getAllSubcategories();
        return view('admin.vocabularies.edit', compact('vocabulary', 'subcategories'));
    }

    public function update(UpdateVocabularyRequest $request, Vocabulary $vocabulary): RedirectResponse
    {
        $data = $request->validated();

        if ($request->hasFile('audio_path')) {
            $this->deleteFile($vocabulary->audio_path);
            $data['audio_path'] = $this->storeFile($request, 'audio_path', 'vocabularies/audio');
        } else {
            unset($data['audio_path']);
        }

        if ($request->hasFile('image_path')) {
            $this->deleteFile($vocabulary->image_path);
            $data['image_path'] = $this->storeFile($request, 'image_path', 'vocabularies/images');
        } else {
            unset($data['image_path']);
        }

        $this->service->update($vocabulary, $data);
        return redirect()->route('admin.vocabularies.index')
            ->with('success', 'Vocabulary entry updated successfully.');
    }

    public function destroy(Vocabulary $vocabulary): RedirectResponse
    {
        $this->deleteFile($vocabulary->audio_path);
        $this->deleteFile($vocabulary->image_path);
        $this->service->delete($vocabulary);
        return redirect()->route('admin.vocabularies.index')
            ->with('success', 'Vocabulary entry deleted successfully.');
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
