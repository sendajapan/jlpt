<?php

namespace Database\Seeders;

use App\Models\Kanji;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class KanjiSeeder extends Seeder
{
    public function run(): void
    {
        $sqlPath = database_path('reference/kanji.sql');

        if (! file_exists($sqlPath)) {
            $this->command->error("Kanji SQL file not found at: {$sqlPath}");

            return;
        }

        $sql = file_get_contents($sqlPath);

        preg_match_all(
            "/\((\d+),\s*'([^']*)',\s*(\d+),\s*(\d+),\s*(\d+),\s*(?:'([^']*)'|NULL),\s*(?:'((?:[^'\\\\]|\\\\.)*)'|NULL),\s*(?:'((?:[^'\\\\]|\\\\.)*)'|NULL),\s*(?:'((?:[^'\\\\]|\\\\.)*)'|NULL),\s*(?:'((?:[^'\\\\]|\\\\.)*)'|NULL),\s*(?:(\d+)|NULL),\s*(?:'((?:[^'\\\\]|\\\\.)*)'|NULL),\s*(\d+),\s*(\d+)\)/",
            $sql,
            $matches,
            PREG_SET_ORDER
        );

        if (empty($matches)) {
            $this->command->error('No kanji records found in SQL file.');

            return;
        }

        DB::statement('SET FOREIGN_KEY_CHECKS=0');
        Kanji::truncate();

        $existingVocabIds = DB::table('vocab_words')->pluck('id')->flip()->all();
        $validJlpt = ['N5', 'N4', 'N3', 'N2', 'N1'];
        $chunks = array_chunk($matches, 500);

        foreach ($chunks as $chunk) {
            $rows = [];
            $now = now();

            foreach ($chunk as $m) {
                $jlpt = trim($m[6]);
                $rawVocabId = (int) $m[13];
                $vocabId = ($rawVocabId > 0 && isset($existingVocabIds[$rawVocabId])) ? $rawVocabId : null;

                $rows[] = [
                    'id' => (int) $m[1],
                    'kanji' => $m[2],
                    'strokes' => (int) $m[3],
                    'grade' => (int) $m[4] ?: null,
                    'freq' => (int) $m[5],
                    'jlpt' => in_array($jlpt, $validJlpt) ? $jlpt : null,
                    'translate' => $m[7] !== '' ? stripslashes($m[7]) : null,
                    'meanings' => $m[8] !== '' ? stripslashes($m[8]) : null,
                    'readings_on' => $m[9] !== '' ? stripslashes($m[9]) : null,
                    'readings_kun' => $m[10] !== '' ? stripslashes($m[10]) : null,
                    'level' => $m[11] !== '' ? (int) $m[11] : null,
                    'radicals' => $m[12] !== '' ? stripslashes($m[12]) : null,
                    'vocab_id' => $vocabId,
                    'is_premium' => (bool) $m[14],
                    'created_at' => $now,
                    'updated_at' => $now,
                ];
            }

            DB::table('kanjis')->insert($rows);
        }

        DB::statement('SET FOREIGN_KEY_CHECKS=1');

        $count = Kanji::count();
        $this->command->info("Seeded {$count} kanji records.");
    }
}
