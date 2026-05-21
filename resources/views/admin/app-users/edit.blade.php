@extends('admin.layouts.app')

@section('title', 'Edit App User')

@section('content')

<div class="flex items-center justify-between mb-4">
    <div>
        <h1 class="text-sm font-semibold text-zinc-900">App Users</h1>
        <p class="text-xs text-zinc-500 mt-0.5">Manage all registered app users</p>
    </div>
</div>

<a href="{{ route('admin.app-users.index') }}" class="inline-flex items-center gap-1.5 text-xs text-zinc-500 hover:text-zinc-800 transition-colors mb-4">
    <svg xmlns="http://www.w3.org/2000/svg" class="w-3 h-3" viewBox="0 0 20 20" fill="currentColor">
        <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
    </svg>
    Back to App Users
</a>

<div class="bg-white rounded-lg border border-zinc-200 overflow-hidden">
    <div class="flex items-center justify-between px-5 py-3 border-b border-zinc-100">
        <div>
            <p class="text-sm font-semibold text-zinc-900">Edit User</p>
            <p class="text-xs text-zinc-500 mt-0.5">{{ $user->username ?? $user->email }}</p>
        </div>
    </div>

    <form method="POST" action="{{ route('admin.app-users.update', $user) }}">
        @csrf
        @method('PUT')

        <div class="p-5 space-y-4">

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Username</label>
                    <input type="text" name="username" value="{{ old('username', $user->username) }}"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('username') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('username')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Full Name</label>
                    <input type="text" name="full_name" value="{{ old('full_name', $user->full_name) }}"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('full_name') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('full_name')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Email<span class="text-zinc-400 ml-0.5">*</span></label>
                    <input type="email" name="email" value="{{ old('email', $user->email) }}"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('email') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('email')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Coins</label>
                    <input type="number" name="coins" value="{{ old('coins', $user->coins) }}" min="0"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('coins') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('coins')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Gender</label>
                    <select name="gender"
                            class="flex h-8 w-full rounded-md border {{ $errors->has('gender') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                        <option value="">— Not set —</option>
                        <option value="male" {{ old('gender', $user->gender) === 'male' ? 'selected' : '' }}>Male</option>
                        <option value="female" {{ old('gender', $user->gender) === 'female' ? 'selected' : '' }}>Female</option>
                        <option value="other" {{ old('gender', $user->gender) === 'other' ? 'selected' : '' }}>Other</option>
                        <option value="prefer_not_to_say" {{ old('gender', $user->gender) === 'prefer_not_to_say' ? 'selected' : '' }}>Prefer not to say</option>
                    </select>
                    @error('gender')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Birth Date</label>
                    <input type="date" name="birth_date" value="{{ old('birth_date', $user->birth_date?->format('Y-m-d')) }}"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('birth_date') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('birth_date')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Native Language Code</label>
                    <input type="text" name="native_language_code" value="{{ old('native_language_code', $user->native_language_code) }}" placeholder="e.g. en"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('native_language_code') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('native_language_code')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Learning Language Code</label>
                    <input type="text" name="learning_language_code" value="{{ old('learning_language_code', $user->learning_language_code) }}" placeholder="e.g. ja"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('learning_language_code') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('learning_language_code')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Proficiency Level</label>
                    <input type="text" name="proficiency_level" value="{{ old('proficiency_level', $user->proficiency_level) }}" placeholder="e.g. N5"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('proficiency_level') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('proficiency_level')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>

                <div>
                    <label class="block text-xs font-medium text-zinc-700 mb-1.5">Daily Goal (minutes)</label>
                    <input type="number" name="daily_goal_minutes" value="{{ old('daily_goal_minutes', $user->daily_goal_minutes) }}" min="0" max="1440"
                           class="flex h-8 w-full rounded-md border {{ $errors->has('daily_goal_minutes') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                    @error('daily_goal_minutes')
                        <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                    @enderror
                </div>
            </div>

            <div>
                <label class="block text-xs font-medium text-zinc-700 mb-1.5">Bio</label>
                <textarea name="bio" rows="3" placeholder="User bio..."
                          class="flex w-full rounded-md border {{ $errors->has('bio') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 py-2 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">{{ old('bio', $user->bio) }}</textarea>
                @error('bio')
                    <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                @enderror
            </div>

            <div class="border-t border-zinc-100 pt-4">
                <p class="text-xs font-semibold text-zinc-700 mb-3">Moderation</p>

                <div class="space-y-3">
                    <label class="flex items-center gap-2 cursor-pointer select-none">
                        <input type="hidden" name="is_banned" value="0">
                        <input type="checkbox" name="is_banned" id="is_banned" value="1"
                               {{ old('is_banned', $user->is_banned) ? 'checked' : '' }}
                               class="w-3.5 h-3.5 rounded border-zinc-300 text-red-600 focus:ring-red-500 focus:ring-offset-0"
                               onchange="document.getElementById('banned-reason-row').style.display = this.checked ? 'block' : 'none'">
                        <span class="text-xs font-medium text-zinc-700">Ban this user</span>
                    </label>

                    <div id="banned-reason-row" style="{{ old('is_banned', $user->is_banned) ? '' : 'display:none' }}">
                        <label class="block text-xs font-medium text-zinc-700 mb-1.5">Ban Reason</label>
                        <input type="text" name="banned_reason" value="{{ old('banned_reason', $user->banned_reason) }}" placeholder="Reason for ban..."
                               class="flex h-8 w-full rounded-md border {{ $errors->has('banned_reason') ? 'border-red-400 focus:ring-red-400' : 'border-zinc-200' }} bg-white px-3 text-sm text-zinc-900 placeholder:text-zinc-400 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-0 transition-shadow duration-150">
                        @error('banned_reason')
                            <p class="mt-1 text-[10px] text-red-500">{{ $message }}</p>
                        @enderror
                    </div>
                </div>
            </div>

        </div>

        <div class="flex items-center gap-2 px-5 py-3.5 border-t border-zinc-100 bg-zinc-50/50">
            <button type="submit" class="inline-flex items-center gap-1.5 h-8 px-3 rounded-md bg-zinc-900 text-white text-xs font-medium hover:bg-zinc-700 active:scale-[0.98] transition-all duration-150 focus:outline-none focus:ring-2 focus:ring-zinc-900 focus:ring-offset-1">
                Save Changes
            </button>
            <a href="{{ route('admin.app-users.index') }}" class="text-xs text-zinc-400 hover:text-zinc-700 px-3 py-1.5 rounded hover:bg-zinc-100 transition-colors duration-150">
                Cancel
            </a>
        </div>
    </form>
</div>

@endsection
