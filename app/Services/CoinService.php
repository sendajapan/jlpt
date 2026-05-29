<?php

namespace App\Services;

use App\Models\AppUser;
use App\Models\CoinTransaction;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\DB;

class CoinService
{
    public function award(AppUser $user, int $amount, string $reason, ?Model $reference = null): CoinTransaction
    {
        return $this->record($user, abs($amount), $reason, $reference);
    }

    public function spend(AppUser $user, int $amount, string $reason, ?Model $reference = null): CoinTransaction
    {
        return $this->record($user, -abs($amount), $reason, $reference);
    }

    private function record(AppUser $user, int $signedAmount, string $reason, ?Model $reference): CoinTransaction
    {
        return DB::transaction(function () use ($user, $signedAmount, $reason, $reference) {
            $user->newQuery()->whereKey($user->id)->increment('coins', $signedAmount);
            $user->refresh();

            return $user->coinTransactions()->create([
                'amount' => $signedAmount,
                'reason' => $reason,
                'reference_type' => $reference?->getMorphClass(),
                'reference_id' => $reference?->getKey(),
            ]);
        });
    }
}
