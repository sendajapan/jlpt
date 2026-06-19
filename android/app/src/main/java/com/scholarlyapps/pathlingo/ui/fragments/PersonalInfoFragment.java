package com.scholarlyapps.pathlingo.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.FragmentPersonalInfoBinding;
import com.scholarlyapps.pathlingo.viewmodels.OnboardingViewModel;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PersonalInfoFragment extends Fragment {

    private FragmentPersonalInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPersonalInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OnboardingViewModel vm = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        updateDateLabel(vm.dobYear, vm.dobMonth, vm.dobDay);

        binding.btnPickDate.setOnClickListener(v -> {
            DobPickerBottomSheet sheet = DobPickerBottomSheet.newInstance(vm.dobYear, vm.dobMonth, vm.dobDay);
            sheet.setOnDateSelectedListener((year, month, day) -> {
                vm.dobYear  = year;
                vm.dobMonth = month;
                vm.dobDay   = day;
                updateDateLabel(year, month, day);
            });
            sheet.show(getParentFragmentManager(), "dob_picker");
        });

        List<View> genderCards = Arrays.asList(
                binding.cardMale,
                binding.cardFemale,
                binding.cardOther,
                binding.cardPreferNot
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
    }

    private void updateDateLabel(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        String formatted = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).format(cal.getTime());
        binding.txtSelectedDate.setText(formatted);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
