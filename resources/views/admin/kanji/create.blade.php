@extends('admin.layouts.app')

@section('title', 'New Kanji')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Kanji</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage all kanji entries</p>
    </div>
</div>

<a href="{{ route('admin.kanji.index') }}" class="inline-flex items-center gap-1.5 text-xs text-zinc-500 hover:text-zinc-800 transition-colors mb-4">
    <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3" viewBox="0 0 20 20" fill="currentColor">
        <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
    </svg>
    Back to Kanji
</a>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="px-5 py-3 border-b border-zinc-100">
        <p class="text-sm font-semibold text-zinc-900">New Kanji</p>
    </div>

    <form method="POST" action="{{ route('admin.kanji.store') }}">
        @csrf

        <div class="p-5 space-y-4">

            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Kanji Character<span class="text-zinc-400 ml-0.5">*</span></label>
                    <input type="text" name="kanji" value="{{ old('kanji') }}" placeholder="e.g. 一"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('kanji') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('kanji') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">JLPT Level</label>
                    <select name="jlpt" class="flex h-8 w-full rounded-md border {{ $errors->has('jlpt') ? 'border-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0">
                        <option value="">— None —</option>
                        @foreach (['N5', 'N4', 'N3', 'N2', 'N1'] as $level)
                            <option value="{{ $level }}" @selected(old('jlpt') === $level)>{{ $level }}</option>
                        @endforeach
                    </select>
                    @error('jlpt') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Vocab ID <span class="text-zinc-400 text-[10px]">(vocab_words.id)</span></label>
                    <input type="number" name="vocab_id" value="{{ old('vocab_id') }}" min="0" placeholder="optional"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('vocab_id') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('vocab_id') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Translation</label>
                    <input type="text" name="translate" value="{{ old('translate') }}" placeholder="e.g. One"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('translate') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('translate') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Meanings</label>
                    <input type="text" name="meanings" value="{{ old('meanings') }}" placeholder="e.g. One, Single"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('meanings') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('meanings') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">On'yomi Readings</label>
                    <input type="text" name="readings_on" value="{{ old('readings_on') }}" placeholder="e.g. いち, いつ"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('readings_on') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('readings_on') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Kun'yomi Readings</label>
                    <input type="text" name="readings_kun" value="{{ old('readings_kun') }}" placeholder="e.g. ひと, ひとつ"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('readings_kun') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('readings_kun') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>
            </div>

            <div class="grid grid-cols-2 sm:grid-cols-4 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Strokes<span class="text-zinc-400 ml-0.5">*</span></label>
                    <input type="number" name="strokes" value="{{ old('strokes', 0) }}" min="0"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('strokes') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('strokes') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Grade</label>
                    <input type="number" name="grade" value="{{ old('grade') }}" min="0" placeholder="1–8"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('grade') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('grade') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Frequency<span class="text-zinc-400 ml-0.5">*</span></label>
                    <input type="number" name="freq" value="{{ old('freq', 0) }}" min="0"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('freq') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('freq') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Level</label>
                    <input type="number" name="level" value="{{ old('level') }}" min="0" placeholder="lesson level"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('level') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('level') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>
            </div>

            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Radicals</label>
                <input type="text" name="radicals" value="{{ old('radicals') }}" placeholder="e.g. Ground, Two"
                       class="flex h-8 w-full rounded-md border {{ $errors->has('radicals') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                @error('radicals') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
            </div>

            <div>
                <label class="flex items-center gap-2 cursor-pointer select-none">
                    <input type="hidden" name="is_premium" value="0">
                    <input type="checkbox" name="is_premium" value="1" {{ old('is_premium') ? 'checked' : '' }}
                           class="w-3.5 h-3.5 rounded border-zinc-300 text-zinc-900 focus:ring-zinc-900 focus:ring-offset-0">
                    <span class="text-xs font-medium text-zinc-700">Premium (locked for free users)</span>
                </label>
            </div>

        </div>

        <div class="flex items-center gap-2 px-5 py-3.5 border-t border-zinc-100 bg-zinc-50/50">
            <button type="submit" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
                Create Kanji
            </button>
            <a href="{{ route('admin.kanji.index') }}" class="text-xs text-zinc-400 hover:text-zinc-700 px-3 py-1.5 rounded hover:bg-zinc-100 transition-colors duration-150">
                Cancel
            </a>
        </div>
    </form>
</div>

@endsection
