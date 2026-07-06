<?php

use App\Models\AppUser;
use App\Models\Kana;
use App\Models\VocabCategory;
use App\Models\VocabSubcategory;
use App\Models\Vocabulary;
use Illuminate\Foundation\Testing\RefreshDatabase;

uses(RefreshDatabase::class);

function createVocab(string $wordJp, bool $approved = true): Vocabulary
{
    $category = VocabCategory::create(['name_en' => 'Basics', 'name_jp' => '基本']);
    $subcategory = VocabSubcategory::create([
        'vocab_category_id' => $category->id,
        'name_en' => 'Greetings',
        'name_jp' => '挨拶',
    ]);

    return Vocabulary::create([
        'vocab_subcategory_id' => $subcategory->id,
        'word_jp' => $wordJp,
        'word_romaji' => 'test',
        'word_en' => 'test',
        'is_approved' => $approved,
    ]);
}

function authHeaders(AppUser $user): array
{
    return ['Authorization' => 'Bearer '.$user->createToken('test')->plainTextToken];
}

it('lists kanas filtered by script ordered by position', function () {
    Kana::factory()->create(['kana' => 'い', 'romaji' => 'i', 'position' => 2]);
    Kana::factory()->create(['kana' => 'あ', 'romaji' => 'a', 'position' => 1]);
    Kana::factory()->katakana()->create(['romaji' => 'a', 'position' => 1]);

    $response = $this->getJson('/api/v1/kanas?script=hiragana');

    $response->assertOk()->assertJsonCount(2, 'data');
    expect($response->json('data.0.kana'))->toBe('あ')
        ->and($response->json('data.1.kana'))->toBe('い')
        ->and($response->json('data.0.is_learned'))->toBeFalse();
});

it('rejects a kana list request without a valid script', function () {
    $this->getJson('/api/v1/kanas')->assertStatus(422);
    $this->getJson('/api/v1/kanas?script=kanji')->assertStatus(422);
});

it('shows a kana with up to five approved example words matching its prefix', function () {
    $kana = Kana::factory()->create(['kana' => 'あ', 'romaji' => 'a', 'position' => 1]);

    foreach (['あい', 'あお', 'あさ', 'あき', 'あめ', 'あか'] as $word) {
        createVocab($word);
    }
    createVocab('いぬ');
    createVocab('あみ', approved: false);

    $response = $this->getJson("/api/v1/kanas/{$kana->id}");

    $response->assertOk()->assertJsonCount(5, 'data.example_words');
    foreach ($response->json('data.example_words') as $word) {
        expect($word['word_jp'])->toStartWith('あ');
    }
});

it('marks a kana learned and awards coins only on the first time', function () {
    $user = AppUser::factory()->create();
    $kana = Kana::factory()->create();
    $headers = authHeaders($user);

    $first = $this->postJson("/api/v1/kanas/{$kana->id}/learned", [], $headers);
    $first->assertCreated();
    expect($first->json('first_time'))->toBeTrue()
        ->and($first->json('coins'))->toBe(10);

    $second = $this->postJson("/api/v1/kanas/{$kana->id}/learned", [], $headers);
    $second->assertCreated();
    expect($second->json('first_time'))->toBeFalse()
        ->and($second->json('coins'))->toBe(10);
});

it('lists learned kana ids for the authenticated user', function () {
    $user = AppUser::factory()->create();
    $kana = Kana::factory()->create();
    $headers = authHeaders($user);

    $this->postJson("/api/v1/kanas/{$kana->id}/learned", [], $headers);

    $response = $this->getJson('/api/v1/me/kana-learned', $headers);
    $response->assertOk();
    expect($response->json('data'))->toBe([$kana->id]);
});

it('flags learned kanas in the list for the authenticated user', function () {
    $user = AppUser::factory()->create();
    $kana = Kana::factory()->create(['position' => 1]);
    Kana::factory()->create(['position' => 2]);

    $user->learnedKanas()->attach($kana->id);

    $response = $this->getJson('/api/v1/kanas?script=hiragana', authHeaders($user));

    expect($response->json('data.0.is_learned'))->toBeTrue()
        ->and($response->json('data.1.is_learned'))->toBeFalse();
});

it('requires authentication for learned endpoints', function () {
    $kana = Kana::factory()->create();

    $this->getJson('/api/v1/me/kana-learned')->assertUnauthorized();
    $this->postJson("/api/v1/kanas/{$kana->id}/learned")->assertUnauthorized();
});
