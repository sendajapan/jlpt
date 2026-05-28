package com.scholarlyapps.pathlingo.data;

import android.content.Context;

import com.scholarlyapps.pathlingo.data.remote.ServiceLocator;
import com.scholarlyapps.pathlingo.data.remote.dto.AppUserDto;
import com.scholarlyapps.pathlingo.data.remote.dto.CategoryDto;
import com.scholarlyapps.pathlingo.data.remote.dto.SubcategoryDto;
import com.scholarlyapps.pathlingo.data.remote.dto.VocabularyDto;
import com.scholarlyapps.pathlingo.data.repo.CatalogData;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.Subcategory;
import com.scholarlyapps.pathlingo.models.User;
import com.scholarlyapps.pathlingo.models.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataManager {

    private static volatile DataManager instance;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private volatile User user;
    private final List<Category> categories = new ArrayList<>();

    public volatile boolean isReady = false;

    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null) {
            synchronized (DataManager.class) {
                if (instance == null) {
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    public void loadData(Context context) {
        ServiceLocator.init(context);
        executor.execute(this::refresh);
    }

    public synchronized void refresh() {
        ApiResult<CatalogData> catalog = ServiceLocator.catalogRepository.loadAll();
        ApiResult<AppUserDto> me = ServiceLocator.userRepository.me();

        if (catalog.isSuccess()) {
            CatalogData data = catalog.getData();
            List<Category> built = buildCategories(data.categories, data.subcategories, data.vocabularies);
            categories.clear();
            categories.addAll(built);
        }
        if (me.isSuccess()) {
            user = mapUser(me.getData());
        }
        isReady = true;
    }

    public User getUser() {
        return user;
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Category getCategoryById(String id) {
        for (Category c : categories) {
            if (c.id != null && c.id.equals(id)) return c;
        }
        return null;
    }

    private List<Category> buildCategories(List<CategoryDto> cats, List<SubcategoryDto> subs, List<VocabularyDto> vocabs) {
        Map<Long, List<SubcategoryDto>> subsByCategory = new HashMap<>();
        for (SubcategoryDto s : subs) {
            subsByCategory.computeIfAbsent(s.vocabCategoryId, k -> new ArrayList<>()).add(s);
        }
        Map<Long, List<VocabularyDto>> vocabsBySubcategory = new HashMap<>();
        for (VocabularyDto v : vocabs) {
            vocabsBySubcategory.computeIfAbsent(v.vocabSubcategoryId, k -> new ArrayList<>()).add(v);
        }

        List<Category> result = new ArrayList<>();
        for (CategoryDto c : cats) {
            Category cat = new Category();
            cat.id = String.valueOf(c.id);
            cat.jp = c.nameJp != null ? c.nameJp : "";
            cat.en = c.nameEn != null ? c.nameEn : "";
            cat.ch = c.nameRomaji != null ? c.nameRomaji : "";
            cat.bg = c.bgUrl != null ? c.bgUrl : "";
            cat.img = c.iconThumbnailUrl != null ? c.iconThumbnailUrl : "";
            cat.iconUrl = c.iconUrl != null ? c.iconUrl : "";
            cat.progress = 0;
            cat.locked = c.isPremium;

            List<SubcategoryDto> mySubs = subsByCategory.getOrDefault(c.id, Collections.emptyList());
            for (SubcategoryDto s : mySubs) {
                Subcategory sub = new Subcategory();
                sub.id = String.valueOf(s.id);
                sub.jp = s.nameJp != null ? s.nameJp : "";
                sub.en = s.nameEn != null ? s.nameEn : "";
                sub.bg = s.iconThumbnailBg != null ? s.iconThumbnailBg : "";
                sub.img = s.iconThumbnailUrl != null ? s.iconThumbnailUrl : "";
                sub.iconUrl = s.iconUrl != null ? s.iconUrl : "";
                sub.mastered = 0;
                sub.locked = s.isPremium;

                List<VocabularyDto> words = vocabsBySubcategory.getOrDefault(s.id, Collections.emptyList());
                for (VocabularyDto v : words) {
                    sub.words.add(mapWord(v));
                }
                sub.total = sub.words.size();
                cat.subcategories.add(sub);
                cat.count += sub.total;
            }
            result.add(cat);
        }
        return result;
    }

    private Word mapWord(VocabularyDto v) {
        Word w = new Word();
        w.kanji = v.wordJp != null ? v.wordJp : "";
        w.reading = v.wordJp != null ? v.wordJp : "";
        w.romaji = v.wordRomaji != null ? v.wordRomaji : "";
        w.en = v.wordEn != null ? v.wordEn : "";
        w.img = v.imageUrl != null ? v.imageUrl : "";
        w.jlpt = "";
        w.type = "";
        w.xp = 0;
        w.example_jp = v.sentenceJp != null ? v.sentenceJp : "";
        w.example_romaji = v.sentenceRomaji != null ? v.sentenceRomaji : "";
        w.example_en = v.sentenceEn != null ? v.sentenceEn : "";
        w.mastery = 0;
        w.maxMastery = 5;
        w.correct = false;
        w.favorite = false;
        return w;
    }

    private User mapUser(AppUserDto dto) {
        User u = new User();
        u.name = dto.name != null ? dto.name : "";
        u.jpName = dto.username != null ? dto.username : "";
        u.level = dto.currentLevel;
        u.xp = dto.xpPoints;
        u.maxXp = 100;
        u.streak = dto.currentStreak;
        u.wordsKnown = dto.learnedWords;
        u.accuracy = 0;
        u.email = dto.email != null ? dto.email : "";
        return u;
    }
}
