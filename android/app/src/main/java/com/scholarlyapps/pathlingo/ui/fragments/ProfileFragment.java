package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.models.User;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = DataManager.getInstance().getUser();

        TextView userName = view.findViewById(R.id.user_name);
        userName.setText(user.name);

        TextView userJpName = view.findViewById(R.id.user_jp_name);
        userJpName.setText(user.jpName);

        TextView userLevel = view.findViewById(R.id.user_level);
        userLevel.setText("Level " + user.level);

        TextView userXp = view.findViewById(R.id.user_xp);
        userXp.setText(user.xp + "/" + user.maxXp + " XP");

        TextView statWords = view.findViewById(R.id.stat_words);
        statWords.setText(user.wordsKnown + " words");

        TextView statStreak = view.findViewById(R.id.stat_streak);
        statStreak.setText(user.streak + " day");

        TextView statAccuracy = view.findViewById(R.id.stat_accuracy);
        statAccuracy.setText(user.accuracy + "%");

        Button btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }
}
