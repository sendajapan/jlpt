<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Http\Resources\AppUserResource;
use App\Models\Avatar;
use App\Models\AvatarPurchase;
use App\Services\AvatarService;
use App\Services\CoinService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;

class AvatarController extends Controller
{
    public function __construct(
        private AvatarService $avatars,
        private CoinService $coins,
    ) {}

    public function index(Request $request): JsonResponse
    {
        $user = $request->user('app_users');
        $ownedIds = [];
        $activeId = null;

        if ($user) {
            $ownedIds = $user->avatarPurchases()->pluck('avatar_id')->toArray();
            $activeId = $user->active_avatar_id;
        }

        $avatars = $this->avatars->allActive()->map(function (Avatar $avatar) use ($user, $ownedIds, $activeId) {
            $isFree = $avatar->coin_price === 0;
            $isOwned = $isFree || in_array($avatar->id, $ownedIds);

            return [
                'id' => $avatar->id,
                'name' => $avatar->name,
                'image_url' => Storage::disk('public')->url($avatar->image_path),
                'coin_price' => $avatar->coin_price,
                'is_owned' => $user ? $isOwned : $isFree,
                'is_active' => $avatar->id === $activeId,
            ];
        });

        return response()->json(['data' => $avatars]);
    }

    public function purchase(Request $request, Avatar $avatar): JsonResponse
    {
        $user = $request->user();

        if (! $avatar->is_active) {
            return response()->json(['message' => 'Avatar is not available.'], 404);
        }

        $alreadyOwned = $avatar->coin_price === 0 ||
            AvatarPurchase::where('app_user_id', $user->id)->where('avatar_id', $avatar->id)->exists();

        if ($alreadyOwned) {
            return response()->json(['message' => 'Already owned.', 'coins' => (int) $user->coins], 200);
        }

        if ($user->coins < $avatar->coin_price) {
            return response()->json(['message' => 'Insufficient coins.', 'coins' => (int) $user->coins], 402);
        }

        DB::transaction(function () use ($user, $avatar) {
            $this->coins->spend($user, $avatar->coin_price, 'avatar_purchase', $avatar);
            AvatarPurchase::create(['app_user_id' => $user->id, 'avatar_id' => $avatar->id]);
        });

        return response()->json(['message' => 'Purchased successfully.', 'coins' => (int) $user->refresh()->coins], 200);
    }

    public function setActive(Request $request): AppUserResource
    {
        $request->validate(['avatar_id' => ['required', 'integer', 'exists:avatars,id']]);

        $user = $request->user();
        $avatar = Avatar::findOrFail($request->integer('avatar_id'));

        $isFree = $avatar->coin_price === 0;
        $isOwned = $isFree || AvatarPurchase::where('app_user_id', $user->id)->where('avatar_id', $avatar->id)->exists();

        if (! $isOwned) {
            return response()->json(['message' => 'You do not own this avatar.'], 403);
        }

        $user->update(['active_avatar_id' => $avatar->id]);

        return new AppUserResource($user->loadCount(['favorites', 'reads']));
    }
}
