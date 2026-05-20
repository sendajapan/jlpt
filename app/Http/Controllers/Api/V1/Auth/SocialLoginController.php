<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Api\V1\Auth\Concerns\IssuesAuthResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Api\SocialLoginRequest;
use App\Services\SocialLoginService;
use Illuminate\Http\JsonResponse;
use Illuminate\Validation\ValidationException;

class SocialLoginController extends Controller
{
    use IssuesAuthResponse;

    public function __construct(private SocialLoginService $social)
    {
    }

    public function google(SocialLoginRequest $request): JsonResponse
    {
        if (! $request->filled('id_token')) {
            throw ValidationException::withMessages(['id_token' => 'id_token is required.']);
        }

        $user = $this->social->loginWithGoogle($request->string('id_token'));

        return $this->authResponse($user, $request->string('device_name'));
    }

    public function facebook(SocialLoginRequest $request): JsonResponse
    {
        if (! $request->filled('access_token')) {
            throw ValidationException::withMessages(['access_token' => 'access_token is required.']);
        }

        $user = $this->social->loginWithFacebook($request->string('access_token'));

        return $this->authResponse($user, $request->string('device_name'));
    }
}
