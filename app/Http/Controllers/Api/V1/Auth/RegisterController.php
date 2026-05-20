<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Api\V1\Auth\Concerns\IssuesAuthResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Api\RegisterRequest;
use App\Services\AppUserAuthService;
use Illuminate\Http\JsonResponse;

class RegisterController extends Controller
{
    use IssuesAuthResponse;

    public function __construct(private AppUserAuthService $auth)
    {
    }

    public function __invoke(RegisterRequest $request): JsonResponse
    {
        $user = $this->auth->register($request->validated());

        return $this->authResponse($user, $request->string('device_name'), 201);
    }
}
