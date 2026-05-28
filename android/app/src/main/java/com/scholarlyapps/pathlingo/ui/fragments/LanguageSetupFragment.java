package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.viewmodels.OnboardingViewModel;

import java.util.Arrays;
import java.util.List;

public class LanguageSetupFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnboardingViewModel vm = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        String[] languages = {"🇺🇸 English", "🇯🇵 Japanese", "🇨🇳 Mandarin", "🇰🇷 Korean", "🇪🇸 Spanish", "🇫🇷 French", "🇩🇪 German", "🇮🇩 Indonesian"};

        Spinner spinnerFrom = view.findViewById(R.id.spinnerFromLanguage);
        Spinner spinnerTo = view.findViewById(R.id.spinnerToLanguage);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), R.layout.item_spinner_dob, Arrays.asList(languages)) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_dob_dropdown, parent, false);
                }
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));
                return convertView;
            }
        };
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
        spinnerFrom.setPopupBackgroundResource(R.drawable.bg_spinner_popup);
        spinnerTo.setPopupBackgroundResource(R.drawable.bg_spinner_popup);
        applyDropdownBelowOffset(spinnerFrom);
        applyDropdownBelowOffset(spinnerTo);

        spinnerFrom.setSelection(findLanguageIndex(languages, vm.fromLanguage));
        spinnerTo.setSelection(findLanguageIndex(languages, vm.toLanguage));

        spinnerFrom.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                vm.fromLanguage = languages[pos].substring(languages[pos].indexOf(' ') + 1);
            }
        });
        spinnerTo.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                vm.toLanguage = languages[pos].substring(languages[pos].indexOf(' ') + 1);
            }
        });

        List<View> goalChips = Arrays.asList(
                view.findViewById(R.id.chip15min),
                view.findViewById(R.id.chip30min),
                view.findViewById(R.id.chip1hour),
                view.findViewById(R.id.chip2hours),
                view.findViewById(R.id.chip3hours)
        );
        int[] goalMinutes = {15, 30, 60, 120, 180};

        View cardTrophy = view.findViewById(R.id.cardTrophy);

        for (int i = 0; i < goalMinutes.length; i++) {
            if (vm.dailyGoalMinutes == goalMinutes[i]) {
                goalChips.get(i).setBackgroundResource(R.drawable.bg_onboarding_selected);
                cardTrophy.setVisibility(View.VISIBLE);
            }
            final int minutes = goalMinutes[i];
            goalChips.get(i).setOnClickListener(v -> {
                vm.dailyGoalMinutes = minutes;
                for (View chip : goalChips) chip.setBackgroundResource(R.drawable.bg_onboarding_unselected);
                v.setBackgroundResource(R.drawable.bg_onboarding_selected);
                cardTrophy.setVisibility(View.VISIBLE);
            });
        }
    }

    private void applyDropdownBelowOffset(Spinner spinner) {
        spinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View sample = LayoutInflater.from(requireContext())
                        .inflate(R.layout.item_spinner_dob_dropdown, spinner, false);
                sample.measure(
                        View.MeasureSpec.makeMeasureSpec(spinner.getWidth(), View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.UNSPECIFIED
                );
                int itemHeight = sample.getMeasuredHeight();
                spinner.setDropDownVerticalOffset(spinner.getHeight() + spinner.getSelectedItemPosition() * itemHeight);
            }
            return false;
        });
    }

    private int findLanguageIndex(String[] languages, String name) {
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].contains(name)) return i;
        }
        return 0;
    }

    private static abstract class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override public void onNothingSelected(AdapterView<?> parent) {}
    }
}
