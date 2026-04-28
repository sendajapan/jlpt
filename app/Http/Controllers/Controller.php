<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Http\UploadedFile;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Str;
use Intervention\Image\Encoders\JpegEncoder;
use Intervention\Image\Laravel\Facades\Image;

abstract class Controller
{
    protected function storeFile(Request $request, string $field, string $folder): ?string
    {
        if (! $request->hasFile($field)) {
            return null;
        }

        return $request->file($field)->store($folder, 'public');
    }

    protected function replaceFile(Request $request, string $field, string $folder, ?string $existing): ?string
    {
        if (! $request->hasFile($field)) {
            return $existing;
        }

        $this->deleteFile($existing);

        return $request->file($field)->store($folder, 'public');
    }

    protected function storeImage(Request $request, string $field, string $folder): ?string
    {
        if (! $request->hasFile($field)) {
            return null;
        }

        return $this->saveImageFile($request->file($field), $folder);
    }

    protected function replaceImage(Request $request, string $field, string $folder, ?string $existing): ?string
    {
        if (! $request->hasFile($field)) {
            return $existing;
        }

        $this->deleteFile($existing);

        return $this->saveImageFile($request->file($field), $folder);
    }


    protected function storeThumbnail(?UploadedFile $file, string $folder, int $size = 256): ?string
    {
        if (! $file) {
            return null;
        }

        $path = $folder . '/thumb_' . Str::uuid() . '.jpg';
        Storage::disk('public')->put($path, Image::decode($file)->cover($size, $size)->encode(new JpegEncoder(85)));

        return $path;
    }

    protected function replaceThumbnail(?UploadedFile $file, string $folder, ?string $existing, int $size = 256): ?string
    {
        if (! $file) {
            return $existing;
        }

        $this->deleteFile($existing);

        return $this->storeThumbnail($file, $folder, $size);
    }

    protected function deleteFile(?string $path): void
    {
        if ($path) {
            Storage::disk('public')->delete($path);
        }
    }

    protected function saveImageFile(UploadedFile $file, string $folder): string
    {
        $ext = $file->getClientOriginalExtension();
        $path = $folder . '/image_' . Str::uuid() . '.' . $ext;
        Storage::disk('public')->put($path, file_get_contents($file->getRealPath()));

        return $path;
    }
}
