@extends('admin.layouts.app')

@section('title', 'Vocabulary')

@section('content')

    <style>
        .toggle-row {
            cursor: pointer;
        }

        .toggle-row:hover {
            background-color: rgba(59, 130, 246, 0.04);
        }

        .toggle-row.expanded {
            background-color: rgba(59, 130, 246, 0.07);
        }

        .toggle-icon {
            transition: transform 0.25s ease;
        }

        .toggle-row.expanded .toggle-icon {
            transform: rotate(180deg);
        }

        .max-w-screen-xl, .mx-auto w-full {
            min-width: 100% !important;
        }
    </style>

    <div class="flex items-center justify-between mb-4">
        <div>
            <h1 class="text-sm font-semibold text-zinc-900">Vocabulary</h1>
            <p class="text-xs text-zinc-500 mt-0.5">Manage all vocabulary entries</p>
        </div>
        <a href="{{ route('admin.vocab.words.create') }}"
           class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
            <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
            </svg>
            New Entry
        </a>
    </div>

    <div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
        <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
            <span class="text-sm font-semibold text-zinc-900">All Vocabulary</span>
            <span class="text-[10px] text-zinc-400">{{ $vocabularies->total() }} total</span>
        </div>

        <form method="GET" action="{{ route('admin.vocab.words.index') }}">
            <div class="border-b border-zinc-100">
                <div class="flex items-center gap-2 px-4 py-2.5 bg-white">
                    <svg class="w-3.5 h-3.5 text-zinc-400 flex-shrink-0" fill="none" stroke="currentColor"
                         stroke-width="2" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round"
                              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                    </svg>
                    <input type="text" name="search" value="{{ request('search') }}"
                           placeholder="Search by word, romaji or meaning..."
                           class="flex-1 bg-transparent text-xs text-zinc-700 placeholder:text-zinc-400 focus:outline-none h-6">
                </div>
                <div x-data="{
                    catId: '{{ request('category_id') }}',
                    subId: '{{ request('subcategory_id') }}',
                    allSubs: @js($subcategories->map(fn($s) => ['id' => $s->id, 'name' => $s->name_en, 'cat' => $s->vocab_category_id])),
                    get subs() { return this.catId ? this.allSubs.filter(s => s.cat == this.catId) : this.allSubs; }
                 }" class="flex items-center gap-2 flex-wrap px-4 py-2.5 bg-zinc-50 border-t border-zinc-100">
                    <select name="category_id" x-model="catId" @change="subId = ''"
                            class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-900">
                        <option value="">All Categories</option>
                        @foreach ($categories as $cat)
                            <option value="{{ $cat->id }}">{{ $cat->name_en }}</option>
                        @endforeach
                    </select>
                    <select name="subcategory_id" x-model="subId"
                            class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-900">
                        <option value="">All Subcategories</option>
                        <template x-for="sub in subs" :key="sub.id">
                            <option :value="sub.id" :selected="sub.id == subId" x-text="sub.name"></option>
                        </template>
                    </select>
                    <select name="is_approved"
                            class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-900">
                        <option value="">All Status</option>
                        <option value="1" {{ request('is_approved') === '1' ? 'selected' : '' }}>Approved</option>
                        <option value="0" {{ request('is_approved') === '0' ? 'selected' : '' }}>Not Approved</option>
                    </select>
                    <select name="image_path"
                            class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-900">
                        <option value="all">All</option>
                        <option value="images" {{ request('image_path') === 'images' ? 'selected' : '' }}>Added Images
                        </option>
                        <option value="pending" {{ request('image_path') === 'pending' ? 'selected' : '' }}>Pending
                            Images
                        </option>
                    </select>
                    <button type="submit"
                            class="inline-flex items-center h-7 px-3 rounded bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 transition-colors">
                        Filter
                    </button>
                    <a href="{{ route('admin.vocab.words.index') }}"
                       class="inline-flex items-center h-7 px-3 rounded border border-zinc-200 bg-white text-xs text-zinc-500 hover:text-zinc-800 hover:bg-zinc-50 transition-colors">Reset</a>
                </div>
            </div>
        </form>

        <div class="overflow-x-auto">
            <table class="min-w-full">
                <thead>
                <tr class="border-b border-zinc-100 bg-zinc-50/50">
                    <th class="w-8 px-2 py-2 text-center border-r border-zinc-100"></th>
                    <th class="w-10 px-3 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">
                        S/N
                    </th>
                    <th class="w-30 px-2 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">
                        Image
                    </th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-blue-500">
                        Word
                    </th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-red-500">
                        Sentence
                    </th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">
                        Category
                    </th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">
                        Subcategory
                    </th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400">
                        Sort
                    </th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400">
                        Approved
                    </th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-l border-zinc-100">
                        Action
                    </th>
                    <th class="w-8 px-2 py-2 text-center"></th>
                </tr>
                </thead>

                @php
                    $grouped = collect($vocabularies->items())->groupBy('vocab_subcategory_id');
                    $rowNum  = ($vocabularies->currentPage() - 1) * $vocabularies->perPage();
                @endphp

                @forelse($grouped as $subcategoryId => $items)
                    <tbody
                        class="sortable-group divide-y divide-zinc-100"
                        data-subcat-id="{{ $subcategoryId }}"
                        data-reorder-url="{{ route('admin.vocab.words.reorder') }}"
                    >

                    @php
                        foreach($items as $vocab){
                            if(isset($_GET['showlinks'])){
                                echo 'https://jlpt.senda.fit/admin/vocab/words/'.$vocab->id.'/edit<br>';
                            }
                    }
                    @endphp

                    @foreach($items as $vocab)
                        @php $rowNum++ @endphp
                        <tr data-id="{{ $vocab->id }}" class="toggle-row" data-row="{{ $vocab->id }}">
                            <td class="w-8 px-2 py-2.5 text-center border-r border-zinc-100">
                                <span
                                    class="drag-handle inline-flex items-center justify-center cursor-grab active:cursor-grabbing text-zinc-300 hover:text-zinc-500 transition-colors duration-100">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" viewBox="0 0 16 16"
                                         fill="currentColor">
                                        <circle cx="5.5" cy="3.5" r="1.25"/>
                                        <circle cx="10.5" cy="3.5" r="1.25"/>
                                        <circle cx="5.5" cy="8" r="1.25"/>
                                        <circle cx="10.5" cy="8" r="1.25"/>
                                        <circle cx="5.5" cy="12.5" r="1.25"/>
                                        <circle cx="10.5" cy="12.5" r="1.25"/>
                                    </svg>
                                </span>
                            </td>
                            <td class="w-10 px-3 py-3 text-center text-xs text-zinc-400 border-r border-zinc-100">{{ $rowNum }}</td>
                            <td class="w-30 px-2 py-3 text-center border-r border-zinc-100">
                                <form method="POST" action="{{ route('admin.vocab.words.update-image', $vocab) }}"
                                      enctype="multipart/form-data">
                                    @csrf
                                    @method('PATCH')
                                    @if($vocab->image_path)
                                        <div x-data="{ hovered: false }" class="relative inline-block"
                                             @mouseenter="hovered = true" @mouseleave="hovered = false"
                                             onclick="event.stopPropagation()">
                                            <img
                                                src="{{ \Illuminate\Support\Facades\Storage::url($vocab->image_path) }}"
                                                alt=""
                                                class="mx-auto rounded object-cover border border-zinc-200 transition-opacity"
                                                :class="hovered ? 'opacity-50' : 'opacity-100'"
                                                style="border-radius:20px; max-height:150px; max-width:150px; background-size:contain; background-repeat:round; background-image:url('{{ asset($vocab->vocab_bg_path) }}')">
                                            <div x-show="hovered"
                                                 class="absolute inset-0 flex items-center justify-center gap-1.5"
                                                 style="display:none">
                                                <a href="{{ \Illuminate\Support\Facades\Storage::url($vocab->image_path) }}"
                                                   target="_blank" onclick="event.stopPropagation()"
                                                   class="inline-flex items-center h-6 px-2 rounded bg-white/90 border border-zinc-200 text-[10px] font-medium text-zinc-700 hover:bg-white transition-colors shadow-sm">Preview</a>
                                                <label
                                                    class="inline-flex items-center h-6 px-2 rounded bg-white/90 border border-zinc-200 text-[10px] font-medium text-zinc-700 hover:bg-white transition-colors shadow-sm cursor-pointer">
                                                    Replace
                                                    <input type="file" name="image_path" accept="image/*" class="hidden"
                                                           onchange="this.form.submit()">
                                                </label>
                                            </div>
                                        </div>
                                    @else
                                        <label
                                            class="cursor-pointer inline-flex items-center h-6 px-2 rounded border border-zinc-200 bg-white text-[10px] font-medium text-zinc-500 hover:text-zinc-800 hover:bg-zinc-50 transition-colors"
                                            onclick="event.stopPropagation()">
                                            Upload
                                            <input type="file" name="image_path" accept="image/*" class="hidden"
                                                   onchange="this.form.submit()">
                                        </label>
                                    @endif
                                </form>
                            </td>
                            <td class="px-4 py-3 text-xs font-medium text-zinc-900">
                                <table class="w-full">
                                    <tr class="border-b border-zinc-300">
                                        <td width="70%" nowrap><span
                                                class="text-blue-500">En: {{ $vocab->word_en ?: '—' }}</span></td>
                                        <td width="30%">
                                            @include('admin.vocab.words._audio_cell', ['field' => 'audio_en'])
                                        </td>
                                    </tr>
                                    <tr class="border-b border-zinc-300">
                                        <td width="70%" nowrap><span
                                                class="text-red-500">Ro: {{ $vocab->word_romaji ?: '—' }}</span></td>
                                        <td width="30%"></td>
                                    </tr>
                                    <tr class="border-b border-zinc-300">
                                        <td width="70%" nowrap><span
                                                class="text-purple-500">Jp: {{ $vocab->word_jp ?: '—' }}</span></td>
                                        <td width="30%">
                                            @include('admin.vocab.words._audio_cell', ['field' => 'audio_jp'])
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td class="px-4 py-3 text-xs font-medium text-zinc-900">
                                <table class="w-full">
                                    <tr class="border-b border-zinc-300">
                                        <td width="90%" nowrap><span
                                                class="text-blue-500">En: {{ $vocab->sentence_en ?: '—' }}</span></td>
                                        <td width="10%">
                                            @include('admin.vocab.words._audio_cell', ['field' => 'sentence_audio_en'])
                                        </td>
                                    </tr>
                                    <tr class="border-b border-zinc-300">
                                        <td width="90%" nowrap><span
                                                class="text-red-500">Ro: {{ $vocab->sentence_romaji ?: '—' }}</span>
                                        </td>
                                        <td width="10%"></td>
                                    </tr>
                                    <tr class="border-b border-zinc-300">
                                        <td width="90%" nowrap><span
                                                class="text-purple-500">Jp: {{ $vocab->sentence_jp ?: '—' }}</span></td>
                                        <td width="10%">
                                            @include('admin.vocab.words._audio_cell', ['field' => 'sentence_audio_jp'])
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td class="px-4 py-3 text-xs text-zinc-600">
                                @if($vocab->subcategory?->category)
                                    <a href="{{ route('admin.vocab.words.index', ['category_id' => $vocab->subcategory->category->id]) }}"
                                       onclick="event.stopPropagation()"
                                       class="underline underline-offset-2 hover:text-zinc-900">{{ $vocab->subcategory->category->name_en }}</a>
                                @else
                                    —
                                @endif
                            </td>
                            <td class="px-4 py-3 text-xs text-zinc-600">
                                @if($vocab->subcategory)
                                    <a href="{{ route('admin.vocab.words.index', ['subcategory_id' => $vocab->subcategory->id]) }}"
                                       onclick="event.stopPropagation()"
                                       class="underline underline-offset-2 hover:text-zinc-900">{{ $vocab->subcategory->name_en }}</a>
                                @else
                                    —
                                @endif
                            </td>
                            <td class="px-4 py-3 text-xs text-zinc-500 text-center sort-cell">{{ $vocab->sort_order }}</td>
                            <td class="px-4 py-3 text-center">
                                <form method="POST" action="{{ route('admin.vocab.words.toggle-approved', $vocab) }}"
                                      onclick="event.stopPropagation()">
                                    @csrf
                                    @method('PATCH')
                                    <button type="submit"
                                            class="relative inline-flex h-5 w-8 items-center rounded-full transition-colors focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1 {{ $vocab->is_approved ? 'bg-zinc-900' : 'bg-white border border-zinc-300' }}"
                                            role="switch" aria-checked="{{ $vocab->is_approved ? 'true' : 'false' }}">
                                        <span
                                            class="inline-block h-3.5 w-3.5 rounded-full transition-transform {{ $vocab->is_approved ? 'bg-white' : 'bg-zinc-900' }}"
                                            style="transform: translateX({{ $vocab->is_approved ? '13px' : '2px' }})"></span>
                                    </button>
                                </form>
                            </td>
                            <td class="px-4 py-3 text-center border-l border-zinc-100">
                                <div class="inline-flex items-center justify-center gap-1">
                                    <a href="{{ route('admin.vocab.words.edit', $vocab) }}"
                                       onclick="event.stopPropagation()"
                                       class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 transition-colors duration-150">
                                        Edit
                                    </a>
                                    <form method="POST" action="{{ route('admin.vocab.words.destroy', $vocab) }}"
                                          onsubmit="return confirm('Delete this vocabulary entry?')"
                                          onclick="event.stopPropagation()">
                                        @csrf
                                        @method('DELETE')
                                        <button type="submit"
                                                class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-red-400 hover:text-red-600 hover:bg-red-50 transition-colors duration-150">
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
                        <td colspan="12" class="px-4 py-10 text-center">
                            <p class="text-xs text-zinc-400">No entries found.</p>
                            <a href="{{ route('admin.vocab.words.create') }}"
                               class="mt-1 inline-flex text-xs text-zinc-900 underline underline-offset-2">Create the
                                first one</a>
                        </td>
                    </tr>
                    </tbody>
                @endforelse
            </table>
        </div>

        @if ($vocabularies->hasPages())
            <div class="px-4 py-2.5 border-t border-zinc-100">{{ $vocabularies->links() }}</div>
        @endif
    </div>

    <div id="reorder-toast"
         class="fixed bottom-5 right-5 hidden px-3 py-2 rounded-lg text-xs font-medium shadow-lg z-50 transition-opacity duration-300"></div>

