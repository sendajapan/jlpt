<?php

namespace App\Services;

use App\Models\AppUser;
use Illuminate\Support\Facades\Http;
use Illuminate\Validation\ValidationException;

class SocialLoginService
{
    public function loginWithGoogle(string $idToken): AppUser
    {
        $payload = $this->verifyGoogle($idToken);

        return $this->findOrCreate(
            provider: 'google',
            providerId: $payload['sub'],
            email: $payload['email'] ?? null,
            name: $payload['name'] ?? ($payload['email'] ?? 'User'),
            avatar: $payload['picture'] ?? null,
            emailVerified: ($payload['email_verified'] ?? false) === true || ($payload['email_verified'] ?? '') === 'true',
        );
    }

    public function loginWithFacebook(string $accessToken): AppUser
    {
        $payload = $this->verifyFacebook($accessToken);

        return $this->findOrCreate(
            provider: 'facebook',
            providerId: $payload['id'],
            email: $payload['email'] ?? null,
            name: $payload['name'] ?? ($payload['email'] ?? 'User'),
            avatar: $payload['picture']['data']['url'] ?? null,
            emailVerified: isset($payload['email']),
        );
    }

    private function verifyGoogle(string $idToken): array
    {
        $response = Http::get('https://oauth2.googleapis.com/tokeninfo', ['id_token' => $idToken]);

        if (! $response->ok()) {
            throw ValidationException::withMessages(['id_token' => 'Invalid Google token.']);
        }

        $payload = $response->json();
        $allowedClientIds = config('services.google.client_ids', []);

        if (! empty($allowedClientIds) && ! in_array($payload['aud'] ?? null, $allowedClientIds, true)) {
            throw ValidationException::withMessages(['id_token' => 'Google token audience not allowed.']);
        }

        if (! isset($payload['sub'])) {
            throw ValidationException::withMessages(['id_token' => 'Google token missing subject.']);
        }

        return $payload;
    }

    private function verifyFacebook(string $accessToken): array
    {
        $appId = config('services.facebook.app_id');
        $appSecret = config('services.facebook.app_secret');

        if ($appId && $appSecret) {
            $debug = Http::get('https://graph.facebook.com/debug_token', [
                'input_token' => $accessToken,
                'access_token' => $appId.'|'.$appSecret,
            ])->json('data', []);

            if (! ($debug['is_valid'] ?? false) || ($debug['app_id'] ?? null) !== (string) $appId) {
                throw ValidationException::withMessages(['access_token' => 'Invalid Facebook token.']);
            }
        }

        $response = Http::get('https://graph.facebook.com/me', [
            'fields' => 'id,name,email,picture',
            'access_token' => $accessToken,
        ]);

        if (! $response->ok() || ! $response->json('id')) {
            throw ValidationException::withMessages(['access_token' => 'Facebook profile fetch failed.']);
        }

        return $response->json();
    }

    private function findOrCreate(string $provider, string $providerId, ?string $email, string $name, ?string $avatar, bool $emailVerified): AppUser
    {
        $providerColumn = $provider.'_id';

        $user = AppUser::where($providerColumn, $providerId)->first();

        if (! $user && $email) {
            $user = AppUser::where('email', $email)->first();
        }

        if ($user) {
            $user->fill([
                $providerColumn => $providerId,
                'avatar' => $user->avatar ?: $avatar,
            ]);
            if ($emailVerified && ! $user->email_verified_at) {
                $user->email_verified_at = now();
            }
            $user->save();

            return $user;
        }

        $user = AppUser::create([
            'full_name' => $name,
            'email' => $email ?: $provider.'_'.$providerId.'@users.noreply',
            'avatar' => $avatar,
            'login_provider' => $provider,
            $providerColumn => $providerId,
            'email_verified_at' => $emailVerified ? now() : null,
            'native_language_code' => 'en',
            'learning_language_code' => 'ja',
        ]);

        return $user->refresh();
    }
}
