package com.scholarlyapps.pathlingo.data.repo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.scholarlyapps.pathlingo.data.local.db.dao.CategoryDao;
import com.scholarlyapps.pathlingo.data.local.db.dao.SubcategoryDao;
import com.scholarlyapps.pathlingo.data.local.db.dao.WordDao;
import com.scholarlyapps.pathlingo.data.local.db.entity.CategoryEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.SubcategoryEntity;
import com.scholarlyapps.pathlingo.data.local.db.entity.WordEntity;
import com.scholarlyapps.pathlingo.data.local.db.relation.CategoryWithChildren;
import com.scholarlyapps.pathlingo.data.local.db.relation.SubcategoryWithWords;
import com.scholarlyapps.pathlingo.data.networking.ApiService;
import com.scholarlyapps.pathlingo.data.remote.dto.CategoryDto;
import com.scholarlyapps.pathlingo.data.remote.dto.ListResponse;
import com.scholarlyapps.pathlingo.data.remote.dto.SubcategoryDto;
import com.scholarlyapps.pathlingo.data.remote.dto.VocabularyDto;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.Subcategory;
import com.scholarlyapps.pathlingo.models.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {

    private final ApiService apiService;
    private final CategoryDao categoryDao;
    private final SubcategoryDao subcategoryDao;
    private final WordDao wordDao;
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    public CategoryRepository(ApiService apiService, CategoryDao categoryDao, SubcategoryDao subcategoryDao, WordDao wordDao) {
        this.apiService = apiService;
        this.categoryDao = categoryDao;
        this.subcategoryDao = subcategoryDao;
        this.wordDao = wordDao;
    }

    public LiveData<List<Category>> getAllCategories() {
        return Transformations.map(categoryDao.getAll(), this::mapCategoryEntities);
    }

    public LiveData<Category> getCategoryById(long id) {
        return Transformations.map(categoryDao.getWithChildrenById(id), this::mapCategory);
    }

    public LiveData<List<Word>> getWordsForSubcategory(long subcategoryId) {
        return Transformations.map(wordDao.getBySubcategoryId(subcategoryId), this::mapWords);
    }

    public LiveData<List<Word>> getFavoriteWords() {
        return Transformations.map(wordDao.getFavorites(), this::mapWords);
    }

    public void refresh() {
        apiService.getCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListResponse<CategoryDto>> call, @NonNull Response<ListResponse<CategoryDto>> response) {
                if (!response.isSuccessful() || response.body() == null) return;
                List<CategoryDto> dtos = response.body().getData();
                dbExecutor.execute(() -> {
                    categoryDao.deleteAll();
                    categoryDao.insertAll(toCategoryEntities(dtos));
                });
            }

            @Override
            public void onFailure(@NonNull Call<ListResponse<CategoryDto>> call, @NonNull Throwable throwable) {}
        });

        apiService.getSubcategories(null).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListResponse<SubcategoryDto>> call, @NonNull Response<ListResponse<SubcategoryDto>> response) {
                if (!response.isSuccessful() || response.body() == null) return;
                List<SubcategoryDto> dtos = response.body().getData();
                dbExecutor.execute(() -> {
                    subcategoryDao.deleteAll();
                    subcategoryDao.insertAll(toSubcategoryEntities(dtos));
                });
            }

            @Override
            public void onFailure(@NonNull Call<ListResponse<SubcategoryDto>> call, @NonNull Throwable throwable) {}
        });

        apiService.getVocabularies(null, null, null).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ListResponse<VocabularyDto>> call, @NonNull Response<ListResponse<VocabularyDto>> response) {
                if (!response.isSuccessful() || response.body() == null) return;
                List<VocabularyDto> dtos = response.body().getData();
                dbExecutor.execute(() -> {
                    wordDao.deleteAll();
                    wordDao.insertAll(toWordEntities(dtos));
                });
            }

            @Override
            public void onFailure(@NonNull Call<ListResponse<VocabularyDto>> call, @NonNull Throwable throwable) {}
        });
    }

    public List<Word> getRandomWords(int count) {
        return mapWords(wordDao.getRandomWordsSync(count));
    }

    public void addFavorite(long wordId) {
        try {
            apiService.addFavorite(wordId).execute();
            wordDao.setFavorite(wordId, true);
        } catch (Exception ignored) {}
    }

    public void removeFavorite(long wordId) {
        try {
            apiService.removeFavorite(wordId).execute();
            wordDao.setFavorite(wordId, false);
        } catch (Exception ignored) {}
    }

    private List<Category> mapCategoryEntities(List<CategoryEntity> entities) {
        List<Category> categories = new ArrayList<>();
        if (entities == null) return categories;

        for (CategoryEntity entity : entities) {
            Category category = new Category();
            category.id = String.valueOf(entity.id);
            category.jp = orEmpty(entity.nameJp);
            category.en = orEmpty(entity.nameEn);
            category.ch = orEmpty(entity.nameRomaji);
            category.bg = orEmpty(entity.bgUrl);
            category.iconUrl = orEmpty(entity.iconUrl);
            category.img = orEmpty(entity.iconThumbnailUrl);
            category.locked = entity.isPremium;
            category.coinPrice = entity.coinPrice;
            category.isLocked = entity.isLocked;
            categories.add(category);
        }

        return categories;
    }

    private List<Category> mapCategories(List<CategoryWithChildren> rows) {
        List<Category> categories = new ArrayList<>();
        if (rows == null) return categories;

        for (CategoryWithChildren row : rows) {
            categories.add(mapCategory(row));
        }

        return categories;
    }

    private Category mapCategory(CategoryWithChildren row) {
        if (row == null) return null;

        Category category = new Category();
        category.id = String.valueOf(row.category.id);
        category.jp = orEmpty(row.category.nameJp);
        category.en = orEmpty(row.category.nameEn);
        category.ch = orEmpty(row.category.nameRomaji);
        category.bg = orEmpty(row.category.bgUrl);
        category.iconUrl = orEmpty(row.category.iconUrl);
        category.img = orEmpty(row.category.iconThumbnailUrl);
        category.locked = row.category.isPremium;
        category.coinPrice = row.category.coinPrice;
        category.isLocked = row.category.isLocked;

        for (SubcategoryWithWords subcategoryRow : row.subcategories) {
            Subcategory subcategory = mapSubcategory(subcategoryRow);
            category.subcategories.add(subcategory);
            category.count += subcategory.total;
        }

        return category;
    }

    private Subcategory mapSubcategory(SubcategoryWithWords row) {
        Subcategory subcategory = new Subcategory();
        subcategory.id = String.valueOf(row.subcategory.id);
        subcategory.jp = orEmpty(row.subcategory.nameJp);
        subcategory.en = orEmpty(row.subcategory.nameEn);
        subcategory.romaji = orEmpty(row.subcategory.nameRomaji);
        subcategory.bg = orEmpty(row.subcategory.iconThumbnailBg);
        subcategory.img = orEmpty(row.subcategory.bgUrl);
        subcategory.iconUrl = orEmpty(row.subcategory.iconUrl);
        subcategory.locked = row.subcategory.isPremium;
        subcategory.coinPrice = row.subcategory.coinPrice;
        subcategory.isLocked = row.subcategory.isLocked;
        subcategory.words = mapWords(row.words);
        subcategory.total = subcategory.words.size();

        return subcategory;
    }

    private List<Word> mapWords(List<WordEntity> entities) {
        List<Word> words = new ArrayList<>();
        if (entities == null) return words;

        for (WordEntity entity : entities) {
            Word word = new Word();
            word.id = entity.id;
            word.kanji = orEmpty(entity.wordJp);
            word.reading = orEmpty(entity.wordJp);
            word.romaji = orEmpty(entity.wordRomaji);
            word.en = orEmpty(entity.wordEn);
            word.img = orEmpty(entity.imageUrl);
            word.example_jp = orEmpty(entity.sentenceJp);
            word.example_romaji = orEmpty(entity.sentenceRomaji);
            word.example_en = orEmpty(entity.sentenceEn);
            word.jlpt = "";
            word.type = "";
            word.xp = 0;
            word.maxMastery = 5;
            word.favorite = entity.isFavorite;
            word.coinPrice = entity.coinPrice;
            word.isLocked = entity.isLocked;
            word.audioUrl = orEmpty(entity.audioJpUrl);
            word.audioEnUrl = orEmpty(entity.audioEnUrl);
            word.sentenceAudioJpUrl = orEmpty(entity.sentenceAudioJpUrl);
            word.sentenceAudioEnUrl = orEmpty(entity.sentenceAudioEnUrl);

            words.add(word);
        }

        return words;
    }

    private List<CategoryEntity> toCategoryEntities(List<CategoryDto> dtos) {
        List<CategoryEntity> entities = new ArrayList<>();

        for (CategoryDto dto : dtos) {
            CategoryEntity entity = new CategoryEntity();
            entity.id = dto.id;
            entity.nameJp = dto.nameJp;
            entity.nameEn = dto.nameEn;
            entity.nameRomaji = dto.nameRomaji;
            entity.iconUrl = dto.iconUrl;
            entity.iconThumbnailUrl = dto.iconThumbnailUrl;
            entity.bgUrl = dto.bgUrl;
            entity.isPremium = dto.isPremium;
            entity.coinPrice = dto.coinPrice;
            entity.isLocked = dto.isLocked;

            entities.add(entity);
        }

        return entities;
    }

    private List<SubcategoryEntity> toSubcategoryEntities(List<SubcategoryDto> dtos) {
        List<SubcategoryEntity> entities = new ArrayList<>();

        for (SubcategoryDto dto : dtos) {
            SubcategoryEntity entity = new SubcategoryEntity();
            entity.id = dto.id;
            entity.categoryId = dto.vocabCategoryId;
            entity.nameJp = dto.nameJp;
            entity.nameEn = dto.nameEn;
            entity.nameRomaji = dto.nameRomaji;
            entity.iconUrl = dto.iconUrl;
            entity.iconThumbnailUrl = dto.iconThumbnailUrl;
            entity.iconThumbnailBg = dto.iconThumbnailBg;
            entity.bgUrl = dto.bgUrl;
            entity.isPremium = dto.isPremium;
            entity.coinPrice = dto.coinPrice;
            entity.isLocked = dto.isLocked;

            entities.add(entity);
        }

        return entities;
    }

    private List<WordEntity> toWordEntities(List<VocabularyDto> dtos) {
        List<WordEntity> entities = new ArrayList<>();

        for (VocabularyDto dto : dtos) {
            WordEntity entity = new WordEntity();
            entity.id = dto.id;
            entity.subcategoryId = dto.vocabSubcategoryId;
            entity.wordJp = dto.wordJp;
            entity.wordRomaji = dto.wordRomaji;
            entity.wordEn = dto.wordEn;
            entity.sentenceJp = dto.sentenceJp;
            entity.sentenceRomaji = dto.sentenceRomaji;
            entity.sentenceEn = dto.sentenceEn;
            entity.audioJpUrl = dto.audioJpUrl;
            entity.audioEnUrl = dto.audioEnUrl;
            entity.sentenceAudioJpUrl = dto.sentenceAudioJpUrl;
            entity.sentenceAudioEnUrl = dto.sentenceAudioEnUrl;
            entity.imageUrl = dto.imageUrl;
            entity.isPremium = dto.isPremium;
            entity.coinPrice = dto.coinPrice;
            entity.isLocked = dto.isLocked;

            entities.add(entity);
        }

        return entities;
    }

    private String orEmpty(String value) {
        return value != null ? value : "";
    }
}
