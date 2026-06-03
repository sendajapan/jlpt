<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\OtpVerifyRequest;
use App\Services\AppUserOtpService;
use Illuminate\Auth\Events\Verified;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use OpenApi\Attributes as OA;

class OtpController extends Controller
{
    public function __construct(private AppUserOtpService $otpService) {}

    #[OA\Post(
        path: '/api/v1/auth/otp/send',
        tags: ['Auth'],
        summary: 'Resend OTP verification code to the authenticated user\'s email',
        security: [['sanctum' => []]],
        responses: [
            new OA\Response(
                response: 200,
                description: 'OTP sent',
                content: new OA\JsonContent(
                    properties: [new OA\Property(property: 'message', type: 'string', example: 'OTP sent to your email.')]
                )
            ),
            new OA\Response(response: 401, description: 'Unauthenticated'),
        ]
    )]
    public function send(Request $request): JsonResponse
    {
        $this->otpService->generateAndSend($request->user());

        return response()->json(['message' => 'OTP sent to your email.']);
    }

    #[OA\Post(
        path: '/api/v1/auth/otp/verify',
        tags: ['Auth'],
        summary: 'Verify the 4-digit OTP code to confirm email ownership',
        security: [['sanctum' => []]],
        requestBody: new OA\RequestBody(
            required: true,
            content: new OA\JsonContent(
                required: ['code'],
                properties: [
                    new OA\Property(property: 'code', type: 'string', minLength: 4, maxLength: 4, example: '4827', description: '4-digit numeric OTP'),
                ]
            )
        ),
        responses: [
            new OA\Response(
                response: 200,
                description: 'Email verified',
                content: new OA\JsonContent(
                    properties: [new OA\Property(property: 'message', type: 'string', example: 'Email verified successfully.')]
                )
            ),
            new OA\Response(response: 401, description: 'Unauthenticated'),
            new OA\Response(response: 422, description: 'Invalid or expired OTP', content: new OA\JsonContent(ref: '#/components/schemas/ValidationError')),
        ]
    )]
    public function verify(OtpVerifyRequest $request): JsonResponse
    {
        $user = $request->user();

        if (! $this->otpService->verify($user, $request->validated('code'))) {
            return response()->json([
                'message' => 'Invalid or expired OTP.',
                'errors' => ['code' => ['Invalid or expired OTP.']],
            ], 422);
        }

        if (! $user->hasVerifiedEmail()) {
            $user->markEmailAsVerified();
            event(new Verified($user));
        }

        return response()->json(['message' => 'Email verified successfully.']);
    }
}
