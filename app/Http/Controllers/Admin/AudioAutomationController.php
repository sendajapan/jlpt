<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Jobs\GenerateNextVocabAudioJob;
use App\Models\AudioGenerationFailure;
use App\Models\AudioSetting;
use App\Services\AudioAutomationService;
use Illuminate\Http\RedirectResponse;
use Illuminate\View\View;

class AudioAutomationController extends Controller
{
    public function __construct(private AudioAutomationService $automation)
    {
    }

    public function index(): View
    {
        $setting = AudioSetting::current();
        $pending = $this->automation->pendingCounts();
        $pendingTotal = array_sum($pending);
        $failures = AudioGenerationFailure::with('vocabulary')
            ->orderByDesc('last_attempt_at')
            ->paginate(200);
        $next = $this->automation->findNextPending();

        return view('admin.audio-automation.index', compact(
            'setting',
            'pending',
            'pendingTotal',
            'failures',
            'next'
        ));
    }

    public function toggle(): RedirectResponse
    {
        $setting = AudioSetting::current();
        $setting->is_running = !$setting->is_running;
        $setting->save();

        notify()->success()
            ->title($setting->is_running ? 'Audio automation started.' : 'Audio automation stopped.')
            ->send();

        return back();
    }

    public function runNow(): RedirectResponse
    {
        GenerateNextVocabAudioJob::dispatch();

        notify()->success()->title('Job dispatched. Next audio will be generated shortly.')->send();

        return back();
    }

    public function resetFailure(AudioGenerationFailure $failure): RedirectResponse
    {
        $failure->delete();

        notify()->success()->title('Failure cleared. Row will be retried.')->send();

        return back();
    }
}
