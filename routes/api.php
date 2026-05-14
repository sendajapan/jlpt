<?php

use App\Http\Controllers\Api\V1\CategoryController;
use App\Http\Controllers\Api\V1\SubcategoryController;
use App\Http\Controllers\Api\V1\VocabularyController;
use Illuminate\Support\Facades\Route;

Route::get('categories', [CategoryController::class, 'index']);
Route::get('subcategories', [SubcategoryController::class, 'index']);
Route::get('vocabularies', [VocabularyController::class, 'index']);
