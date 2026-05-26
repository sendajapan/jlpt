package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.viewmodels.OnboardingViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class OnboardingStep1Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_step1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnboardingViewModel vm = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        List<View> genderCards = Arrays.asList(
                view.findViewById(R.id.cardMale),
                view.findViewById(R.id.cardFemale),
                view.findViewById(R.id.cardOther),
                view.findViewById(R.id.cardPreferNot)
        );
        List<String> genderValues = Arrays.asList("Male", "Female", "Other", "Prefer not to say");

        for (int i = 0; i < genderCards.size(); i++) {
            final String gender = genderValues.get(i);
            genderCards.get(i).setOnClickListener(v -> {
                vm.gender = gender;
                for (View card : genderCards) card.setBackgroundResource(R.drawable.bg_onboarding_unselected);
                v.setBackgroundResource(R.drawable.bg_onboarding_selected);
            });
        }

        if (!vm.gender.isEmpty()) {
            int idx = genderValues.indexOf(vm.gender);
            if (idx >= 0) genderCards.get(idx).setBackgroundResource(R.drawable.bg_onboarding_selected);
        }

        Spinner spinnerYear = view.findViewById(R.id.spinnerYear);
        Spinner spinnerMonth = view.findViewById(R.id.spinnerMonth);
        Spinner spinnerDay = view.findViewById(R.id.spinnerDay);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> years = new ArrayList<>();
        for (int y = currentYear - 10; y >= currentYear - 80; y--) years.add(String.valueOf(y));

        List<String> months = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        List<String> days = new ArrayList<>();
        for (int d = 1; d <= 31; d++) days.add(String.valueOf(d));

        setupSpinner(spinnerYear, years);
        setupSpinner(spinnerMonth, months);
        setupSpinner(spinnerDay, days);

        int yearIdx = years.indexOf(String.valueOf(vm.dobYear));
        if (yearIdx >= 0) spinnerYear.setSelection(yearIdx);
        spinnerMonth.setSelection(vm.dobMonth);
        spinnerDay.setSelection(vm.dobDay - 1);

        spinnerYear.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                vm.dobYear = Integer.parseInt(years.get(pos));
            }
        });
        spinnerMonth.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                vm.dobMonth = pos;
            }
        });
        spinnerDay.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                vm.dobDay = pos + 1;
            }
        });
    }

    private void setupSpinner(Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private static abstract class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override public void onNothingSelected(AdapterView<?> parent) {}
    }
}
