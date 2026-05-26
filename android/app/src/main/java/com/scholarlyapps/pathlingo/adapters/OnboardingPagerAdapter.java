package com.scholarlyapps.pathlingo.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.scholarlyapps.pathlingo.ui.fragments.OnboardingStep1Fragment;
import com.scholarlyapps.pathlingo.ui.fragments.OnboardingStep2Fragment;
import com.scholarlyapps.pathlingo.ui.fragments.OnboardingStep3Fragment;

public class OnboardingPagerAdapter extends FragmentStateAdapter {

    public OnboardingPagerAdapter(FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return new OnboardingStep2Fragment();
            case 2: return new OnboardingStep3Fragment();
            default: return new OnboardingStep1Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
