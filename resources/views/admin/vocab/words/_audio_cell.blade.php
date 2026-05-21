@php
    $existing = $vocab->{$field};
    $reviewedField = $field.'_reviewed';
    $audioId = "audio-{$field}-{$vocab->id}";
    $generateUrl = route('admin.vocab.words.generate-audio', $vocab);
    $regenerateUrl = route('admin.vocab.words.regenerate-audio', $vocab);
    $deleteUrl = route('admin.vocab.words.delete-audio', $vocab);
    $toggleReviewedUrl = route('admin.vocab.words.toggle-reviewed', $vocab);
    $initialVoiceId = $vocab->voice_id ?? $defaultVoiceId;
    $initialReviewed = (bool) $vocab->{$reviewedField};
@endphp

<div x-data="{
        gone: false,
        loading: false,
        url: null,
        confirm: false,
        voiceId: '{{ $initialVoiceId }}',
        reviewed: {{ $initialReviewed ? 'true' : 'false' }},
        run(url) {
            this.loading = true;
            return fetch(url + '?field={{ $field }}&voice=' + this.voiceId)
                .then(r => r.json())
                .then(d => { this.url = d.url; this.loading = false; })
                .catch(() => { this.loading = false; });
        },
        del() {
            return fetch('{{ $deleteUrl }}', {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json', 'X-CSRF-TOKEN': document.querySelector('meta[name=csrf-token]').content },
                body: JSON.stringify({ field: '{{ $field }}' })
            }).then(() => { this.url = null; this.gone = true; this.confirm = false; });
        },
        toggleReviewed(val) {
            this.reviewed = val;
            fetch('{{ $toggleReviewedUrl }}', {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json', 'X-CSRF-TOKEN': document.querySelector('meta[name=csrf-token]').content },
                body: JSON.stringify({ field: '{{ $field }}', value: val })
            });
        }
     }"
     @voice-sync.window="if ($event.detail.vocabId === {{ $vocab->id }}) voiceId = $event.detail.voiceId"
     onclick="event.stopPropagation()">

    @if($existing)
        <audio id="{{ $audioId }}" class="hidden"><source src="{{ \Illuminate\Support\Facades\Storage::url($existing) }}"></audio>
    @endif

    <template x-if="!gone && !url && {{ $existing ? 'true' : 'false' }}">
        <div id="id_{{$field}}" class="inline-flex items-center gap-1">
            <label class="inline-flex items-center gap-1 cursor-pointer select-none">
                <input type="checkbox" :checked="reviewed" @change="toggleReviewed($event.target.checked)" class="accent-green-600 w-3.5 h-3.5">
                <span class="text-[10px]" :class="reviewed ? 'text-green-600 font-medium' : 'text-zinc-400'">Reviewed</span>
            </label>
            <button class="inline-flex h-7 items-center gap-1 px-2 rounded border border-blue-200 bg-white hover:bg-blue-50 text-[10px] font-medium text-blue-600 transition-colors" onclick="document.getElementById('{{ $audioId }}').play()">
                <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 20 20"><path d="M6.3 2.841A1.5 1.5 0 004 4.11V15.89a1.5 1.5 0 002.3 1.269l9.344-5.89a1.5 1.5 0 000-2.538L6.3 2.84z"/></svg>
                Play
            </button>
            <template x-if="!reviewed">
                <span class="inline-flex items-center gap-1">
                    @include('admin.vocab.words._voice_select')
                    <span x-show="!confirm"><button @click="run('{{ $regenerateUrl }}')" class="inline-flex h-7 items-center px-2 rounded border border-amber-200 bg-white hover:bg-amber-50 text-[10px] font-medium text-amber-600 transition-all">Regenerate</button></span>
                    <span x-show="!confirm"><button @click="confirm = true" class="inline-flex h-7 items-center px-2 rounded border border-red-200 bg-white hover:bg-red-50 text-[10px] font-medium text-red-400 transition-all">Delete</button></span>
                    <span x-show="confirm" style="display:none" class="inline-flex items-center gap-0.5">
                        <button @click="confirm = false" class="inline-flex h-7 items-center px-1.5 rounded border border-zinc-200 bg-white text-[10px] text-zinc-400 hover:text-zinc-700 transition-colors">Cancel</button>
                        <button @click="del()" class="inline-flex h-7 items-center px-2 rounded border border-red-400 bg-red-50 text-[10px] font-medium text-red-600 hover:bg-red-100 transition-colors">Yes, delete</button>
                    </span>
                </span>
            </template>
        </div>
    </template>

    <template x-if="(!{{ $existing ? 'true' : 'false' }} || gone) && !loading && !url">
        <div class="inline-flex items-center gap-1">
            @include('admin.vocab.words._voice_select')
            <button @click="run('{{ $generateUrl }}')" class="inline-flex h-7 items-center px-2 rounded border border-zinc-200 bg-white hover:bg-zinc-50 text-[10px] font-medium text-zinc-500 hover:text-zinc-800 transition-colors">Generate</button>
        </div>
    </template>

    <template x-if="loading">
        <span class="text-[10px] text-zinc-400">Generating...</span>
    </template>

    <template x-if="url">
        <div class="inline-flex items-center gap-1">
            <label class="inline-flex items-center gap-1 cursor-pointer select-none">
                <input type="checkbox" :checked="reviewed" @change="toggleReviewed($event.target.checked)" class="accent-green-600 w-3.5 h-3.5">
                <span class="text-[10px]" :class="reviewed ? 'text-green-600 font-medium' : 'text-zinc-400'">Reviewed</span>
            </label>
            <button @click="$refs.player.play()" class="inline-flex h-7 items-center gap-1 px-2 rounded border border-blue-200 bg-white hover:bg-blue-50 text-[10px] font-medium text-blue-600 transition-colors">
                <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 20 20"><path d="M6.3 2.841A1.5 1.5 0 004 4.11V15.89a1.5 1.5 0 002.3 1.269l9.344-5.89a1.5 1.5 0 000-2.538L6.3 2.84z"/></svg>
                Play
            </button>
            <template x-if="!reviewed">
                <span class="inline-flex items-center gap-1">
                    @include('admin.vocab.words._voice_select')
                    <span x-show="!confirm"><button @click="run('{{ $regenerateUrl }}')" class="inline-flex h-7 items-center px-2 rounded border border-amber-200 bg-white hover:bg-amber-50 text-[10px] font-medium text-amber-600 transition-all">Regenerate</button></span>
                    <span x-show="!confirm"><button @click="confirm = true" class="inline-flex h-7 items-center px-2 rounded border border-red-200 bg-white hover:bg-red-50 text-[10px] font-medium text-red-400 transition-all">Delete</button></span>
                    <span x-show="confirm" style="display:none" class="inline-flex items-center gap-0.5">
                        <button @click="confirm = false" class="inline-flex h-7 items-center px-1.5 rounded border border-zinc-200 bg-white text-[10px] text-zinc-400 hover:text-zinc-700 transition-colors">Cancel</button>
                        <button @click="del()" class="inline-flex h-7 items-center px-2 rounded border border-red-400 bg-red-50 text-[10px] font-medium text-red-600 hover:bg-red-100 transition-colors">Yes, delete</button>
                    </span>
                </span>
            </template>
            <audio x-ref="player" class="hidden"><source :src="url"></audio>
        </div>
    </template>
</div>
