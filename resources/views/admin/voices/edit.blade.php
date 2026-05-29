@extends('admin.layouts.app')

@section('title', 'Edit Voice')

@section('content')

    <a href="{{ route('admin.voices.index') }}"
       class="inline-flex items-center gap-1.5 text-xs text-zinc-500 hover:text-zinc-800 transition-colors mb-4">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd"
                  d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                  clip-rule="evenodd"/>
        </svg>
        Back to Voices
    </a>

    <div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
        <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
            <div>
                <p class="text-sm font-semibold text-zinc-900">Edit Voice</p>
                <p class="text-xs text-zinc-500 mt-0.5">{{ $voice->name }}</p>
            </div>
        </div>

        <form method="POST" action="{{ route('admin.voices.update', $voice) }}" enctype="multipart/form-data">
            @csrf
            @method('PUT')
            @include('admin.voices.voice-form', ['voice' => $voice])

            <div class="flex items-center gap-2 px-5 py-3.5 border-t border-zinc-100 bg-zinc-50/50">
                <button type="submit"
                        class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 transition-all">
                    Update Voice
                </button>
                <a href="{{ route('admin.voices.index') }}"
                   class="text-xs text-zinc-400 hover:text-zinc-700 px-3 py-1.5 rounded hover:bg-zinc-100 transition-colors">
                    Cancel
                </a>
            </div>
        </form>
    </div>

@endsection
