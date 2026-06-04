<?php

use App\Http\Controllers\Api\V1\Auth\EmailVerificationController;
use App\Http\Controllers\Api\V1\Auth\GuestLoginController;
use App\Http\Controllers\Api\V1\Auth\LoginController;
use App\Http\Controllers\Api\V1\Auth\LogoutController;
use App\Http\Controllers\Api\V1\Auth\OtpController;
use App\Http\Controllers\Api\V1\Auth\PasswordResetController;
use App\Http\Controllers\Api\V1\Auth\RegisterController;
use App\Http\Controllers\Api\V1\Auth\SocialLoginController;
use App\Http\Controllers\Api\V1\AvatarController;
use App\Http\Controllers\Api\V1\CategoryController;
use App\Http\Controllers\Api\V1\CoinController;
use App\Http\Controllers\Api\V1\FavoriteController;
use App\Http\Controllers\Api\V1\ProfileController;
use App\Http\Controllers\Api\V1\SubcategoryController;
use App\Http\Controllers\Api\V1\VocabularyController;
use App\Http\Controllers\Api\V1\WordReadController;
use Illuminate\Support\Facades\Route;

Route::get('avatars', [AvatarController::class, 'index']);

Route::get('categories', [CategoryController::class, 'index']);
Route::get('subcategories', [SubcategoryController::class, 'index']);
Route::get('vocabularies', [VocabularyController::class, 'index']);

Route::prefix('auth')->group(function () {
    Route::post('guest', GuestLoginController::class);
    Route::post('register', RegisterController::class);
    Route::post('login', LoginController::class);
    Route::post('social/google', [SocialLoginController::class, 'google']);
    Route::post('social/facebook', [SocialLoginController::class, 'facebook']);
    Route::post('forgot-password', [PasswordResetController::class, 'forgot']);
    Route::post('reset-password', [PasswordResetController::class, 'reset']);
    Route::post('otp/send', [OtpController::class, 'send']);
    Route::post('otp/verify', [OtpController::class, 'verify']);

    Route::get('email/verify/{id}/{hash}', [EmailVerificationController::class, 'verify'])
        ->middleware('signed')
        ->name('api.app.verification.verify');
});

Route::middleware('auth:app_users')->group(function () {
    Route::post('auth/logout', LogoutController::class);
    Route::post('auth/email/verify-send', [EmailVerificationController::class, 'send']);

    Route::get('me', [ProfileController::class, 'show']);
    Route::match(['post', 'patch'], 'me', [ProfileController::class, 'update']);

    Route::get('favorites', [FavoriteController::class, 'index']);
    Route::post('favorites/{vocabulary}', [FavoriteController::class, 'store']);
    Route::delete('favorites/{vocabulary}', [FavoriteController::class, 'destroy']);

    Route::get('me/reads', [WordReadController::class, 'index']);
    Route::post('words/{vocabulary}/read', [WordReadController::class, 'store']);

    Route::get('me/coins', [CoinController::class, 'balance']);
    Route::get('me/coins/transactions', [CoinController::class, 'transactions']);

    Route::post('avatars/{avatar}/purchase', [AvatarController::class, 'purchase']);
    Route::patch('me/avatar', [AvatarController::class, 'setActive']);
});
