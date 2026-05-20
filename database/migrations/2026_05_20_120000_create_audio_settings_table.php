<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('audio_settings', function (Blueprint $table) {
            $table->id();
            $table->boolean('is_running')->default(false);
            $table->unsignedBigInteger('generated_count')->default(0);
            $table->timestamp('last_run_at')->nullable();
            $table->text('last_error')->nullable();
            $table->timestamps();
        });

        DB::table('audio_settings')->insert([
            'id' => 1,
            'is_running' => false,
            'generated_count' => 0,
            'created_at' => now(),
            'updated_at' => now(),
        ]);
    }

    public function down(): void
    {
        Schema::dropIfExists('audio_settings');
    }
};
