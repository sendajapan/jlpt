@extends('admin.layouts.app')
@section('title', 'Edit Vocabulary Entry')
@php use Illuminate\Support\Facades\Storage; @endphp

@section('content')

{{-- Back Link --}}
<a href="{{ route('admin.vocabularies.index') }}" class="inline-flex items-center gap-1.5 text-xs text-zinc-500 hover:text-zinc-800 transition-colors mb-4">
    <svg class="w-3 h-3" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
    </svg>
    Back to Vocabulary
</a>

{{-- Card --}}
<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden max-w-2xl">

    {{-- Card Header --}}
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">Edit Vocabulary Entry</span>
        <span class="text-[10px] text-zinc-400">ID #{{ $vocabulary->id }}</span>
    </div>

    <form method="POST" action="{{ route('admin.vocabularies.update', $vocabulary) }}" enctype="multipart/form-data">
        @csrf
        @method('PUT')

        <div class="px-5 py-4 space-y-4">

            {{-- Subcategory --}}
            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Subcategory <span class="text-red-500">*</span></label>
                <select name="subcategory_id"
                        class="{{ $errors->has('subcategory_id') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                    <option value="">— Select subcategory —</option>
                    @foreach ($subcategories as $sub)
                        <option value="{{ $sub->id }}" {{ old('subcategory_id', $vocabulary->subcategory_id) == $sub->id ? 'selected' : '' }}>
                            {{ $sub->category->name_en ?? '' }} › {{ $sub->name_en }}
                        </option>
                    @endforeach
                </select>
                @error('subcategory_id') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
            </div>

            {{-- Word JP / Romaji --}}
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Word (Japanese) <span class="text-red-500">*</span></label>
                    <input type="text" name="word_jp" value="{{ old('word_jp', $vocabulary->word_jp) }}"
                           class="{{ $errors->has('word_jp') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                    @error('word_jp') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Romaji <span class="text-red-500">*</span></label>
                    <input type="text" name="word_romaji" value="{{ old('word_romaji', $vocabulary->word_romaji) }}"
                           class="{{ $errors->has('word_romaji') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                    @error('word_romaji') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>
            </div>

            {{-- Meaning EN --}}
            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Meaning (English) <span class="text-red-500">*</span></label>
                <input type="text" name="meaning_en" value="{{ old('meaning_en', $vocabulary->meaning_en) }}"
                       class="{{ $errors->has('meaning_en') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                @error('meaning_en') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
            </div>

            {{-- Example Sentence JP --}}
            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Example Sentence (Japanese)</label>
                <textarea name="example_sentence_jp" rows="2"
                          class="{{ $errors->has('example_sentence_jp') ? 'flex w-full rounded-md border border-red-400 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150 resize-none' : 'flex w-full rounded-md border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150 resize-none' }}">{{ old('example_sentence_jp', $vocabulary->example_sentence_jp) }}</textarea>
                @error('example_sentence_jp') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
            </div>

            {{-- Example Sentence EN --}}
            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Example Sentence (English)</label>
                <textarea name="example_sentence_en" rows="2"
                          class="{{ $errors->has('example_sentence_en') ? 'flex w-full rounded-md border border-red-400 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150 resize-none' : 'flex w-full rounded-md border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150 resize-none' }}">{{ old('example_sentence_en', $vocabulary->example_sentence_en) }}</textarea>
                @error('example_sentence_en') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
            </div>

            {{-- Audio / Image --}}
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Audio</label>
                    @if ($vocabulary->audio_path)
                        <audio controls class="h-7 w-full mt-1 mb-2"><source src="{{ Storage::url($vocabulary->audio_path) }}"></audio>
                    @endif
                    <input type="file" name="audio_path" accept="audio/*"
                           class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer">
                    @if ($vocabulary->audio_path)
                        <p class="mt-1 text-[10px] text-zinc-400">Leave empty to keep current file</p>
                    @else
                        <p class="mt-1 text-[10px] text-zinc-400">MP3, WAV, OGG, AAC</p>
                    @endif
                    @error('audio_path') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Image</label>
                    @if ($vocabulary->image_path)
                        <img src="{{ Storage::url($vocabulary->image_path) }}" alt="Current image"
                             class="w-16 h-16 rounded-md object-cover border border-zinc-200 mt-1 mb-2">
                    @endif
                    <input type="file" name="image_path" accept="image/*"
                           class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer">
                    @if ($vocabulary->image_path)
                        <p class="mt-1 text-[10px] text-zinc-400">Leave empty to keep current file</p>
                    @else
                        <p class="mt-1 text-[10px] text-zinc-400">JPG, PNG, WebP</p>
                    @endif
                    @error('image_path') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>
            </div>

            {{-- JLPT Level / Sort Order --}}
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">JLPT Level</label>
                    <select name="jlpt_level"
                            class="{{ $errors->has('jlpt_level') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                        <option value="">— None —</option>
                        @foreach (['N5', 'N4', 'N3', 'N2', 'N1'] as $level)
                            <option value="{{ $level }}" {{ old('jlpt_level', $vocabulary->jlpt_level) === $level ? 'selected' : '' }}>{{ $level }}</option>
                        @endforeach
                    </select>
                    @error('jlpt_level') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Sort Order</label>
                    <input type="number" name="sort_order" value="{{ old('sort_order', $vocabulary->sort_order) }}" min="0" max="9999"
                           class="{{ $errors->has('sort_order') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                    @error('sort_order') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>
            </div>

        </div>

        {{-- Form Footer --}}
        <div class="flex items-center gap-2 px-5 py-3.5 border-t border-zinc-100 bg-zinc-50/50">
            <button type="submit"
                    class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
                Update Entry
            </button>
            <a href="{{ route('admin.vocabularies.index') }}"
               class="text-xs text-zinc-400 hover:text-zinc-700 px-3 py-1.5 rounded hover:bg-zinc-100 transition-colors duration-150">
                Cancel
            </a>
        </div>

    </form>
</div>

@endsection
