@extends('admin.layouts.app')

@section('title', 'Audio Automation')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Audio Automation</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Background generation of missing vocab audio, one at a time.</p>
    </div>
    <div class="flex items-center gap-2">
        <form method="POST" action="{{ route('admin.audio-automation.run-now') }}">
            @csrf
            <button type="submit" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-white border border-zinc-200 text-zinc-700 text-xs font-medium hover:bg-zinc-50 transition-colors">
                Run one now
            </button>
        </form>
        <form method="POST" action="{{ route('admin.audio-automation.toggle') }}">
            @csrf
            @if($setting->is_running)
                <button type="submit" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-red-600 text-white text-xs font-medium hover:bg-red-700 transition-colors">
                    Stop automation
                </button>
            @else
                <button type="submit" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-emerald-600 text-white text-xs font-medium hover:bg-emerald-700 transition-colors">
                    Start automation
                </button>
            @endif
        </form>
    </div>
</div>

<div class="grid grid-cols-4 gap-3 mb-4">
    <div class="bg-white rounded-lg border border-zinc-200 p-4">
        <p class="text-[10px] uppercase tracking-wider text-zinc-400 font-semibold">Status</p>
        <p class="mt-1.5 flex items-center gap-1.5 text-sm font-semibold {{ $setting->is_running ? 'text-emerald-600' : 'text-zinc-500' }}">
            <span class="inline-block w-2 h-2 rounded-full {{ $setting->is_running ? 'bg-emerald-500 animate-pulse' : 'bg-zinc-300' }}"></span>
            {{ $setting->is_running ? 'Running' : 'Stopped' }}
        </p>
    </div>
    <div class="bg-white rounded-lg border border-zinc-200 p-4">
        <p class="text-[10px] uppercase tracking-wider text-zinc-400 font-semibold">Generated total</p>
        <p class="mt-1.5 text-sm font-semibold text-zinc-900">{{ number_format($setting->generated_count) }}</p>
    </div>
    <div class="bg-white rounded-lg border border-zinc-200 p-4">
        <p class="text-[10px] uppercase tracking-wider text-zinc-400 font-semibold">Pending</p>
        <p class="mt-1.5 text-sm font-semibold text-zinc-900">{{ number_format($pendingTotal) }}</p>
    </div>
    <div class="bg-white rounded-lg border border-zinc-200 p-4">
        <p class="text-[10px] uppercase tracking-wider text-zinc-400 font-semibold">Last run</p>
        <p class="mt-1.5 text-sm font-semibold text-zinc-900">
            {{ $setting->last_run_at ? $setting->last_run_at->diffForHumans() : '—' }}
        </p>
    </div>
</div>

<div class="grid grid-cols-2 gap-3 mb-4">
    <div class="bg-white rounded-lg border border-zinc-200 p-4">
        <p class="text-xs font-semibold text-zinc-900 mb-3">Pending by field</p>
        <div class="space-y-2">
            @foreach($pending as $field => $count)
                <div class="flex items-center justify-between text-xs">
                    <span class="text-zinc-600 font-mono">{{ $field }}</span>
                    <span class="font-semibold text-zinc-900">{{ number_format($count) }}</span>
                </div>
            @endforeach
        </div>
    </div>
    <div class="bg-white rounded-lg border border-zinc-200 p-4">
        <p class="text-xs font-semibold text-zinc-900 mb-3">Next in queue</p>
        @if($next)
            <p class="text-xs text-zinc-700">
                <span class="font-mono text-zinc-500">{{ $next['field'] }}</span>
                <span class="mx-1 text-zinc-300">·</span>
                #{{ $next['vocab']->id }} {{ $next['vocab']->word_jp }} / {{ $next['vocab']->word_en }}
            </p>
        @else
            <p class="text-xs text-zinc-400">Nothing pending. All approved words have audio.</p>
        @endif
        @if($setting->last_error)
            <p class="mt-3 text-[11px] text-red-600 break-words">Last error: {{ $setting->last_error }}</p>
        @endif
    </div>
</div>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">Failures (skipped after {{ \App\Models\AudioGenerationFailure::MAX_ATTEMPTS }} attempts)</span>
        <span class="text-[10px] text-zinc-400">{{ $failures->total() }} total</span>
    </div>

    <div class="overflow-x-auto">
        <table class="min-w-full">
            <thead>
                <tr class="border-b border-zinc-100 bg-zinc-50/50">
                    <th class="w-10 px-3 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">S/N</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Vocab</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Field</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Attempts</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Last error</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">When</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-l border-zinc-100">Action</th>
                </tr>
            </thead>
            <tbody class="divide-y divide-zinc-100">
                @forelse($failures as $failure)
                    <tr class="hover:bg-zinc-50/30 transition-colors">
                        <td class="w-10 px-3 py-2.5 text-center text-xs text-zinc-400 border-r border-zinc-100">
                            {{ ($failures->currentPage() - 1) * $failures->perPage() + $loop->iteration }}
                        </td>
                        <td class="px-4 py-2.5 text-xs text-zinc-700">
                            @if($failure->vocabulary)
                                #{{ $failure->vocabulary->id }} {{ $failure->vocabulary->word_jp }} / {{ $failure->vocabulary->word_en }}
                            @else
                                <span class="text-zinc-400">deleted</span>
                            @endif
                        </td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600 font-mono">{{ $failure->field }}</td>
                        <td class="px-4 py-2.5 text-xs">
                            <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] {{ $failure->attempts >= \App\Models\AudioGenerationFailure::MAX_ATTEMPTS ? 'bg-red-50 text-red-700' : 'bg-amber-50 text-amber-700' }}">
                                {{ $failure->attempts }} / {{ \App\Models\AudioGenerationFailure::MAX_ATTEMPTS }}
                            </span>
                        </td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600 max-w-md truncate" title="{{ $failure->last_error }}">{{ $failure->last_error }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-500">{{ $failure->last_attempt_at?->diffForHumans() }}</td>
                        <td class="px-4 py-2.5 text-center border-l border-zinc-100">
                            <div class="flex items-center justify-center gap-0.5">
                                <form method="POST" action="{{ route('admin.audio-automation.failures.reset', $failure) }}"
                                      onsubmit="return confirm('Clear this failure and retry?')">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit" class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-emerald-700 hover:bg-emerald-50 transition-colors">
                                        Retry
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                @empty
                    <tr>
                        <td colspan="7" class="px-4 py-10 text-center">
                            <p class="text-xs text-zinc-400">No failures recorded.</p>
                        </td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>

    @if($failures->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">
            {{ $failures->links() }}
        </div>
    @endif
</div>

<div class="mt-4 p-3 rounded-md bg-blue-50 border border-blue-100 text-[11px] text-blue-900">
    <p class="font-semibold mb-1">Setup notes</p>
    <p>1. Add to crontab: <code class="bg-white px-1 py-0.5 rounded">* * * * * cd {{ base_path() }} && php artisan schedule:run >> /dev/null 2>&1</code></p>
    <p>2. Run a queue worker: <code class="bg-white px-1 py-0.5 rounded">php artisan queue:work --queue=default --sleep=3 --tries=1</code> (use Supervisor in production).</p>
</div>

@endsection
