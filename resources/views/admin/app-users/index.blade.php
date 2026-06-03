@extends('admin.layouts.app')

@section('title', 'App Users')

@section('content')

<div x-data="{
    open: false,
    userName: '',
    formAction: '',
    confirm(name, action) {
        this.userName = name;
        this.formAction = action;
        this.open = true;
    }
}">

<div x-show="open"
     x-transition:enter="transition ease-out duration-150"
     x-transition:enter-start="opacity-0"
     x-transition:enter-end="opacity-100"
     x-transition:leave="transition ease-in duration-100"
     x-transition:leave-start="opacity-100"
     x-transition:leave-end="opacity-0"
     class="fixed inset-0 z-50 flex items-center justify-center bg-black/40"
     @keydown.escape.window="open = false">

    <div x-show="open"
         x-transition:enter="transition ease-out duration-150"
         x-transition:enter-start="opacity-0 scale-95"
         x-transition:enter-end="opacity-100 scale-100"
         x-transition:leave="transition ease-in duration-100"
         x-transition:leave-start="opacity-100 scale-100"
         x-transition:leave-end="opacity-0 scale-95"
         class="bg-white rounded-xl shadow-xl w-full max-w-sm mx-4">

        <div class="px-6 pt-6 pb-5">
            <div class="flex items-start gap-4 mb-4">
                <div class="w-10 h-10 rounded-full bg-red-50 flex items-center justify-center flex-shrink-0">
                    <svg class="w-5 h-5 text-red-500" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v2m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/>
                    </svg>
                </div>
                <div class="pt-0.5">
                    <p class="text-sm font-semibold text-zinc-900">Delete User</p>
                    <p class="text-xs text-zinc-500 mt-1">This action cannot be undone.</p>
                </div>
            </div>

            <p class="text-xs text-zinc-600 leading-relaxed">
                Are you sure you want to delete <span class="font-semibold text-zinc-900" x-text="userName"></span>? All their data will be permanently removed.
            </p>
        </div>

        <div class="flex items-center justify-end gap-2 px-6 py-4 border-t border-zinc-100 bg-zinc-50 rounded-b-xl">
            <button type="button" @click="open = false"
                    class="h-9 px-4 rounded-lg text-xs font-medium text-zinc-600 bg-white border border-zinc-200 hover:bg-zinc-100 transition-colors duration-150">
                Cancel
            </button>
            <form method="POST" :action="formAction">
                @csrf
                @method('DELETE')
                <button type="submit"
                        class="h-9 px-4 rounded-lg text-xs font-medium text-white bg-red-500 hover:bg-red-600 transition-colors duration-150">
                    Delete
                </button>
            </form>
        </div>
    </div>
</div>

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">App Users</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage all registered app users</p>
    </div>
