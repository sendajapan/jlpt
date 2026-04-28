@extends('admin.layouts.app')

@section('title', 'Vocabulary')

@section('content')

<style>
  .toggle-row { cursor: pointer; }
  .toggle-row:hover { background-color: rgba(59, 130, 246, 0.04); }
  .toggle-row.expanded { background-color: rgba(59, 130, 246, 0.07); }
  .toggle-icon { transition: transform 0.25s ease; }
  .toggle-row.expanded .toggle-icon { transform: rotate(180deg); }
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
                <svg class="w-3.5 h-3.5 text-zinc-400 flex-shrink-0" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
                </svg>
                <input type="text" name="search" value="{{ request('search') }}" placeholder="Search by word, romaji or meaning..."
                       class="flex-1 bg-transparent text-xs text-zinc-700 placeholder:text-zinc-400 focus:outline-none h-6">
            </div>
            <div class="flex items-center gap-2 flex-wrap px-4 py-2.5 bg-zinc-50 border-t border-zinc-100">
                <select name="category_id" class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-900">
                    <option value="">All Categories</option>
                    @foreach ($categories as $cat)
                        <option value="{{ $cat->id }}" {{ request('category_id') == $cat->id ? 'selected' : '' }}>{{ $cat->name_en }}</option>
                    @endforeach
                </select>
                <select name="subcategory_id" class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-900">
                    <option value="">All Subcategories</option>
                    @foreach ($subcategories as $sub)
                        <option value="{{ $sub->id }}" {{ request('subcategory_id') == $sub->id ? 'selected' : '' }}>{{ $sub->category->name_en ?? '' }} › {{ $sub->name_en }}</option>
                    @endforeach
                </select>
                <select name="is_approved" class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-900">
                    <option value="">All Status</option>
                    <option value="1" {{ request('is_approved') === '1' ? 'selected' : '' }}>Approved</option>
                    <option value="0" {{ request('is_approved') === '0' ? 'selected' : '' }}>Not Approved</option>
                </select>
                <button type="submit" class="inline-flex items-center h-7 px-3 rounded bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 transition-colors">Filter</button>
                <a href="{{ route('admin.vocab.words.index') }}" class="inline-flex items-center h-7 px-3 rounded border border-zinc-200 bg-white text-xs text-zinc-500 hover:text-zinc-800 hover:bg-zinc-50 transition-colors">Reset</a>
            </div>
        </div>
    </form>

    <div class="overflow-x-auto">
        <table class="min-w-full">
            <thead>
                <tr class="border-b border-zinc-100 bg-zinc-50/50">
                    <th class="w-10 px-3 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">S/N</th>
                    <th class="w-10 px-2 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">Image</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-blue-500">EN</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-red-500">JP</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-emerald-500">RM</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Category</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Subcategory</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Sort</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Approved</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-l border-zinc-100">Action</th>
                    <th class="w-8 px-2 py-2 text-center"></th>
                </tr>
            </thead>
            <tbody class="divide-y divide-zinc-100">
                @forelse ($vocabularies as $vocab)
                    <!-- Main Row -->
                    <tr class="toggle-row" data-row="{{ $vocab->id }}">
                        <td class="w-10 px-3 py-3 text-center text-xs text-zinc-400 border-r border-zinc-100">
                            {{ ($vocabularies->currentPage() - 1) * $vocabularies->perPage() + $loop->iteration }}
                        </td>
                        <td class="w-10 px-2 py-3 text-center border-r border-zinc-100">
                            <form method="POST" action="{{ route('admin.vocab.words.update-image', $vocab) }}" enctype="multipart/form-data">
                                @csrf
                                @method('PATCH')
                                <label class="cursor-pointer block group">
                                    @if($vocab->image_path)
                                        <img src="{{ \Illuminate\Support\Facades\Storage::url($vocab->image_path) }}" alt="" class="w-6 h-6 mx-auto rounded object-cover border border-zinc-200 group-hover:opacity-60 transition-opacity">
                                    @else
                                        <div class="w-6 h-6 mx-auto rounded border-2 border-dashed border-zinc-200 flex items-center justify-center group-hover:border-zinc-400 transition-colors">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3 text-zinc-300 group-hover:text-zinc-400" viewBox="0 0 20 20" fill="currentColor">
                                                <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
                                            </svg>
                                        </div>
                                    @endif
                                    <input type="file" name="image_path" accept="image/*" class="hidden" onchange="this.form.submit()">
                                </label>
                            </form>
                        </td>
                        <td class="px-4 py-3 text-xs font-medium text-zinc-900">{{ $vocab->word_en ?: '—' }}</td>
                        <td class="px-4 py-3 text-xs font-medium text-zinc-900">{{ $vocab->word_jp ?: '—' }}</td>
                        <td class="px-4 py-3 text-xs text-zinc-600">{{ $vocab->word_romaji ?: '—' }}</td>
                        <td class="px-4 py-3 text-xs text-zinc-600">{{ $vocab->subcategory->category->name_en ?? '—' }}</td>
                        <td class="px-4 py-3 text-xs text-zinc-600">{{ $vocab->subcategory->name_en ?? '—' }}</td>
                        <td class="px-4 py-3 text-xs text-zinc-500 text-center">{{ $vocab->sort_order }}</td>
                        <td class="px-4 py-3 text-center">
                            <form method="POST" action="{{ route('admin.vocab.words.toggle-approved', $vocab) }}" onclick="event.stopPropagation()">
                                @csrf
                                @method('PATCH')
                                <button type="submit" class="relative inline-flex h-5 w-8 items-center rounded-full transition-colors focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1 {{ $vocab->is_approved ? 'bg-zinc-900' : 'bg-white border border-zinc-300' }}" role="switch" aria-checked="{{ $vocab->is_approved ? 'true' : 'false' }}">
                                    <span class="inline-block h-3.5 w-3.5 rounded-full transition-transform {{ $vocab->is_approved ? 'bg-white' : 'bg-zinc-900' }}" style="transform: translateX({{ $vocab->is_approved ? '13px' : '2px' }})"></span>
                                </button>
                            </form>
                        </td>
                        <td class="px-4 py-3 text-center border-l border-zinc-100">
                            <div class="inline-flex items-center justify-center gap-1">
                                <a href="{{ route('admin.vocab.words.edit', $vocab) }}"
                                   class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 transition-colors duration-150">
                                    Edit
                                </a>
                                <form method="POST" action="{{ route('admin.vocab.words.destroy', $vocab) }}" onsubmit="return confirm('Delete this vocabulary entry?')">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit" class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-red-400 hover:text-red-600 hover:bg-red-50 transition-colors duration-150">
                                        Delete
                                    </button>
                                </form>
                            </div>
                        </td>
                        <td class="w-8 px-2 py-3 text-center">
                            <svg class="toggle-icon w-4 h-4 text-zinc-400 mx-auto" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7"/>
                            </svg>
                        </td>
                    </tr>

                    <!-- Expanded Row -->
                    <tr class="row-expanded-{{ $vocab->id }} hidden border-b border-zinc-100">
                        <td colspan="11" style="padding:0;">
                            <div style="display:grid;grid-template-columns:repeat(3,1fr);border-top:1px solid #f4f4f5;">

                                <!-- ENGLISH -->
                                <div style="border-right:1px solid #f4f4f5;">
                                    <div style="height:3px;background:#3b82f6;"></div>
                                    <div class="px-4 pt-4 pb-4 space-y-2.5">
                                        <div class="flex items-center gap-1.5 mb-3">
                                            <span class="w-5 h-5 rounded bg-blue-50 text-blue-600 text-[9px] font-bold border border-blue-100 flex items-center justify-center">EN</span>
                                            <span class="text-[10px] font-semibold uppercase tracking-widest text-zinc-500">English</span>
                                        </div>
                                        <div>
                                            <p class="text-[10px] font-medium text-zinc-500 mb-1">Word</p>
                                            <p class="text-xs font-medium text-zinc-900">{{ $vocab->word_en ?: '—' }}</p>
                                        </div>
                                        <div>
                                            <p class="text-[10px] font-medium text-zinc-500 mb-1">Word Audio</p>
                                            @if($vocab->audio_en)
                                                <button class="inline-flex h-8 items-center gap-1.5 px-3 rounded-md border border-blue-200 bg-white hover:bg-blue-50 text-xs font-medium text-blue-600 transition-colors" onclick="document.getElementById('audio-en-{{ $vocab->id }}').play()">
                                                    <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 20 20"><path d="M6.3 2.841A1.5 1.5 0 004 4.11V15.89a1.5 1.5 0 002.3 1.269l9.344-5.89a1.5 1.5 0 000-2.538L6.3 2.84z"/></svg>
                                                    Play
                                                </button>
                                                <audio id="audio-en-{{ $vocab->id }}" class="hidden"><source src="{{ \Illuminate\Support\Facades\Storage::url($vocab->audio_en) }}"></audio>
                                            @else
                                                <span class="text-[10px] text-zinc-400">No audio</span>
                                            @endif
                                        </div>
                                        <div class="pt-1 border-t border-zinc-100">
                                            <p class="text-[10px] font-medium text-zinc-500 mb-1">Sentence</p>
                                            <p class="text-[11px] text-zinc-700 leading-relaxed">{{ $vocab->sentence_en ?: '—' }}</p>
                                        </div>
                                        <div>
                                            <p class="text-[10px] font-medium text-zinc-500 mb-1">Sentence Audio</p>
                                            @if($vocab->sentence_audio_en)
                                                <button class="inline-flex h-8 items-center gap-1.5 px-3 rounded-md border border-blue-200 bg-white hover:bg-blue-50 text-xs font-medium text-blue-600 transition-colors" onclick="document.getElementById('audio-en-sent-{{ $vocab->id }}').play()">
                                                    <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 20 20"><path d="M6.3 2.841A1.5 1.5 0 004 4.11V15.89a1.5 1.5 0 002.3 1.269l9.344-5.89a1.5 1.5 0 000-2.538L6.3 2.84z"/></svg>
                                                    Play
                                                </button>
                                                <audio id="audio-en-sent-{{ $vocab->id }}" class="hidden"><source src="{{ \Illuminate\Support\Facades\Storage::url($vocab->sentence_audio_en) }}"></audio>
                                            @else
                                                <span class="text-[10px] text-zinc-400">No audio</span>
                                            @endif
                                        </div>
                                    </div>
                                </div>

                                <!-- JAPANESE -->
                                <div style="border-right:1px solid #f4f4f5;">
                                    <div style="height:3px;background:#ef4444;"></div>
                                    <div class="px-4 pt-4 pb-4 space-y-2.5">
                                        <div class="flex items-center gap-1.5 mb-3">
                                            <span class="w-5 h-5 rounded bg-red-50 text-red-600 text-[9px] font-bold border border-red-100 flex items-center justify-center">JP</span>
                                            <span class="text-[10px] font-semibold uppercase tracking-widest text-zinc-500">Japanese</span>
                                        </div>
                                        <div>
                                            <p class="text-[10px] font-medium text-zinc-500 mb-1">Word</p>
                                            <p class="text-xs font-medium text-zinc-900">{{ $vocab->word_jp ?: '—' }}</p>
                                        </div>
                                        <div>
                                            <p class="flex items-center gap-1 text-[10px] font-medium text-zinc-500 mb-1">
                                                Word Audio <span class="text-[8px] text-zinc-400 bg-zinc-100 px-1 rounded">shared</span>
                                            </p>
                                            @if($vocab->audio_jp)
                                                <button class="inline-flex h-8 items-center gap-1.5 px-3 rounded-md border border-red-200 bg-white hover:bg-red-50 text-xs font-medium text-red-600 transition-colors" onclick="document.getElementById('audio-jp-{{ $vocab->id }}').play()">
                                                    <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 20 20"><path d="M6.3 2.841A1.5 1.5 0 004 4.11V15.89a1.5 1.5 0 002.3 1.269l9.344-5.89a1.5 1.5 0 000-2.538L6.3 2.84z"/></svg>
                                                    Play
                                                </button>
                                                <audio id="audio-jp-{{ $vocab->id }}" class="hidden"><source src="{{ \Illuminate\Support\Facades\Storage::url($vocab->audio_jp) }}"></audio>
                                            @else
                                                <span class="text-[10px] text-zinc-400">No audio</span>
                                            @endif
                                        </div>
                                        <div class="pt-1 border-t border-zinc-100">
                                            <p class="text-[10px] font-medium text-zinc-500 mb-1">Sentence</p>
                                            <p class="text-[11px] text-zinc-700 leading-relaxed">{{ $vocab->sentence_jp ?: '—' }}</p>
                                        </div>
                                        <div>
                                            <p class="flex items-center gap-1 text-[10px] font-medium text-zinc-500 mb-1">
                                                Sentence Audio <span class="text-[8px] text-zinc-400 bg-zinc-100 px-1 rounded">shared</span>
                                            </p>
                                            @if($vocab->sentence_audio_jp)
                                                <button class="inline-flex h-8 items-center gap-1.5 px-3 rounded-md border border-red-200 bg-white hover:bg-red-50 text-xs font-medium text-red-600 transition-colors" onclick="document.getElementById('audio-jp-sent-{{ $vocab->id }}').play()">
                                                    <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 20 20"><path d="M6.3 2.841A1.5 1.5 0 004 4.11V15.89a1.5 1.5 0 002.3 1.269l9.344-5.89a1.5 1.5 0 000-2.538L6.3 2.84z"/></svg>
                                                    Play
                                                </button>
                                                <audio id="audio-jp-sent-{{ $vocab->id }}" class="hidden"><source src="{{ \Illuminate\Support\Facades\Storage::url($vocab->sentence_audio_jp) }}"></audio>
                                            @else
                                                <span class="text-[10px] text-zinc-400">No audio</span>
                                            @endif
                                        </div>
                                    </div>
                                </div>

                                <!-- ROMAJI -->
                                <div>
                                    <div style="height:3px;background:#10b981;"></div>
                                    <div class="px-4 pt-4 pb-4 space-y-2.5">
                                        <div class="flex items-center gap-1.5 mb-3">
                                            <span class="w-5 h-5 rounded bg-emerald-50 text-emerald-600 text-[9px] font-bold border border-emerald-100 flex items-center justify-center">RM</span>
                                            <span class="text-[10px] font-semibold uppercase tracking-widest text-zinc-500">Romaji</span>
                                        </div>
                                        <div>
                                            <p class="text-[10px] font-medium text-zinc-500 mb-1">Word</p>
                                            <p class="text-xs font-medium text-zinc-900">{{ $vocab->word_romaji ?: '—' }}</p>
                                        </div>
                                        <div>
                                            <p class="text-[10px] font-medium text-zinc-500 mb-1">Word Audio</p>
                                            <span class="text-[10px] text-zinc-400 italic">Uses JP audio</span>
                                        </div>
                                        <div class="pt-1 border-t border-zinc-100">
                                            <p class="text-[10px] font-medium text-zinc-500 mb-1">Sentence</p>
                                            <p class="text-[11px] text-zinc-700 leading-relaxed">{{ $vocab->sentence_romaji ?: '—' }}</p>
                                        </div>
                                        <div>
                                            <p class="text-[10px] font-medium text-zinc-500 mb-1">Sentence Audio</p>
                                            <span class="text-[10px] text-zinc-400 italic">Uses JP sentence audio</span>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </td>
                    </tr>
                @empty
                    <tr>
                        <td colspan="11" class="px-4 py-10 text-center">
                            <p class="text-xs text-zinc-400">No entries found.</p>
                            <a href="{{ route('admin.vocab.words.create') }}" class="mt-1 inline-flex text-xs text-zinc-900 underline underline-offset-2">Create the first one</a>
                        </td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>

    @if ($vocabularies->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">{{ $vocabularies->links() }}</div>
    @endif
</div>

<script>
  document.querySelectorAll('.toggle-row').forEach(row => {
    row.addEventListener('click', function(e) {
      if (e.target.closest('button, a, form') || e.target.closest('label')) return;

      const rowId = this.dataset.row;
      const expandedRow = document.querySelector(`.row-expanded-${rowId}`);
      const arrow = this.querySelector('.arrow-down');

      this.classList.toggle('expanded');
      expandedRow.classList.toggle('hidden');
    });
  });
</script>

@endsection
