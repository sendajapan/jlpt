<?php

namespace App\Http\Requests\Api;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rules\Password;

class RegisterRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'name' => ['required', 'string', 'max:100'],
            'display_name' => ['nullable', 'string', 'max:60'],
            'email' => ['required', 'email', 'max:150', 'unique:app_users,email'],
            'password' => ['required', 'confirmed', Password::min(8)],
            'device_name' => ['required', 'string', 'max:100'],
        ];
    }
}
