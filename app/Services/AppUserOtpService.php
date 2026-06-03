<?php

namespace App\Services;

use App\Mail\OtpMail;
use App\Models\AppUser;
use App\Models\AppUserOtp;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Mail;

class AppUserOtpService
{
    public function generateAndSend(AppUser $user): void
    {
        AppUserOtp::where('email', $user->email)->delete();

        $code = str_pad((string) random_int(0, 9999), 4, '0', STR_PAD_LEFT);

        AppUserOtp::create([
            'email' => $user->email,
            'code' => Hash::make($code),
            'expires_at' => now()->addMinutes(10),
        ]);

        Mail::to($user->email)->send(new OtpMail($user->full_name, $code));
    }

    public function verify(AppUser $user, string $code): bool
    {
        $otp = AppUserOtp::where('email', $user->email)
            ->where('expires_at', '>', now())
            ->latest()
            ->first();

        if (! $otp || ! Hash::check($code, $otp->code)) {
            return false;
        }

        $otp->delete();

        return true;
    }
}