</div>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <span class="text-sm font-semibold text-zinc-900">All Users</span>
        <span class="text-[10px] text-zinc-400">{{ $users->total() }} {{ Str::plural('user', $users->total()) }} total</span>
    </div>

    <form method="GET" action="{{ route('admin.app-users.index') }}">
        <div class="flex flex-wrap items-center gap-2 px-4 py-2.5 border-b border-zinc-100 bg-zinc-50/30">
            <div class="flex items-center gap-2 flex-1 min-w-[200px]">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5 text-zinc-400 shrink-0" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
                </svg>
                <input type="text" name="search" value="{{ request('search') }}" placeholder="Search by username, email or name..."
                       class="flex-1 bg-transparent text-xs text-zinc-700 placeholder:text-zinc-400 focus:outline-none h-6">
            </div>

            <select name="status" onchange="this.form.submit()"
                    class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-400">
                <option value="">All Statuses</option>
                <option value="verified" {{ request('status') === 'verified' ? 'selected' : '' }}>Verified</option>
                <option value="unverified" {{ request('status') === 'unverified' ? 'selected' : '' }}>Unverified</option>
                <option value="banned" {{ request('status') === 'banned' ? 'selected' : '' }}>Banned</option>
            </select>

            <select name="provider" onchange="this.form.submit()"
                    class="h-7 rounded border border-zinc-200 bg-white px-2 text-xs text-zinc-700 focus:outline-none focus:ring-1 focus:ring-zinc-400">
                <option value="">All Providers</option>
                <option value="email" {{ request('provider') === 'email' ? 'selected' : '' }}>Email</option>
                <option value="google" {{ request('provider') === 'google' ? 'selected' : '' }}>Google</option>
                <option value="apple" {{ request('provider') === 'apple' ? 'selected' : '' }}>Apple</option>
                <option value="facebook" {{ request('provider') === 'facebook' ? 'selected' : '' }}>Facebook</option>
                <option value="guest" {{ request('provider') === 'guest' ? 'selected' : '' }}>Guest</option>
            </select>

            @if(request('search') || request('status') || request('provider'))
                <a href="{{ route('admin.app-users.index') }}" class="text-xs text-zinc-400 hover:text-zinc-600 transition-colors shrink-0">Clear</a>
            @endif
        </div>
    </form>

    <div class="overflow-x-auto">
        <table class="min-w-full">
            <thead>
                <tr class="border-b border-zinc-100 bg-zinc-50/50">
                    <th class="w-10 px-3 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-r border-zinc-100">S/N</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Username</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Full Name</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Email</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Provider</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Status</th>
                    <th class="px-4 py-2 text-right text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Coins</th>
                    <th class="px-4 py-2 text-left text-[10px] font-semibold uppercase tracking-wider text-zinc-400">Joined</th>
                    <th class="px-4 py-2 text-center text-[10px] font-semibold uppercase tracking-wider text-zinc-400 border-l border-zinc-100">Action</th>
                </tr>
            </thead>
            <tbody class="divide-y divide-zinc-100">
                @forelse($users as $user)
                    <tr class="hover:bg-zinc-50/30 transition-colors duration-100">
                        <td class="w-10 px-3 py-2.5 text-center text-xs text-zinc-400 border-r border-zinc-100">
                            {{ ($users->currentPage() - 1) * $users->perPage() + $loop->iteration }}
                        </td>
                        <td class="px-4 py-2.5 text-xs font-medium text-zinc-900">{{ $user->username ?? '—' }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $user->full_name ?? '—' }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600">{{ $user->email }}</td>
                        <td class="px-4 py-2.5 text-center">
                            @php
                                $providerColors = [
                                    'google'   => 'bg-blue-50 text-blue-700 border-blue-200/80',
                                    'apple'    => 'bg-zinc-100 text-zinc-700',
                                    'facebook' => 'bg-indigo-50 text-indigo-700 border-indigo-200/80',
                                    'guest'    => 'bg-zinc-50 text-zinc-400',
                                    'email'    => 'bg-emerald-50 text-emerald-700 border-emerald-200/80',
                                ];
                                $colorClass = $providerColors[$user->login_provider] ?? 'bg-zinc-100 text-zinc-500';
                            @endphp
                            <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide border {{ $colorClass }}">
                                {{ $user->login_provider ?? '—' }}
                            </span>
                        </td>
                        <td class="px-4 py-2.5 text-center">
                            @if($user->is_banned)
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-red-50 text-red-700 border border-red-200/80">Banned</span>
                            @elseif($user->is_verified)
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-emerald-50 text-emerald-700 border border-emerald-200/80">Verified</span>
                            @else
                                <span class="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wide bg-zinc-100 text-zinc-500">Unverified</span>
                            @endif
                        </td>
                        <td class="px-4 py-2.5 text-xs text-zinc-600 text-right">{{ number_format($user->coins ?? 0) }}</td>
                        <td class="px-4 py-2.5 text-xs text-zinc-400">{{ $user->created_at?->format('d M Y') ?? '—' }}</td>
                        <td class="px-4 py-2.5 text-center border-l border-zinc-100">
                            <div class="flex items-center justify-center gap-0.5">
                                <a href="{{ route('admin.app-users.edit', $user) }}"
                                   class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-zinc-400 hover:text-zinc-900 hover:bg-zinc-100 transition-colors duration-150">
                                    Edit
                                </a>
                                <button type="button"
                                        @click="confirm('{{ addslashes($user->full_name ?? $user->email) }}', '{{ route('admin.app-users.destroy', $user) }}')"
                                        class="inline-flex items-center h-7 px-2 rounded text-xs font-medium text-red-400 hover:text-red-600 hover:bg-red-50 transition-colors duration-150">
                                    Delete
                                </button>
                            </div>
                        </td>
                    </tr>
                @empty
                    <tr>
                        <td colspan="9" class="px-4 py-10 text-center">
                            <p class="text-xs text-zinc-400">No users found.</p>
                        </td>
                    </tr>
                @endforelse
            </tbody>
        </table>
    </div>

    @if($users->hasPages())
        <div class="px-4 py-2.5 border-t border-zinc-100">
            {{ $users->links() }}
        </div>
    @endif
</div>

</div>

@endsection
