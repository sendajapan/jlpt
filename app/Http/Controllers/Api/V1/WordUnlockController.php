<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\AppUserCategoryUnlock;
use App\Models\AppUserWordUnlock;
use App\Models\VocabCategory;
use App\Models\VocabSubcategory;
use App\Models\Vocabulary;
use App\Services\WordUnlockService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class WordUnlockController extends Controller
{
    public function __construct(private WordUnlockService $unlocks) {}

    public function store(Request $request, Vocabulary $vocabulary): JsonResponse
    {
        $user = $request->user();

        if (! $vocabulary->is_premium) {
            return response()->json(['message' => 'This word is free.', 'coins' => (int) $user->coins], 200);
        }

        $alreadyUnlocked = AppUserWordUnlock::where('app_user_id', $user->id)
            ->where('vocab_id', $vocabulary->id)
            ->exists();

        if (! $alreadyUnlocked) {
            $subcategoryId = $vocabulary->vocab_subcategory_id;

            $alreadyUnlocked = AppUserCategoryUnlock::where('app_user_id', $user->id)
                ->where(function ($q) use ($vocabulary, $subcategoryId) {
                    $q->where(['unlockable_type' => VocabSubcategory::class, 'unlockable_id' => $subcategoryId])
                        ->orWhere(['unlockable_type' => VocabCategory::class, 'unlockable_id' => $vocabulary->subcategory->vocab_category_id]);
                })
                ->exists();
        }

        if ($alreadyUnlocked) {
            return response()->json(['message' => 'Already unlocked.', 'coins' => (int) $user->coins], 200);
        }

        if ($user->coins < $vocabulary->coin_price) {
            return response()->json(['message' => 'Insufficient coins.', 'coins' => (int) $user->coins], 402);
        }

        $this->unlocks->unlockWord($user, $vocabulary);

        return response()->json(['message' => 'Word unlocked successfully.', 'coins' => (int) $user->refresh()->coins], 200);
    }
}
