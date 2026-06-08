<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Models\AppUserCategoryUnlock;
use App\Models\VocabCategory;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use OpenApi\Attributes as OA;

class CategoryController extends Controller
{
    #[OA\Get(
        path: '/api/v1/categories',
        summary: 'List all vocab categories',
        tags: ['Public'],
        responses: [
            new OA\Response(
                response: 200,
                description: 'List of vocab categories ordered by sort_order then name_en',
                content: new OA\JsonContent(
                    properties: [
                        new OA\Property(
                            property: 'data',
                            type: 'array',
                            items: new OA\Items(ref: '#/components/schemas/VocabCategory')
                        ),
                    ]
                )
            ),
        ]
    )]
    public function index(Request $request): JsonResponse
    {
        $user = $request->user('app_users');

        $unlockedCategoryIds = $user
            ? AppUserCategoryUnlock::where('app_user_id', $user->id)
                ->where('unlockable_type', VocabCategory::class)
                ->pluck('unlockable_id')
                ->flip()
            : collect();

        $categories = VocabCategory::query()
            ->with('background')
            ->orderBy('sort_order')
            ->orderBy('name_en')
            ->get()
            ->map(fn (VocabCategory $c) => [
                'id' => $c->id,
                'name_en' => $c->name_en,
                'name_jp' => $c->name_jp,
                'name_romaji' => $c->name_romaji,
                'icon_url' => $this->url($c->icon_path),
                'icon_thumbnail_url' => $this->url($c->icon_thumbnail_path),
                'category_bg_path' => $c->category_bg_path,
                'bg_url' => $c->background ? asset($c->background->vocab_bg_path) : null,
                'audio_url' => $this->url($c->audio_path),
                'sort_order' => $c->sort_order,
                'is_premium' => $c->is_premium,
                'coin_price' => $c->coin_price,
                'is_locked' => $c->is_premium && ! $unlockedCategoryIds->has($c->id),
            ]);

        return response()->json(['data' => $categories]);
    }

    private function url(?string $path): ?string
    {
        return $path ? Storage::disk('public')->url($path) : null;
    }
}
