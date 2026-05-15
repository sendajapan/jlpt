<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('vocab_words', function (Blueprint $table) {
            $table->foreignId('voice_id')->nullable()->after('image_thumbnail_bg')->constrained('voices')->nullOnDelete();
        });
    }

    public function down(): void
    {
        Schema::table('vocab_words', function (Blueprint $table) {
            $table->dropConstrainedForeignId('voice_id');
        });
    }
};
