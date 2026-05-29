<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Http\Resources\CoinTransactionResource;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use OpenApi\Attributes as OA;

class CoinController extends Controller
{
    #[OA\Get(
        path: '/api/v1/me/coins',
        tags: ['Coins'],
        summary: 'Get current coin balance',
        security: [['sanctum' => []]],
        responses: [
            new OA\Response(
                response: 200,
                description: 'OK',
                content: new OA\JsonContent(properties: [new OA\Property(property: 'coins', type: 'integer')])
            ),
        ]
    )]
    public function balance(Request $request): JsonResponse
    {
        return response()->json(['coins' => (int) $request->user()->coins]);
    }

    #[OA\Get(
        path: '/api/v1/me/coins/transactions',
        tags: ['Coins'],
        summary: 'Paginated coin transaction ledger',
        security: [['sanctum' => []]],
        responses: [
            new OA\Response(
                response: 200,
                description: 'OK',
                content: new OA\JsonContent(
                    properties: [
                        new OA\Property(
                            property: 'data',
                            type: 'array',
                            items: new OA\Items(ref: '#/components/schemas/CoinTransaction')
                        ),
                    ]
                )
            ),
        ]
    )]
    public function transactions(Request $request): JsonResponse
    {
        $transactions = $request->user()
            ->coinTransactions()
            ->orderByDesc('id')
            ->paginate(20);

        return CoinTransactionResource::collection($transactions)->response();
    }
}
