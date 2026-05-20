<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Api\V1\Auth\Concerns\IssuesAuthResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Api\RegisterRequest;
use App\Services\AppUserAuthService;
use Illuminate\Http\JsonResponse;
use OpenApi\Attributes as OA;

class RegisterController extends Controller
{
    use IssuesAuthResponse;

    public function __construct(private AppUserAuthService $auth)
    {
    }

    #[OA\Post(
        path: '/api/v1/auth/register',
        tags: ['Auth'],
        summary: 'Register a new app user with email and password',
        requestBody: new OA\RequestBody(
            required: true,
            content: new OA\JsonContent(
                required: ['name', 'email', 'password', 'password_confirmation', 'device_name'],
                properties: [
                    new OA\Property(property: 'name', type: 'string', example: 'Sulaiman MD'),
                    new OA\Property(property: 'display_name', type: 'string', nullable: true, example: 'smensulaiman'),
                    new OA\Property(property: 'email', type: 'string', format: 'email'),
                    new OA\Property(property: 'password', type: 'string', format: 'password', minLength: 8),
                    new OA\Property(property: 'password_confirmation', type: 'string', format: 'password'),
                    new OA\Property(property: 'device_name', type: 'string', example: 'iphone-15'),
                ]
            )
        ),
        responses: [
            new OA\Response(response: 201, description: 'Created', content: new OA\JsonContent(ref: '#/components/schemas/AuthResponse')),
            new OA\Response(response: 422, description: 'Validation error', content: new OA\JsonContent(ref: '#/components/schemas/ValidationError')),
        ]
    )]
    public function __invoke(RegisterRequest $request): JsonResponse
    {
        $user = $this->auth->register($request->validated());

        return $this->authResponse($user, $request->string('device_name'), 201);
    }
}
