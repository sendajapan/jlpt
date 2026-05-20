<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\RegisterRequest;
use App\Http\Resources\AppUserResource;
use App\Services\AppUserAuthService;
use Illuminate\Http\JsonResponse;

class RegisterController extends Controller
{
    public function __construct(private AppUserAuthService $auth)
    {
    }

    public function __invoke(RegisterRequest $request): JsonResponse
    {
        $user = $this->auth->register($request->validated());
        $token = $this->auth->issueToken($user, $request->string('device_name'));

        return response()->json([
            'user' => new AppUserResource($user),
            'token' => $token,
        ], 201);
    }
}
