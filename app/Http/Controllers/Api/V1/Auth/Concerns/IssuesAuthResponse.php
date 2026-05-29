<?php

namespace App\Http\Controllers\Api\V1\Auth\Concerns;

use App\Http\Resources\AppUserResource;
use App\Models\AppUser;
use App\Services\AppUserAuthService;
use Illuminate\Http\JsonResponse;

trait IssuesAuthResponse
{
    protected function authResponse(AppUser $user, string $deviceName, int $status = 200): JsonResponse
    {
        $user->loadCount(['favorites', 'reads']);
        $token = app(AppUserAuthService::class)->issueToken($user, $deviceName);

        return response()->json([
            'user' => new AppUserResource($user),
            'token' => $token,
        ], $status);
    }
}
