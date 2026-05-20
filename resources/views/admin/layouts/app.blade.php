<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}" class="h-full">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    <title>@yield('title', 'Dashboard') — JLPT Admin</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    @vite(['resources/css/app.css', 'resources/js/app.js'])
</head>
<body class="h-full bg-[#fafafa] antialiased" style="font-family: 'Inter', ui-sans-serif, system-ui, sans-serif;">

<div class="flex h-screen overflow-hidden">

    {{-- ─── Sidebar ─────────────────────────────────────────────────── --}}
    <aside class="w-[220px] flex-shrink-0 flex flex-col bg-[#09090b] border-r border-zinc-800/60 select-none">

        {{-- Logo --}}
        <div class="h-12 flex items-center gap-2.5 px-4 border-b border-zinc-800/60">
            <div class="w-6 h-6 rounded bg-blue-600 flex items-center justify-center flex-shrink-0">
                <svg class="w-3.5 h-3.5 text-white" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
                </svg>
            </div>
            <span class="text-white font-semibold text-sm tracking-tight">JLPT Admin</span>
        </div>

        {{-- Nav --}}
        <nav class="flex-1 overflow-y-auto px-2 py-3 space-y-0.5">

            <p class="px-2 mb-2 text-[9px] font-semibold uppercase tracking-[0.12em] text-zinc-600">Main Menu</p>

            @php
                $navItems = [
                    [
                        'route'   => 'admin.dashboard',
                        'pattern' => 'admin.dashboard',
                        'label'   => 'Dashboard',
                        'icon'    => '<path stroke-linecap="round" stroke-linejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/>',
                    ],
                    [
                        'route'   => 'admin.vocab.categories.index',
                        'pattern' => 'admin.vocab.categories.*',
                        'label'   => 'Categories',
                        'icon'    => '<path stroke-linecap="round" stroke-linejoin="round" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"/>',
                    ],
                    [
                        'route'   => 'admin.vocab.subcategories.index',
                        'pattern' => 'admin.vocab.subcategories.*',
                        'label'   => 'Subcategories',
                        'icon'    => '<path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 10h16M4 14h16M4 18h16"/>',
                    ],
                    [
                        'route'   => 'admin.vocab.words.index',
                        'pattern' => 'admin.vocab.words.*',
                        'label'   => 'Vocabulary',
                        'icon'    => '<path stroke-linecap="round" stroke-linejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>',
                    ],
                    [
                        'route'   => 'admin.voices.index',
                        'pattern' => 'admin.voices.*',
                        'label'   => 'Voices',
                        'icon'    => '<path stroke-linecap="round" stroke-linejoin="round" d="M19 11a7 7 0 01-14 0m7 7v4m-4 0h8m-4-8a3 3 0 003-3V5a3 3 0 00-6 0v6a3 3 0 003 3z"/>',
                    ],
                    [
                        'route'   => 'admin.audio-automation.index',
                        'pattern' => 'admin.audio-automation.*',
                        'label'   => 'Audio Automation',
                        'icon'    => '<path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>',
                    ],
                ];
            @endphp

            @foreach ($navItems as $item)
                @php $active = request()->routeIs($item['pattern']); @endphp
                <a href="{{ route($item['route']) }}"
                   class="relative flex items-center gap-2.5 px-2.5 py-1.5 rounded-md text-xs font-medium transition-colors duration-150
                          {{ $active
                              ? 'bg-zinc-800 text-white before:absolute before:left-0 before:top-1/2 before:-translate-y-1/2 before:h-[60%] before:w-0.5 before:bg-blue-500 before:rounded-r'
                              : 'text-zinc-400 hover:text-zinc-100 hover:bg-zinc-800/70' }}">
                    <svg class="w-3.5 h-3.5 flex-shrink-0 {{ $active ? 'text-white' : 'text-zinc-500' }}" fill="none" stroke="currentColor" stroke-width="1.75" viewBox="0 0 24 24">
                        {!! $item['icon'] !!}
                    </svg>
                    {{ $item['label'] }}
                    @if ($active)
                        <span class="ml-auto w-1.5 h-1.5 rounded-full bg-blue-500 flex-shrink-0"></span>
                    @endif
                </a>
            @endforeach

        </nav>

        {{-- User footer --}}
        <div class="border-t border-zinc-800/60 px-3 py-2.5">
            <div class="flex items-center gap-2.5">
                <div class="w-6 h-6 rounded-full bg-zinc-700 border border-zinc-600 flex items-center justify-center text-white text-[10px] font-bold flex-shrink-0">
                    {{ strtoupper(substr(auth()->user()->name, 0, 1)) }}
                </div>
                <div class="min-w-0 flex-1">
                    <p class="text-[11px] font-medium text-zinc-200 truncate leading-tight">{{ auth()->user()->name }}</p>
                    <p class="text-[9px] text-zinc-500 truncate leading-tight">{{ auth()->user()->email }}</p>
                </div>
                <form method="POST" action="{{ route('logout') }}">
                    @csrf
                    <button type="submit" title="Logout"
                            class="w-6 h-6 flex items-center justify-center rounded text-zinc-500 hover:text-zinc-200 hover:bg-zinc-800 transition-colors duration-150">
                        <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
                        </svg>
                    </button>
                </form>
            </div>
        </div>

    </aside>

    {{-- ─── Main area ───────────────────────────────────────────────── --}}
    <div class="flex-1 flex flex-col overflow-hidden">

        {{-- Top bar --}}
        <header class="h-12 flex-shrink-0 bg-white border-b border-zinc-200 flex items-center justify-between px-5">
            {{-- Breadcrumb / page title --}}
            <div class="flex items-center gap-2 text-xs text-zinc-500">
                <span class="font-medium text-zinc-900">@yield('title', 'Dashboard')</span>
            </div>

            {{-- Right actions --}}
            <div class="flex items-center gap-3">
                {{-- Divider --}}
                <div class="h-4 w-px bg-zinc-200"></div>
                {{-- User pill --}}
                <div class="flex items-center gap-1.5">
                    <div class="w-5 h-5 rounded-full bg-zinc-900 flex items-center justify-center text-white text-[9px] font-bold">
                        {{ strtoupper(substr(auth()->user()->name, 0, 1)) }}
                    </div>
                    <span class="text-xs font-medium text-zinc-700">{{ auth()->user()->name }}</span>
                </div>
            </div>
        </header>

        {{-- Page content --}}
        <main class="flex-1 overflow-y-auto dot-grid">
            <div class="animate-page p-5 max-w-screen-xl mx-auto w-full">

                @yield('content')

            </div>
        </main>

    </div>

</div>


<x-notify::notify />

@stack('scripts')
</body>
</html>
