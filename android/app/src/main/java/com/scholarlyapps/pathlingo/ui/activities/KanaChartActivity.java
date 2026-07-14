package com.scholarlyapps.pathlingo.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.scholarlyapps.pathlingo.data.remote.dto.KanaDto;
import com.scholarlyapps.pathlingo.databinding.ActivityKanaChartBinding;
import com.scholarlyapps.pathlingo.ui.adapters.KanaGridAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.KanaChartViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KanaChartActivity extends AppCompatActivity {

    public static final String EXTRA_SCRIPT = "script";

    private static final String SECTION_BASIC = "basic";
    private static final String SECTION_DAKUTEN = "dakuten";
    private static final String SECTION_COMBO = "combo";
    private static final String[] SECTIONS = {SECTION_BASIC, SECTION_DAKUTEN, SECTION_COMBO};

    private ActivityKanaChartBinding binding;
    private KanaChartViewModel viewModel;
    private String script;
    private List<KanaDto> allKanas = new ArrayList<>();

    public static Intent intent(Context context, String script) {
        return new Intent(context, KanaChartActivity.class).putExtra(EXTRA_SCRIPT, script);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKanaChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.scrollView.setPadding(0, 0, 0, bottom);
            return insets;
        });

        script = getIntent().getStringExtra(EXTRA_SCRIPT);
        if (script == null) {
            finish();
            return;
        }

        binding.toolbar.setOnBackClickListener(v -> finish());
        binding.txtScriptTitle.setText(script.toUpperCase(Locale.ROOT));
        binding.quizCard.btnStartQuiz.setOnClickListener(v -> startActivity(new Intent(this, QuizActivity.class)));

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Letters"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Tenten & Maru"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Combo"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showSection(SECTIONS[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(KanaChartViewModel.class);

        viewModel.getKanas().observe(this, kanas -> {
            if (kanas == null) return;
            allKanas = kanas;
            updateProgress();
            showSection(SECTIONS[binding.tabLayout.getSelectedTabPosition()]);
        });

        viewModel.getError().observe(this, message -> {
            if (message == null) return;
            viewModel.clearError();
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.load(script);
    }

    private void updateProgress() {
        int learned = 0;
        for (KanaDto kana : allKanas) {
            if (kana.isLearned) learned++;
        }
        binding.progressLearned.setMax(Math.max(allKanas.size(), 1));
        binding.progressLearned.setProgress(learned);
        binding.txtProgressCount.setText(learned + "/" + allKanas.size());
    }

    private void showSection(String section) {
        int spanCount = SECTION_COMBO.equals(section) ? 3 : 5;
        List<KanaDto> cells = buildCells(section, spanCount);

        binding.rvKanas.setLayoutManager(new GridLayoutManager(this, spanCount));
        binding.rvKanas.setAdapter(new KanaGridAdapter(cells, this::openKanaDetail));
    }

    private List<KanaDto> buildCells(String section, int spanCount) {
        List<KanaDto> sectionKanas = new ArrayList<>();
        for (KanaDto kana : allKanas) {
            if (section.equals(kana.section)) sectionKanas.add(kana);
        }

        int maxIndex = -1;
        List<KanaDto> cells = new ArrayList<>();
        for (KanaDto kana : sectionKanas) {
            int index = cellIndex(kana, spanCount);
            while (cells.size() <= index) {
                cells.add(null);
            }
            cells.set(index, kana);
            maxIndex = Math.max(maxIndex, index);
        }

        return cells;
    }

    private int cellIndex(KanaDto kana, int spanCount) {
        if (SECTION_BASIC.equals(kana.section)) {
            return kana.position - 1;
        }
        if (SECTION_DAKUTEN.equals(kana.section)) {
            return kana.position - 61;
        }
        int offset = kana.position - 101;
        return (offset / 5) * spanCount + (offset % 5);
    }

    private void openKanaDetail(KanaDto kana) {
        startActivity(KanaDetailActivity.intent(this, kana.id));
    }
}
