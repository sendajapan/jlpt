<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Api\V1\Auth\Concerns\IssuesAuthResponse;
use App\Http\Controllers\Controller;
use App\Http\Requests\Api\SocialLoginRequest;
use App\Services\SocialLoginService;
use Illuminate\Http\JsonResponse;
use Illuminate\Validation\ValidationException;
use OpenApi\Attributes as OA;

class SocialLoginController extends Controller
{
    use IssuesAuthResponse;

    public function __construct(private SocialLoginService $social)
    {
    }

    #[OA\Post(
        path: '/api/v1/auth/social/google',
        tags: ['Auth'],
        summary: 'Login or register via Google ID token',
        description: 'Mobile app performs native Google sign-in and posts the resulting id_token here.',
        requestBody: new OA\RequestBody(
            required: true,
            content: new OA\JsonContent(
                required: ['id_token', 'device_name'],
                properties: [
                    new OA\Property(property: 'id_token', type: 'string'),
                    new OA\Property(property: 'device_name', type: 'string'),
                ]
            )
        ),
        responses: [
            new OA\Response(response: 200, description: 'OK', content: new OA\JsonContent(ref: '#/components/schemas/AuthResponse')),
            new OA\Response(response: 422, description: 'Invalid Google token'),
        ]
    )]
    public function google(SocialLoginRequest $request): JsonResponse
    {
        if (! $request->filled('id_token')) {
            throw ValidationException::withMessages(['id_token' => 'id_token is required.']);
        }

        $user = $this->social->loginWithGoogle($request->string('id_token'));

        return $this->authResponse($user, $request->string('device_name'));
    }

    #[OA\Post(
        path: '/api/v1/auth/social/facebook',
        tags: ['Auth'],
        summary: 'Login or register via Facebook access token',
        requestBody: new OA\RequestBody(
            required: true,
            content: new OA\JsonContent(
                required: ['access_token', 'device_name'],
                properties: [
                    new OA\Property(property: 'access_token', type: 'string'),
                    new OA\Property(property: 'device_name', type: 'string'),
                ]
            )
        ),
        responses: [
            new OA\Response(response: 200, description: 'OK', content: new OA\JsonContent(ref: '#/components/schemas/AuthResponse')),
            new OA\Response(response: 422, description: 'Invalid Facebook token'),
        ]
    )]
    public function facebook(SocialLoginRequest $request): JsonResponse
    {
        if (! $request->filled('access_token')) {
            throw ValidationException::withMessages(['access_token' => 'access_token is required.']);
        }

        $user = $this->social->loginWithFacebook($request->string('access_token'));

        return $this->authResponse($user, $request->string('device_name'));
    }
}
