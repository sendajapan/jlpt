<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Api\V1\Auth\Concerns\IssuesAuthResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Api\LoginRequest;
use App\Services\AppUserAuthService;
use Illuminate\Http\JsonResponse;
use OpenApi\Attributes as OA;

class LoginController extends Controller
{
    use IssuesAuthResponse;

    public function __construct(private AppUserAuthService $auth) {}

    #[OA\Post(
        path: '/api/v1/auth/login',
        tags: ['Auth'],
        summary: 'Login with email and password',
        requestBody: new OA\RequestBody(
            required: true,
            content: new OA\JsonContent(
                required: ['email', 'password', 'device_name'],
                properties: [
                    new OA\Property(property: 'email', type: 'string', format: 'email', example: 'sulaiman@sendajapan.com'),
                    new OA\Property(property: 'password', type: 'string', format: 'password', example: 'p@ssword'),
                    new OA\Property(property: 'device_name', type: 'string', example: 'iphone-15'),
                ]
            )
        ),
        responses: [
            new OA\Response(response: 200, description: 'OK', content: new OA\JsonContent(ref: '#/components/schemas/AuthResponse')),
            new OA\Response(response: 422, description: 'Invalid credentials', content: new OA\JsonContent(ref: '#/components/schemas/ValidationError')),
        ]
    )]
    public function __invoke(LoginRequest $request): JsonResponse
    {
        $user = $this->auth->login($request->string('email'), $request->string('password'));

        return $this->authResponse($user, $request->string('device_name'));
    }
}
