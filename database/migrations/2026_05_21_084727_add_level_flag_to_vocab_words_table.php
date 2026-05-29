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
            $table->enum('level', ["N5", "N4", "N3", "N2", "N1", "N/A"])->default("N/A")->after('is_approved');
        });
    }

    public function down(): void
    {
        Schema::table('vocab_words', function (Blueprint $table) {
//            $table->dropColumn(['audio_jp_reviewed', 'audio_en_reviewed', 'sentence_audio_jp_reviewed', 'sentence_audio_en_reviewed']);
        });
    }
};
