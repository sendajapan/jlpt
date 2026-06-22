package com.scholarlyapps.pathlingo.data.local.db.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.scholarlyapps.pathlingo.data.local.db.entity.SubcategoryEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.WordEntity;

import java.util.List;

public class SubcategoryWithWords {
    @Embedded
    public SubcategoryEntity subcategory;

    @Relation(parentColumn = "id", entityColumn = "subcategoryId")
    public List<WordEntity> words;
}
