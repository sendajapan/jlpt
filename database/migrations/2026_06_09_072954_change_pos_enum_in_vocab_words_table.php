<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Support\Facades\DB;

return new class extends Migration
{
    public function up(): void
    {
        DB::statement("ALTER TABLE vocab_words MODIFY COLUMN pos ENUM('noun','pronoun','adjective','verb','adverb','preposition','conjunction','interjection') NULL");
    }

    public function down(): void
    {
        DB::statement("ALTER TABLE vocab_words MODIFY COLUMN pos ENUM('noun','verb','i-adjective','na-adjective','adverb','particle','conjunction','interjection','pronoun','expression','counter','prefix','suffix') NULL");
    }
};
