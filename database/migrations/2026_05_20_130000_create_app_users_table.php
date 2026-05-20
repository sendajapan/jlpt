<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        if (! Schema::hasTable('app_users')) {
            Schema::create('app_users', function (Blueprint $table) {
                $table->id();
                $table->string('username', 50)->nullable()->unique();
                $table->string('full_name', 100)->nullable();
                $table->string('email', 150)->nullable()->unique();
                $table->string('password')->nullable();
                $table->enum('login_provider', ['email', 'google', 'apple', 'facebook', 'guest'])->default('email');
                $table->string('google_id')->nullable()->unique();
                $table->string('facebook_id')->nullable()->unique();
                $table->string('avatar')->nullable();
                $table->text('bio')->nullable();
                $table->enum('gender', ['male', 'female', 'other', 'prefer_not_to_say'])->nullable();
                $table->date('birth_date')->nullable();
                $table->string('native_language_code', 10);
                $table->string('learning_language_code', 10);
                $table->integer('current_level')->default(1);
                $table->bigInteger('xp_points')->default(0);
                $table->float('coins')->default(0);
                $table->integer('current_streak')->default(0);
                $table->integer('longest_streak')->default(0);
                $table->timestamp('last_streak_at')->nullable();
                $table->integer('learned_words')->default(0);
                $table->integer('completed_lessons')->default(0);
                $table->integer('total_study_minutes')->default(0);
                $table->enum('proficiency_level', ['beginner', 'elementary', 'intermediate', 'upper_intermediate', 'advanced', 'native_like'])->default('beginner');
                $table->enum('learning_goal', ['travel', 'school', 'business', 'anime', 'conversation', 'exam', 'hobby', 'other'])->default('conversation');
                $table->integer('daily_goal_minutes')->default(15);
                $table->string('timezone', 100)->nullable();
                $table->boolean('notifications_enabled')->default(true);
                $table->time('reminder_time')->nullable();
                $table->boolean('is_verified')->default(false);
                $table->boolean('is_banned')->default(false);
                $table->text('banned_reason')->nullable();
                $table->timestamp('last_seen_at')->nullable();
                $table->timestamp('last_lesson_at')->nullable();
                $table->timestamp('email_verified_at')->nullable();
                $table->rememberToken();
                $table->timestamp('created_at')->useCurrent();
                $table->timestamp('updated_at')->useCurrent()->useCurrentOnUpdate();
            });
        } else {
            Schema::table('app_users', function (Blueprint $table) {
                if (! Schema::hasColumn('app_users', 'google_id')) {
                    $table->string('google_id')->nullable()->unique()->after('login_provider');
                }
                if (! Schema::hasColumn('app_users', 'facebook_id')) {
                    $table->string('facebook_id')->nullable()->unique()->after('google_id');
                }
            });
        }

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
        Schema::dropIfExists('app_users');
    }
};
