@extends('admin.layouts.app')

@section('title', 'Voices')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Voices</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage TTS voices used by FluentVox</p>
    </div>
    <a href="{{ route('admin.voices.create') }}" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
        </svg>
        New Voice
    </a>
</div>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">All Voices</span>
        <span class="text-[10px] text-zinc-400">{{ $voices->total() }} {{ Str::plural('voice', $voices->total()) }} total</span>
    </div>

    <form method="GET" action="{{ route('admin.voices.index') }}">
        <div class="flex items-center gap-2 px-4 py-2.5 border-b border-zinc-100 bg-zinc-50/30">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5 text-zinc-400 shrink-0" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
            </svg>
            <input type="text" name="search" value="{{ request('search') }}" placeholder="Search voices..."
                   class="flex-1 bg-transparent text-xs text-zinc-700 placeholder:text-zinc-400 focus:outline-none h-6">
            @if(request('search'))
                <a href="{{ route('admin.voices.index') }}" class="text-xs text-zinc-400 hover:text-zinc-600 transition-colors shrink-0">Clear</a>
            @endif
        </div>
    </form>

    <div class="overflow-x-auto">
        <table class="min-w-full">
            <thead>
                <tr class="border-b border-zinc-100 bg-zinc-50/50">
                    <th class="w-10 px-3 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">S/N</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Name</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Language</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Gender</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Model</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Settings</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Sample</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Default</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-l border-zinc-100">Action</th>
                </tr>
            </thead>
            <tbody class="divide-y divide-zinc-100">
                @forelse($voices as $voice)
                    <tr class="hover:bg-zinc-50/30 transition-colors duration-100">
                        <td class="w-10 px-3 py-2.5 text-center text-xs text-zinc-400 border-r border-zinc-100">
                            {{ ($voices->currentPage() - 1) * $voices->perPage() + $loop->iteration }}
                        </td>
                        <td class="px-4 py-2.5 text-xs font-medium text-zinc-900">
                            {{ $voice->name }}
                            @if($voice->description)
                                <p class="mt-0.5 text-[10px] font-normal text-zinc-400 max-w-xs truncate" title="{{ $voice->description }}">{{ $voice->description }}</p>
                            @endif
                        </td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $voice->language ?: '—' }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $voice->gender ? ucfirst($voice->gender) : '—' }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $voice->settings['model'] ?? '—' }}</td>
                        <td class="px-4 py-2.5">
                            <div class="flex flex-wrap gap-1">
                                @isset($voice->settings['exaggeration'])
                                    <span class="inline-flex items-center px-1.5 py-0.5 rounded bg-sky-50 text-sky-700 text-[10px]" title="Exaggeration">E <strong class="ml-0.5">{{ $voice->settings['exaggeration'] }}</strong></span>
                                @endisset
                                @isset($voice->settings['cfg_weight'])
                                    <span class="inline-flex items-center px-1.5 py-0.5 rounded bg-emerald-50 text-emerald-700 text-[10px]" title="CFG Weight / Pace">C <strong class="ml-0.5">{{ $voice->settings['cfg_weight'] }}</strong></span>
                                @endisset
                                @isset($voice->settings['temperature'])
                                    <span class="inline-flex items-center px-1.5 py-0.5 rounded bg-amber-50 text-amber-700 text-[10px]" title="Temperature">T <strong class="ml-0.5">{{ $voice->settings['temperature'] }}</strong></span>
                                @endisset
                                @isset($voice->settings['seed'])
                                    <span class="inline-flex items-center px-1.5 py-0.5 rounded bg-rose-50 text-rose-700 text-[10px]" title="Seed">S <strong class="ml-0.5">{{ $voice->settings['seed'] }}</strong></span>
                                @endisset
                            </div>
                        </td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">
                            @if($voice->reference_path)
                                <audio controls class="h-7" style="height:28px">
                                    <source src="{{ \Illuminate\Support\Facades\Storage::url($voice->reference_path) }}">
                                </audio>
                            @else
                                —
                            @endif
                        </td>
                        <td class="px-4 py-2.5 text-center">
                            <form method="POST" action="{{ route('admin.voices.toggle-default', $voice) }}" class="inline-flex">
                                @csrf
                                @method('PATCH')
                                <button type="submit"
                                        title="{{ $voice->is_default ? 'Click to unset default' : 'Set as default' }}"
                                        class="relative inline-flex items-center h-5 w-9 rounded-full transition-colors duration-150 focus:outline-none focus:ring-2 focus:ring-offset-1 focus:ring-emerald-500 {{ $voice->is_default ? 'bg-emerald-500' : 'bg-zinc-200' }}">
                                    <span class="inline-block w-4 h-4 bg-white rounded-full shadow transform transition-transform duration-150 {{ $voice->is_default ? 'translate-x-4' : 'translate-x-0.5' }}"></span>
                                </button>
                            </form>
                        </td>
                        <td class="px-4 py-2.5 text-center border-l border-zinc-100">
                            <div class="flex items-center justify-center gap-0.5">
                                <a href="{{ route('admin.voices.edit', $voice) }}"
                                   class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 transition-colors duration-150">
                                    Edit
                                </a>
                                <form method="POST" action="{{ route('admin.voices.destroy', $voice) }}"
                                      onsubmit="return confirm('Delete this voice? This action cannot be undone.')">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit"
                                            class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-red-600 hover:bg-red-50 transition-colors duration-150">
                                        Delete
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                @empty
                    <tr>
                        <td colspan="9" class="px-4 py-10 text-center">
                            <p class="text-xs text-zinc-400">No voices found.</p>
                            <a href="{{ route('admin.voices.create') }}" class="mt-1 inline-flex text-xs text-zinc-900 underline underline-offset-2">Create the first one</a>
                        </td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>

    @if($voices->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">
            {{ $voices->links() }}
        </div>
    @endif
</div>

@endsection
