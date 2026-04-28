<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('vocabularies', function (Blueprint $table) {
            $table->id();
            $table->foreignId('subcategory_id')->constrained('subcategories')->cascadeOnDelete();
            $table->string('word_jp');
            $table->string('audio_jp')->nullable();
            $table->text('sentence_jp')->nullable();
            $table->string('sentence_audio_jp')->nullable();
            $table->string('word_romaji');
            $table->text('sentence_romaji')->nullable();
            $table->string('word_en');
            $table->string('audio_en')->nullable();
            $table->text('sentence_en')->nullable();
            $table->string('sentence_audio_en')->nullable();
            $table->string('image_path')->nullable();
            $table->string('image_thumbnail_path')->nullable();
            $table->unsignedSmallInteger('sort_order')->default(99);
            $table->boolean('is_premium')->default(false);
            $table->boolean('is_approved')->default(true);
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('vocabularies');
    }
};
