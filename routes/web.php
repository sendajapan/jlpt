<?php

use App\Http\Controllers\Admin\DashboardController;
use App\Http\Controllers\Admin\VocabCategoryController;
use App\Http\Controllers\Admin\VocabSubcategoryController;
use App\Http\Controllers\Admin\VocabularyController;
use Illuminate\Support\Facades\Route;

require __DIR__.'/auth.php';

Route::redirect('/', '/admin/dashboard');

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

        Route::resource('words', VocabularyController::class)
            ->except(['show'])
            ->parameters(['words' => 'vocabulary']);
        Route::patch('words/{vocabulary}/image', [VocabularyController::class, 'updateImage'])->name('words.update-image');
        Route::patch('words/{vocabulary}/toggle-approved', [VocabularyController::class, 'toggleApproved'])->name('words.toggle-approved');
    });
});
