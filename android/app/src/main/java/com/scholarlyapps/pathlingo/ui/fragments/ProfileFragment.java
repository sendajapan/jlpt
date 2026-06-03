package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.databinding.FragmentProfileBinding;
import com.scholarlyapps.pathlingo.models.User;
import com.scholarlyapps.pathlingo.ui.utils.LogoutHelper;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        User user = DataManager.getInstance().getUser();

        if (user != null) {
            binding.userCard.setVisibility(View.VISIBLE);
            binding.txtUserName.setText(user.name);
            binding.txtEmail.setText(user.email);
            binding.txtXp.setText("XP: " + user.xp);
        }

        binding.rowEditProfile.setOnClickListener(v -> showToast("Edit Profile"));
        binding.rowProgress.setOnClickListener(v -> showToast("Progress"));
        binding.rowCoinsEarning.setOnClickListener(v -> showToast("Coins Earning"));
        binding.rowNotifications.setOnClickListener(v -> showToast("Notifications"));
        binding.rowPreferences.setOnClickListener(v -> showToast("Preferences"));
        binding.rowChangePassword.setOnClickListener(v -> showToast("Change Password"));
        binding.rowTerms.setOnClickListener(v -> showToast("Terms & Conditions"));
        binding.rowPrivacy.setOnClickListener(v -> showToast("Privacy Policy"));
        binding.rowDeactivate.setOnClickListener(v -> showToast("Deactivate Account"));
        binding.rowDeleteAccount.setOnClickListener(v -> showToast("Delete Account"));
        binding.btnSignOut.setOnClickListener(v -> LogoutHelper.logout(requireContext()));
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
