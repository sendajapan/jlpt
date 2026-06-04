<?php

namespace App\Http\Controllers\Admin;

use App\Http\Controllers\Controller;
use App\Http\Requests\Admin\StoreAvatarRequest;
use App\Http\Requests\Admin\UpdateAvatarRequest;
use App\Models\Avatar;
use App\Services\AvatarService;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\View\View;

class AvatarController extends Controller
{
    public function __construct(private AvatarService $service) {}

    public function index(Request $request): View
    {
        $avatars = $this->service->all($request->input('search'));

        return view('admin.avatars.index', compact('avatars'));
    }

    public function create(): View
    {
        return view('admin.avatars.create');
    }

    public function store(StoreAvatarRequest $request): RedirectResponse
    {
        $data = $request->validated();
        $data['image_path'] = $this->saveAvatarImage($request->file('image_path'), 'avatars');
        $data['is_active'] = $request->boolean('is_active', true);
        $data['sort_order'] = $request->input('sort_order', 99);

        $this->service->create($data);

        notify()->success()->title('Avatar created successfully.')->send();

        return redirect()->route('admin.avatars.index');
    }

    public function edit(Avatar $avatar): View
    {
        return view('admin.avatars.edit', compact('avatar'));
    }

    public function update(UpdateAvatarRequest $request, Avatar $avatar): RedirectResponse
    {
        $data = $request->validated();
        $data['image_path'] = $this->replaceAvatarImage($request, 'image_path', 'avatars', $avatar->image_path);
        $data['is_active'] = $request->boolean('is_active', true);
        $data['sort_order'] = $request->input('sort_order', 99);

        $this->service->update($avatar, $data);

        notify()->success()->title('Avatar updated successfully.')->send();

        return redirect()->route('admin.avatars.index');
    }

    public function destroy(Avatar $avatar): RedirectResponse
    {
        $this->deleteFile($avatar->image_path);
        $this->service->delete($avatar);

        notify()->success()->title('Avatar deleted.')->send();

        return redirect()->route('admin.avatars.index');
    }
}
