@extends('admin.layouts.app')

@section('title', 'Kanji')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Kanji</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage all kanji entries</p>
    </div>
    <a href="{{ route('admin.kanji.create') }}" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
        </svg>
        New Kanji
    </a>
</div>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">All Kanji</span>
        <span class="text-[10px] text-zinc-400">{{ $kanjis->total() }} total</span>
    </div>

    <form method="GET" action="{{ route('admin.kanji.index') }}">
        <div class="flex items-center gap-2 px-4 py-2.5 border-b border-zinc-100 bg-zinc-50/30 flex-wrap">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5 text-zinc-400 shrink-0" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
            </svg>
            <input type="text" name="search" value="{{ request('search') }}" placeholder="Search kanji, translation or meaning..."
                   class="flex-1 min-w-[160px] bg-transparent text-xs text-zinc-700 placeholder:text-zinc-400 focus:outline-none h-6">
            <select name="jlpt" class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-900">
                <option value="">All Levels</option>
                @foreach (['N5', 'N4', 'N3', 'N2', 'N1'] as $level)
                    <option value="{{ $level }}" @selected(request('jlpt') === $level)>{{ $level }}</option>
                @endforeach
            </select>
            <select name="is_premium" class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-900">
                <option value="">All</option>
                <option value="0" @selected(request('is_premium') === '0')>Free</option>
                <option value="1" @selected(request('is_premium') === '1')>Premium</option>
            </select>
            <button type="submit" class="h-7 px-3 rounded bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 transition-colors">Filter</button>
            @if(request()->hasAny(['search', 'jlpt', 'is_premium']))
                <a href="{{ route('admin.kanji.index') }}" class="text-xs text-zinc-400 hover:text-zinc-600 transition-colors">Clear</a>
            @endif
        </div>
    </form>

    <div class="overflow-x-auto">
        <table class="min-w-full">
            <thead>
                <tr class="border-b border-zinc-100 bg-zinc-50/50">
                    <th class="w-10 px-3 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">S/N</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Kanji</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Translation</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">On / Kun</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">JLPT</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Strokes</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Status</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-l border-zinc-100">Action</th>
                </tr>
            </thead>
            <tbody class="divide-y divide-zinc-100">
                @forelse($kanjis as $kanji)
                    <tr class="hover:bg-zinc-50/30 transition-colors duration-100">
                        <td class="w-10 px-3 text-center text-xs text-zinc-400 border-r border-zinc-100">
                            {{ ($kanjis->currentPage() - 1) * $kanjis->perPage() + $loop->iteration }}
                        </td>
                        <td class="px-4 py-2.5">
                            <span class="text-2xl font-medium text-zinc-900">{{ $kanji->kanji }}</span>
                        </td>
                        <td class="px-4 py-2.5">
                            <p class="text-xs font-medium text-zinc-900">{{ $kanji->translate }}</p>
                            <p class="text-[10px] text-zinc-400 mt-0.5 truncate max-w-[200px]">{{ $kanji->meanings }}</p>
                        </td>
                        <td class="px-4 py-2.5">
                            <p class="text-[10px] text-zinc-500">{{ $kanji->readings_on }}</p>
                            <p class="text-[10px] text-zinc-400 mt-0.5">{{ $kanji->readings_kun }}</p>
                        </td>
                        <td class="px-4 py-2.5">
                            @if($kanji->jlpt)
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-blue-50 text-blue-700 border border-blue-200/80">{{ $kanji->jlpt }}</span>
                            @else
                                <span class="text-xs text-zinc-300">—</span>
                            @endif
                        </td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $kanji->strokes }}</td>
                        <td class="px-4 py-2.5">
                            @if($kanji->is_premium)
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-amber-50 text-amber-700 border border-amber-200/80">Premium</span>
                            @else
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-green-50 text-green-700 border border-green-200/80">Free</span>
                            @endif
                        </td>
                        <td class="px-4 text-center border-l border-zinc-100">
                            <div class="flex items-center justify-center gap-0.5">
                                <a href="{{ route('admin.kanji.edit', $kanji) }}"
                                   class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 transition-colors duration-150">
                                    Edit
                                </a>
                                <form method="POST" action="{{ route('admin.kanji.destroy', $kanji) }}"
                                      onsubmit="return confirm('Delete this kanji? This cannot be undone.')">
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
                        <td colspan="8" class="px-4 py-10 text-center">
                            <p class="text-xs text-zinc-400">No kanji found.</p>
                            <a href="{{ route('admin.kanji.create') }}" class="mt-1 inline-flex text-xs text-zinc-900 underline underline-offset-2">Create the first one</a>
                        </td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>

    @if($kanjis->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">
            {{ $kanjis->links() }}
        </div>
    @endif
</div>

@endsection
