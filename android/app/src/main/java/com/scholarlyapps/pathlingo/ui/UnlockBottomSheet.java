package com.scholarlyapps.pathlingo.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.MessageResponse;
import com.scholarlyapps.pathlingo.databinding.DialogUnlockBinding;
import com.scholarlyapps.pathlingo.ui.utils.ToastHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnlockBottomSheet extends BottomSheetDialogFragment {

    public enum Type { WORD, SUBCATEGORY, CATEGORY }

    public interface OnUnlockListener {
        void onUnlocked();
    }

    private DialogUnlockBinding binding;
    private Type type;
    private long itemId;
    private String itemName;
    private int coinPrice;
    private int userCoins;
    private OnUnlockListener listener;

    public static UnlockBottomSheet create(Type type, long itemId, String itemName, int coinPrice, int userCoins) {
        UnlockBottomSheet sheet = new UnlockBottomSheet();
        sheet.type = type;
        sheet.itemId = itemId;
        sheet.itemName = itemName;
        sheet.coinPrice = coinPrice;
        sheet.userCoins = userCoins;
        return sheet;
    }

    public void setOnUnlockListener(OnUnlockListener listener) {
        this.listener = listener;
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
        binding = DialogUnlockBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.txtCoinBalance.setText(userCoins + " coins");
        binding.txtItemName.setText(itemName);
        binding.txtCoinCost.setText("Unlock for " + coinPrice + " coins");

        binding.btnCancel.setOnClickListener(v -> dismiss());
        binding.btnUnlock.setOnClickListener(v -> attemptUnlock());
    }

    private void attemptUnlock() {
        if (userCoins < coinPrice) {
            ToastHelper.error(requireActivity(), "Not enough coins. Need " + coinPrice + " coins.");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnUnlock.setEnabled(false);
        binding.btnCancel.setEnabled(false);

        Call<MessageResponse> call = buildCall();

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> c, @NonNull Response<MessageResponse> response) {
                if (!isAdded()) return;
                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    if (listener != null) listener.onUnlocked();
                    dismiss();
                } else if (response.code() == 402) {
                    binding.btnUnlock.setEnabled(true);
                    binding.btnCancel.setEnabled(true);
                    ToastHelper.error(requireActivity(), "Not enough coins.");
                } else {
                    binding.btnUnlock.setEnabled(true);
                    binding.btnCancel.setEnabled(true);
                    ToastHelper.error(requireActivity(), "Unlock failed. Please try again.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> c, @NonNull Throwable t) {
                if (!isAdded()) return;
                binding.progressBar.setVisibility(View.GONE);
                binding.btnUnlock.setEnabled(true);
                binding.btnCancel.setEnabled(true);
                ToastHelper.error(requireActivity(), "Network error. Please try again.");
            }
        });
    }

    private Call<MessageResponse> buildCall() {
        switch (type) {
            case WORD: return ServiceLocator.api.unlockWord(itemId);
            case CATEGORY: return ServiceLocator.api.unlockCategory(itemId);
            default: return ServiceLocator.api.unlockSubcategory(itemId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
