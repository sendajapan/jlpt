@extends('admin.layouts.app')
@section('title', 'Edit Vocabulary Entry')
@php use Illuminate\Support\Facades\Storage; @endphp

@section('content')

<a href="{{ route('admin.vocab.words.index') }}" class="inline-flex items-center gap-1.5 text-xs text-zinc-500 hover:text-zinc-800 transition-colors mb-4">
    <svg class="w-3 h-3" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
    </svg>
    Back to Vocabulary
</a>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">

    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">Edit Vocabulary Entry</span>
        <span class="text-[10px] text-zinc-400">ID #{{ $vocabulary->id }}</span>
    </div>

    <form method="POST" action="{{ route('admin.vocab.words.update', $vocabulary) }}" enctype="multipart/form-data">
        @csrf
        @method('PUT')

        <div class="flex divide-x divide-zinc-100">

            <div class="flex-1 px-5 py-4 space-y-5 min-w-0">

                <div x-data="{
                    catId: '{{ old('vocab_category_id', $vocabulary->subcategory->vocab_category_id ?? '') }}',
                    subId: '{{ old('vocab_subcategory_id', $vocabulary->vocab_subcategory_id) }}',
                    allSubs: @js($subcategories->map(fn($s) => ['id' => $s->id, 'name' => $s->name_en, 'cat' => $s->vocab_category_id])),
                    get subs() { return this.catId ? this.allSubs.filter(s => s.cat == this.catId) : []; }
                }" class="space-y-3">
                    <input type="hidden" name="vocab_category_id" :value="catId">

                    <div>
                        <label class="block text-xs font-medium text-zinc-700 mb-1.5">Category <span class="text-red-500">*</span></label>
                        <select x-model="catId" @change="subId = ''"
                                class="flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                            <option value="">Select category</option>
                            @foreach($categories as $cat)
                                <option value="{{ $cat->id }}">{{ $cat->name_en }}</option>
                            @endforeach
                        </select>
                    </div>

                    <div>
                        <label class="block text-xs font-medium text-zinc-700 mb-1.5">Subcategory <span class="text-red-500">*</span></label>
                        <select name="vocab_subcategory_id" x-model="subId"
                                :disabled="!catId"
                                :class="!catId ? 'opacity-50 cursor-not-allowed' : ''"
                                class="{{ $errors->has('vocab_subcategory_id') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                            <option value="">Select subcategory</option>
                            <template x-for="sub in subs" :key="sub.id">
                                <option :value="sub.id" :selected="sub.id == subId" x-text="sub.name"></option>
                            </template>
                        </select>
                        @error('vocab_subcategory_id') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                    </div>

                    <div>
                        <label class="block text-xs font-medium text-zinc-700 mb-1.5">Part of Speech</label>
                        <select name="pos" class="{{ $errors->has('pos') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                            <option value="">— none —</option>
                            @foreach(['noun','pronoun','adjective','verb','adverb','preposition','conjunction','interjection'] as $posOption)
                                <option value="{{ $posOption }}" {{ old('pos', $vocabulary->pos) === $posOption ? 'selected' : '' }}>{{ ucfirst($posOption) }}</option>
                            @endforeach
                        </select>
                        @error('pos') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                    </div>
                </div>

                <div class="pl-3 border-l-2 border-blue-500">
                    <span class="text-[9px] font-bold uppercase tracking-[0.18em] text-blue-500 block mb-2.5">English</span>
                    <div class="grid grid-cols-2 gap-3 mb-2.5">
                        <div>
                            <label class="block text-[10px] font-medium text-zinc-500 mb-1">Word <span class="text-red-400">*</span></label>
                            <input type="text" name="word_en" value="{{ old('word_en', $vocabulary->word_en) }}"
                                   class="{{ $errors->has('word_en') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                            @error('word_en') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                        </div>
                        <div x-data="{ src: @js($vocabulary->audio_en ? Storage::url($vocabulary->audio_en) : null) }">
                            <label class="block text-[10px] font-medium text-zinc-500 mb-1">Word Audio</label>
                            <audio x-show="src" :src="src" controls class="h-7 w-full mb-1.5"></audio>
                            <input type="file" name="audio_en" accept="audio/*"
                                   x-on:change="src = $event.target.files[0] ? URL.createObjectURL($event.target.files[0]) : null"
                                   class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer">
                            <p class="mt-1 text-[10px] text-zinc-400">{{ $vocabulary->audio_en ? 'Leave empty to keep current' : 'MP3, WAV, OGG, AAC' }}</p>
                            @error('audio_en') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                        </div>
                    </div>
                    <div class="grid grid-cols-2 gap-3">
                        <div>
                            <label class="block text-[10px] font-medium text-zinc-500 mb-1">Sentence</label>
                            <textarea name="sentence_en" rows="2"
                                      class="{{ $errors->has('sentence_en') ? 'flex w-full rounded-md border border-red-400 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150 resize-none' : 'flex w-full rounded-md border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150 resize-none' }}">{{ old('sentence_en', $vocabulary->sentence_en) }}</textarea>
                            @error('sentence_en') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                        </div>
                        <div x-data="{ src: @js($vocabulary->sentence_audio_en ? Storage::url($vocabulary->sentence_audio_en) : null) }">
                            <label class="block text-[10px] font-medium text-zinc-500 mb-1">Sentence Audio</label>
                            <audio x-show="src" :src="src" controls class="h-7 w-full mb-1.5"></audio>
                            <input type="file" name="sentence_audio_en" accept="audio/*"
                                   x-on:change="src = $event.target.files[0] ? URL.createObjectURL($event.target.files[0]) : null"
                                   class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer">
                            <p class="mt-1 text-[10px] text-zinc-400">{{ $vocabulary->sentence_audio_en ? 'Leave empty to keep current' : 'MP3, WAV, OGG, AAC' }}</p>
                            @error('sentence_audio_en') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                        </div>
                    </div>
                </div>

                <div class="pl-3 border-l-2 border-red-500">
                    <span class="text-[9px] font-bold uppercase tracking-[0.18em] text-red-500 block mb-2.5">Japanese</span>
                    <div class="grid grid-cols-2 gap-3 mb-2.5">
                        <div>
                            <label class="block text-[10px] font-medium text-zinc-500 mb-1">Word <span class="text-red-400">*</span></label>
                            <input type="text" name="word_jp" value="{{ old('word_jp', $vocabulary->word_jp) }}"
                                   class="{{ $errors->has('word_jp') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                            @error('word_jp') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                        </div>
                        <div x-data="{ src: @js($vocabulary->audio_jp ? Storage::url($vocabulary->audio_jp) : null) }">
                            <label class="flex items-center gap-1 text-[10px] font-medium text-zinc-500 mb-1">
                                Word Audio
                                <span class="text-[8px] text-zinc-400 bg-zinc-100 px-1 py-0.5 rounded">shared with Romaji</span>
                            </label>
                            <audio x-show="src" :src="src" controls class="h-7 w-full mb-1.5"></audio>
                            <input type="file" name="audio_jp" accept="audio/*"
                                   x-on:change="src = $event.target.files[0] ? URL.createObjectURL($event.target.files[0]) : null"
                                   class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer">
                            <p class="mt-1 text-[10px] text-zinc-400">{{ $vocabulary->audio_jp ? 'Leave empty to keep current' : 'MP3, WAV, OGG, AAC' }}</p>
                            @error('audio_jp') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                        </div>
                    </div>
                    <div class="grid grid-cols-2 gap-3">
                        <div>
                            <label class="block text-[10px] font-medium text-zinc-500 mb-1">Sentence</label>
                            <textarea name="sentence_jp" rows="2"
                                      class="{{ $errors->has('sentence_jp') ? 'flex w-full rounded-md border border-red-400 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150 resize-none' : 'flex w-full rounded-md border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150 resize-none' }}">{{ old('sentence_jp', $vocabulary->sentence_jp) }}</textarea>
                            @error('sentence_jp') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                        </div>
                        <div x-data="{ src: @js($vocabulary->sentence_audio_jp ? Storage::url($vocabulary->sentence_audio_jp) : null) }">
                            <label class="flex items-center gap-1 text-[10px] font-medium text-zinc-500 mb-1">
                                Sentence Audio
                                <span class="text-[8px] text-zinc-400 bg-zinc-100 px-1 py-0.5 rounded">shared with Romaji</span>
                            </label>
                            <audio x-show="src" :src="src" controls class="h-7 w-full mb-1.5"></audio>
                            <input type="file" name="sentence_audio_jp" accept="audio/*"
                                   x-on:change="src = $event.target.files[0] ? URL.createObjectURL($event.target.files[0]) : null"
                                   class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer">
                            <p class="mt-1 text-[10px] text-zinc-400">{{ $vocabulary->sentence_audio_jp ? 'Leave empty to keep current' : 'MP3, WAV, OGG, AAC' }}</p>
                            @error('sentence_audio_jp') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                        </div>
                    </div>
                </div>

                <div class="pl-3 border-l-2 border-emerald-500">
                    <span class="text-[9px] font-bold uppercase tracking-[0.18em] text-emerald-500 block mb-2.5">Romaji</span>
                    <div class="grid grid-cols-2 gap-3 mb-2.5">
                        <div>
                            <label class="block text-[10px] font-medium text-zinc-500 mb-1">Word <span class="text-red-400">*</span></label>
                            <input type="text" name="word_romaji" value="{{ old('word_romaji', $vocabulary->word_romaji) }}"
                                   class="{{ $errors->has('word_romaji') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                            @error('word_romaji') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                        </div>
                        <div>
                            <label class="block text-[10px] font-medium text-zinc-500 mb-1">Word Audio</label>
                            <div class="flex h-8 w-full items-center gap-2 rounded-md border border-zinc-100 bg-zinc-50 px-2.5">
                                <svg class="w-3 h-3 text-zinc-300 shrink-0" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M13.19 8.688a4.5 4.5 0 011.242 7.244l-4.5 4.5a4.5 4.5 0 01-6.364-6.364l1.757-1.757m13.35-.622l1.757-1.757a4.5 4.5 0 00-6.364-6.364l-4.5 4.5a4.5 4.5 0 001.242 7.244"/></svg>
                                <span class="text-[10px] text-zinc-400 italic">Uses Japanese word audio</span>
                            </div>
                        </div>
                    </div>
                    <div class="grid grid-cols-2 gap-3">
                        <div>
                            <label class="block text-[10px] font-medium text-zinc-500 mb-1">Sentence</label>
                            <textarea name="sentence_romaji" rows="2"
                                      class="{{ $errors->has('sentence_romaji') ? 'flex w-full rounded-md border border-red-400 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150 resize-none' : 'flex w-full rounded-md border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150 resize-none' }}">{{ old('sentence_romaji', $vocabulary->sentence_romaji) }}</textarea>
                            @error('sentence_romaji') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                        </div>
                        <div>
                            <label class="block text-[10px] font-medium text-zinc-500 mb-1">Sentence Audio</label>
                            <div class="flex h-8 w-full items-center gap-2 rounded-md border border-zinc-100 bg-zinc-50 px-2.5">
                                <svg class="w-3 h-3 text-zinc-300 shrink-0" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M13.19 8.688a4.5 4.5 0 011.242 7.244l-4.5 4.5a4.5 4.5 0 01-6.364-6.364l1.757-1.757m13.35-.622l1.757-1.757a4.5 4.5 0 00-6.364-6.364l-4.5 4.5a4.5 4.5 0 001.242 7.244"/></svg>
                                <span class="text-[10px] text-zinc-400 italic">Uses Japanese sentence audio</span>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            <div class="flex-shrink-0 px-4 py-4 bg-zinc-50/40 flex flex-col gap-4">

                <div>
                    <label class="block text-[10px] font-medium text-zinc-500 mb-1.5">Image</label>
                    <x-image-preview name="image_path" id="image_path_preview" hint="JPG, PNG, WebP" :en="$vocabulary->word_en" :jp="$vocabulary->word_jp" :rm="$vocabulary->word_romaji" :current="$vocabulary->image_path ? Storage::url($vocabulary->image_path) : null" >
                        @error('image_path')
                            <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                        @enderror
                    </x-image-preview>
                </div>
                <div class="float-left">
                    <input type="hidden" name="image_thumbnail_bg" id="image_thumbnail_bg" value="{{ old('image_thumbnail_bg', $vocabulary->image_thumbnail_bg) }}">
                    <div class="pl-3 border-l-2 border-blue-500">
                        <span class="text-[9px] font-bold uppercase tracking-[0.18em] text-blue-500 block mb-2.5">Light</span>
                    </div>


                    @foreach($vocab_bg as $key=>$color)

                        @if($key%5==0)
                        <div style="clear:both"></div>
                        @endif

                        @if($color->vocab_bg_sort==41)

                        <div style="clear:both"></div>
                        <div class="pl-3 border-l-2 border-blue-500">
                            <span class="text-[9px] font-bold uppercase tracking-[0.18em] text-blue-500 block mb-2.5">Medium</span>
                        </div>
                        @endif
                        @if($color->vocab_bg_sort==81)

                        <div style="clear:both"></div>
                        <div class="pl-3 border-l-2 border-blue-500">
                            <span class="text-[9px] font-bold uppercase tracking-[0.18em] text-blue-500 block mb-2.5">Dark</span>
                        </div>
                        @endif


                        <div class="w-12 h-12 rounded cursor-pointer border-2 border-gray-300 hover:scale-110 transition bg-cover bg-center mr-2 mb-2"
                            style="background-image: url('{{ asset($color->vocab_bg_path) }}'); background-size:cover; float:left;"
                            onclick="selectBgColor('{{ $color->vocab_bg_id }}', '{{ asset($color->vocab_bg_path) }}')"
                            title="{{ $color->vocab_bg_path }}"
                        ></div>

                    @endforeach
                </div>
                <div>
                    <label class="block text-[10px] font-medium text-zinc-500 mb-1">Sort Order</label>
                    <input type="number" name="sort_order" value="{{ old('sort_order', $vocabulary->sort_order) }}" min="0" max="9999"
                           class="{{ $errors->has('sort_order') ? 'flex h-8 w-full rounded-md border border-red-400 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-red-400 focus:ring-offset-0 transition-shadow duration-150' : 'flex h-8 w-full rounded-md border border-zinc-200 bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150' }}">
                    @error('sort_order') <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p> @enderror
                </div>

                <div class="mt-auto pt-3 border-t border-zinc-100 space-y-2.5">
                    <label class="flex items-center gap-2 cursor-pointer select-none">
                        <input type="hidden" name="is_premium" value="0">
                        <input type="checkbox" name="is_premium" id="is_premium" value="1"
                               {{ old('is_premium', $vocabulary->is_premium) ? 'checked' : '' }}
                               class="w-3.5 h-3.5 rounded border-zinc-300 text-zinc-900 focus:ring-zinc-900 focus:ring-offset-0">
                        <span class="text-xs font-medium text-zinc-700">Premium content</span>
                    </label>

                    <label class="flex items-center gap-2 cursor-pointer select-none">
                        <input type="hidden" name="is_approved" value="0">
                        <input type="checkbox" name="is_approved" id="is_approved" value="1"
                               {{ old('is_approved', $vocabulary->is_approved) ? 'checked' : '' }}
                               class="w-3.5 h-3.5 rounded border-zinc-300 text-emerald-600 focus:ring-emerald-600 focus:ring-offset-0">
                        <span class="text-xs font-medium text-zinc-700">Approved</span>
                    </label>
                </div>

            </div>

        </div>

        <div class="flex items-center gap-2 px-5 py-3.5 border-t border-zinc-100 bg-zinc-50/50">
            <button type="submit"
                    class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
                Update Entry
            </button>
            <a href="{{ route('admin.vocab.words.index') }}"
               class="text-xs text-zinc-400 hover:text-zinc-700 px-3 py-1.5 rounded hover:bg-zinc-100 transition-colors duration-150">
                Cancel
            </a>
        </div>

    </form>
</div>

@endsection


@push('scripts')

<script>
function selectBgColor(vocab_id, vocab_path){
    document.getElementById('image_thumbnail_bg').value = vocab_id;
    document.getElementById('is_approved').checked = true;
    change_bg(vocab_path);
}
function change_bg(vocab_path){
    const previewList = document.getElementsByClassName('object-cover');
    preview = previewList[0];
    preview.style.backgroundImage = `url(${vocab_path})`;
    preview.style.backgroundSize = 'cover';
    preview.style.backgroundPosition = 'center';
}
    document.addEventListener('DOMContentLoaded', function () {
        const vocabBg = @json($vocab_bg);
        const bg = vocabBg.find(item => item.vocab_bg_id == {{ $vocabulary->image_thumbnail_bg }});
        change_bg('{{ asset('') }}'+bg.vocab_bg_path);
    });

</script>


@endpush
