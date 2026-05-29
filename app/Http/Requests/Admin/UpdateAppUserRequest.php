<?php

namespace App\Http\Requests\Admin;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class UpdateAppUserRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        $userId = $this->route('appUser')->id;

        return [
            'full_name'               => ['nullable', 'string', 'max:255'],
            'username'                => ['nullable', 'string', 'max:255', Rule::unique('app_users', 'username')->ignore($userId)],
            'email'                   => ['required', 'email', 'max:255', Rule::unique('app_users', 'email')->ignore($userId)],
            'bio'                     => ['nullable', 'string', 'max:1000'],
            'gender'                  => ['nullable', 'string', 'in:male,female,other,prefer_not_to_say'],
            'birth_date'              => ['nullable', 'date'],
            'native_language_code'    => ['nullable', 'string', 'max:10'],
            'learning_language_code'  => ['nullable', 'string', 'max:10'],
            'proficiency_level'       => ['nullable', 'string', 'max:50'],
            'learning_goal'           => ['nullable', 'string', 'max:255'],
            'daily_goal_minutes'      => ['nullable', 'integer', 'min:0', 'max:1440'],
            'coins'                   => ['nullable', 'integer', 'min:0'],
            'is_banned'               => ['boolean'],
            'banned_reason'           => ['nullable', 'string', 'max:500'],
        ];
    }

    protected function prepareForValidation(): void
    {
        $this->merge([
            'is_banned' => $this->boolean('is_banned'),
        ]);
    }
}
