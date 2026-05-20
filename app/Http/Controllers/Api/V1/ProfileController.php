<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Http\Resources\AppUserResource;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class ProfileController extends Controller
{
    public function show(Request $request): AppUserResource
    {
        return new AppUserResource($request->user());
    }

    public function update(Request $request): AppUserResource
    {
        $validated = $request->validate([
            'name' => ['sometimes', 'string', 'max:100'],
            'username' => ['sometimes', 'nullable', 'string', 'max:50', 'unique:app_users,username,'.$request->user()->id],
            'avatar' => ['sometimes', 'file', 'image', 'max:4096'],
        ]);

        $data = [];
        if (array_key_exists('name', $validated)) {
            $data['full_name'] = $validated['name'];
        }
        if (array_key_exists('username', $validated)) {
            $data['username'] = $validated['username'];
        }

        $user = $request->user();

        if ($request->hasFile('avatar')) {
            if ($user->avatar) {
                Storage::disk('public')->delete($user->avatar);
            }
            $data['avatar'] = $request->file('avatar')->store('app_user_avatars', 'public');
        }

        $user->fill($data)->save();

        return new AppUserResource($user);
    }
}
