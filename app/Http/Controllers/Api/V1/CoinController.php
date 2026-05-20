<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Http\Resources\CoinTransactionResource;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class CoinController extends Controller
{
    public function balance(Request $request): JsonResponse
    {
        return response()->json(['coins' => (int) $request->user()->coins]);
    }

    public function transactions(Request $request): JsonResponse
    {
        $transactions = $request->user()
            ->coinTransactions()
            ->orderByDesc('id')
            ->paginate(20);

        return CoinTransactionResource::collection($transactions)->response();
    }
}
