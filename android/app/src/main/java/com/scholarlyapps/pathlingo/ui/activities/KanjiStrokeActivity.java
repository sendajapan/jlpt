package com.scholarlyapps.pathlingo.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.scholarlyapps.pathlingo.databinding.ActivityKanjiStrokeBinding;
import com.scholarlyapps.pathlingo.ui.adapters.KanjiStrokeAdapter;
import com.scholarlyapps.pathlingo.ui.decorations.GridSpacingItemDecoration;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.KanjiStrokeViewModel;

public class KanjiStrokeActivity extends AppCompatActivity {

    private ActivityKanjiStrokeBinding binding;
    private KanjiStrokeViewModel viewModel;
    private KanjiStrokeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKanjiStrokeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.rvStrokes.setPadding(binding.rvStrokes.getPaddingLeft(), 0, binding.rvStrokes.getPaddingRight(), bottom);
            return insets;
        });

        binding.toolbar.setOnBackClickListener(v -> finish());

        adapter = new KanjiStrokeAdapter(group ->
                startActivity(KanjiGridActivity.intentForStrokes(this, group.strokes)));
        int spacing = (int) (8 * getResources().getDisplayMetrics().density);
        binding.rvStrokes.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rvStrokes.addItemDecoration(new GridSpacingItemDecoration(3, spacing));
        binding.rvStrokes.setAdapter(adapter);

        viewModel = new ViewModelProvider(this, new AppViewModelFactory()).get(KanjiStrokeViewModel.class);

        viewModel.getGroups().observe(this, groups -> {
            if (groups == null) return;
            adapter.submit(groups);
        });

        viewModel.getError().observe(this, message -> {
            if (message == null) return;
            viewModel.clearError();
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
        });

        viewModel.load();
    }
}
