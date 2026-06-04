<?php

namespace App\Services;

use App\Models\AppUser;
use App\Models\AppUserWordRead;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Str;
use Illuminate\Validation\ValidationException;
use Laravel\Sanctum\PersonalAccessToken;

class AppUserAuthService
{
    public function __construct(
        private AvatarService $avatarService,
        private CoinService $coinService,
    ) {}

    public function register(array $data, bool $isGuestConversion = false): AppUser
    {
        $user = AppUser::create([
            'full_name' => $data['name'],
            'username' => $data['display_name'] ?? null,
            'email' => $data['email'],
            'password' => $data['password'],
            'native_language_code' => 'en',
            'learning_language_code' => 'ja',
        ]);

        $freeAvatar = $this->avatarService->randomFree();
        if ($freeAvatar) {
            $user->update(['active_avatar_id' => $freeAvatar->id]);
        }

        if (! $isGuestConversion) {
            $this->coinService->award($user, 500, 'welcome_bonus');
        }

        $user->refresh();

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

    public function loginAsGuest(string $guestName, string $deviceName): AppUser
    {
        $username = $this->generateUniqueGuestUsername();

        $user = AppUser::create([
            'full_name' => $guestName ?: 'Guest User',
            'username' => $username,
            'email' => $username.'@scholarlyapps.com',
            'login_provider' => 'guest',
            'native_language_code' => 'en',
            'learning_language_code' => 'ja',
        ]);

        $freeAvatar = $this->avatarService->randomFree();
        if ($freeAvatar) {
            $user->update(['active_avatar_id' => $freeAvatar->id]);
        }

        $this->coinService->award($user, 100, 'guest_welcome_bonus');

        $user->refresh();

        return $user;
    }

    private function generateUniqueGuestUsername(): string
    {
        do {
            $username = 'user_'.Str::lower(Str::random(6));
        } while (AppUser::where('username', $username)->exists());

        return $username;
    }

    public function mergeGuestAccount(AppUser $realUser, string $guestToken): void
    {
        $pat = PersonalAccessToken::findToken($guestToken);

        if (! $pat || $pat->tokenable_type !== AppUser::class) {
            return;
        }

        $guest = AppUser::find($pat->tokenable_id);

        if (! $guest || $guest->login_provider !== 'guest') {
            return;
        }

        DB::transaction(function () use ($guest, $realUser) {
            $existingFavoriteIds = $realUser->favorites()->pluck('vocab_words.id')->toArray();
            $guestFavoriteIds = $guest->favorites()->pluck('vocab_words.id')->toArray();
            $newFavoriteIds = array_diff($guestFavoriteIds, $existingFavoriteIds);

            if ($newFavoriteIds) {
                $pivots = array_map(fn ($id) => [
                    'app_user_id' => $realUser->id,
                    'vocab_id' => $id,
                    'created_at' => now(),
                    'updated_at' => now(),
                ], $newFavoriteIds);
                DB::table('app_user_favorites')->insert($pivots);
            }

            $existingReadIds = $realUser->reads()->pluck('vocab_id')->toArray();

            AppUserWordRead::where('app_user_id', $guest->id)
                ->whereNotIn('vocab_id', $existingReadIds)
                ->update(['app_user_id' => $realUser->id]);

            $guest->tokens()->delete();
            $guest->delete();
        });

        $this->coinService->award($realUser, 400, 'guest_conversion_bonus');
    }

    public function issueToken(AppUser $user, string $deviceName): string
    {
        return $user->createToken($deviceName)->plainTextToken;
    }
}
