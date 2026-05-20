<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('app_users', function (Blueprint $table) {
            if (! Schema::hasColumn('app_users', 'google_id')) {
                $table->string('google_id')->nullable()->unique()->after('login_provider');
            }
            if (! Schema::hasColumn('app_users', 'facebook_id')) {
                $table->string('facebook_id')->nullable()->unique()->after('google_id');
            }
        });

        if (! Schema::hasTable('app_users_password_reset_tokens')) {
            Schema::create('app_users_password_reset_tokens', function (Blueprint $table) {
                $table->string('email')->primary();
                $table->string('token');
                $table->timestamp('created_at')->nullable();
            });
        }
    }

    public function down(): void
    {
        Schema::dropIfExists('app_users_password_reset_tokens');

        Schema::table('app_users', function (Blueprint $table) {
            if (Schema::hasColumn('app_users', 'facebook_id')) {
                $table->dropUnique(['facebook_id']);
                $table->dropColumn('facebook_id');
            }
            if (Schema::hasColumn('app_users', 'google_id')) {
                $table->dropUnique(['google_id']);
                $table->dropColumn('google_id');
            }
        });
    }
};
