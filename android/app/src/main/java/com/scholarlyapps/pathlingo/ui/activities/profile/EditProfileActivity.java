package com.scholarlyapps.pathlingo.ui.activities.profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.databinding.ActivityEditProfileBinding;
import com.scholarlyapps.pathlingo.ui.AvatarPickerBottomSheet;
import com.scholarlyapps.pathlingo.ui.utils.NavAnim;
import com.scholarlyapps.pathlingo.ui.utils.ToastHelper;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.EditProfileViewModel;

import coil.Coil;
import coil.request.ImageRequest;

import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private EditProfileViewModel viewModel;

    private int currentUserCoins = 0;

    private static final String[] GENDER_LABELS = {"Male", "Female", "Other", "Prefer not to say"};
    private static final String[] GENDER_VALUES = {"male", "female", "other", "prefer_not_to_say"};

    private static final String[] PROFICIENCY_LABELS = {"Beginner", "Elementary", "Intermediate", "Upper Intermediate", "Advanced", "Native-like"};
    private static final String[] PROFICIENCY_VALUES = {"beginner", "elementary", "intermediate", "upper_intermediate", "advanced", "native_like"};

    private static final String[] LEARNING_GOAL_LABELS = {"Travel", "School", "Business", "Anime", "Conversation", "Exam", "Hobby", "Other"};
    private static final String[] LEARNING_GOAL_VALUES = {"travel", "school", "business", "anime", "conversation", "exam", "hobby", "other"};

    private static final String[] DAILY_GOAL_LABELS = {"5 minutes", "10 minutes", "15 minutes", "20 minutes", "30 minutes", "45 minutes", "60 minutes"};
    private static final int[] DAILY_GOAL_VALUES = {5, 10, 15, 20, 30, 45, 60};

    private String selectedGenderValue = "";
    private String selectedBirthDate = "";
    private String selectedProficiencyValue = "";
    private String selectedLearningGoalValue = "";
    private int selectedDailyGoalMinutes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.toolbar.getLayoutParams();
            params.topMargin = bars.top;
            binding.toolbar.setLayoutParams(params);
            binding.scrollView.setPadding(0, 0, 0, bars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(EditProfileViewModel.class);

        setupDropdowns();
        setupBirthDatePicker();

        binding.toolbar.setNavigationOnClickListener(v -> {
            finish();
            NavAnim.slideBack(this);
        });

        binding.btnSave.setOnClickListener(v -> submitSave());
        binding.imgAvatar.setOnClickListener(v -> openAvatarPicker());

        viewModel.getState().observe(this, state -> {
            boolean loading = state.getLoading();
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            binding.btnSave.setEnabled(!loading);
            binding.btnSave.setText(loading ? "Saving…" : "Save Changes");

            AppUserDto profile = state.getProfile();
            if (profile != null) {
                prefillForm(profile);
            }

            String error = state.getError();
            if (error != null && !error.isEmpty()) {
                ToastHelper.error(this, error);
            }

            if (state.getSuccess()) {
                ToastHelper.success(this, "Profile updated successfully!");
                finish();
                NavAnim.slideBack(this);
            }
        });

        viewModel.loadProfile();
    }

    private void setupDropdowns() {
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, GENDER_LABELS);
        binding.spinnerGender.setAdapter(genderAdapter);
        binding.spinnerGender.setOnItemClickListener((p, v, i, id) -> selectedGenderValue = GENDER_VALUES[i]);

        ArrayAdapter<String> proficiencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, PROFICIENCY_LABELS);
        binding.spinnerProficiency.setAdapter(proficiencyAdapter);
        binding.spinnerProficiency.setOnItemClickListener((p, v, i, id) -> selectedProficiencyValue = PROFICIENCY_VALUES[i]);

        ArrayAdapter<String> goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, LEARNING_GOAL_LABELS);
        binding.spinnerLearningGoal.setAdapter(goalAdapter);
        binding.spinnerLearningGoal.setOnItemClickListener((p, v, i, id) -> selectedLearningGoalValue = LEARNING_GOAL_VALUES[i]);

        ArrayAdapter<String> dailyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, DAILY_GOAL_LABELS);
        binding.spinnerDailyGoal.setAdapter(dailyAdapter);
        binding.spinnerDailyGoal.setOnItemClickListener((p, v, i, id) -> selectedDailyGoalMinutes = DAILY_GOAL_VALUES[i]);
    }

    private void setupBirthDatePicker() {
        binding.editBirthDate.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
            selectedBirthDate = String.format(Locale.US, "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            binding.editBirthDate.setText(selectedBirthDate);
        }, year, month, day).show();
    }

    private void openAvatarPicker() {
        AvatarPickerBottomSheet sheet = new AvatarPickerBottomSheet();
        sheet.setUserCoins(currentUserCoins);
        sheet.setOnAvatarSelectedListener(updatedUser -> {
            currentUserCoins = updatedUser.coins;
            loadAvatarImage(updatedUser.avatarUrl != null ? updatedUser.avatarUrl : updatedUser.avatar);
        });
        sheet.show(getSupportFragmentManager(), "avatar_picker");
    }

    private void loadAvatarImage(String url) {
        if (url == null || url.isEmpty()) return;
        binding.imgAvatar.clearColorFilter();
        Coil.imageLoader(this).enqueue(
                new ImageRequest.Builder(this)
                        .data(url)
                        .target(binding.imgAvatar)
                        .build()
        );
    }

    private void prefillForm(AppUserDto profile) {
        currentUserCoins = profile.coins;

        String avatarUrl = profile.avatarUrl != null ? profile.avatarUrl : profile.avatar;
        loadAvatarImage(avatarUrl);

        if (profile.name != null) binding.editName.setText(profile.name);
        if (profile.username != null) binding.editUsername.setText(profile.username);
        if (profile.bio != null) binding.editBio.setText(profile.bio);

        if (profile.birthDate != null && !profile.birthDate.isEmpty()) {
            selectedBirthDate = profile.birthDate;
            binding.editBirthDate.setText(profile.birthDate);
        }

        setDropdown(binding.spinnerGender, GENDER_VALUES, GENDER_LABELS, profile.gender,
                value -> selectedGenderValue = value);

        setDropdown(binding.spinnerProficiency, PROFICIENCY_VALUES, PROFICIENCY_LABELS, profile.proficiencyLevel,
                value -> selectedProficiencyValue = value);

        setDropdown(binding.spinnerLearningGoal, LEARNING_GOAL_VALUES, LEARNING_GOAL_LABELS, profile.learningGoal,
                value -> selectedLearningGoalValue = value);

        if (profile.dailyGoalMinutes > 0) {
            for (int i = 0; i < DAILY_GOAL_VALUES.length; i++) {
                if (DAILY_GOAL_VALUES[i] == profile.dailyGoalMinutes) {
                    selectedDailyGoalMinutes = DAILY_GOAL_VALUES[i];
                    binding.spinnerDailyGoal.setText(DAILY_GOAL_LABELS[i], false);
                    break;
                }
            }
        }
    }

    private void setDropdown(android.widget.AutoCompleteTextView view, String[] values, String[] labels,
                             String currentValue, java.util.function.Consumer<String> onSet) {
        if (currentValue == null || currentValue.isEmpty()) return;
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(currentValue)) {
                onSet.accept(currentValue);
                view.setText(labels[i], false);
                break;
            }
        }
    }

    private void submitSave() {
        String name = binding.editName.getText() != null ? binding.editName.getText().toString().trim() : "";
        String username = binding.editUsername.getText() != null ? binding.editUsername.getText().toString().trim() : "";
        String bio = binding.editBio.getText() != null ? binding.editBio.getText().toString().trim() : "";
        String dailyGoal = selectedDailyGoalMinutes > 0 ? String.valueOf(selectedDailyGoalMinutes) : "";
        viewModel.save(name, username, bio, selectedGenderValue, selectedBirthDate,
                selectedProficiencyValue, selectedLearningGoalValue, dailyGoal);
    }

    @Override
    public void onBackPressed() {
        NavAnim.slideBack(this);
        super.onBackPressed();
    }
}
