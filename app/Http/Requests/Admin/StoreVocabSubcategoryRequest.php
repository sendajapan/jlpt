<?php

namespace App\Http\Requests\Admin;

use Illuminate\Foundation\Http\FormRequest;

class StoreVocabSubcategoryRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'vocab_category_id' => ['required', 'integer', 'exists:vocab_categories,id'],
            'name_en'           => ['required', 'string', 'max:255'],
            'name_jp'           => ['required', 'string', 'max:255'],
            'name_romaji'       => ['nullable', 'string', 'max:255'],
            'icon_path'         => ['nullable', 'file', 'mimes:jpg,jpeg,png,svg,webp', 'max:2048'],
            'audio_path'        => ['nullable', 'file', 'mimes:mp3,wav,ogg,aac,m4a', 'max:20480'],
            'sort_order'        => ['required', 'integer', 'min:0', 'max:9999'],
            'is_premium'        => ['boolean'],
        ];
    }

    protected function prepareForValidation(): void
    {
        $this->merge(['is_premium' => $this->boolean('is_premium')]);
    }
}
