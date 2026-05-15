<?php

namespace App\Http\Requests\Admin;

use Illuminate\Foundation\Http\FormRequest;

class StoreVoiceRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'name'           => ['required', 'string', 'max:255'],
            'language'       => ['nullable', 'string', 'max:10'],
            'gender'         => ['nullable', 'in:male,female,neutral'],
            'description'    => ['nullable', 'string'],
            'reference_path' => ['nullable', 'file', 'mimes:wav,mp3,flac', 'max:20480'],
            'model'          => ['nullable', 'in:chatterbox,chatterbox-turbo,chatterbox-multilingual'],
            'exaggeration'   => ['nullable', 'numeric', 'min:0.25', 'max:2.0'],
            'cfg_weight'     => ['nullable', 'numeric', 'min:0.2', 'max:1.0'],
            'temperature'    => ['nullable', 'numeric', 'min:0.05', 'max:5.0'],
            'seed'           => ['nullable', 'integer', 'min:0'],
            'is_default'     => ['boolean'],
        ];
    }

    protected function prepareForValidation(): void
    {
        $this->merge(['is_default' => $this->boolean('is_default')]);
    }
}
