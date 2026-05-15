<?php

use App\Http\Controllers\Admin\DashboardController;
use App\Http\Controllers\Admin\VocabCategoryController;
use App\Http\Controllers\Admin\VocabSubcategoryController;
use App\Http\Controllers\Admin\VocabularyController;
use App\Http\Controllers\Admin\VoiceController;
use Illuminate\Support\Facades\Route;

require __DIR__.'/auth.php';

Route::redirect('/', '/admin/dashboard');

Route::prefix('admin/vocab')->name('admin.vocab.')->group(function () {
    Route::get('words/{vocabulary}/generate-audio', [\App\Http\Controllers\Admin\VocabularyController::class, 'generateAudio'])->name('words.generate-audio');
    Route::get('words/{vocabulary}/regenerate-audio', [\App\Http\Controllers\Admin\VocabularyController::class, 'regenerateAudio'])->name('words.regenerate-audio');
});

Route::prefix('admin')->name('admin.')->middleware(['auth'])->group(function () {
    Route::get('/dashboard', [DashboardController::class, 'index'])->name('dashboard');

    Route::prefix('vocab')->name('vocab.')->group(function () {
        Route::post('categories/reorder', [VocabCategoryController::class, 'reorder'])->name('categories.reorder');
        Route::resource('categories', VocabCategoryController::class)
            ->except(['show'])
            ->parameters(['categories' => 'vocabCategory']);
        Route::patch('categories/{vocabCategory}/icon', [VocabCategoryController::class, 'updateIcon'])->name('categories.update-icon');

        Route::post('subcategories/reorder', [VocabSubcategoryController::class, 'reorder'])->name('subcategories.reorder');
        Route::resource('subcategories', VocabSubcategoryController::class)
            ->except(['show'])
            ->parameters(['subcategories' => 'vocabSubcategory']);
        Route::patch('subcategories/{vocabSubcategory}/icon', [VocabSubcategoryController::class, 'updateIcon'])->name('subcategories.update-icon');

        Route::post('words/reorder', [VocabularyController::class, 'reorder'])->name('words.reorder');
        Route::resource('words', VocabularyController::class)
            ->except(['show'])
            ->parameters(['words' => 'vocabulary']);
        Route::patch('words/{vocabulary}/image', [VocabularyController::class, 'updateImage'])->name('words.update-image');
        Route::patch('words/{vocabulary}/toggle-approved', [VocabularyController::class, 'toggleApproved'])->name('words.toggle-approved');
        Route::delete('words/{vocabulary}/delete-audio', [VocabularyController::class, 'deleteAudio'])->name('words.delete-audio');
    });

    Route::patch('voices/{voice}/toggle-default', [VoiceController::class, 'toggleDefault'])->name('voices.toggle-default');
    Route::resource('voices', VoiceController::class)->except(['show']);
});
