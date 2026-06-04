<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;
use Illuminate\Support\Facades\Storage;

class AppUserResource extends JsonResource
{
    public function toArray($request): array
    {
        return [
            'id' => $this->id,
            'name' => $this->full_name,
            'username' => $this->username,
            'email' => $this->email,
            'email_verified' => (bool) $this->email_verified_at,
            'avatar' => $this->avatar ? asset('storage/'.$this->avatar) : null,
            'avatar_url' => $this->active_avatar_id && $this->activeAvatar
                ? Storage::disk('public')->url($this->activeAvatar->image_path)
                : ($this->avatar ? asset('storage/'.$this->avatar) : null),
            'active_avatar_id' => $this->active_avatar_id,
            'login_provider' => $this->login_provider,
            'is_guest' => $this->login_provider === 'guest',
            'created_at' => $this->created_at?->toIso8601String(),

            'bio' => $this->bio,
            'gender' => $this->gender,
            'birth_date' => $this->birth_date?->toDateString(),

            'native_language_code' => $this->native_language_code,
            'learning_language_code' => $this->learning_language_code,
            'proficiency_level' => $this->proficiency_level,
            'learning_goal' => $this->learning_goal,
            'daily_goal_minutes' => (int) $this->daily_goal_minutes,
            'timezone' => $this->timezone,
            'reminder_time' => $this->reminder_time,
            'notifications_enabled' => (bool) $this->notifications_enabled,

            'coins' => (int) $this->coins,
            'xp_points' => (int) $this->xp_points,
            'current_level' => (int) $this->current_level,
            'current_streak' => (int) $this->current_streak,
            'longest_streak' => (int) $this->longest_streak,
            'learned_words' => (int) $this->learned_words,
            'completed_lessons' => (int) $this->completed_lessons,
            'total_study_minutes' => (int) $this->total_study_minutes,
            'last_streak_at' => $this->last_streak_at?->toIso8601String(),
            'last_seen_at' => $this->last_seen_at?->toIso8601String(),
            'last_lesson_at' => $this->last_lesson_at?->toIso8601String(),

            'favorites_count' => (int) ($this->favorites_count ?? $this->favorites()->count()),
            'reads_count' => (int) ($this->reads_count ?? $this->reads()->count()),
        ];
    }
}
