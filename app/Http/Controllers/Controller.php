<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

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

    protected function deleteFile(?string $path): void
    {
        if ($path) {
            Storage::disk('public')->delete($path);
        }
    }
}
