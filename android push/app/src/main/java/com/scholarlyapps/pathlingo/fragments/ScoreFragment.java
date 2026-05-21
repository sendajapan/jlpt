package com.scholarlyapps.pathlingo.fragments;

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
import org.json.JSONObject;

public class ScoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JSONObject lesson = DataManager.getInstance().getLessonData();
        if (lesson != null) {
            int score    = lesson.optInt("score", 0);
            int maxScore = lesson.optInt("maxScore", 100);
            int correct  = lesson.optInt("correctCount", 0);
            int total    = lesson.optInt("totalCount", 0);
            int stars    = lesson.optInt("stars", 0);

            String heading = stars == 3 ? "すばらしい！ Wonderful!"
                           : stars == 2 ? "よくできました！ Well done!"
                           : "がんばって！ Keep going!";
            ((TextView) view.findViewById(R.id.heading)).setText(heading);
            ((TextView) view.findViewById(R.id.score_text)).setText(score + "/" + maxScore);
        }

        Button btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_score_to_home));
    }
}
