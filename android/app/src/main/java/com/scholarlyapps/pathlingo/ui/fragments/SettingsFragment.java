package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.ui.auth.LogoutHelper;
import com.scholarlyapps.pathlingo.models.User;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = DataManager.getInstance().getUser();
        if (user != null) {
            ((TextView) view.findViewById(R.id.tv_name)).setText(user.name);
            ((TextView) view.findViewById(R.id.tv_jp_name)).setText(user.jpName);
            ((TextView) view.findViewById(R.id.tv_level_badge)).setText("Lv. " + user.level);
            ((TextView) view.findViewById(R.id.tv_words_known)).setText(String.valueOf(user.wordsKnown));
            ((TextView) view.findViewById(R.id.tv_streak_stat)).setText(String.valueOf(user.streak));
            ((TextView) view.findViewById(R.id.tv_accuracy)).setText(user.accuracy + "%");
            ((TextView) view.findViewById(R.id.tv_email)).setText(user.email);
        }

        view.findViewById(R.id.btn_logout).setOnClickListener(v -> {
            LogoutHelper.logout(requireContext());
        });
    }
}
