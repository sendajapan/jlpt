package com.scholarlyapps.pathlingo.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.scholarlyapps.pathlingo.ui.fragments.LanguageSetupFragment;
import com.scholarlyapps.pathlingo.ui.fragments.LearningPreferencesFragment;
import com.scholarlyapps.pathlingo.ui.fragments.PersonalInfoFragment;

public class OnboardingPagerAdapter extends FragmentStateAdapter {

    public OnboardingPagerAdapter(FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return new LearningPreferencesFragment();
            case 2: return new LanguageSetupFragment();
            default: return new PersonalInfoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
