@extends('admin.layouts.app')

@section('title', 'New Category')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Categories</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage all JLPT categories</p>
    </div>
</div>

<a href="{{ route('admin.categories.index') }}" class="inline-flex items-center gap-1.5 text-xs text-zinc-500 hover:text-zinc-800 transition-colors mb-4">
    <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3" viewBox="0 0 20 20" fill="currentColor">
        <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
    </svg>
    Back to Categories
</a>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <div>
            <p class="text-sm font-semibold text-zinc-900">New Category</p>
            <p class="text-xs text-zinc-500 mt-0.5">Fill in the details to create a new category</p>
        </div>
    </div>

    <form method="POST" action="{{ route('admin.categories.store') }}" enctype="multipart/form-data">
        @csrf

        <div class="p-5 space-y-4">

            {{-- Name EN + Name JP --}}
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">
                        Name (English)<span class="text-zinc-400 ml-0.5">*</span>
                    </label>
                    <input
                        type="text"
                        name="name_en"
                        value="{{ old('name_en') }}"
                        placeholder="e.g. Greetings"
                        class="flex h-8 w-full rounded-md border {{ $errors->has('name_en') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150"
                    >
                    @error('name_en')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">
                        Name (Japanese)<span class="text-zinc-400 ml-0.5">*</span>
                    </label>
                    <input
                        type="text"
                        name="name_jp"
                        value="{{ old('name_jp') }}"
                        placeholder="e.g. あいさつ"
                        class="flex h-8 w-full rounded-md border {{ $errors->has('name_jp') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150"
                    >
                    @error('name_jp')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>
            </div>

            {{-- Romaji --}}
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Romaji</label>
                    <input
                        type="text"
                        name="name_romaji"
                        value="{{ old('name_romaji') }}"
                        placeholder="e.g. aisatsu"
                        class="flex h-8 w-full rounded-md border {{ $errors->has('name_romaji') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150"
                    >
                    @error('name_romaji')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">
                        Sort Order<span class="text-zinc-400 ml-0.5">*</span>
                    </label>
                    <input
                        type="number"
                        name="sort_order"
                        value="{{ old('sort_order', 99) }}"
                        min="0"
                        max="9999"
                        class="flex h-8 w-full rounded-md border {{ $errors->has('sort_order') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150"
                    >
                    @error('sort_order')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>
            </div>

            {{-- Icon + Audio --}}
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Icon Image</label>
                    <x-image-preview name="icon_path">
                        @error('icon_path')
                            <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                        @enderror
                    </x-image-preview>
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Audio File</label>
                    <input
                        type="file"
                        name="audio_path"
                        accept="audio/*"
                        class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer"
                    >
                    <p class="mt-1 text-[10px] text-zinc-400">MP3, WAV, OGG, AAC</p>
                    @error('audio_path')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>
            </div>

            {{-- Premium checkbox --}}
            <div>
                <label class="flex items-center gap-2 cursor-pointer select-none">
                    <input type="hidden" name="is_premium" value="0">
                    <input
                        type="checkbox"
                        name="is_premium"
                        id="is_premium"
                        value="1"
                        {{ old('is_premium') ? 'checked' : '' }}
                        class="w-3.5 h-3.5 rounded border-zinc-300 text-zinc-900 focus:ring-zinc-900 focus:ring-offset-0"
                    >
                    <span class="text-xs font-medium text-zinc-700">Premium content</span>
                </label>
            </div>

        </div>

        <div class="flex items-center gap-2 px-5 py-3.5 border-t border-zinc-100 bg-zinc-50/50">
            <button type="submit" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
                Create Category
            </button>
            <a href="{{ route('admin.categories.index') }}" class="text-xs text-zinc-400 hover:text-zinc-700 px-3 py-1.5 rounded hover:bg-zinc-100 transition-colors duration-150">
                Cancel
            </a>
        </div>
    </form>
</div>

@endsection
