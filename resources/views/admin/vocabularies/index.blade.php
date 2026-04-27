@extends('admin.layouts.app')
@section('title', 'Vocabulary')

@section('content')

{{-- Page Header --}}
<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Vocabulary</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage all vocabulary entries</p>
    </div>
    <a href="{{ route('admin.vocabularies.create') }}"
       class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
        <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
        </svg>
        New Entry
    </a>
</div>

{{-- Card --}}
<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">

    {{-- Card Header --}}
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">All Vocabulary</span>
        <span class="text-[10px] text-zinc-400">{{ $vocabularies->total() }} total</span>
    </div>

    {{-- Search --}}
    <form method="GET" action="{{ route('admin.vocabularies.index') }}">
        <div class="flex items-center gap-2 px-4 py-2.5 border-b border-zinc-100 bg-zinc-50/30">
            <svg class="w-3.5 h-3.5 text-zinc-400 flex-shrink-0" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
            </svg>
            <input type="text" name="search" value="{{ request('search') }}" placeholder="Search by word, romaji or meaning..."
                   class="flex-1 bg-transparent text-xs text-zinc-700 placeholder:text-zinc-400 focus:outline-none h-6">
            @if(request('search'))
                <a href="{{ route('admin.vocabularies.index') }}" class="text-[10px] text-zinc-400 hover:text-zinc-700 transition-colors">Clear</a>
            @endif
        </div>
    </form>

    {{-- Table --}}
    <div class="overflow-x-auto">
        <table class="min-w-full">
            <thead>
                <tr class="border-b border-zinc-100 bg-zinc-50/50">
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Word (JP)</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Romaji</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Meaning</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Subcategory</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">JLPT</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Sort</th>
                    <th class="px-4 py-2 text-right text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Actions</th>
                </tr>
            </thead>
            <tbody class="divide-y divide-zinc-100">
                @forelse ($vocabularies as $vocab)
                    <tr class="hover:bg-zinc-50/30 transition-colors duration-100">
                        <td class="px-4 py-2.5 text-xs font-medium text-zinc-900">{{ $vocab->word_jp }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $vocab->word_romaji }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600 max-w-[180px] truncate">{{ $vocab->meaning_en }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $vocab->subcategory->name_en ?? '—' }}</td>
                        <td class="px-4 py-2.5">
                            @if ($vocab->jlpt_level === 'N1')
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-bold bg-red-50 text-red-700 border border-red-200/80">N1</span>
                            @elseif ($vocab->jlpt_level === 'N2')
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-bold bg-orange-50 text-orange-700 border border-orange-200/80">N2</span>
                            @elseif ($vocab->jlpt_level === 'N3')
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-bold bg-yellow-50 text-yellow-700 border border-yellow-200/80">N3</span>
                            @elseif ($vocab->jlpt_level === 'N4')
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-bold bg-blue-50 text-blue-700 border border-blue-200/80">N4</span>
                            @elseif ($vocab->jlpt_level === 'N5')
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-bold bg-emerald-50 text-emerald-700 border border-emerald-200/80">N5</span>
                            @else
                                <span class="text-[10px] text-zinc-400">—</span>
                            @endif
                        </td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $vocab->sort_order }}</td>
                        <td class="px-4 py-2.5 text-right">
                            <div class="flex items-center justify-end gap-0.5">
                                <a href="{{ route('admin.vocabularies.edit', $vocab) }}"
                                   class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 transition-colors duration-150">
                                    Edit
                                </a>
                                <form method="POST" action="{{ route('admin.vocabularies.destroy', $vocab) }}"
                                      onsubmit="return confirm('Delete this vocabulary entry?')">
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
                        <td colspan="7" class="px-4 py-10 text-center">
                            <p class="text-xs text-zinc-400">No entries found.</p>
                            <a href="{{ route('admin.vocabularies.create') }}" class="mt-1 inline-flex text-xs text-zinc-900 underline underline-offset-2">Create the first one</a>
                        </td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>

    {{-- Pagination --}}
    @if ($vocabularies->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">{{ $vocabularies->links() }}</div>
    @endif

</div>

@endsection
