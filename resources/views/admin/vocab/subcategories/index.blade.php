@extends('admin.layouts.app')

@section('title', 'Subcategories')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Subcategories</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage subcategories grouped by category</p>
    </div>
    <a href="{{ route('admin.vocab.subcategories.create') }}"
       class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
        <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
        </svg>
        New Subcategory
    </a>
</div>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">All Subcategories</span>
        <span class="text-[10px] text-zinc-400">{{ $subcategories->total() }} total</span>
    </div>

    <form method="GET" action="{{ route('admin.vocab.subcategories.index') }}">
        <div class="flex items-center gap-2 px-4 py-2.5 border-b border-zinc-100 bg-zinc-50/30">
            <svg class="w-3.5 h-3.5 text-zinc-400 flex-shrink-0" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
            </svg>
            <input type="text" name="search" value="{{ request('search') }}" placeholder="Search subcategories..."
                   class="flex-1 bg-transparent text-xs text-zinc-700 placeholder:text-zinc-400 focus:outline-none h-6">
            @if(request('search'))
                <a href="{{ route('admin.vocab.subcategories.index') }}" class="text-[10px] text-zinc-400 hover:text-zinc-700 transition-colors">Clear</a>
            @endif
        </div>
    </form>

    <div class="overflow-x-auto">
        <table class="min-w-full">
            <thead>
                <tr class="border-b border-zinc-100 bg-zinc-50/50">
                    <th class="w-10 px-3 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">S/N</th>
                    <th class="w-12 px-2 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">Icon</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Name (EN)</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Name (JP)</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Romaji</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Category</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Sort</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Status</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-l border-zinc-100">Action</th>
                </tr>
            </thead>
            <tbody class="divide-y divide-zinc-100">
                @forelse ($subcategories as $subcategory)
                    <tr class="hover:bg-zinc-50/30 transition-colors duration-100">
                        <td class="w-10 px-3 py-2.5 text-center text-xs text-zinc-400 border-r border-zinc-100">
                            {{ ($subcategories->currentPage() - 1) * $subcategories->perPage() + $loop->iteration }}
                        </td>
                        <td class="w-12 px-2 py-2 text-center border-r border-zinc-100">
                            <form method="POST" action="{{ route('admin.vocab.subcategories.update-icon', $subcategory) }}" enctype="multipart/form-data">
                                @csrf
                                @method('PATCH')
                                <label class="cursor-pointer block group">
                                    @if($subcategory->icon_path)
                                        <img src="{{ \Illuminate\Support\Facades\Storage::url($subcategory->icon_path) }}" alt="" class="w-7 h-7 mx-auto rounded object-cover border border-zinc-200 group-hover:opacity-60 transition-opacity">
                                    @else
                                        <div class="w-7 h-7 mx-auto rounded border-2 border-dashed border-zinc-200 flex items-center justify-center group-hover:border-zinc-400 transition-colors">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3 text-zinc-300 group-hover:text-zinc-400" viewBox="0 0 20 20" fill="currentColor">
                                                <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
                                            </svg>
                                        </div>
                                    @endif
                                    <input type="file" name="icon_path" accept="image/*" class="hidden" onchange="this.form.submit()">
                                </label>
                            </form>
                        </td>
                        <td class="px-4 py-2.5 text-xs font-medium text-zinc-900">{{ $subcategory->name_en }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $subcategory->name_jp }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $subcategory->name_romaji ?? '—' }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $subcategory->category->name_en ?? '—' }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $subcategory->sort_order }}</td>
                        <td class="px-4 py-2.5">
                            @if ($subcategory->is_premium)
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-amber-50 text-amber-700 border border-amber-200/80">Premium</span>
                            @else
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-zinc-100 text-zinc-500">Free</span>
                            @endif
                        </td>
                        <td class="px-4 py-2.5 text-center border-l border-zinc-100">
                            <div class="flex items-center justify-center gap-0.5">
                                <a href="{{ route('admin.vocab.subcategories.edit', $subcategory) }}"
                                   class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 transition-colors duration-150">
                                    Edit
                                </a>
                                <form method="POST" action="{{ route('admin.vocab.subcategories.destroy', $subcategory) }}"
                                      onsubmit="return confirm('Delete this subcategory? All associated vocabulary will also be deleted.')">
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
                            <p class="text-xs text-zinc-400">No entries found.</p>
                            <a href="{{ route('admin.vocab.subcategories.create') }}" class="mt-1 inline-flex text-xs text-zinc-900 underline underline-offset-2">Create the first one</a>
                        </td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>

    @if ($subcategories->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">{{ $subcategories->links() }}</div>
    @endif
</div>

@endsection
