<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\VocabCategory;
use App\Models\VocabSubcategory;
use App\Models\Vocabulary;
use Illuminate\View\View;

class DashboardController extends Controller
{
    public function index(): View
    {
        return view('admin.dashboard', [
            'categoryCount'    => VocabCategory::count(),
            'subcategoryCount' => VocabSubcategory::count(),
            'vocabularyCount'  => Vocabulary::count(),
        ]);
    }
}
