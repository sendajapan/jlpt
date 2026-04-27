@extends('admin.layouts.app')

@section('title', 'Categories')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Categories</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage all JLPT categories</p>
    </div>
    <a href="{{ route('admin.categories.create') }}" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
        </svg>
        New Category
    </a>
</div>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <div>
            <p class="text-sm font-semibold text-zinc-900">All Categories</p>
            <p class="text-xs text-zinc-500 mt-0.5">{{ $categories->total() }} {{ Str::plural('category', $categories->total()) }} total</p>
        </div>
    </div>

    <form method="GET" action="{{ route('admin.categories.index') }}">
        <div class="flex items-center gap-2 px-4 py-2.5 border-b border-zinc-100 bg-zinc-50/30">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5 text-zinc-400 shrink-0" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
            </svg>
            <input
                type="text"
                name="search"
                value="{{ request('search') }}"
                placeholder="Search categories..."
                class="flex-1 bg-transparent text-xs text-zinc-700 placeholder:text-zinc-400 focus:outline-none h-6"
            >
            @if(request('search'))
                <a href="{{ route('admin.categories.index') }}" class="text-xs text-zinc-400 hover:text-zinc-600 transition-colors shrink-0">Clear</a>
            @endif
        </div>
    </form>

    <table class="min-w-full">
        <thead>
            <tr class="border-b border-zinc-100 bg-zinc-50/50">
                <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Name</th>
                <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Japanese</th>
                <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Romaji</th>
                <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Sort</th>
                <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Type</th>
                <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400"></th>
            </tr>
        </thead>
        <tbody class="divide-y divide-zinc-100">
            @forelse($categories as $category)
                <tr class="hover:bg-zinc-50/30 transition-colors duration-100">
                    <td class="px-4 py-2.5 text-xs font-medium text-zinc-900">
                        <div class="flex items-center gap-2">
                            @if($category->icon_path)
                                <img src="{{ \Illuminate\Support\Facades\Storage::url($category->icon_path) }}" alt="{{ $category->name_en }}" class="w-6 h-6 rounded object-cover border border-zinc-200 shrink-0">
                            @endif
                            {{ $category->name_en }}
                        </div>
                    </td>
                    <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $category->name_jp }}</td>
                    <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $category->name_romaji ?? '—' }}</td>
                    <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $category->sort_order }}</td>
                    <td class="px-4 py-2.5 text-xs text-zinc-600">
                        @if($category->is_premium)
                            <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-amber-50 text-amber-700 border border-amber-200/80">Premium</span>
                        @else
                            <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-zinc-100 text-zinc-500">Free</span>
                        @endif
                    </td>
                    <td class="px-4 py-2.5 text-xs text-zinc-600">
                        <div class="flex items-center gap-1 justify-end">
                            <a href="{{ route('admin.categories.edit', $category) }}" class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 transition-colors duration-150">
                                <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" viewBox="0 0 20 20" fill="currentColor">
                                    <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                                </svg>
                            </a>
                            <form method="POST" action="{{ route('admin.categories.destroy', $category) }}" onsubmit="return confirm('Delete this category? This action cannot be undone.')">
                                @csrf
                                @method('DELETE')
                                <button type="submit" class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-red-600 hover:bg-red-50 transition-colors duration-150">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" viewBox="0 0 20 20" fill="currentColor">
                                        <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd" />
                                    </svg>
                                </button>
                            </form>
                        </div>
                    </td>
                </tr>
            @empty
                <tr>
                    <td colspan="6" class="px-4 py-10 text-center">
                        <p class="text-xs text-zinc-400">No categories found.</p>
                        <a href="{{ route('admin.categories.create') }}" class="mt-1 inline-flex text-xs text-zinc-900 underline underline-offset-2">Create the first one</a>
                    </td>
                </tr>
            @endforelse
        </tbody>
    </table>

    @if($categories->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">
            {{ $categories->links() }}
        </div>
    @endif
</div>

@endsection
