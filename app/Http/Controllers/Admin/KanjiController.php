<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Kanji;
use App\Services\KanjiService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class KanjiController extends Controller
{
    public function __construct(private KanjiService $service) {}

    public function index(Request $request): View
    {
        $filters = $request->only(['search', 'jlpt', 'is_premium']);
        $kanjis = $this->service->getAll($filters)->paginate(50)->withQueryString();

        return view('admin.kanji.index', compact('kanjis'));
    }

    public function create(): View
    {
        return view('admin.kanji.create');
    }

    public function store(Request $request): RedirectResponse
    {
        $data = $this->validated($request);
        $this->service->create($data);

        notify()->success()->title('Kanji created successfully.')->send();

        return redirect()->route('admin.kanji.index');
    }

    public function edit(Kanji $kanji): View
    {
        return view('admin.kanji.edit', compact('kanji'));
    }

    public function update(Request $request, Kanji $kanji): RedirectResponse
    {
        $data = $this->validated($request);
        $this->service->update($kanji, $data);

        notify()->success()->title('Kanji updated successfully.')->send();

        return redirect()->route('admin.kanji.index');
    }

    public function destroy(Kanji $kanji): RedirectResponse
    {
        $this->service->delete($kanji);

        notify()->success()->title('Kanji deleted.')->send();

        return redirect()->route('admin.kanji.index');
    }

    private function validated(Request $request): array
    {
        return $request->validate([
            'kanji' => ['required', 'string', 'max:20'],
            'strokes' => ['required', 'integer', 'min:0'],
            'grade' => ['nullable', 'integer', 'min:0'],
            'freq' => ['required', 'integer', 'min:0'],
            'jlpt' => ['nullable', 'in:N5,N4,N3,N2,N1'],
            'translate' => ['nullable', 'string', 'max:255'],
            'meanings' => ['nullable', 'string', 'max:255'],
            'readings_on' => ['nullable', 'string', 'max:255'],
            'readings_kun' => ['nullable', 'string', 'max:255'],
            'level' => ['nullable', 'integer', 'min:0'],
            'radicals' => ['nullable', 'string', 'max:255'],
            'vocab_id' => ['nullable', 'integer', 'exists:vocab_words,id'],
            'is_premium' => ['boolean'],
        ]);
    }
}
