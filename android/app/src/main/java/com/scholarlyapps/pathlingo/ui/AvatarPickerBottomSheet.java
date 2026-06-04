package com.scholarlyapps.pathlingo.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.data.remote.dto.AvatarDto;
import com.scholarlyapps.pathlingo.data.remote.dto.CoinsBalanceResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.ListResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.WrappedResponse;
import com.scholarlyapps.pathlingo.databinding.DialogAvatarPickerBinding;
import com.scholarlyapps.pathlingo.ui.adapters.AvatarAdapter;
import com.scholarlyapps.pathlingo.ui.utils.ToastHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvatarPickerBottomSheet extends BottomSheetDialogFragment {

    public interface OnAvatarSelectedListener {
        void onAvatarSelected(AppUserDto updatedUser);
    }

    private DialogAvatarPickerBinding binding;
    private AvatarAdapter adapter;
    private OnAvatarSelectedListener listener;
    private int userCoins = 0;
    private AvatarDto selectedAvatar = null;

    public void setOnAvatarSelectedListener(OnAvatarSelectedListener listener) {
        this.listener = listener;
    }

    public void setUserCoins(int coins) {
        this.userCoins = coins;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(d -> {
            FrameLayout sheet = ((BottomSheetDialog) d).findViewById(
                    com.google.android.material.R.id.design_bottom_sheet);
            if (sheet != null) {
                sheet.setBackgroundResource(android.R.color.transparent);
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(sheet);
                behavior.setSkipCollapsed(true);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAvatarPickerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.txtCoinBalance.setText(userCoins + " coins");

        adapter = new AvatarAdapter(new ArrayList<>(), this::onAvatarClicked);
        binding.recyclerAvatars.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.recyclerAvatars.setAdapter(adapter);

        binding.btnClose.setOnClickListener(v -> dismiss());

        binding.btnChoose.setOnClickListener(v -> {
            if (selectedAvatar == null) return;
            if (selectedAvatar.isOwned || selectedAvatar.coinPrice == 0) {
                selectAvatar(selectedAvatar.id);
            } else {
                purchaseThenSelect(selectedAvatar);
            }
        });

        loadAvatars();
    }

    private void onAvatarClicked(AvatarDto avatar) {
        selectedAvatar = avatar;
        adapter.setSelected(avatar.id);
        binding.btnChoose.setEnabled(true);
    }

    private void loadAvatars() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerAvatars.setVisibility(View.GONE);
        binding.txtEmpty.setVisibility(View.GONE);

        ServiceLocator.api.getAvatars().enqueue(new Callback<ListResponse<AvatarDto>>() {
            @Override
            public void onResponse(@NonNull Call<ListResponse<AvatarDto>> call, @NonNull Response<ListResponse<AvatarDto>> response) {
                if (!isAdded()) return;
                binding.progressBar.setVisibility(View.GONE);

                List<AvatarDto> avatars = response.isSuccessful() && response.body() != null
                        ? response.body().getData()
                        : Collections.emptyList();

                if (avatars.isEmpty()) {
                    binding.txtEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerAvatars.setVisibility(View.VISIBLE);
                    adapter.setData(avatars);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListResponse<AvatarDto>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                binding.progressBar.setVisibility(View.GONE);
                binding.txtEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void purchaseThenSelect(AvatarDto avatar) {
        if (userCoins < avatar.coinPrice) {
            ToastHelper.error(requireActivity(), "Not enough coins. Need " + avatar.coinPrice + " coins.");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnChoose.setEnabled(false);

        ServiceLocator.api.purchaseAvatar(avatar.id).enqueue(new Callback<WrappedResponse<CoinsBalanceResponse>>() {
            @Override
            public void onResponse(@NonNull Call<WrappedResponse<CoinsBalanceResponse>> call, @NonNull Response<WrappedResponse<CoinsBalanceResponse>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    userCoins = response.body().data.coins;
                    binding.txtCoinBalance.setText(userCoins + " coins");
                    selectAvatar(avatar.id);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnChoose.setEnabled(true);
                    ToastHelper.error(requireActivity(), "Purchase failed. Please try again.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WrappedResponse<CoinsBalanceResponse>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                binding.progressBar.setVisibility(View.GONE);
                binding.btnChoose.setEnabled(true);
                ToastHelper.error(requireActivity(), "Network error. Please try again.");
            }
        });
    }

    private void selectAvatar(long avatarId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnChoose.setEnabled(false);

        Map<String, Long> body = new HashMap<>();
        body.put("avatar_id", avatarId);

        ServiceLocator.api.setActiveAvatar(body).enqueue(new Callback<WrappedResponse<AppUserDto>>() {
            @Override
            public void onResponse(@NonNull Call<WrappedResponse<AppUserDto>> call, @NonNull Response<WrappedResponse<AppUserDto>> response) {
                if (!isAdded()) return;
                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    if (listener != null) {
                        listener.onAvatarSelected(response.body().data);
                    }
                    dismiss();
                } else {
                    binding.btnChoose.setEnabled(true);
                    ToastHelper.error(requireActivity(), "Failed to set avatar.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WrappedResponse<AppUserDto>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                binding.progressBar.setVisibility(View.GONE);
                binding.btnChoose.setEnabled(true);
                ToastHelper.error(requireActivity(), "Network error. Please try again.");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
