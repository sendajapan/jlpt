<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('vocab_categories', function (Blueprint $table) {
            $table->id();
            $table->string('name_en');
            $table->string('name_jp');
            $table->string('name_romaji')->nullable();
            $table->string('icon_path')->nullable();
            $table->string('icon_thumbnail_path')->nullable();
            $table->unsignedSmallInteger('category_bg_path')->default(99);
            $table->string('audio_path')->nullable();
            $table->unsignedSmallInteger('sort_order')->default(99);
            $table->boolean('is_premium')->default(false);
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('vocab_categories');
    }
};
