<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::table('vocab_words', function (Blueprint $table) {
            $table->boolean('audio_jp_reviewed')->default(false)->after('audio_jp');
            $table->boolean('audio_en_reviewed')->default(false)->after('audio_en');
            $table->boolean('sentence_audio_jp_reviewed')->default(false)->after('sentence_audio_jp');
            $table->boolean('sentence_audio_en_reviewed')->default(false)->after('sentence_audio_en');
        });
    }

    public function down(): void
    {
        Schema::table('vocab_words', function (Blueprint $table) {
            $table->dropColumn(['audio_jp_reviewed', 'audio_en_reviewed', 'sentence_audio_jp_reviewed', 'sentence_audio_en_reviewed']);
        });
    }
};
