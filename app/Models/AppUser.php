<?php

namespace App\Models;

use App\Notifications\AppUserResetPasswordNotification;
use App\Notifications\AppUserVerifyEmailNotification;
use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class AppUser extends Authenticatable implements MustVerifyEmail
{
    use HasApiTokens;
    use HasFactory;
    use Notifiable;

    protected $table = 'app_users';

    protected $fillable = [
        'full_name',
        'username',
        'email',
        'password',
        'avatar',
        'login_provider',
        'google_id',
        'facebook_id',
        'email_verified_at',
        'coins',
        'xp',
        'bio',
        'gender',
        'birth_date',
        'native_language_code',
        'learning_language_code',
        'proficiency_level',
        'learning_goal',
        'daily_goal_minutes',
        'timezone',
        'reminder_time',
        'notifications_enabled',
        'is_banned',
        'banned_reason',
        'active_avatar_id',
    ];

    protected $hidden = [
        'password',
        'remember_token',
        'google_id',
        'facebook_id',
    ];

    protected function casts(): array
    {
        return [
            'email_verified_at' => 'datetime',
            'password' => 'hashed',
            'coins' => 'integer',
            'xp' => 'integer',
            'birth_date' => 'date',
            'last_streak_at' => 'datetime',
            'last_seen_at' => 'datetime',
            'last_lesson_at' => 'datetime',
            'notifications_enabled' => 'boolean',
            'is_verified' => 'boolean',
            'is_banned' => 'boolean',
        ];
    }

    public function favorites(): BelongsToMany
    {
        return $this->belongsToMany(Vocabulary::class, 'app_user_favorites', 'app_user_id', 'vocab_id')
            ->withTimestamps();
    }

    public function bookmarks(): BelongsToMany
    {
        return $this->belongsToMany(Vocabulary::class, 'app_user_bookmarks', 'app_user_id', 'vocab_id')
            ->withTimestamps();
    }

    public function reads(): HasMany
    {
        return $this->hasMany(AppUserWordRead::class);
    }

    public function learnedKanas(): BelongsToMany
    {
        return $this->belongsToMany(Kana::class, 'app_user_kana_learned', 'app_user_id', 'kana_id')
            ->withTimestamps();
    }

    public function learnedKanjis(): BelongsToMany
    {
        return $this->belongsToMany(Kanji::class, 'app_user_kanji_learned', 'app_user_id', 'kanji_id')
            ->withTimestamps();
    }

    public function coinTransactions(): HasMany
    {
        return $this->hasMany(CoinTransaction::class);
    }

    public function activeAvatar(): BelongsTo
    {
        return $this->belongsTo(Avatar::class, 'active_avatar_id');
    }

    public function avatarPurchases(): HasMany
    {
        return $this->hasMany(AvatarPurchase::class);
    }

    public function wordUnlocks(): HasMany
    {
        return $this->hasMany(AppUserWordUnlock::class);
    }

    public function categoryUnlocks(): HasMany
    {
        return $this->hasMany(AppUserCategoryUnlock::class);
    }

    public function sendPasswordResetNotification($token): void
    {
        $this->notify(new AppUserResetPasswordNotification($token));
    }

    public function sendEmailVerificationNotification(): void
    {
        $this->notify(new AppUserVerifyEmailNotification);
    }
}
