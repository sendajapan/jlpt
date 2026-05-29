<?php

namespace App\Http\Requests\Api;

use Illuminate\Foundation\Http\FormRequest;

class SocialLoginRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'id_token' => ['required_without:access_token', 'nullable', 'string'],
            'access_token' => ['required_without:id_token', 'nullable', 'string'],
            'device_name' => ['required', 'string', 'max:100'],
        ];
    }
}
