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

            <div class="h-3.5 w-px bg-zinc-200 flex-shrink-0"></div>

            <select name="category" onchange="this.form.submit()"
                    class="h-6 rounded border border-zinc-200 bg-white text-xs text-zinc-600 px-2 pr-6 focus:outline-none focus:ring-1 focus:ring-zinc-300 cursor-pointer">
                <option value="">All Categories</option>
                @foreach($categories as $cat)
                    <option value="{{ $cat->id }}" {{ request('category') == $cat->id ? 'selected' : '' }}>
                        {{ $cat->name_en }}
                    </option>
                @endforeach
            </select>

            <select name="type" onchange="this.form.submit()"
                    class="h-6 rounded border border-zinc-200 bg-white text-xs text-zinc-600 px-2 pr-6 focus:outline-none focus:ring-1 focus:ring-zinc-300 cursor-pointer">
                <option value="">All Types</option>
                <option value="free" {{ request('type') === 'free' ? 'selected' : '' }}>Free</option>
                <option value="premium" {{ request('type') === 'premium' ? 'selected' : '' }}>Premium</option>
            </select>

            @if(request('search') || request('category') || request('type'))
                <a href="{{ route('admin.vocab.subcategories.index') }}" class="text-[10px] text-zinc-400 hover:text-zinc-700 transition-colors flex-shrink-0">Clear</a>
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
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Name (EN)</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Name (JP)</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Romaji</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Category</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Sort</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Status</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-l border-zinc-100">Action</th>
                </tr>
            </thead>

            @php
                $grouped = collect($subcategories->items())->groupBy('vocab_category_id');
                $rowNum  = ($subcategories->currentPage() - 1) * $subcategories->perPage();
            @endphp

            @forelse($grouped as $categoryId => $items)
                <tbody>
                    <tr class="border-b border-t border-zinc-100 bg-zinc-50/80">
                        <td colspan="10" class="px-4 py-1.5">
                            <span class="text-[10px] font-semibold uppercase tracking-wider text-zinc-500">
                                {{ $items->first()->category->name_en ?? 'Uncategorized' }}
                            </span>
                            <span class="ml-1.5 text-[10px] text-zinc-400">{{ $items->count() }} {{ Str::plural('item', $items->count()) }}</span>
                        </td>
                    </tr>
                </tbody>
                <tbody
                    class="sortable-group divide-y divide-zinc-100"
                    data-category-id="{{ $categoryId }}"
                    data-reorder-url="{{ route('admin.vocab.subcategories.reorder') }}"
                >
                    @foreach($items as $subcategory)
                        @php $rowNum++ @endphp
                        <tr data-id="{{ $subcategory->id }}" class="hover:bg-zinc-50/30 transition-colors duration-100">
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
                            <td class="w-10 px-3 py-2.5 text-center text-xs text-zinc-400 border-r border-zinc-100">
                                {{ $rowNum }}
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
                            <td class="px-4 py-2.5 text-xs text-zinc-600 sort-cell">{{ $subcategory->sort_order }}</td>
                            <td class="px-4 py-2.5">
                                @if($subcategory->is_premium)
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
                    @endforeach
                </tbody>
            @empty
                <tbody>
                    <tr>
                        <td colspan="10" class="px-4 py-10 text-center">
                            <p class="text-xs text-zinc-400">No entries found.</p>
                            <a href="{{ route('admin.vocab.subcategories.create') }}" class="mt-1 inline-flex text-xs text-zinc-900 underline underline-offset-2">Create the first one</a>
                        </td>
                    </tr>
                </tbody>
            @endforelse
        </table>
    </div>

    @if($subcategories->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">{{ $subcategories->links() }}</div>
    @endif
</div>

<div id="reorder-toast" class="fixed bottom-5 right-5 hidden px-3 py-2 rounded-lg text-xs font-medium shadow-lg z-50 transition-opacity duration-300"></div>

@endsection

@push('scripts')
<script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.2/Sortable.min.js"></script>
<script>
(function () {
    const csrfToken = document.querySelector('meta[name="csrf-token"]').content;

    document.querySelectorAll('.sortable-group').forEach(function (tbody) {
        const categoryId = tbody.dataset.categoryId;
        const reorderUrl = tbody.dataset.reorderUrl;

        new Sortable(tbody, {
            handle: '.drag-handle',
            animation: 150,
            ghostClass: 'opacity-40',
            chosenClass: 'bg-blue-50',
            onEnd: function () {
                const rows = Array.from(tbody.querySelectorAll('tr[data-id]'));
                const ids  = rows.map(function (r) { return r.dataset.id; });

                rows.forEach(function (row, i) {
                    const sortCell = row.querySelector('.sort-cell');
                    if (sortCell) sortCell.textContent = i + 1;
                });

                fetch(reorderUrl, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': csrfToken,
                    },
                    body: JSON.stringify({ ids: ids, category_id: categoryId }),
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
    });

    function showToast(message, type) {
        const toast = document.getElementById('reorder-toast');
        const base  = 'fixed bottom-5 right-5 px-3 py-2 rounded-lg text-xs font-medium shadow-lg z-50 transition-opacity duration-300';
        const color = type === 'success' ? 'bg-zinc-900 text-white' : 'bg-red-600 text-white';
        toast.className   = base + ' ' + color;
        toast.textContent = message;
        toast.style.opacity = '1';

        setTimeout(function () { toast.style.opacity = '0'; }, 1800);
        setTimeout(function () { toast.className = 'fixed bottom-5 right-5 hidden'; }, 2100);
    }
})();
</script>
@endpush
