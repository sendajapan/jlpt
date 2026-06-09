<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('vocab_words', function (Blueprint $table) {
            $table->enum('pos', [
                'noun', 'verb', 'i-adjective', 'na-adjective',
                'adverb', 'particle', 'conjunction', 'interjection',
                'pronoun', 'expression', 'counter', 'prefix', 'suffix',
            ])->nullable()->after('word_en');
        });
    }

    public function down(): void
    {
        Schema::table('vocab_words', function (Blueprint $table) {
            $table->dropColumn('pos');
        });
    }
};
