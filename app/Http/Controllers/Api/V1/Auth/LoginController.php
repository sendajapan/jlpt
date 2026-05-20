<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Api\V1\Auth\Concerns\IssuesAuthResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Api\LoginRequest;
use App\Services\AppUserAuthService;
use Illuminate\Http\JsonResponse;

class LoginController extends Controller
{
    use IssuesAuthResponse;

    public function __construct(private AppUserAuthService $auth)
    {
    }

    public function __invoke(LoginRequest $request): JsonResponse
    {
        $user = $this->auth->login($request->string('email'), $request->string('password'));

        return $this->authResponse($user, $request->string('device_name'));
    }
}
