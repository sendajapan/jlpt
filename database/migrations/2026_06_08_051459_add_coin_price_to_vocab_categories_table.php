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
        Schema::table('vocab_categories', function (Blueprint $table) {
            $table->unsignedSmallInteger('coin_price')->default(500)->after('is_premium');
        });
    }

    public function down(): void
    {
        Schema::table('vocab_categories', function (Blueprint $table) {
            $table->dropColumn('coin_price');
        });
    }
};
