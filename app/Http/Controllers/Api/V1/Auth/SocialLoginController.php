<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\SocialLoginRequest;
use App\Http\Resources\AppUserResource;
use App\Services\AppUserAuthService;
use App\Services\SocialLoginService;
use Illuminate\Http\JsonResponse;
use Illuminate\Validation\ValidationException;

class SocialLoginController extends Controller
{
    public function __construct(
        private SocialLoginService $social,
        private AppUserAuthService $auth,
    ) {
    }

    public function google(SocialLoginRequest $request): JsonResponse
    {
        if (! $request->filled('id_token')) {
            throw ValidationException::withMessages(['id_token' => 'id_token is required.']);
        }

        $user = $this->social->loginWithGoogle($request->string('id_token'));

        return $this->respond($user, $request->string('device_name'));
    }

    public function facebook(SocialLoginRequest $request): JsonResponse
    {
        if (! $request->filled('access_token')) {
            throw ValidationException::withMessages(['access_token' => 'access_token is required.']);
        }

        $user = $this->social->loginWithFacebook($request->string('access_token'));

        return $this->respond($user, $request->string('device_name'));
    }

    private function respond($user, string $deviceName): JsonResponse
    {
        $token = $this->auth->issueToken($user, $deviceName);

        return response()->json([
            'user' => new AppUserResource($user),
            'token' => $token,
        ]);
    }
}
