<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Api\V1\Auth\Concerns\IssuesAuthResponse;
use App\Http\Controllers\Controller;
use App\Services\AppUserAuthService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class GuestLoginController extends Controller
{
    use IssuesAuthResponse;

    public function __construct(private AppUserAuthService $auth) {}

    public function __invoke(Request $request): JsonResponse
    {
        $request->validate([
            'device_name' => ['required', 'string', 'max:100'],
            'guest_name' => ['nullable', 'string', 'max:50'],
        ]);

        $user = $this->auth->loginAsGuest(
            $request->string('guest_name')->toString(),
            $request->string('device_name')->toString(),
        );

        return $this->authResponse($user, $request->string('device_name'));
    }
}
