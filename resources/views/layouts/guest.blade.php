<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="csrf-token" content="{{ csrf_token() }}">

        <title>{{ config('app.name', 'Laravel') }}</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

        @vite(['resources/css/app.css', 'resources/js/app.js'])
    </head>
    <body class="antialiased bg-zinc-50" style="font-family: 'Inter', ui-sans-serif, system-ui, sans-serif;">

        <div class="min-h-screen flex items-center justify-center px-4 py-12">
            <div class="w-full max-w-sm">

                {{-- Logo / Brand --}}
                <div class="flex flex-col items-center mb-8">
                    <div class="w-10 h-10 rounded-xl bg-blue-600 flex items-center justify-center mb-4 shadow-sm">
                        <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
                        </svg>
                    </div>
                    <h1 class="text-lg font-semibold text-zinc-900 tracking-tight">JLPT Admin</h1>
                    <p class="text-sm text-zinc-500 mt-1">Sign in to your account</p>
                </div>

                {{-- Card --}}
                <div class="bg-white rounded-2xl border border-zinc-200 shadow-sm px-8 py-8">
                    {{ $slot }}
                </div>

                <p class="text-center text-xs text-zinc-400 mt-6">
                    &copy; {{ date('Y') }} JLPT — Senda Japan Ltd.
                </p>

            </div>
        </div>

    </body>
</html>
