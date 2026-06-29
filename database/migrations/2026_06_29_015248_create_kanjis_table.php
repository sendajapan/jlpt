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
        Schema::create('kanjis', function (Blueprint $table) {
            $table->id();
            $table->string('kanji', 20);
            $table->unsignedSmallInteger('strokes')->default(0);
            $table->unsignedTinyInteger('grade')->nullable();
            $table->unsignedInteger('freq')->default(0);
            $table->enum('jlpt', ['N5', 'N4', 'N3', 'N2', 'N1'])->nullable();
            $table->string('translate')->nullable();
            $table->string('meanings')->nullable();
            $table->string('readings_on')->nullable();
            $table->string('readings_kun')->nullable();
            $table->unsignedTinyInteger('level')->nullable();
            $table->string('radicals')->nullable();
            $table->foreignId('vocab_id')->nullable()->constrained('vocab_words')->nullOnDelete();
            $table->boolean('is_premium')->default(false);
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('kanjis');
    }
};
