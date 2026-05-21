package com.scholarlyapps.pathlingo.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scholarlyapps.pathlingo.R;
import com.scholarlyapps.pathlingo.adapters.CategoryAdapter;
import com.scholarlyapps.pathlingo.data.DataManager;

public class CategoryListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        RecyclerView grid = findViewById(R.id.category_grid);
        grid.setLayoutManager(new GridLayoutManager(this, 2));
        CategoryAdapter adapter = new CategoryAdapter(DataManager.getInstance().getCategories());
        adapter.setOnCategoryClickListener(category -> {
            Intent intent = new Intent(this, SubcategoryActivity.class);
            intent.putExtra("categoryId", category.id);
            startActivity(intent);
        });
        grid.setAdapter(adapter);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}
