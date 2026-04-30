@extends('admin.layouts.app')

@section('title', 'Categories')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Categories</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage all JLPT categories</p>
    </div>
    <a href="{{ route('admin.vocab.categories.create') }}" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
        </svg>
        New Category
    </a>
</div>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">All Categories</span>
        <span class="text-[10px] text-zinc-400">{{ $categories->total() }} {{ Str::plural('category', $categories->total()) }} total</span>
    </div>

    <form method="GET" action="{{ route('admin.vocab.categories.index') }}">
        <div class="flex items-center gap-2 px-4 py-2.5 border-b border-zinc-100 bg-zinc-50/30">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5 text-zinc-400 shrink-0" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
            </svg>
            <input type="text" name="search" value="{{ request('search') }}" placeholder="Search categories..."
                   class="flex-1 bg-transparent text-xs text-zinc-700 placeholder:text-zinc-400 focus:outline-none h-6">
            @if(request('search'))
                <a href="{{ route('admin.vocab.categories.index') }}" class="text-xs text-zinc-400 hover:text-zinc-600 transition-colors shrink-0">Clear</a>
            @endif
        </div>
    </form>

    <div class="overflow-x-auto">
        <table class="min-w-full">
            <thead>
                <tr class="border-b border-zinc-100 bg-zinc-50/50">
                    <th class="w-8 px-2 py-2 text-center border-r border-zinc-100"></th>
                    <th class="w-10 px-3 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">S/N</th>
                    <th class="w-12 px-2 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">Icon</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Name</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Japanese</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Romaji</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Sort</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Type</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-l border-zinc-100">Action</th>
                </tr>
            </thead>
            <tbody
                id="sortable-tbody"
                data-reorder-url="{{ route('admin.vocab.categories.reorder') }}"
                data-page-offset="{{ ($categories->currentPage() - 1) * $categories->perPage() }}"
                class="divide-y divide-zinc-100"
            >
                @forelse($categories as $category)
                    <tr data-id="{{ $category->id }}" class="hover:bg-zinc-50/30 transition-colors duration-100">
                        <td class="w-8 px-2 py-2.5 text-center border-r border-zinc-100">
                            <span class="drag-handle inline-flex items-center justify-center cursor-grab active:cursor-grabbing text-zinc-300 hover:text-zinc-500 transition-colors duration-100">
                                <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" viewBox="0 0 16 16" fill="currentColor">
                                    <circle cx="5.5" cy="3.5" r="1.25"/>
                                    <circle cx="10.5" cy="3.5" r="1.25"/>
                                    <circle cx="5.5" cy="8" r="1.25"/>
                                    <circle cx="10.5" cy="8" r="1.25"/>
                                    <circle cx="5.5" cy="12.5" r="1.25"/>
                                    <circle cx="10.5" cy="12.5" r="1.25"/>
                                </svg>
                            </span>
                        </td>
                        <td class="w-10 px-3 py-2.5 text-center text-xs text-zinc-400 border-r border-zinc-100 sn-cell">
                            {{ ($categories->currentPage() - 1) * $categories->perPage() + $loop->iteration }}
                        </td>
                        <td class="w-12 px-2 py-2 text-center border-r border-zinc-100">
                            <form method="POST" action="{{ route('admin.vocab.categories.update-icon', $category) }}" enctype="multipart/form-data">
                                @csrf
                                @method('PATCH')
                                <label class="cursor-pointer block group">
                                    @if($category->icon_path)
                                        <img src="{{ \Illuminate\Support\Facades\Storage::url($category->icon_path) }}" alt="" class="w-7 h-7 mx-auto rounded object-cover border border-zinc-200 group-hover:opacity-60 transition-opacity">
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
                        <td class="px-4 py-2.5 text-xs font-medium text-zinc-900">{{ $category->name_en }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $category->name_jp }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $category->name_romaji ?? '—' }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600 sort-cell">{{ $category->sort_order }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">
                            @if($category->is_premium)
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-amber-50 text-amber-700 border border-amber-200/80">Premium</span>
                            @else
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-zinc-100 text-zinc-500">Free</span>
                            @endif
                        </td>
                        <td class="px-4 py-2.5 text-center border-l border-zinc-100">
                            <div class="flex items-center justify-center gap-0.5">
                                <a href="{{ route('admin.vocab.categories.edit', $category) }}"
                                   class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 transition-colors duration-150">
                                    Edit
                                </a>
                                <form method="POST" action="{{ route('admin.vocab.categories.destroy', $category) }}"
                                      onsubmit="return confirm('Delete this category? This action cannot be undone.')">
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
                            <p class="text-xs text-zinc-400">No categories found.</p>
                            <a href="{{ route('admin.vocab.categories.create') }}" class="mt-1 inline-flex text-xs text-zinc-900 underline underline-offset-2">Create the first one</a>
                        </td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>

    @if($categories->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">
            {{ $categories->links() }}
        </div>
    @endif
</div>

<div id="reorder-toast" class="fixed bottom-5 right-5 hidden px-3 py-2 rounded-lg text-xs font-medium shadow-lg z-50 transition-opacity duration-300"></div>

@endsection

@push('scripts')
<script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.2/Sortable.min.js"></script>
<script>
(function () {
    const tbody = document.getElementById('sortable-tbody');
    if (!tbody) return;

    const reorderUrl = tbody.dataset.reorderUrl;
    const csrfToken = document.querySelector('meta[name="csrf-token"]').content;

    new Sortable(tbody, {
        handle: '.drag-handle',
        animation: 150,
        ghostClass: 'opacity-40',
        chosenClass: 'bg-blue-50',
        onEnd: function () {
            const rows    = Array.from(tbody.querySelectorAll('tr[data-id]'));
            const offset  = parseInt(tbody.dataset.pageOffset, 10);
            const ids     = rows.map(r => r.dataset.id);

            rows.forEach(function (row, i) {
                const snCell   = row.querySelector('.sn-cell');
                const sortCell = row.querySelector('.sort-cell');
                const newOrder = offset + i + 1;
                if (snCell)   snCell.textContent   = newOrder;
                if (sortCell) sortCell.textContent = newOrder;
            });

            fetch(reorderUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken,
                },
                body: JSON.stringify({ ids: ids, offset: offset }),
            })
            .then(function (res) { return res.json(); })
            .then(function (data) {
                if (data.ok) showToast('Order saved', 'success');
                else         showToast('Failed to save order', 'error');
            })
            .catch(function () {
                showToast('Failed to save order', 'error');
            });
        },
    });

    function showToast(message, type) {
        const toast = document.getElementById('reorder-toast');
        const base  = 'fixed bottom-5 right-5 px-3 py-2 rounded-lg text-xs font-medium shadow-lg z-50 transition-opacity duration-300';
        const color = type === 'success' ? 'bg-zinc-900 text-white' : 'bg-red-600 text-white';
        toast.className  = base + ' ' + color;
        toast.textContent = message;
        toast.style.opacity = '1';

        setTimeout(function () { toast.style.opacity = '0'; }, 1800);
        setTimeout(function () { toast.className = 'fixed bottom-5 right-5 hidden'; }, 2100);
    }
})();
</script>
@endpush
