<?php

namespace App\Http\Requests\Api;

use Illuminate\Foundation\Http\FormRequest;

class OtpVerifyRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'receiver_email' => ['required', 'email'],
            'code' => ['required', 'string', 'size:4', 'regex:/^\d{4}$/'],
        ];
    }
}
