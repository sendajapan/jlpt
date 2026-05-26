package com.scholarlyapps.pathlingo.viewmodels;

import androidx.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.Set;

public class OnboardingViewModel extends ViewModel {
    public int dobYear = 2000;
    public int dobMonth = 0;
    public int dobDay = 1;
    public String gender = "";
    public String proficiency = "Beginner";
    public final Set<String> learningGoals = new HashSet<>();
    public String fromLanguage = "English";
    public String toLanguage = "Japanese";
    public int dailyGoalMinutes = 0;
}
