<?php

namespace App\Services;

use App\Models\AppUser;
use App\Models\AppUserWordRead;
use App\Models\Vocabulary;
use Illuminate\Support\Facades\DB;

class WordReadService
{
    public function __construct(private CoinService $coins)
    {
    }

    public function markRead(AppUser $user, Vocabulary $vocab): AppUserWordRead
    {
        return DB::transaction(function () use ($user, $vocab) {
            $existing = AppUserWordRead::where('app_user_id', $user->id)
                ->where('vocab_id', $vocab->id)
                ->first();

            if ($existing) {
                $existing->update([
                    'read_count' => $existing->read_count + 1,
                    'last_read_at' => now(),
                ]);

                return $existing;
            }

            $read = AppUserWordRead::create([
                'app_user_id' => $user->id,
                'vocab_id' => $vocab->id,
                'read_count' => 1,
                'last_read_at' => now(),
            ]);

            $reward = (int) config('services.app_rewards.coins_per_first_read', 1);
            if ($reward > 0) {
                $this->coins->award($user, $reward, 'first_read', $vocab);
            }

            return $read;
        });
    }
}
