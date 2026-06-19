package com.scholarlyapps.pathlingo.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.databinding.ActivityWordListBinding;
import com.scholarlyapps.pathlingo.models.Subcategory;
import com.scholarlyapps.pathlingo.models.Word;
import com.scholarlyapps.pathlingo.ui.adapters.WordListAdapter;
import com.scholarlyapps.pathlingo.viewmodels.AppViewModelFactory;
import com.scholarlyapps.pathlingo.viewmodels.SubcategoryViewModel;
import com.scholarlyapps.pathlingo.viewmodels.WordDetailViewModel;

import java.util.List;

public class WordListActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_ID = "categoryId";
    public static final String EXTRA_SELECTED_SUBCATEGORY_ID = "selectedSubcategoryId";

    private ActivityWordListBinding binding;
    private List<Subcategory> subcategories;
    private List<Word> currentWords;
    private String selectedSubcategoryId;
    private boolean tabsInitialized = false;

    private WordDetailViewModel wordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            binding.toolbar.setPadding(0, top, 0, 0);
            binding.rvWords.setPadding(
                binding.rvWords.getPaddingLeft(),
                binding.rvWords.getPaddingTop(),
                binding.rvWords.getPaddingRight(),
                bottom
            );
            return insets;
        });

        String categoryId = getIntent().getStringExtra(EXTRA_CATEGORY_ID);
        selectedSubcategoryId = getIntent().getStringExtra(EXTRA_SELECTED_SUBCATEGORY_ID);

        if (categoryId == null) {
            finish();
            return;
        }

        binding.toolbar.setOnBackClickListener(v -> finish());
        binding.rvWords.setLayoutManager(new LinearLayoutManager(this));

        wordViewModel = new ViewModelProvider(this, new AppViewModelFactory())
                .get(WordDetailViewModel.class);

        wordViewModel.getWords().observe(this, words -> {
            currentWords = words;
            binding.rvWords.setAdapter(new WordListAdapter(words, this::openWordActivity));
        });

        SubcategoryViewModel subcatViewModel = new ViewModelProvider(this, new AppViewModelFactory())
                .get(SubcategoryViewModel.class);

        subcatViewModel.getCategory().observe(this, category -> {
            if (category == null) return;
            subcategories = category.subcategories;

            if (!tabsInitialized) {
                tabsInitialized = true;

                int initialIndex = 0;
                for (int i = 0; i < subcategories.size(); i++) {
                    binding.tabLayout.addTab(binding.tabLayout.newTab().setText(subcategories.get(i).en));
                    if (subcategories.get(i).id.equals(selectedSubcategoryId)) {
                        initialIndex = i;
                    }
                }

                loadWordsForIndex(initialIndex);

                TabLayout.Tab initialTab = binding.tabLayout.getTabAt(initialIndex);
                if (initialTab != null) initialTab.select();

                binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        loadWordsForIndex(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {}
                });
            }
        });

        subcatViewModel.load(Long.parseLong(categoryId));
    }

    private void loadWordsForIndex(int index) {
        if (subcategories == null || index < 0 || index >= subcategories.size()) return;
        Subcategory sub = subcategories.get(index);
        wordViewModel.load(Long.parseLong(sub.id));
    }

    private void openWordActivity(Word word) {
        if (currentWords == null) return;
        int tabPos = binding.tabLayout.getSelectedTabPosition();
        if (subcategories == null || tabPos < 0 || tabPos >= subcategories.size()) return;
        String subcategoryId = subcategories.get(tabPos).id;
        int wordIndex = currentWords.indexOf(word);
        Intent intent = new Intent(this, WordActivity.class);
        intent.putExtra(WordActivity.EXTRA_SUBCATEGORY_ID, subcategoryId);
        intent.putExtra(WordActivity.EXTRA_WORD_INDEX, wordIndex);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
