<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('app_user_kana_learned', function (Blueprint $table) {
            $table->id();
            $table->foreignId('app_user_id')->constrained('app_users')->cascadeOnDelete();
            $table->foreignId('kana_id')->constrained('kanas')->cascadeOnDelete();
            $table->timestamps();
            $table->unique(['app_user_id', 'kana_id']);
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('app_user_kana_learned');
    }
};
