<?php

namespace App\Services;

use App\Models\AppUser;
use App\Models\Kana;
use App\Models\Vocabulary;
use Illuminate\Database\Eloquent\Collection;
use Illuminate\Support\Facades\DB;

class KanaService
{
    public function __construct(private CoinService $coins) {}

    public function getByScript(string $script): Collection
    {
        return Kana::where('script', $script)->orderBy('position')->get();
    }

    public function exampleWords(Kana $kana, int $limit = 5): Collection
    {
        return Vocabulary::where('is_approved', true)
            ->where('word_jp', 'like', $kana->kana.'%')
            ->orderBy('sort_order')
            ->orderBy('word_jp')
            ->limit($limit)
            ->get();
    }

    public function learnedKanaIds(AppUser $user): array
    {
        return $user->learnedKanas()->pluck('kanas.id')->all();
    }

    public function markLearned(AppUser $user, Kana $kana): bool
    {
        return DB::transaction(function () use ($user, $kana) {
            $result = $user->learnedKanas()->syncWithoutDetaching([$kana->id]);
            $firstTime = count($result['attached']) > 0;

            $reward = (int) config('services.app_rewards.coins_per_kana_learned', 10);
            if ($firstTime && $reward > 0) {
                $this->coins->award($user, $reward, 'kana_learned', $kana);
            }

            return $firstTime;
        });
    }
}
