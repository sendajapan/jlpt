<?php

namespace App\Services;

use App\Models\AppUser;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

class AppUserAuthService
{
    public function register(array $data): AppUser
    {
        $user = AppUser::create([
            'full_name' => $data['name'],
            'username' => $data['display_name'] ?? null,
            'email' => $data['email'],
            'password' => $data['password'],
            'login_provider' => 'email',
            'native_language_code' => 'en',
            'learning_language_code' => 'ja',
        ]);

        $user->refresh();
        $user->sendEmailVerificationNotification();

        return $user;
    }

    public function login(string $email, string $password): AppUser
    {
        $user = AppUser::where('email', $email)->first();

        if (! $user || ! $user->password || ! Hash::check($password, $user->password)) {
            throw ValidationException::withMessages(['email' => 'The provided credentials are incorrect.']);
        }

        return $user;
    }

    public function issueToken(AppUser $user, string $deviceName): string
    {
        return $user->createToken($deviceName)->plainTextToken;
    }
}
