<?php

namespace App\Http\Controllers\Api\V1\Auth;

use App\Http\Controllers\Controller;
use App\Models\AppUser;
use Illuminate\Auth\Events\Verified;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use OpenApi\Attributes as OA;
use Symfony\Component\HttpKernel\Exception\HttpException;

class EmailVerificationController extends Controller
{
    #[OA\Post(
        path: '/api/v1/auth/email/verify-send',
        tags: ['Auth'],
        summary: 'Resend the email verification link to the authenticated user',
        security: [['sanctum' => []]],
        responses: [new OA\Response(response: 200, description: 'Verification email sent')]
    )]
    public function send(Request $request): JsonResponse
    {
        $user = $request->user();

        if ($user->hasVerifiedEmail()) {
            return response()->json(['message' => 'Already verified.']);
        }

        $user->sendEmailVerificationNotification();

        return response()->json(['message' => 'Verification email sent.']);
    }

    #[OA\Get(
        path: '/api/v1/auth/email/verify/{id}/{hash}',
        tags: ['Auth'],
        summary: 'Confirm email via the signed link sent over email',
        parameters: [
            new OA\Parameter(name: 'id', in: 'path', required: true, schema: new OA\Schema(type: 'integer')),
            new OA\Parameter(name: 'hash', in: 'path', required: true, schema: new OA\Schema(type: 'string')),
        ],
        responses: [
            new OA\Response(response: 200, description: 'Email verified'),
            new OA\Response(response: 403, description: 'Invalid verification link'),
        ]
    )]
    public function verify(Request $request, int $id, string $hash): JsonResponse
    {
        $user = AppUser::findOrFail($id);

        if (! hash_equals($hash, sha1($user->getEmailForVerification()))) {
            throw new HttpException(403, 'Invalid verification link.');
        }

        if (! $user->hasVerifiedEmail()) {
            $user->markEmailAsVerified();
            event(new Verified($user));
        }

        return response()->json(['message' => 'Email verified.']);
    }
}
