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
        Schema::create('app_user_category_unlocks', function (Blueprint $table) {
            $table->id();
            $table->foreignId('app_user_id')->constrained('app_users')->cascadeOnDelete();
            $table->morphs('unlockable');
            $table->unsignedSmallInteger('coins_spent')->default(0);
            $table->timestamps();
            $table->unique(['app_user_id', 'unlockable_type', 'unlockable_id'], 'auco_user_type_id_unique');
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('app_user_category_unlocks');
    }
};
