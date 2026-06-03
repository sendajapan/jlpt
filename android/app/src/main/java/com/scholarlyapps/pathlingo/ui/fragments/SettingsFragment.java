package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.databinding.FragmentSettingsBinding;
import com.scholarlyapps.pathlingo.models.User;
import com.scholarlyapps.pathlingo.ui.utils.LogoutHelper;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        User user = DataManager.getInstance().getUser();

        if (user != null) {
            binding.userCard.setVisibility(View.VISIBLE);
            binding.statsRow.setVisibility(View.VISIBLE);
            binding.labelAccount.setVisibility(View.VISIBLE);
            binding.accountCard.setVisibility(View.VISIBLE);

            binding.txtUserName.setText(user.name);
            binding.txtJpName.setText(user.jpName);
            binding.txtLevelBadge.setText("Lv. " + user.level);
            binding.txtWordsKnown.setText(String.valueOf(user.wordsKnown));
            binding.txtStreakStat.setText(String.valueOf(user.streak));
            binding.txtAccuracy.setText(user.accuracy + "%");
            binding.txtEmail.setText(user.email);
        }

        binding.btnLogout.setOnClickListener(v -> LogoutHelper.logout(requireContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
