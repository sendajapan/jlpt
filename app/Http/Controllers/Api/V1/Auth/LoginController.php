<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\LoginRequest;
use App\Http\Resources\AppUserResource;
use App\Services\AppUserAuthService;
use Illuminate\Http\JsonResponse;

class LoginController extends Controller
{
    public function __construct(private AppUserAuthService $auth)
    {
    }

    public function __invoke(LoginRequest $request): JsonResponse
    {
        $user = $this->auth->login($request->string('email'), $request->string('password'));
        $token = $this->auth->issueToken($user, $request->string('device_name'));

        return response()->json([
            'user' => new AppUserResource($user),
            'token' => $token,
        ]);
    }
}
