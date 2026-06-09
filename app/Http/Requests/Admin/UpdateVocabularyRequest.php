<?php

namespace App\Http\Requests\Admin;

use Illuminate\Foundation\Http\FormRequest;

class UpdateVocabularyRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'vocab_subcategory_id' => ['required', 'integer', 'exists:vocab_subcategories,id'],
            'word_jp' => ['required', 'string', 'max:255'],
            'audio_jp' => ['nullable', 'file', 'mimes:mp3,wav,ogg,aac,m4a', 'max:20480'],
            'sentence_jp' => ['nullable', 'string'],
            'sentence_audio_jp' => ['nullable', 'file', 'mimes:mp3,wav,ogg,aac,m4a', 'max:20480'],
            'word_romaji' => ['required', 'string', 'max:255'],
            'sentence_romaji' => ['nullable', 'string'],
            'word_en' => ['required', 'string', 'max:500'],
            'pos' => ['nullable', 'string', 'in:noun,verb,i-adjective,na-adjective,adverb,particle,conjunction,interjection,pronoun,expression,counter,prefix,suffix'],
            'audio_en' => ['nullable', 'file', 'mimes:mp3,wav,ogg,aac,m4a', 'max:20480'],
            'sentence_en' => ['nullable', 'string'],
            'sentence_audio_en' => ['nullable', 'file', 'mimes:mp3,wav,ogg,aac,m4a', 'max:20480'],
            'image_path' => ['nullable', 'file', 'mimes:jpg,jpeg,png,webp', 'max:4096'],
            'sort_order' => ['required', 'integer', 'min:0', 'max:9999'],
            'is_premium' => ['required', 'boolean'],
            'is_approved' => ['required', 'boolean'],
        ];
    }
}
