<?php

namespace App\Http\Requests\Admin;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class StoreVocabularyRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'subcategory_id'       => ['required', 'integer', 'exists:subcategories,id'],
            'word_jp'              => ['required', 'string', 'max:255'],
            'word_romaji'          => ['required', 'string', 'max:255'],
            'meaning_en'           => ['required', 'string', 'max:500'],
            'audio_path'           => ['nullable', 'file', 'mimes:mp3,wav,ogg,aac,m4a', 'max:20480'],
            'image_path'           => ['nullable', 'file', 'mimes:jpg,jpeg,png,webp', 'max:4096'],
            'example_sentence_jp'  => ['nullable', 'string'],
            'example_sentence_en'  => ['nullable', 'string'],
            'jlpt_level'           => ['nullable', Rule::in(['N5', 'N4', 'N3', 'N2', 'N1'])],
            'sort_order'           => ['required', 'integer', 'min:0', 'max:9999'],
        ];
    }
}
