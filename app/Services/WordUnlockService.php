<?php

namespace App\Services;

use App\Models\AppUser;
use App\Models\AppUserCategoryUnlock;
use App\Models\AppUserWordUnlock;
use App\Models\VocabCategory;
use App\Models\VocabSubcategory;
use App\Models\Vocabulary;
use Illuminate\Support\Collection;
use Illuminate\Support\Facades\DB;

class WordUnlockService
{
    public function __construct(private CoinService $coins) {}

    public function seedFreeWordsForUser(AppUser $user): void
    {
        $freeVocabIds = $this->freeVocabIds();

        if ($freeVocabIds->isEmpty()) {
            return;
        }

        $now = now()->toDateTimeString();

        $rows = $freeVocabIds->map(fn (int $id) => [
            'app_user_id' => $user->id,
            'vocab_id' => $id,
            'unlock_type' => 'free',
            'coins_spent' => 0,
            'created_at' => $now,
            'updated_at' => $now,
        ])->values()->all();

        foreach (array_chunk($rows, 500) as $chunk) {
            DB::table('app_user_word_unlocks')->insertOrIgnore($chunk);
        }
    }

    public function unlockWord(AppUser $user, Vocabulary $vocab): AppUserWordUnlock
    {
        $existing = AppUserWordUnlock::where('app_user_id', $user->id)
            ->where('vocab_id', $vocab->id)
            ->first();

        if ($existing) {
            return $existing;
        }

        return DB::transaction(function () use ($user, $vocab) {
            $this->coins->spend($user, $vocab->coin_price, 'word_unlock', $vocab);

            return AppUserWordUnlock::create([
                'app_user_id' => $user->id,
                'vocab_id' => $vocab->id,
                'unlock_type' => 'purchased',
                'coins_spent' => $vocab->coin_price,
            ]);
        });
    }

    public function unlockCategory(AppUser $user, VocabCategory $category): AppUserCategoryUnlock
    {
        $existing = AppUserCategoryUnlock::where('app_user_id', $user->id)
            ->where('unlockable_type', VocabCategory::class)
            ->where('unlockable_id', $category->id)
            ->first();

        if ($existing) {
            return $existing;
        }

        return DB::transaction(function () use ($user, $category) {
            $this->coins->spend($user, $category->coin_price, 'category_unlock', $category);

            $unlock = AppUserCategoryUnlock::create([
                'app_user_id' => $user->id,
                'unlockable_type' => VocabCategory::class,
                'unlockable_id' => $category->id,
                'coins_spent' => $category->coin_price,
            ]);

            $this->bulkInsertCategoryWordUnlocks($user, $category->id);

            return $unlock;
        });
    }

    public function unlockSubcategory(AppUser $user, VocabSubcategory $subcategory): AppUserCategoryUnlock
    {
        $existing = AppUserCategoryUnlock::where('app_user_id', $user->id)
            ->where('unlockable_type', VocabSubcategory::class)
            ->where('unlockable_id', $subcategory->id)
            ->first();

        if ($existing) {
            return $existing;
        }

        return DB::transaction(function () use ($user, $subcategory) {
            $this->coins->spend($user, $subcategory->coin_price, 'subcategory_unlock', $subcategory);

            $unlock = AppUserCategoryUnlock::create([
                'app_user_id' => $user->id,
                'unlockable_type' => VocabSubcategory::class,
                'unlockable_id' => $subcategory->id,
                'coins_spent' => $subcategory->coin_price,
            ]);

            $this->bulkInsertSubcategoryWordUnlocks($user, $subcategory->id);

            return $unlock;
        });
    }

    private function bulkInsertCategoryWordUnlocks(AppUser $user, int $categoryId): void
    {
        $vocabIds = DB::table('vocab_words')
            ->join('vocab_subcategories', 'vocab_words.vocab_subcategory_id', '=', 'vocab_subcategories.id')
            ->where('vocab_subcategories.vocab_category_id', $categoryId)
            ->where('vocab_words.is_approved', true)
            ->pluck('vocab_words.id');

        $this->bulkInsertWordUnlocks($user, $vocabIds, 'category');
    }

    private function bulkInsertSubcategoryWordUnlocks(AppUser $user, int $subcategoryId): void
    {
        $vocabIds = DB::table('vocab_words')
            ->where('vocab_subcategory_id', $subcategoryId)
            ->where('is_approved', true)
            ->pluck('id');

        $this->bulkInsertWordUnlocks($user, $vocabIds, 'category');
    }

    private function bulkInsertWordUnlocks(AppUser $user, Collection $vocabIds, string $unlockType): void
    {
        if ($vocabIds->isEmpty()) {
            return;
        }

        $now = now()->toDateTimeString();

        $rows = $vocabIds->map(fn (int $id) => [
            'app_user_id' => $user->id,
            'vocab_id' => $id,
            'unlock_type' => $unlockType,
            'coins_spent' => 0,
            'created_at' => $now,
            'updated_at' => $now,
        ])->values()->all();

        foreach (array_chunk($rows, 500) as $chunk) {
            DB::table('app_user_word_unlocks')->insertOrIgnore($chunk);
        }
    }

    private function freeVocabIds(): Collection
    {
        return DB::table('vocab_words')
            ->join('vocab_subcategories', 'vocab_words.vocab_subcategory_id', '=', 'vocab_subcategories.id')
            ->join('vocab_categories', 'vocab_subcategories.vocab_category_id', '=', 'vocab_categories.id')
            ->where('vocab_words.is_premium', false)
            ->where('vocab_subcategories.is_premium', false)
            ->where('vocab_categories.is_premium', false)
            ->where('vocab_words.is_approved', true)
            ->pluck('vocab_words.id');
    }
}
