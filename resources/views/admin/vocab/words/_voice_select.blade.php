<select x-model="voiceId" @change="$dispatch('voice-sync', { vocabId: {{ $vocab->id }}, voiceId: voiceId })" class="h-7 rounded border border-zinc-200 bg-white px-1.5 text-[10px] text-zinc-600 focus:outline-none focus:ring-1 focus:ring-zinc-900">
    @foreach($voices as $v)
        <option value="{{ $v->id }}">{{ $v->name }}</option>
    @endforeach
</select>
