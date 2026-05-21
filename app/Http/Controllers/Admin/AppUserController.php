<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\UpdateAppUserRequest;
use App\Models\AppUser;
use App\Services\AppUserService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class AppUserController extends Controller
{
    public function __construct(private AppUserService $service) {}

    public function index(Request $request): View
    {
        $users = $this->service->getAll(
            $request->input('search'),
            $request->input('status'),
            $request->input('provider'),
        );

        return view('admin.app-users.index', compact('users'));
    }

    public function edit(AppUser $appUser): View
    {
        return view('admin.app-users.edit', ['user' => $appUser]);
    }

    public function update(UpdateAppUserRequest $request, AppUser $appUser): RedirectResponse
    {
        $this->service->update($appUser, $request->validated());

        notify()->success()->title('User updated successfully.')->send();

        return redirect()->route('admin.app-users.index');
    }
}
