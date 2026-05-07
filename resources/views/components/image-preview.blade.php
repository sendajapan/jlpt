@props([
    'name',
    'current' => null,
    'hint' => 'JPG, PNG, SVG, WebP',
])

<div x-data="{ preview: @js($current) }">
    <div class="w-64 h-64 rounded-lg border-2 border-dashed border-zinc-200 bg-zinc-50 overflow-hidden flex items-center justify-center mb-3">
        <template x-if="preview">
            <img :src="preview" class="w-full h-full object-cover">
        </template>
        <template x-if="!preview">
            <div class="text-center select-none">
                <svg class="w-8 h-8 text-zinc-300 mx-auto" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 15.75l5.159-5.159a2.25 2.25 0 013.182 0l5.159 5.159m-1.5-1.5l1.409-1.409a2.25 2.25 0 013.182 0l2.909 2.909M3.75 19.5h16.5M3.75 4.5h16.5a.75.75 0 01.75.75v13.5a.75.75 0 01-.75.75H3.75a.75.75 0 01-.75-.75V5.25a.75.75 0 01.75-.75z"/>
                </svg>
                <p class="mt-1.5 text-[10px] text-zinc-400">No image selected</p>
            </div>
        </template>
    </div>

    <input
        type="file"
        name="{{ $name }}"
        accept="image/*"
        x-on:change="preview = $event.target.files[0] ? URL.createObjectURL($event.target.files[0]) : @js($current)"
        class="block w-full text-xs text-zinc-500 file:mr-3 file:h-7 file:px-3 file:rounded file:border-0 file:text-xs file:font-medium file:bg-zinc-100 file:text-zinc-700 hover:file:bg-zinc-200 transition-colors cursor-pointer"
    >
    <p class="mt-1 text-[10px] text-zinc-400">{{ $current ? 'Leave empty to keep current.' : $hint }}</p>

    {{ $slot }}
</div>
