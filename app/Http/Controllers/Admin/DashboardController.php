<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Models\Category;
use App\Models\Subcategory;
use App\Models\Vocabulary;
use Illuminate\View\View;

class DashboardController extends Controller
{
    public function index(): View
    {
        return view('admin.dashboard', [
            'categoryCount'    => Category::count(),
            'subcategoryCount' => Subcategory::count(),
            'vocabularyCount'  => Vocabulary::count(),
        ]);
    }
}
