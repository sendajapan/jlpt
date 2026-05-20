<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class AppUserResource extends JsonResource
{
    public function toArray($request): array
    {
        return [
            'id' => $this->id,
            'name' => $this->full_name,
            'username' => $this->username,
            'email' => $this->email,
            'email_verified' => (bool) $this->email_verified_at,
            'avatar' => $this->avatar ? asset('storage/'.$this->avatar) : null,
            'login_provider' => $this->login_provider,
            'coins' => (int) $this->coins,
            'created_at' => $this->created_at?->toIso8601String(),
        ];
    }
}
