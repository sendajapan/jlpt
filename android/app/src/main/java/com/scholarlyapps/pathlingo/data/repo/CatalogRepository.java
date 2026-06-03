package com.scholarlyapps.pathlingo.data.repo;

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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Response;

public class CatalogRepository {

    private final ApiService api;
    private final CategoryDao categoryDao;
    private final SubcategoryDao subcategoryDao;
    private final WordDao wordDao;

    public CatalogRepository(ApiService api, CategoryDao categoryDao, SubcategoryDao subcategoryDao, WordDao wordDao) {
        this.api = api;
        this.categoryDao = categoryDao;
        this.subcategoryDao = subcategoryDao;
        this.wordDao = wordDao;
    }

    public LiveData<List<Category>> getAllCategories() {
        return Transformations.map(categoryDao.getAllWithChildren(), this::mapCategories);
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
        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            Future<List<CategoryDto>> catFuture = pool.submit(() -> {
                Response<ListResponse<CategoryDto>> r = api.getCategories().execute();
                return r.isSuccessful() && r.body() != null ? r.body().getData() : Collections.emptyList();
            });
            Future<List<SubcategoryDto>> subFuture = pool.submit(() -> {
                Response<ListResponse<SubcategoryDto>> r = api.getSubcategories(null).execute();
                return r.isSuccessful() && r.body() != null ? r.body().getData() : Collections.emptyList();
            });
            Future<List<VocabularyDto>> vocabFuture = pool.submit(() -> {
                Response<ListResponse<VocabularyDto>> r = api.getVocabularies(null, null, null).execute();
                return r.isSuccessful() && r.body() != null ? r.body().getData() : Collections.emptyList();
            });

            List<CategoryDto> cats = catFuture.get();
            List<SubcategoryDto> subs = subFuture.get();
            List<VocabularyDto> vocabs = vocabFuture.get();

            wordDao.deleteAll();
            subcategoryDao.deleteAll();
            categoryDao.deleteAll();
            categoryDao.insertAll(toCategoryEntities(cats));
            subcategoryDao.insertAll(toSubcategoryEntities(subs));
            wordDao.insertAll(toWordEntities(vocabs));
        } catch (Exception ignored) {
        } finally {
            pool.shutdown();
        }
    }

    public void addFavorite(long wordId) {
        try {
            api.addFavorite(wordId).execute();
            wordDao.setFavorite(wordId, true);
        } catch (Exception ignored) {}
    }

    public void removeFavorite(long wordId) {
        try {
            api.removeFavorite(wordId).execute();
            wordDao.setFavorite(wordId, false);
        } catch (Exception ignored) {}
    }

    private List<Category> mapCategories(List<CategoryWithChildren> rows) {
        if (rows == null) return new ArrayList<>();
        List<Category> result = new ArrayList<>();
        for (CategoryWithChildren row : rows) {
            result.add(mapCategory(row));
        }
        return result;
    }

    private Category mapCategory(CategoryWithChildren row) {
        if (row == null) return null;
        Category cat = new Category();
        cat.id = String.valueOf(row.category.id);
        cat.jp = orEmpty(row.category.nameJp);
        cat.en = orEmpty(row.category.nameEn);
        cat.ch = orEmpty(row.category.nameRomaji);
        cat.bg = orEmpty(row.category.bgUrl);
        cat.iconUrl = orEmpty(row.category.iconUrl);
        cat.img = orEmpty(row.category.iconThumbnailUrl);
        cat.locked = row.category.isPremium;
        for (SubcategoryWithWords subRow : row.subcategories) {
            Subcategory sub = mapSubcategory(subRow);
            cat.subcategories.add(sub);
            cat.count += sub.total;
        }
        return cat;
    }

    private Subcategory mapSubcategory(SubcategoryWithWords row) {
        Subcategory sub = new Subcategory();
        sub.id = String.valueOf(row.subcategory.id);
        sub.jp = orEmpty(row.subcategory.nameJp);
        sub.en = orEmpty(row.subcategory.nameEn);
        sub.bg = orEmpty(row.subcategory.iconThumbnailBg);
        sub.img = orEmpty(row.subcategory.iconThumbnailUrl);
        sub.iconUrl = orEmpty(row.subcategory.iconUrl);
        sub.locked = row.subcategory.isPremium;
        sub.words = mapWords(row.words);
        sub.total = sub.words.size();
        return sub;
    }

    private List<Word> mapWords(List<WordEntity> entities) {
        if (entities == null) return new ArrayList<>();
        List<Word> result = new ArrayList<>();
        for (WordEntity e : entities) {
            Word w = new Word();
            w.kanji = orEmpty(e.wordJp);
            w.reading = orEmpty(e.wordJp);
            w.romaji = orEmpty(e.wordRomaji);
            w.en = orEmpty(e.wordEn);
            w.img = orEmpty(e.imageUrl);
            w.example_jp = orEmpty(e.sentenceJp);
            w.example_romaji = orEmpty(e.sentenceRomaji);
            w.example_en = orEmpty(e.sentenceEn);
            w.jlpt = "";
            w.type = "";
            w.xp = 0;
            w.maxMastery = 5;
            w.favorite = e.isFavorite;
            result.add(w);
        }
        return result;
    }

    private List<CategoryEntity> toCategoryEntities(List<CategoryDto> dtos) {
        List<CategoryEntity> result = new ArrayList<>();
        for (CategoryDto dto : dtos) {
            CategoryEntity e = new CategoryEntity();
            e.id = dto.id;
            e.nameJp = dto.nameJp;
            e.nameEn = dto.nameEn;
            e.nameRomaji = dto.nameRomaji;
            e.iconUrl = dto.iconUrl;
            e.iconThumbnailUrl = dto.iconThumbnailUrl;
            e.bgUrl = dto.bgUrl;
            e.isPremium = dto.isPremium;
            result.add(e);
        }
        return result;
    }

    private List<SubcategoryEntity> toSubcategoryEntities(List<SubcategoryDto> dtos) {
        List<SubcategoryEntity> result = new ArrayList<>();
        for (SubcategoryDto dto : dtos) {
            SubcategoryEntity e = new SubcategoryEntity();
            e.id = dto.id;
            e.categoryId = dto.vocabCategoryId;
            e.nameJp = dto.nameJp;
            e.nameEn = dto.nameEn;
            e.nameRomaji = dto.nameRomaji;
            e.iconUrl = dto.iconUrl;
            e.iconThumbnailUrl = dto.iconThumbnailUrl;
            e.iconThumbnailBg = dto.iconThumbnailBg;
            e.isPremium = dto.isPremium;
            result.add(e);
        }
        return result;
    }

    private List<WordEntity> toWordEntities(List<VocabularyDto> dtos) {
        List<WordEntity> result = new ArrayList<>();
        for (VocabularyDto dto : dtos) {
            WordEntity e = new WordEntity();
            e.id = dto.id;
            e.subcategoryId = dto.vocabSubcategoryId;
            e.wordJp = dto.wordJp;
            e.wordRomaji = dto.wordRomaji;
            e.wordEn = dto.wordEn;
            e.sentenceJp = dto.sentenceJp;
            e.sentenceRomaji = dto.sentenceRomaji;
            e.sentenceEn = dto.sentenceEn;
            e.audioJpUrl = dto.audioJpUrl;
            e.imageUrl = dto.imageUrl;
            e.isPremium = dto.isPremium;
            result.add(e);
        }
        return result;
    }

    private String orEmpty(String s) {
        return s != null ? s : "";
    }
}
