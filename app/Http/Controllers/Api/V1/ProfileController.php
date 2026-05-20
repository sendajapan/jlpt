<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Http\Resources\AppUserResource;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class ProfileController extends Controller
{
    public function show(Request $request): AppUserResource
    {
        return new AppUserResource(
            $request->user()->loadCount(['favorites', 'reads'])
        );
    }

    public function update(Request $request): AppUserResource
    {
        $user = $request->user();

        $validated = $request->validate([
            'name' => ['sometimes', 'string', 'max:100'],
            'username' => ['sometimes', 'nullable', 'string', 'max:50', 'unique:app_users,username,'.$user->id],
            'avatar' => ['sometimes', 'file', 'image', 'max:4096'],
            'bio' => ['sometimes', 'nullable', 'string', 'max:1000'],
            'gender' => ['sometimes', 'nullable', 'in:male,female,other,prefer_not_to_say'],
            'birth_date' => ['sometimes', 'nullable', 'date', 'before:today'],
            'native_language_code' => ['sometimes', 'string', 'max:10'],
            'learning_language_code' => ['sometimes', 'string', 'max:10'],
            'proficiency_level' => ['sometimes', 'in:beginner,elementary,intermediate,upper_intermediate,advanced,native_like'],
            'learning_goal' => ['sometimes', 'in:travel,school,business,anime,conversation,exam,hobby,other'],
            'daily_goal_minutes' => ['sometimes', 'integer', 'min:1', 'max:600'],
            'timezone' => ['sometimes', 'nullable', 'string', 'max:100'],
            'reminder_time' => ['sometimes', 'nullable', 'date_format:H:i'],
            'notifications_enabled' => ['sometimes', 'boolean'],
        ]);

        if (array_key_exists('name', $validated)) {
            $validated['full_name'] = $validated['name'];
            unset($validated['name']);
        }

        if ($request->hasFile('avatar')) {
            if ($user->avatar) {
                Storage::disk('public')->delete($user->avatar);
            }
            $validated['avatar'] = $request->file('avatar')->store('app_user_avatars', 'public');
        }

        $user->fill($validated)->save();

        return new AppUserResource($user->loadCount(['favorites', 'reads']));
    }
}
