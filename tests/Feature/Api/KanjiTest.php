<?php

use App\Models\AppUser;
use App\Models\Kanji;
use App\Models\VocabCategory;
use App\Models\VocabSubcategory;
use App\Models\Vocabulary;
use Illuminate\Foundation\Testing\RefreshDatabase;

uses(RefreshDatabase::class);

function kanjiAuthHeaders(AppUser $user): array
{
    return ['Authorization' => 'Bearer '.$user->createToken('test')->plainTextToken];
}

function createKanjiVocab(string $wordJp): Vocabulary
{
    $category = VocabCategory::create(['name_en' => 'Basics', 'name_jp' => '基本']);
    $subcategory = VocabSubcategory::create([
        'vocab_category_id' => $category->id,
        'name_en' => 'Numbers',
        'name_jp' => '数字',
    ]);

    return Vocabulary::create([
        'vocab_subcategory_id' => $subcategory->id,
        'word_jp' => $wordJp,
        'word_romaji' => 'test',
        'word_en' => 'test',
        'is_approved' => true,
    ]);
}

it('lists kanjis paginated with meta', function () {
    Kanji::factory()->count(3)->create();

    $response = $this->getJson('/api/v1/kanjis?per_page=2');

    $response->assertOk()->assertJsonCount(2, 'data');
    expect($response->json('meta.current_page'))->toBe(1)
        ->and($response->json('meta.last_page'))->toBe(2)
        ->and($response->json('meta.per_page'))->toBe(2)
        ->and($response->json('meta.total'))->toBe(3)
        ->and($response->json('data.0.is_learned'))->toBeFalse();
});

it('filters kanjis by jlpt level', function () {
    Kanji::factory()->create(['kanji' => '一', 'jlpt' => 'N5']);
    Kanji::factory()->create(['kanji' => '議', 'jlpt' => 'N1']);

    $response = $this->getJson('/api/v1/kanjis?jlpt=N5');

    $response->assertOk()->assertJsonCount(1, 'data');
    expect($response->json('data.0.kanji'))->toBe('一');
});

it('filters kanjis by strokes excluding kanjis without a jlpt level', function () {
    Kanji::factory()->create(['kanji' => '日', 'strokes' => 4, 'jlpt' => 'N5']);
    Kanji::factory()->create(['kanji' => '木', 'strokes' => 4, 'jlpt' => null]);
    Kanji::factory()->create(['kanji' => '一', 'strokes' => 1, 'jlpt' => 'N5']);

    $response = $this->getJson('/api/v1/kanjis?strokes=4');

    $response->assertOk()->assertJsonCount(1, 'data');
    expect($response->json('data.0.kanji'))->toBe('日');
});

it('rejects invalid kanji list filters', function () {
    $this->getJson('/api/v1/kanjis?jlpt=N6')->assertStatus(422);
    $this->getJson('/api/v1/kanjis?strokes=0')->assertStatus(422);
    $this->getJson('/api/v1/kanjis?per_page=500')->assertStatus(422);
});

it('shows a kanji with its linked vocab as example word', function () {
    $vocab = createKanjiVocab('一つ');
    $kanji = Kanji::factory()->create(['kanji' => '一', 'vocab_id' => $vocab->id]);

    $response = $this->getJson("/api/v1/kanjis/{$kanji->id}");

    $response->assertOk()->assertJsonCount(1, 'data.example_words');
    expect($response->json('data.kanji'))->toBe('一')
        ->and($response->json('data.example_words.0.word_jp'))->toBe('一つ')
        ->and($response->json('data.is_learned'))->toBeFalse();
});

it('shows a kanji without example words when no vocab is linked', function () {
    $kanji = Kanji::factory()->create();

    $response = $this->getJson("/api/v1/kanjis/{$kanji->id}");

    $response->assertOk()->assertJsonCount(0, 'data.example_words');
});

it('aggregates stroke counts over jlpt kanjis only ordered ascending', function () {
    Kanji::factory()->create(['strokes' => 4, 'jlpt' => 'N5']);
    Kanji::factory()->create(['strokes' => 4, 'jlpt' => 'N4']);
    Kanji::factory()->create(['strokes' => 1, 'jlpt' => 'N5']);
    Kanji::factory()->create(['strokes' => 4, 'jlpt' => null]);

    $response = $this->getJson('/api/v1/kanjis/strokes');

    $response->assertOk()->assertJsonCount(2, 'data');
    expect($response->json('data.0'))->toBe(['strokes' => 1, 'count' => 1])
        ->and($response->json('data.1'))->toBe(['strokes' => 4, 'count' => 2]);
});

it('marks a kanji learned and awards coins only on the first time', function () {
    $user = AppUser::factory()->create();
    $kanji = Kanji::factory()->create();
    $headers = kanjiAuthHeaders($user);

    $first = $this->postJson("/api/v1/kanjis/{$kanji->id}/learned", [], $headers);
    $first->assertCreated();
    expect($first->json('first_time'))->toBeTrue()
        ->and($first->json('coins'))->toBe(10);

    $second = $this->postJson("/api/v1/kanjis/{$kanji->id}/learned", [], $headers);
    $second->assertCreated();
    expect($second->json('first_time'))->toBeFalse()
        ->and($second->json('coins'))->toBe(10);
});

it('lists learned kanji ids for the authenticated user', function () {
    $user = AppUser::factory()->create();
    $kanji = Kanji::factory()->create();
    $headers = kanjiAuthHeaders($user);

    $this->postJson("/api/v1/kanjis/{$kanji->id}/learned", [], $headers);

    $response = $this->getJson('/api/v1/me/kanji-learned', $headers);
    $response->assertOk();
    expect($response->json('data'))->toBe([$kanji->id]);
});

it('flags learned kanjis in the list for the authenticated user', function () {
    $user = AppUser::factory()->create();
    $kanji = Kanji::factory()->create(['kanji' => '一']);
    Kanji::factory()->create(['kanji' => '二']);

    $user->learnedKanjis()->attach($kanji->id);

    $response = $this->getJson('/api/v1/kanjis', kanjiAuthHeaders($user));

    expect($response->json('data.0.is_learned'))->toBeTrue()
        ->and($response->json('data.1.is_learned'))->toBeFalse();
});

it('requires authentication for kanji learned endpoints', function () {
    $kanji = Kanji::factory()->create();

    $this->getJson('/api/v1/me/kanji-learned')->assertUnauthorized();
    $this->postJson("/api/v1/kanjis/{$kanji->id}/learned")->assertUnauthorized();
});
