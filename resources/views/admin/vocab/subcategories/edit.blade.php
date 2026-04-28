@extends('admin.layouts.app')
@section('title', 'Edit Subcategory')
@php use Illuminate\Support\Facades\Storage; @endphp

@section('content')

{{-- Back Link --}}
<a href="{{ route('admin.vocab.subcategories.index') }}" class="inline-flex items-center gap-1.5 text-xs text-zinc-500 hover:text-zinc-800 transition-colors mb-4">
    <svg class="w-3 h-3" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
    </svg>
    Back to Subcategories
</a>

{{-- Card --}}
<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden max-w-2xl">

    {{-- Card Header --}}
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">Edit Subcategory</span>
        <span class="text-[10px] text-zinc-400">ID #{{ $subcategory->id }}</span>
    </div>

    <form method="POST" action="{{ route('admin.vocab.subcategories.update', $subcategory) }}" enctype="multipart/form-data">
        @csrf
        @method('PUT')

        <div class="px-5 py-4 space-y-4">

            {{-- Category --}}
            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Category <span class="text-red-500">*</span></label>
                <select name="vocab_category_id"
                        class="{{ $errors->has('category_id') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                    <option value="">窶・Select category 窶・/option>
                    @foreach ($categories as $cat)
                        <option value="{{ $cat->id }}" {{ old('vocab_category_id', $subcategory->vocab_category_id ?? '') == $cat->id ? 'selected' : '' }}>
                            {{ $cat->name_en }}
                        </option>
                    @endforeach
                </select>
                @error('category_id') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
            </div>

            {{-- Name EN / Name JP --}}
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Name (English) <span class="text-red-500">*</span></label>
                    <input type="text" name="name_en" value="{{ old('name_en', $subcategory->name_en) }}"
                           class="{{ $errors->has('name_en') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                    @error('name_en') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Name (Japanese) <span class="text-red-500">*</span></label>
                    <input type="text" name="name_jp" value="{{ old('name_jp', $subcategory->name_jp) }}"
                           class="{{ $errors->has('name_jp') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                    @error('name_jp') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>
            </div>

            {{-- Romaji --}}
            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Romaji</label>
                <input type="text" name="name_romaji" value="{{ old('name_romaji', $subcategory->name_romaji) }}"
                       class="{{ $errors->has('name_romaji') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                @error('name_romaji') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
            </div>

            {{-- Icon / Audio --}}
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Icon</label>
                    <x-image-preview name="icon_path" :current="$subcategory->icon_path ? Storage::url($subcategory->icon_path) : null">
                        @error('icon_path')
                            <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                        @enderror
                    </x-image-preview>
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Audio</label>
                    @if ($subcategory->audio_path)
                        <audio controls class="h-7 w-full mt-1 mb-2"><source src="{{ Storage::url($subcategory->audio_path) }}"></audio>
                    @endif
                    <input type="file" name="audio_path" accept="audio/*"
                           class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer">
                    @if ($subcategory->audio_path)
                        <p class="mt-1 text-[10px] text-zinc-400">Leave empty to keep current file</p>
                    @else
                        <p class="mt-1 text-[10px] text-zinc-400">MP3, WAV, OGG, AAC</p>
                    @endif
                    @error('audio_path') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>
            </div>

            {{-- Sort Order / Premium --}}
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 items-end">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Sort Order</label>
                    <input type="number" name="sort_order" value="{{ old('sort_order', $subcategory->sort_order) }}" min="0" max="9999"
                           class="{{ $errors->has('sort_order') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                    @error('sort_order') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div class="pb-1">
                    <label class="flex items-center gap-2 cursor-pointer select-none">
                        <input type="hidden" name="is_premium" value="0">
                        <input type="checkbox" name="is_premium" id="is_premium" value="1"
                               {{ old('is_premium', $subcategory->is_premium) ? 'checked' : '' }}
                               class="w-3.5 h-3.5 rounded border-zinc-300 text-zinc-900 focus:ring-zinc-900 focus:ring-offset-0">
                        <span class="text-xs font-medium text-zinc-700">Premium content</span>
                    </label>
                </div>
            </div>

        </div>

        {{-- Form Footer --}}
        <div class="flex items-center gap-2 px-5 py-3.5 border-t border-zinc-100 bg-zinc-50/50">
            <button type="submit"
                    class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
                Update Subcategory
            </button>
            <a href="{{ route('admin.vocab.subcategories.index') }}"
               class="text-xs text-zinc-400 hover:text-zinc-700 px-3 py-1.5 rounded hover:bg-zinc-100 transition-colors duration-150">
                Cancel
            </a>
        </div>

    </form>
</div>

@endsection

