package com.scholarlyapps.pathlingo.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import coil.Coil;
import coil.request.ImageRequest;

import com.scholarlyapps.pathlingo.databinding.DialogConfirmLogoutBinding;
import com.scholarlyapps.pathlingo.databinding.FragmentProfileBinding;
import com.scholarlyapps.pathlingo.ui.activities.profile.ChangePasswordActivity;
import com.scholarlyapps.pathlingo.ui.activities.profile.EditProfileActivity;
import com.scholarlyapps.pathlingo.ui.utils.LogoutHelper;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.ProgressViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProgressViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), bars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(ProgressViewModel.class);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;
            binding.userCard.setVisibility(View.VISIBLE);
            binding.txtUserName.setText(user.name);
            binding.txtEmail.setText(user.email);
            binding.txtXp.setText("XP: " + user.xp);
        });

        viewModel.getEmail().observe(getViewLifecycleOwner(), emailValue -> {
            if (emailValue != null && !emailValue.isEmpty()) {
                binding.txtEmail.setText(emailValue);
            }
        });

        viewModel.getAvatarUrl().observe(getViewLifecycleOwner(), url -> {
            if (url == null || url.isEmpty()) return;
            ImageViewCompat.setImageTintList(binding.imgAvatar, null);
            Coil.imageLoader(requireContext()).enqueue(
                    new ImageRequest.Builder(requireContext())
                            .data(url)
                            .target(binding.imgAvatar)
                            .build()
            );
        });

        binding.btnLogout.setOnClickListener(v -> showLogoutDialog());

        binding.rowEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), EditProfileActivity.class));
            NavAnim.slideForward(requireActivity());
        });
        binding.rowProgress.setOnClickListener(v -> showToast("Progress"));
        binding.rowCoinsEarning.setOnClickListener(v -> showToast("Coins Earning"));
        binding.rowNotifications.setOnClickListener(v -> showToast("Notifications"));
        binding.rowPreferences.setOnClickListener(v -> showToast("Preferences"));
        binding.rowChangePassword.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ChangePasswordActivity.class));
            NavAnim.slideForward(requireActivity());
        });
        binding.rowTerms.setOnClickListener(v -> showToast("Terms & Conditions"));
        binding.rowPrivacy.setOnClickListener(v -> showToast("Privacy Policy"));
        binding.rowDeactivate.setOnClickListener(v -> showToast("Deactivate Account"));
        binding.rowDeleteAccount.setOnClickListener(v -> showToast("Delete Account"));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refresh();
        }
    }

    private void showLogoutDialog() {
        Dialog dialog = new Dialog(requireContext());
        DialogConfirmLogoutBinding dialogBinding = DialogConfirmLogoutBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.82);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCancelable(true);

        dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.btnConfirmLogout.setOnClickListener(v -> {
            dialog.dismiss();
            LogoutHelper.logout(requireContext());
        });

        dialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
