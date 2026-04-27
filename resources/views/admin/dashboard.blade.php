@extends('admin.layouts.app')
@section('title', 'Dashboard')

@section('content')

{{-- Page header --}}
<div class="mb-5">
    <h1 class="text-sm font-semibold text-zinc-900">Dashboard</h1>
    <p class="text-xs text-zinc-500 mt-0.5">Overview of your JLPT content library</p>
</div>

{{-- Stats row --}}
<div class="grid grid-cols-1 sm:grid-cols-3 gap-3 mb-5">

    <a href="{{ route('admin.categories.index') }}"
       class="group bg-white rounded-lg border border-zinc-200 px-5 py-4 flex items-center justify-between hover:border-zinc-300 hover:shadow-sm transition-all duration-150">
        <div>
            <p class="text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Categories</p>
            <p class="mt-1 text-2xl font-bold text-zinc-900 tabular-nums">{{ $categoryCount }}</p>
            <p class="mt-1 text-[10px] text-zinc-400 group-hover:text-blue-600 transition-colors">View all →</p>
        </div>
        <div class="w-9 h-9 rounded-lg bg-blue-50 border border-blue-100 flex items-center justify-center flex-shrink-0">
            <svg class="w-4 h-4 text-blue-600" fill="none" stroke="currentColor" stroke-width="1.75" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"/>
            </svg>
        </div>
    </a>

    <a href="{{ route('admin.subcategories.index') }}"
       class="group bg-white rounded-lg border border-zinc-200 px-5 py-4 flex items-center justify-between hover:border-zinc-300 hover:shadow-sm transition-all duration-150">
        <div>
            <p class="text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Subcategories</p>
            <p class="mt-1 text-2xl font-bold text-zinc-900 tabular-nums">{{ $subcategoryCount }}</p>
            <p class="mt-1 text-[10px] text-zinc-400 group-hover:text-violet-600 transition-colors">View all →</p>
        </div>
        <div class="w-9 h-9 rounded-lg bg-violet-50 border border-violet-100 flex items-center justify-center flex-shrink-0">
            <svg class="w-4 h-4 text-violet-600" fill="none" stroke="currentColor" stroke-width="1.75" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 10h16M4 14h16M4 18h16"/>
            </svg>
        </div>
    </a>

    <a href="{{ route('admin.vocabularies.index') }}"
       class="group bg-white rounded-lg border border-zinc-200 px-5 py-4 flex items-center justify-between hover:border-zinc-300 hover:shadow-sm transition-all duration-150">
        <div>
            <p class="text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Vocabulary</p>
            <p class="mt-1 text-2xl font-bold text-zinc-900 tabular-nums">{{ $vocabularyCount }}</p>
            <p class="mt-1 text-[10px] text-zinc-400 group-hover:text-emerald-600 transition-colors">View all →</p>
        </div>
        <div class="w-9 h-9 rounded-lg bg-emerald-50 border border-emerald-100 flex items-center justify-center flex-shrink-0">
            <svg class="w-4 h-4 text-emerald-600" fill="none" stroke="currentColor" stroke-width="1.75" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
            </svg>
        </div>
    </a>

</div>

{{-- Quick actions --}}
<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="px-5 py-3 border-b border-zinc-100">
        <p class="text-xs font-semibold text-zinc-900">Quick Actions</p>
        <p class="text-[10px] text-zinc-400 mt-0.5">Jump to common tasks</p>
    </div>
    <div class="grid grid-cols-1 sm:grid-cols-3 divide-y sm:divide-y-0 sm:divide-x divide-zinc-100">

        <a href="{{ route('admin.categories.create') }}"
           class="flex items-center gap-3 px-5 py-4 hover:bg-zinc-50/60 transition-colors duration-100 group">
            <div class="w-8 h-8 rounded-md border border-zinc-200 flex items-center justify-center flex-shrink-0 group-hover:border-zinc-300 group-hover:bg-white transition-all">
                <svg class="w-3.5 h-3.5 text-zinc-500" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
                </svg>
            </div>
            <div>
                <p class="text-xs font-medium text-zinc-800">New Category</p>
                <p class="text-[10px] text-zinc-400">Add a content category</p>
            </div>
        </a>

        <a href="{{ route('admin.subcategories.create') }}"
           class="flex items-center gap-3 px-5 py-4 hover:bg-zinc-50/60 transition-colors duration-100 group">
            <div class="w-8 h-8 rounded-md border border-zinc-200 flex items-center justify-center flex-shrink-0 group-hover:border-zinc-300 group-hover:bg-white transition-all">
                <svg class="w-3.5 h-3.5 text-zinc-500" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
                </svg>
            </div>
            <div>
                <p class="text-xs font-medium text-zinc-800">New Subcategory</p>
                <p class="text-[10px] text-zinc-400">Group words by topic</p>
            </div>
        </a>

        <a href="{{ route('admin.vocabularies.create') }}"
           class="flex items-center gap-3 px-5 py-4 hover:bg-zinc-50/60 transition-colors duration-100 group">
            <div class="w-8 h-8 rounded-md border border-zinc-200 flex items-center justify-center flex-shrink-0 group-hover:border-zinc-300 group-hover:bg-white transition-all">
                <svg class="w-3.5 h-3.5 text-zinc-500" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
                </svg>
            </div>
            <div>
                <p class="text-xs font-medium text-zinc-800">New Vocabulary</p>
                <p class="text-[10px] text-zinc-400">Add a Japanese word</p>
            </div>
        </a>

    </div>
</div>

@endsection
