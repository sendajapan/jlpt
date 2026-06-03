<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\OtpVerifyRequest;
use App\Services\AppUserOtpService;
use Illuminate\Auth\Events\Verified;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class OtpController extends Controller
{
    public function __construct(private AppUserOtpService $otpService) {}

    public function send(Request $request): JsonResponse
    {
        $this->otpService->generateAndSend($request->user());

        return response()->json(['message' => 'OTP sent to your email.']);
    }

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
