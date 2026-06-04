@extends('admin.layouts.app')

@section('title', 'New Avatar')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Avatars</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage all purchasable avatars</p>
    </div>
</div>

<a href="{{ route('admin.avatars.index') }}" class="inline-flex items-center gap-1.5 text-xs text-zinc-500 hover:text-zinc-800 transition-colors mb-4">
    <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3" viewBox="0 0 20 20" fill="currentColor">
        <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
    </svg>
    Back to Avatars
</a>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <div>
            <p class="text-sm font-semibold text-zinc-900">New Avatar</p>
            <p class="text-xs text-zinc-500 mt-0.5">Image will be auto-compressed to 512×512px, max 256 KB</p>
        </div>
    </div>

    <form method="POST" action="{{ route('admin.avatars.store') }}" enctype="multipart/form-data">
        @csrf

        <div class="p-5 space-y-4">

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">
                        Name<span class="text-zinc-400 ml-0.5">*</span>
                    </label>
                    <input
                        type="text"
                        name="name"
                        value="{{ old('name') }}"
                        placeholder="e.g. Ninja Fox"
                        class="flex h-8 w-full rounded-md border {{ $errors->has('name') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150"
                    >
                    @error('name')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">
                        Coin Price<span class="text-zinc-400 ml-0.5">*</span>
                    </label>
                    <input
                        type="number"
                        name="coin_price"
                        value="{{ old('coin_price', 0) }}"
                        min="0"
                        placeholder="0 = free"
                        class="flex h-8 w-full rounded-md border {{ $errors->has('coin_price') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150"
                    >
                    @error('coin_price')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Avatar Image<span class="text-zinc-400 ml-0.5">*</span></label>
                    <div x-data="{ preview: null }">
                        <div class="w-32 h-32 rounded-xl border-2 border-dashed border-zinc-200 bg-zinc-50 overflow-hidden flex items-center justify-center mb-2">
                            <template x-if="preview">
                                <img :src="preview" class="w-full h-full object-cover">
                            </template>
                            <template x-if="!preview">
                                <div class="text-center select-none">
                                    <svg class="w-8 h-8 text-zinc-300 mx-auto" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"/>
                                    </svg>
                                    <p class="mt-1 text-[10px] text-zinc-400">No image</p>
                                </div>
                            </template>
                        </div>
                        <input
                            type="file"
                            name="image_path"
                            accept="image/*"
                            x-on:change="preview = $event.target.files[0] ? URL.createObjectURL($event.target.files[0]) : null"
                            class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer"
                        >
                        <p class="mt-1 text-[10px] text-zinc-400">JPG, PNG, WebP — auto-resized to 512×512</p>
                        @error('image_path')
                            <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                        @enderror
                    </div>
                </div>

                <div class="space-y-4">
                    <div>
                        <label class="block text-xs font-medium text-zinc-700 mb-1.5">Sort Order</label>
                        <input
                            type="number"
                            name="sort_order"
                            value="{{ old('sort_order', 99) }}"
                            min="0"
                            class="flex h-8 w-full rounded-md border {{ $errors->has('sort_order') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150"
                        >
                    </div>

                    <div>
                        <label class="flex items-center gap-2 cursor-pointer select-none">
                            <input type="hidden" name="is_active" value="0">
                            <input
                                type="checkbox"
                                name="is_active"
                                value="1"
                                {{ old('is_active', true) ? 'checked' : '' }}
                                class="w-3.5 h-3.5 rounded border-zinc-300 text-zinc-900 focus:ring-zinc-900 focus:ring-offset-0"
                            >
                            <span class="text-xs font-medium text-zinc-700">Active (visible to users)</span>
                        </label>
                    </div>
                </div>
            </div>

        </div>

        <div class="flex items-center gap-2 px-5 py-3.5 border-t border-zinc-100 bg-zinc-50/50">
            <button type="submit" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
                Create Avatar
            </button>
            <a href="{{ route('admin.avatars.index') }}" class="text-xs text-zinc-400 hover:text-zinc-700 px-3 py-1.5 rounded hover:bg-zinc-100 transition-colors duration-150">
                Cancel
            </a>
        </div>
    </form>
</div>

@endsection
