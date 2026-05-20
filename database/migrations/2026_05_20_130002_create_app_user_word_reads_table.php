<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('app_user_word_reads', function (Blueprint $table) {
            $table->id();
            $table->foreignId('app_user_id')->constrained('app_users')->cascadeOnDelete();
            $table->foreignId('vocab_id')->constrained('vocab_words')->cascadeOnDelete();
            $table->unsignedInteger('read_count')->default(1);
            $table->timestamp('last_read_at')->nullable();
            $table->timestamps();
            $table->unique(['app_user_id', 'vocab_id']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('app_user_word_reads');
    }
};
