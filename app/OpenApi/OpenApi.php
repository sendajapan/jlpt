<?php

namespace App\OpenApi;

use OpenApi\Attributes as OA;

#[OA\Info(
    version: '1.0.0',
    title: 'JLPT Mobile API',
    description: 'REST API for the JLPT mobile app. All authenticated endpoints expect a Sanctum bearer token issued by /auth/login or /auth/register.'
)]
#[OA\Server(url: 'https://pathlingo.scholarlyapps.com', description: 'API Server')]
#[OA\SecurityScheme(
    securityScheme: 'sanctum',
    type: 'http',
    scheme: 'bearer',
    bearerFormat: 'Sanctum token'
)]
#[OA\Tag(name: 'Auth', description: 'Registration, login, social login, password reset, email verification')]
#[OA\Tag(name: 'Profile', description: 'Authenticated user profile')]
#[OA\Tag(name: 'Favorites', description: 'Vocabulary favorites')]
#[OA\Tag(name: 'Reads', description: 'Mark vocab words as read, list read history')]
#[OA\Tag(name: 'Coins', description: 'Coin balance and transaction history')]
#[OA\Tag(name: 'Public', description: 'Public catalog endpoints (no auth)')]
#[OA\Schema(
    schema: 'AppUser',
    type: 'object',
    properties: [
        new OA\Property(property: 'id', type: 'integer', example: 5),
        new OA\Property(property: 'name', type: 'string', example: 'Sulaiman MD'),
        new OA\Property(property: 'username', type: 'string', nullable: true, example: 'smensulaiman'),
        new OA\Property(property: 'email', type: 'string', format: 'email'),
        new OA\Property(property: 'email_verified', type: 'boolean'),
        new OA\Property(property: 'avatar', type: 'string', nullable: true),
        new OA\Property(property: 'login_provider', type: 'string', enum: ['email', 'google', 'apple', 'facebook', 'guest']),
        new OA\Property(property: 'created_at', type: 'string', format: 'date-time'),
        new OA\Property(property: 'bio', type: 'string', nullable: true),
        new OA\Property(property: 'gender', type: 'string', nullable: true, enum: ['male', 'female', 'other', 'prefer_not_to_say']),
        new OA\Property(property: 'birth_date', type: 'string', format: 'date', nullable: true),
        new OA\Property(property: 'native_language_code', type: 'string'),
        new OA\Property(property: 'learning_language_code', type: 'string'),
        new OA\Property(property: 'proficiency_level', type: 'string', enum: ['beginner', 'elementary', 'intermediate', 'upper_intermediate', 'advanced', 'native_like']),
        new OA\Property(property: 'learning_goal', type: 'string', enum: ['travel', 'school', 'business', 'anime', 'conversation', 'exam', 'hobby', 'other']),
        new OA\Property(property: 'daily_goal_minutes', type: 'integer'),
        new OA\Property(property: 'timezone', type: 'string', nullable: true),
        new OA\Property(property: 'reminder_time', type: 'string', nullable: true),
        new OA\Property(property: 'notifications_enabled', type: 'boolean'),
        new OA\Property(property: 'coins', type: 'integer'),
        new OA\Property(property: 'xp_points', type: 'integer'),
        new OA\Property(property: 'current_level', type: 'integer'),
        new OA\Property(property: 'current_streak', type: 'integer'),
        new OA\Property(property: 'longest_streak', type: 'integer'),
        new OA\Property(property: 'learned_words', type: 'integer'),
        new OA\Property(property: 'completed_lessons', type: 'integer'),
        new OA\Property(property: 'total_study_minutes', type: 'integer'),
        new OA\Property(property: 'last_streak_at', type: 'string', format: 'date-time', nullable: true),
        new OA\Property(property: 'last_seen_at', type: 'string', format: 'date-time', nullable: true),
        new OA\Property(property: 'last_lesson_at', type: 'string', format: 'date-time', nullable: true),
        new OA\Property(property: 'favorites_count', type: 'integer'),
        new OA\Property(property: 'reads_count', type: 'integer'),
    ]
)]
#[OA\Schema(
    schema: 'AuthResponse',
    type: 'object',
    properties: [
        new OA\Property(property: 'user', ref: '#/components/schemas/AppUser'),
        new OA\Property(property: 'token', type: 'string'),
    ]
)]
#[OA\Schema(
    schema: 'ValidationError',
    type: 'object',
    properties: [
        new OA\Property(property: 'message', type: 'string'),
        new OA\Property(property: 'errors', type: 'object'),
    ]
)]
#[OA\Schema(
    schema: 'CoinTransaction',
    type: 'object',
    properties: [
        new OA\Property(property: 'id', type: 'integer'),
        new OA\Property(property: 'amount', type: 'integer'),
        new OA\Property(property: 'reason', type: 'string'),
        new OA\Property(property: 'reference_type', type: 'string', nullable: true),
        new OA\Property(property: 'reference_id', type: 'integer', nullable: true),
        new OA\Property(property: 'created_at', type: 'string', format: 'date-time'),
    ]
)]
#[OA\Schema(
    schema: 'VocabCategory',
    type: 'object',
    properties: [
        new OA\Property(property: 'id', type: 'integer', example: 1),
        new OA\Property(property: 'name_en', type: 'string', nullable: true, example: 'Animals'),
        new OA\Property(property: 'name_jp', type: 'string', nullable: true, example: 'どうぶつ'),
        new OA\Property(property: 'name_romaji', type: 'string', nullable: true, example: 'doubutsu'),
        new OA\Property(property: 'icon_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'icon_thumbnail_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'category_bg_path', type: 'string', nullable: true, example: '1'),
        new OA\Property(property: 'audio_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'sort_order', type: 'integer', nullable: true, example: 10),
        new OA\Property(property: 'is_premium', type: 'boolean', example: false),
    ]
)]
#[OA\Schema(
    schema: 'VocabSubcategory',
    type: 'object',
    properties: [
        new OA\Property(property: 'id', type: 'integer', example: 3),
        new OA\Property(property: 'vocab_category_id', type: 'integer', example: 1),
        new OA\Property(property: 'name_en', type: 'string', nullable: true, example: 'Farm Animals'),
        new OA\Property(property: 'name_jp', type: 'string', nullable: true, example: 'のうじょうのどうぶつ'),
        new OA\Property(property: 'name_romaji', type: 'string', nullable: true, example: 'noujou no doubutsu'),
        new OA\Property(property: 'icon_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'icon_thumbnail_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'icon_thumbnail_bg', type: 'string', nullable: true, example: '#e8f5e9'),
        new OA\Property(property: 'audio_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'sort_order', type: 'integer', nullable: true, example: 10),
        new OA\Property(property: 'is_premium', type: 'boolean', example: false),
    ]
)]
#[OA\Schema(
    schema: 'VocabWord',
    type: 'object',
    properties: [
        new OA\Property(property: 'id', type: 'integer', example: 42),
        new OA\Property(property: 'vocab_subcategory_id', type: 'integer', example: 3),
        new OA\Property(property: 'word_jp', type: 'string', example: 'ねこ'),
        new OA\Property(property: 'word_romaji', type: 'string', nullable: true, example: 'neko'),
        new OA\Property(property: 'word_en', type: 'string', nullable: true, example: 'cat'),
        new OA\Property(property: 'sentence_jp', type: 'string', nullable: true, example: 'ねこがいます。'),
        new OA\Property(property: 'sentence_romaji', type: 'string', nullable: true, example: 'Neko ga imasu.'),
        new OA\Property(property: 'sentence_en', type: 'string', nullable: true, example: 'There is a cat.'),
        new OA\Property(property: 'audio_jp_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'audio_en_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'sentence_audio_jp_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'sentence_audio_en_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'image_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'image_thumbnail_url', type: 'string', format: 'uri', nullable: true),
        new OA\Property(property: 'image_thumbnail_bg', type: 'string', nullable: true, example: '#e3f2fd'),
        new OA\Property(property: 'sort_order', type: 'integer', nullable: true, example: 10),
        new OA\Property(property: 'is_premium', type: 'boolean', example: false),
    ]
)]
class OpenApi {}
