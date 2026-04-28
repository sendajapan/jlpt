<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('vocabularies', function (Blueprint $table) {
            $table->renameColumn('meaning_en', 'word_en');
            $table->renameColumn('audio_path', 'audio_jp');
        });

        Schema::table('vocabularies', function (Blueprint $table) {
            $table->dropColumn(['example_sentence_jp', 'example_sentence_en', 'jlpt_level']);
        });

        Schema::table('vocabularies', function (Blueprint $table) {
            $table->string('audio_en')->nullable()->after('word_en');
            $table->text('sentence_en')->nullable()->after('audio_en');
            $table->string('sentence_audio_en')->nullable()->after('sentence_en');
            $table->text('sentence_jp')->nullable()->after('audio_jp');
            $table->string('sentence_audio_jp')->nullable()->after('sentence_jp');
            $table->text('sentence_romaji')->nullable()->after('word_romaji');
            $table->boolean('is_premium')->default(false)->after('sort_order');
            $table->boolean('is_approved')->default(true)->after('is_premium');
        });
    }

    public function down(): void
    {
        Schema::table('vocabularies', function (Blueprint $table) {
            $table->dropColumn(['audio_en', 'sentence_en', 'sentence_audio_en', 'sentence_jp', 'sentence_audio_jp', 'sentence_romaji', 'is_premium', 'is_approved']);
        });

        Schema::table('vocabularies', function (Blueprint $table) {
            $table->renameColumn('word_en', 'meaning_en');
            $table->renameColumn('audio_jp', 'audio_path');
            $table->text('example_sentence_jp')->nullable();
            $table->text('example_sentence_en')->nullable();
            $table->enum('jlpt_level', ['N5', 'N4', 'N3', 'N2', 'N1'])->nullable();
        });
    }
};
