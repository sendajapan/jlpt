@php
    use B7s\FluentVox\Enums\Language;
    use B7s\FluentVox\Enums\Model;

    $settings = $voice->settings ?? [];
@endphp

<div class="p-5 space-y-4">

    <div>
        <label class="block text-xs font-medium text-zinc-700 mb-1.5">
            Name<span class="text-zinc-400 ml-0.5">*</span>
        </label>
        <input type="text" name="name" value="{{ old('name', $voice->name) }}" placeholder="e.g. Test Voice"
               class="flex h-8 w-full rounded-md border {{ $errors->has('name') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900">
        @error('name') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
    </div>

    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <div>
            <label class="block text-xs font-medium text-zinc-700 mb-1.5">Language</label>
            <select name="language"
                    class="flex h-8 w-full rounded-md border border-zinc-200 bg-white px-2 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900">
                <option value="">—</option>
                @foreach(Language::cases() as $lang)
                    <option
                        value="{{ $lang->value }}" {{ old('language', $voice->language) === $lang->value ? 'selected' : '' }}>{{ $lang->name() }}
                        ({{ $lang->value }})
                    </option>
                @endforeach
            </select>
        </div>

        <div>
            <label class="block text-xs font-medium text-zinc-700 mb-1.5">Gender</label>
            <select name="gender"
                    class="flex h-8 w-full rounded-md border border-zinc-200 bg-white px-2 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900">
                <option value="">—</option>
                @foreach(['male', 'female', 'neutral'] as $g)
                    <option
                        value="{{ $g }}" {{ old('gender', $voice->gender) === $g ? 'selected' : '' }}>{{ ucfirst($g) }}</option>
                @endforeach
            </select>
        </div>
    </div>

    <div>
        <label class="block text-xs font-medium text-zinc-700 mb-1.5">Description</label>
        <textarea name="description" rows="2" placeholder="Notes about this voice"
                  class="block w-full rounded-md border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900">{{ old('description', $voice->description) }}</textarea>
    </div>

    <div>
        <label class="block text-xs font-medium text-zinc-700 mb-1.5">Reference Audio (for voice cloning)</label>
        @if(!empty($voice->reference_path))
            <audio controls class="mb-2">
                <source src="{{ \Illuminate\Support\Facades\Storage::url($voice->reference_path) }}">
            </audio>
        @endif
        <input type="file" name="reference_path" accept="audio/wav,audio/mpeg,audio/flac"
               class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer">
        <p class="mt-1 text-[10px] text-zinc-400">WAV, MP3, FLAC. The generated speech will mimic this speaker.</p>
        @error('reference_path') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
    </div>

    <div class="pt-2 border-t border-zinc-100">
        <p class="text-[11px] font-semibold uppercase tracking-wide text-zinc-500 mb-3">FluentVox Settings</p>

        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Model</label>
                <select name="model"
                        class="flex h-8 w-full rounded-md border border-zinc-200 bg-white px-2 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900">
                    <option value="">Default</option>
                    @foreach(Model::cases() as $m)
                        <option value="{{ $m->value }}" {{ old('model', $settings['model'] ?? '') === $m->value ? 'selected' : '' }}>{{ $m->value }}</option>
                    @endforeach
                </select>
            </div>

            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Exaggeration (0.25 – 2.0)</label>
                <input type="number" step="0.05" min="0.25" max="2.0" name="exaggeration"
                       value="{{ old('exaggeration', $settings['exaggeration'] ?? 0.5) }}"
                       class="flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900">
                <p class="mt-1 text-[10px] text-zinc-400 leading-5">Control the emotional intensity and expressiveness of the generated speech. Higher values produce more dramatic, animated voices while lower values create calmer, more subdued speech.</p>
                <div class="mt-1.5 flex flex-wrap gap-1">
                    <span class="inline-block px-1.5 py-0.5 rounded bg-sky-50 text-sky-700 text-[10px]"><strong>0.3</strong> subtle, understated calm delivery</span>
                    <span class="inline-block px-1.5 py-0.5 rounded bg-emerald-50 text-emerald-700 text-[10px]"><strong>0.5</strong> neutral, balanced and natural</span>
                    <span class="inline-block px-1.5 py-0.5 rounded bg-amber-50 text-amber-700 text-[10px]"><strong>0.7</strong> expressive, more animated and emotional</span>
                    <span class="inline-block px-1.5 py-0.5 rounded bg-rose-50 text-rose-700 text-[10px]"><strong>1.0</strong> dramatic, highly theatrical</span>
                </div>
            </div>

            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">CFG Weight / Pace (0.2 – 1.0)</label>
                <input type="number" step="0.05" min="0.2" max="1.0" name="cfg_weight"
                       value="{{ old('cfg_weight', $settings['cfg_weight'] ?? 0.3) }}"
                       class="flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900">
                <p class="mt-1 text-[10px] text-zinc-400 leading-5">Adjust the rhythm and speed of speech delivery. CFG (Classifier-Free Guidance) weight controls how closely the model follows the text pacing. Lower values create slower, more deliberate speech while higher values speed up delivery.</p>
                <div class="mt-1.5 flex flex-wrap gap-1">
                    <span class="inline-block px-1.5 py-0.5 rounded bg-sky-50 text-sky-700 text-[10px]"><strong>0.3</strong> slow, deliberate and measured pacing</span>
                    <span class="inline-block px-1.5 py-0.5 rounded bg-emerald-50 text-emerald-700 text-[10px]"><strong>0.5</strong> natural speaking speed</span>
                    <span class="inline-block px-1.5 py-0.5 rounded bg-amber-50 text-amber-700 text-[10px]"><strong>0.7</strong> fast, quick and energetic delivery</span>
                </div>
            </div>

            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Temperature (0.05 – 5.0)</label>
                <input type="number" step="0.05" min="0.05" max="5.0" name="temperature"
                       value="{{ old('temperature', $settings['temperature'] ?? 0.8) }}"
                       class="flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900">
                <p class="mt-1 text-[10px] text-zinc-400 leading-5">Control the variability and creativity in speech generation. Temperature affects how predictable vs. varied the output is.</p>
                <div class="mt-1.5 flex flex-wrap gap-1">
                    <span class="inline-block px-1.5 py-0.5 rounded bg-sky-50 text-sky-700 text-[10px]"><strong>0.3</strong> deterministic, consistent and predictable output</span>
                    <span class="inline-block px-1.5 py-0.5 rounded bg-emerald-50 text-emerald-700 text-[10px]"><strong>0.8</strong> default, balanced natural variation</span>
                    <span class="inline-block px-1.5 py-0.5 rounded bg-amber-50 text-amber-700 text-[10px]"><strong>1.2</strong> creative, more varied and spontaneous speech</span>
                </div>
            </div>

            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Seed</label>
                <input type="number" step="1" min="0" name="seed"
                       value="{{ old('seed', $settings['seed'] ?? 0) }}"
                       class="flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900">
                <p class="mt-1 text-[10px] text-zinc-400 leading-5">Seed for reproducibility — reuse the same seed to get identical audio for the same input.</p>
                <div class="mt-1.5 flex flex-wrap gap-1">
                    <span class="inline-block px-1.5 py-0.5 rounded bg-sky-50 text-sky-700 text-[10px]"><strong>0</strong> random each generation (default)</span>
                    <span class="inline-block px-1.5 py-0.5 rounded bg-amber-50 text-amber-700 text-[10px]"><strong>42</strong> any positive integer locks the output so the same text always produces the same waveform</span>
                </div>
            </div>
        </div>
    </div>

    <div>
        <label class="flex items-center gap-2 cursor-pointer select-none">
            <input type="hidden" name="is_default" value="0">
            <input type="checkbox" name="is_default" value="1"
                   {{ old('is_default', $voice->is_default) ? 'checked' : '' }}
                   class="w-3.5 h-3.5 rounded border-zinc-300 text-zinc-900 focus:ring-zinc-900">
            <span class="text-xs font-medium text-zinc-700">Use as default voice</span>
        </label>
    </div>

</div>
