<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\AppUserCategoryUnlock;
use App\Models\VocabCategory;
use App\Models\VocabSubcategory;
use App\Services\WordUnlockService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class CategoryUnlockController extends Controller
{
    public function __construct(private WordUnlockService $unlocks) {}

    public function unlockCategory(Request $request, VocabCategory $category): JsonResponse
    {
        $user = $request->user();

        if (! $category->is_premium) {
            return response()->json(['message' => 'This category is free.', 'coins' => (int) $user->coins], 200);
        }

        $alreadyUnlocked = AppUserCategoryUnlock::where('app_user_id', $user->id)
            ->where('unlockable_type', VocabCategory::class)
            ->where('unlockable_id', $category->id)
            ->exists();

        if ($alreadyUnlocked) {
            return response()->json(['message' => 'Already unlocked.', 'coins' => (int) $user->coins], 200);
        }

        if ($user->coins < $category->coin_price) {
            return response()->json(['message' => 'Insufficient coins.', 'coins' => (int) $user->coins], 402);
        }

        $this->unlocks->unlockCategory($user, $category);

        return response()->json(['message' => 'Category unlocked successfully.', 'coins' => (int) $user->refresh()->coins], 200);
    }

    public function unlockSubcategory(Request $request, VocabSubcategory $subcategory): JsonResponse
    {
        $user = $request->user();

        if (! $subcategory->is_premium) {
            return response()->json(['message' => 'This subcategory is free.', 'coins' => (int) $user->coins], 200);
        }

        $alreadyUnlocked = AppUserCategoryUnlock::where('app_user_id', $user->id)
            ->where('unlockable_type', VocabSubcategory::class)
            ->where('unlockable_id', $subcategory->id)
            ->exists();

        if ($alreadyUnlocked) {
            return response()->json(['message' => 'Already unlocked.', 'coins' => (int) $user->coins], 200);
        }

        if ($user->coins < $subcategory->coin_price) {
            return response()->json(['message' => 'Insufficient coins.', 'coins' => (int) $user->coins], 402);
        }

        $this->unlocks->unlockSubcategory($user, $subcategory);

        return response()->json(['message' => 'Subcategory unlocked successfully.', 'coins' => (int) $user->refresh()->coins], 200);
    }
}
