<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('audio_generation_failures', function (Blueprint $table) {
            $table->id();
            $table->foreignId('vocab_id')->constrained('vocab_words')->cascadeOnDelete();
            $table->string('field');
            $table->unsignedSmallInteger('attempts')->default(0);
            $table->text('last_error')->nullable();
            $table->timestamp('last_attempt_at')->nullable();
            $table->timestamps();
            $table->unique(['vocab_id', 'field']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('audio_generation_failures');
    }
};
