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
        Schema::create('app_user_word_unlocks', function (Blueprint $table) {
            $table->id();
            $table->foreignId('app_user_id')->constrained('app_users')->cascadeOnDelete();
            $table->foreignId('vocab_id')->constrained('vocab_words')->cascadeOnDelete();
            $table->enum('unlock_type', ['free', 'purchased', 'category'])->default('free');
            $table->unsignedSmallInteger('coins_spent')->default(0);
            $table->timestamps();
            $table->unique(['app_user_id', 'vocab_id']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('app_user_word_unlocks');
    }
};
