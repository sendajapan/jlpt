package com.scholarlyapps.pathlingo.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.scholarlyapps.pathlingo.data.remote.dto.KanjiDto;
import com.scholarlyapps.pathlingo.databinding.ActivityKanjiGridBinding;
import com.scholarlyapps.pathlingo.ui.adapters.KanjiGridAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.KanjiGridViewModel;

public class KanjiGridActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "mode";
    public static final String EXTRA_JLPT = "jlpt";
    public static final String EXTRA_STROKES = "strokes";

    public static final String MODE_JLPT = "jlpt";
    public static final String MODE_STROKES = "strokes";

    private static final String[] LEVELS = {"N5", "N4", "N3", "N2", "N1"};
    private static final int SPAN_COUNT = 4;
    private static final int LOAD_MORE_THRESHOLD = 8;

    private ActivityKanjiGridBinding binding;
    private KanjiGridViewModel viewModel;
    private KanjiGridAdapter adapter;

    public static Intent intentForJlpt(Context context, String level) {
        return new Intent(context, KanjiGridActivity.class)
                .putExtra(EXTRA_MODE, MODE_JLPT)
                .putExtra(EXTRA_JLPT, level);
    }

    public static Intent intentForStrokes(Context context, int strokes) {
        return new Intent(context, KanjiGridActivity.class)
                .putExtra(EXTRA_MODE, MODE_STROKES)
                .putExtra(EXTRA_STROKES, strokes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKanjiGridBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.rvKanjis.setPadding(binding.rvKanjis.getPaddingLeft(), 0, binding.rvKanjis.getPaddingRight(), bottom);
            return insets;
        });

        String mode = getIntent().getStringExtra(EXTRA_MODE);
        if (mode == null) {
            finish();
            return;
        }

        binding.toolbar.setOnBackClickListener(v -> finish());

        adapter = new KanjiGridAdapter(this::openKanjiDetail);
        setupGrid();

        viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(KanjiGridViewModel.class);

        viewModel.getItems().observe(this, items -> {
            if (items == null) return;
            adapter.submit(items);
        });

        viewModel.getLoadingMore().observe(this, loading ->
                adapter.setShowFooter(Boolean.TRUE.equals(loading)));

        viewModel.getError().observe(this, message -> {
            if (message == null) return;
            viewModel.clearError();
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
        });

        if (MODE_JLPT.equals(mode)) {
            setupJlptMode(getIntent().getStringExtra(EXTRA_JLPT));
        } else {
            setupStrokesMode(getIntent().getIntExtra(EXTRA_STROKES, 1));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refreshLearned();
    }

    private void setupGrid() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isFooter(position) ? SPAN_COUNT : 1;
            }
        });
        binding.rvKanjis.setLayoutManager(layoutManager);
        binding.rvKanjis.setAdapter(adapter);

        binding.rvKanjis.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0) return;
                if (layoutManager.findLastVisibleItemPosition() >= adapter.getItemCount() - LOAD_MORE_THRESHOLD
                        && viewModel.canLoadMore()) {
                    viewModel.loadNextPage();
                }
            }
        });
    }

    private void setupJlptMode(String initialLevel) {
        binding.txtTitle.setText("Kanji by JLPT Level");

        for (String level : LEVELS) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(level));
        }

        int initialIndex = levelIndex(initialLevel);
        TabLayout.Tab initialTab = binding.tabLayout.getTabAt(initialIndex);
        if (initialTab != null) initialTab.select();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewModel.setJlpt(LEVELS[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewModel.setJlpt(LEVELS[initialIndex]);
    }

    private void setupStrokesMode(int strokes) {
        binding.txtTitle.setText(strokes + " Stroke Kanji");
        binding.tabLayout.setVisibility(View.GONE);
        viewModel.setStrokes(strokes);
    }

    private int levelIndex(String level) {
        for (int i = 0; i < LEVELS.length; i++) {
            if (LEVELS[i].equals(level)) return i;
        }
        return 0;
    }

    private void openKanjiDetail(KanjiDto kanji) {
        startActivity(KanjiDetailActivity.intent(this, kanji.id));
    }
}
