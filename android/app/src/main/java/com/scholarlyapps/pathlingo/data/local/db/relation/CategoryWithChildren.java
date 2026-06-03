package com.scholarlyapps.pathlingo.data.local.db.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.scholarlyapps.pathlingo.data.local.db.entity.CategoryEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.SubcategoryEntity;

import java.util.List;

public class CategoryWithChildren {
    @Embedded
    public CategoryEntity category;

    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId",
        entity = SubcategoryEntity.class
    )
    public List<SubcategoryWithWords> subcategories;
}