@endsection

@push('scripts')
    <script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.2/Sortable.min.js"></script>
    <script>
        (function () {
            const csrfToken = document.querySelector('meta[name="csrf-token"]').content;

            document.querySelectorAll('.sortable-group').forEach(function (tbody) {
                const subcatId = tbody.dataset.subcatId;
                const reorderUrl = tbody.dataset.reorderUrl;

                new Sortable(tbody, {
                    handle: '.drag-handle',
                    filter: '.detail-row',
                    animation: 150,
                    ghostClass: 'opacity-40',
                    chosenClass: 'bg-blue-50',
                    onEnd: function (evt) {
                        const draggedRow = evt.item;
                        const rowId = draggedRow.dataset.id;

                        const detailRow = tbody.querySelector('.row-expanded-' + rowId);
                        if (detailRow) {
                            tbody.insertBefore(detailRow, draggedRow.nextSibling);
                        }

                        const mainRows = Array.from(tbody.querySelectorAll('tr[data-id]'));
                        const ids = mainRows.map(function (r) {
                            return r.dataset.id;
                        });

                        mainRows.forEach(function (row, i) {
                            const sortCell = row.querySelector('.sort-cell');
                            if (sortCell) sortCell.textContent = i + 1;
                        });

                        fetch(reorderUrl, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                                'X-CSRF-TOKEN': csrfToken,
                            },
                            body: JSON.stringify({ids: ids, subcategory_id: subcatId}),
                        })
                            .then(function (res) {
                                return res.json();
                            })
                            .then(function (data) {
                                if (data.ok) showToast('Order saved', 'success');
                                else showToast('Failed to save order', 'error');
                            })
                            .catch(function () {
                                showToast('Failed to save order', 'error');
                            });
                    },
                });
            });

            document.querySelectorAll('.toggle-row').forEach(function (row) {
                row.addEventListener('click', function (e) {
                    if (e.target.closest('button, a, form') || e.target.closest('label') || e.target.closest('.drag-handle')) return;

                    const rowId = this.dataset.row;
                    const expandedRow = document.querySelector('.row-expanded-' + rowId);

                    this.classList.toggle('expanded');
                    expandedRow.classList.toggle('hidden');
                });
            });

            function showToast(message, type) {
                const toast = document.getElementById('reorder-toast');
                const base = 'fixed bottom-5 right-5 px-3 py-2 rounded-lg text-xs font-medium shadow-lg z-50 transition-opacity duration-300';
                const color = type === 'success' ? 'bg-zinc-900 text-white' : 'bg-red-600 text-white';
                toast.className = base + ' ' + color;
                toast.textContent = message;
                toast.style.opacity = '1';

                setTimeout(function () {
                    toast.style.opacity = '0';
                }, 1800);
                setTimeout(function () {
                    toast.className = 'fixed bottom-5 right-5 hidden';
                }, 2100);
            }
        })();
    </script>
@endpush
