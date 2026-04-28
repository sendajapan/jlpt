<?php

use App\Http\Controllers\Admin\CategoryController;
use App\Http\Controllers\Admin\DashboardController;
use App\Http\Controllers\Admin\SubcategoryController;
use App\Http\Controllers\Admin\VocabularyController;
use Illuminate\Support\Facades\Route;

require __DIR__.'/auth.php';

Route::redirect('/', '/admin/dashboard');

Route::prefix('admin')->name('admin.')->middleware(['auth'])->group(function () {
    Route::get('/dashboard', [DashboardController::class, 'index'])->name('dashboard');
    Route::resource('categories', CategoryController::class)->except(['show']);
    Route::patch('categories/{category}/icon', [CategoryController::class, 'updateIcon'])->name('categories.update-icon');

    Route::resource('subcategories', SubcategoryController::class)->except(['show']);
    Route::patch('subcategories/{subcategory}/icon', [SubcategoryController::class, 'updateIcon'])->name('subcategories.update-icon');

    Route::resource('vocabularies', VocabularyController::class)->except(['show']);
    Route::patch('vocabularies/{vocabulary}/image', [VocabularyController::class, 'updateImage'])->name('vocabularies.update-image');
    Route::patch('vocabularies/{vocabulary}/toggle-approved', [VocabularyController::class, 'toggleApproved'])->name('vocabularies.toggle-approved');
});
