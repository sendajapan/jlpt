<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreVoiceRequest;
use App\Http\Requests\Admin\UpdateVoiceRequest;
use App\Models\Voice;
use App\Services\VoiceService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class VoiceController extends Controller
{
    public function __construct(private VoiceService $service) {}

    public function index(Request $request): View
    {
        $voices = $this->service->getAll($request->input('search'));

        return view('admin.voices.index', compact('voices'));
    }

    public function create(): View
    {
        return view('admin.voices.create');
    }

    public function store(StoreVoiceRequest $request): RedirectResponse
    {
        $data = $this->prepareData($request);
        $data['reference_path'] = $this->storeFile($request, 'reference_path', 'voice-references');

        $this->service->create($data);

        notify()->success()->title('Voice created successfully.')->send();

        return redirect()->route('admin.voices.index');
    }

    public function edit(Voice $voice): View
    {
        return view('admin.voices.edit', compact('voice'));
    }

    public function update(UpdateVoiceRequest $request, Voice $voice): RedirectResponse
    {
        $data = $this->prepareData($request);
        $data['reference_path'] = $this->replaceFile($request, 'reference_path', 'voice-references', $voice->reference_path);

        $this->service->update($voice, $data);

        notify()->success()->title('Voice updated successfully.')->send();

        return redirect()->route('admin.voices.index');
    }

    public function toggleDefault(Voice $voice): RedirectResponse
    {
        $this->service->toggleDefault($voice);

        notify()->success()->title($voice->is_default ? 'Default voice updated.' : 'Default voice cleared.')->send();

        return back();
    }

    public function destroy(Voice $voice): RedirectResponse
    {
        $this->deleteFile($voice->reference_path);
        $this->service->delete($voice);

        notify()->success()->title('Voice deleted.')->send();

        return redirect()->route('admin.voices.index');
    }

    private function prepareData(Request $request): array
    {
        $validated = $request->validated();

        $settings = array_filter([
            'model'                   => $request->input('model'),
            'exaggeration'            => $request->filled('exaggeration') ? (float) $request->input('exaggeration') : null,
            'cfg_weight'              => $request->filled('cfg_weight') ? (float) $request->input('cfg_weight') : null,
            'temperature'             => $request->filled('temperature') ? (float) $request->input('temperature') : null,
            'single_word_cfg_weight'  => $request->filled('single_word_cfg_weight') ? (float) $request->input('single_word_cfg_weight') : null,
            'single_word_temperature' => $request->filled('single_word_temperature') ? (float) $request->input('single_word_temperature') : null,
            'trim_trailing_noise'     => $request->boolean('trim_trailing_noise'),
            'seed'                    => $request->filled('seed') ? (int) $request->input('seed') : null,
        ], fn ($v) => $v !== null && $v !== '');

        return [
            'name'        => $validated['name'],
            'language'    => $validated['language'] ?? null,
            'gender'      => $validated['gender'] ?? null,
            'description' => $validated['description'] ?? null,
            'settings'    => $settings,
            'is_default'  => $validated['is_default'] ?? false,
        ];
    }
}
