package com.scholarlyapps.pathlingo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.adapters.SubcategoryAdapter;
import com.scholarlyapps.pathlingo.data.DataManager;
import com.scholarlyapps.pathlingo.models.Category;

public class SubcategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);

        String categoryId = getIntent().getStringExtra("categoryId");
        Category category = DataManager.getInstance().getCategoryById(categoryId);

        if (category != null) {
            TextView titleView = findViewById(R.id.title);
            titleView.setText(category.jp + " / " + category.en);

            TextView countView = findViewById(R.id.word_count);
            countView.setText(category.count + " words · " + category.subcategories.size() + " chapters");

            RecyclerView listView = findViewById(R.id.subcategory_list);
            listView.setLayoutManager(new LinearLayoutManager(this));
            SubcategoryAdapter adapter = new SubcategoryAdapter(this, category.subcategories, categoryId);
            adapter.setOnSubcategoryClickListener((catId, subcat) -> {
                Intent intent = new Intent(this, WordDetailActivity.class);
                intent.putExtra("categoryId", catId);
                intent.putExtra("subcategoryId", subcat.id);
                intent.putExtra("wordIndex", 0);
                startActivity(intent);
            });
            listView.setAdapter(adapter);
        }

        Button backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> finish());
    }
}
