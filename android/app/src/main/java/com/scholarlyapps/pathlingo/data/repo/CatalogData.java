package com.scholarlyapps.pathlingo.data.repo;

import com.scholarlyapps.pathlingo.data.remote.dto.CategoryDto;
import com.scholarlyapps.pathlingo.data.remote.dto.SubcategoryDto;
import com.scholarlyapps.pathlingo.data.remote.dto.VocabularyDto;

import java.util.List;

public class CatalogData {
    public final List<CategoryDto> categories;
    public final List<SubcategoryDto> subcategories;
    public final List<VocabularyDto> vocabularies;

    public CatalogData(List<CategoryDto> categories, List<SubcategoryDto> subcategories, List<VocabularyDto> vocabularies) {
        this.categories = categories;
        this.subcategories = subcategories;
        this.vocabularies = vocabularies;
    }
}
