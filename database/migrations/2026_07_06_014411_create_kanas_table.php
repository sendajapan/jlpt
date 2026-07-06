<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('kanas', function (Blueprint $table) {
            $table->id();
            $table->enum('script', ['hiragana', 'katakana']);
            $table->string('kana', 8);
            $table->string('romaji', 8);
            $table->string('sound_url')->nullable();
            $table->enum('section', ['basic', 'dakuten', 'combo']);
            $table->unsignedInteger('position');
            $table->timestamps();
            $table->unique(['script', 'position']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('kanas');
    }
};
