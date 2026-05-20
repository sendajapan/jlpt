<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Http\Resources\AppUserResource;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use OpenApi\Attributes as OA;

class ProfileController extends Controller
{
    #[OA\Get(
        path: '/api/v1/me',
        tags: ['Profile'],
        summary: "Get the authenticated user's profile",
        security: [['sanctum' => []]],
        responses: [
            new OA\Response(
                response: 200,
                description: 'OK',
                content: new OA\JsonContent(properties: [new OA\Property(property: 'data', ref: '#/components/schemas/AppUser')])
            ),
        ]
    )]
    public function show(Request $request): AppUserResource
    {
        return new AppUserResource(
            $request->user()->loadCount(['favorites', 'reads'])
        );
    }

    #[OA\Patch(
        path: '/api/v1/me',
        tags: ['Profile'],
        summary: 'Update profile fields (POST also accepted)',
        security: [['sanctum' => []]],
        requestBody: new OA\RequestBody(
            content: new OA\MediaType(
                mediaType: 'multipart/form-data',
                schema: new OA\Schema(
                    properties: [
                        new OA\Property(property: 'name', type: 'string', maxLength: 100),
                        new OA\Property(property: 'username', type: 'string', maxLength: 50, nullable: true),
                        new OA\Property(property: 'avatar', type: 'string', format: 'binary'),
                        new OA\Property(property: 'bio', type: 'string', nullable: true),
                        new OA\Property(property: 'gender', type: 'string', enum: ['male', 'female', 'other', 'prefer_not_to_say'], nullable: true),
                        new OA\Property(property: 'birth_date', type: 'string', format: 'date', nullable: true),
                        new OA\Property(property: 'native_language_code', type: 'string'),
                        new OA\Property(property: 'learning_language_code', type: 'string'),
                        new OA\Property(property: 'proficiency_level', type: 'string', enum: ['beginner', 'elementary', 'intermediate', 'upper_intermediate', 'advanced', 'native_like']),
                        new OA\Property(property: 'learning_goal', type: 'string', enum: ['travel', 'school', 'business', 'anime', 'conversation', 'exam', 'hobby', 'other']),
                        new OA\Property(property: 'daily_goal_minutes', type: 'integer'),
                        new OA\Property(property: 'timezone', type: 'string', nullable: true),
                        new OA\Property(property: 'reminder_time', type: 'string', example: '09:00', nullable: true),
                        new OA\Property(property: 'notifications_enabled', type: 'boolean'),
                    ]
                )
            )
        ),
        responses: [
            new OA\Response(
                response: 200,
                description: 'Updated',
                content: new OA\JsonContent(properties: [new OA\Property(property: 'data', ref: '#/components/schemas/AppUser')])
            ),
        ]
    )]
    public function update(Request $request): AppUserResource
    {
        $user = $request->user();

        $validated = $request->validate([
            'name' => ['sometimes', 'string', 'max:100'],
            'username' => ['sometimes', 'nullable', 'string', 'max:50', 'unique:app_users,username,'.$user->id],
            'avatar' => ['sometimes', 'file', 'image', 'max:4096'],
            'bio' => ['sometimes', 'nullable', 'string', 'max:1000'],
            'gender' => ['sometimes', 'nullable', 'in:male,female,other,prefer_not_to_say'],
            'birth_date' => ['sometimes', 'nullable', 'date', 'before:today'],
            'native_language_code' => ['sometimes', 'string', 'max:10'],
            'learning_language_code' => ['sometimes', 'string', 'max:10'],
            'proficiency_level' => ['sometimes', 'in:beginner,elementary,intermediate,upper_intermediate,advanced,native_like'],
            'learning_goal' => ['sometimes', 'in:travel,school,business,anime,conversation,exam,hobby,other'],
            'daily_goal_minutes' => ['sometimes', 'integer', 'min:1', 'max:600'],
            'timezone' => ['sometimes', 'nullable', 'string', 'max:100'],
            'reminder_time' => ['sometimes', 'nullable', 'date_format:H:i'],
            'notifications_enabled' => ['sometimes', 'boolean'],
        ]);

        if (array_key_exists('name', $validated)) {
            $validated['full_name'] = $validated['name'];
            unset($validated['name']);
        }

        if ($request->hasFile('avatar')) {
            if ($user->avatar) {
                Storage::disk('public')->delete($user->avatar);
            }
            $validated['avatar'] = $request->file('avatar')->store('app_user_avatars', 'public');
        }

        $user->fill($validated)->save();

        return new AppUserResource($user->loadCount(['favorites', 'reads']));
    }
}
