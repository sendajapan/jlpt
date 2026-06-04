@extends('admin.layouts.app')

@section('title', 'Avatars')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">Avatars</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage all purchasable avatars</p>
    </div>
    <a href="{{ route('admin.avatars.create') }}" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
        </svg>
        New Avatar
    </a>
</div>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">All Avatars</span>
        <span class="text-[10px] text-zinc-400">{{ $avatars->total() }} {{ Str::plural('avatar', $avatars->total()) }} total</span>
    </div>

    <form method="GET" action="{{ route('admin.avatars.index') }}">
        <div class="flex items-center gap-2 px-4 py-2.5 border-b border-zinc-100 bg-zinc-50/30">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5 text-zinc-400 shrink-0" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
            </svg>
            <input type="text" name="search" value="{{ request('search') }}" placeholder="Search avatars..."
                   class="flex-1 bg-transparent text-xs text-zinc-700 placeholder:text-zinc-400 focus:outline-none h-6">
            @if(request('search'))
                <a href="{{ route('admin.avatars.index') }}" class="text-xs text-zinc-400 hover:text-zinc-600 transition-colors shrink-0">Clear</a>
            @endif
        </div>
    </form>

    <div class="overflow-x-auto">
        <table class="min-w-full">
            <thead>
                <tr class="border-b border-zinc-100 bg-zinc-50/50">
                    <th class="w-10 px-3 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">S/N</th>
                    <th class="w-16 px-2 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">Preview</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Name</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Price</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Status</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Sort</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-l border-zinc-100">Action</th>
                </tr>
            </thead>
            <tbody class="divide-y divide-zinc-100">
                @forelse($avatars as $avatar)
                    <tr class="hover:bg-zinc-50/30 transition-colors duration-100">
                        <td class="w-10 px-3 py-2.5 text-center text-xs text-zinc-400 border-r border-zinc-100">
                            {{ ($avatars->currentPage() - 1) * $avatars->perPage() + $loop->iteration }}
                        </td>
                        <td class="w-16 px-2 py-2 text-center border-r border-zinc-100">
                            <img src="{{ Storage::url($avatar->image_path) }}" alt="{{ $avatar->name }}"
                                 class="w-10 h-10 rounded-lg object-cover mx-auto border border-zinc-200">
                        </td>
                        <td class="px-4 py-2.5 text-xs font-medium text-zinc-900">{{ $avatar->name }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">
                            @if($avatar->coin_price === 0)
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-green-50 text-green-700 border border-green-200/80">Free</span>
                            @else
                                <span class="inline-flex items-center gap-0.5 text-xs font-medium text-amber-700">
                                    {{ $avatar->coin_price }} coins
                                </span>
                            @endif
                        </td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">
                            @if($avatar->is_active)
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-blue-50 text-blue-700 border border-blue-200/80">Active</span>
                            @else
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-zinc-100 text-zinc-500">Inactive</span>
                            @endif
                        </td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $avatar->sort_order }}</td>
                        <td class="px-4 py-2.5 text-center border-l border-zinc-100">
                            <div class="flex items-center justify-center gap-0.5">
                                <a href="{{ route('admin.avatars.edit', $avatar) }}"
                                   class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 transition-colors duration-150">
                                    Edit
                                </a>
                                <form method="POST" action="{{ route('admin.avatars.destroy', $avatar) }}"
                                      onsubmit="return confirm('Delete this avatar? This cannot be undone.')">
                                    @csrf
                                    @method('DELETE')
                                    <button type="submit"
                                            class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-red-600 hover:bg-red-50 transition-colors duration-150">
                                        Delete
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                @empty
                    <tr>
                        <td colspan="7" class="px-4 py-10 text-center">
                            <p class="text-xs text-zinc-400">No avatars found.</p>
                            <a href="{{ route('admin.avatars.create') }}" class="mt-1 inline-flex text-xs text-zinc-900 underline underline-offset-2">Create the first one</a>
                        </td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>

    @if($avatars->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">
            {{ $avatars->links() }}
        </div>
    @endif
</div>

@endsection
