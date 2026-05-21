package com.scholarlyapps.pathlingo.data;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.scholarlyapps.pathlingo.models.Category;
import com.scholarlyapps.pathlingo.models.Subcategory;
import com.scholarlyapps.pathlingo.models.Word;
import com.scholarlyapps.pathlingo.models.User;

public class DataManager {
    private static DataManager instance;
    private User user;
    private List<Category> categories = new ArrayList<>();
    private JSONObject lessonData;

    private DataManager() {}

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void loadData(Context context) {
        try {
            String jsonData = loadJsonFromAssets(context, "data.json");
            JSONObject root = new JSONObject(jsonData);

            parseUser(root.getJSONObject("user"));
            parseCategories(root.getJSONArray("categories"));
            lessonData = root.optJSONObject("lesson");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String loadJsonFromAssets(Context context, String filename) throws IOException {
        InputStream is = context.getAssets().open(filename);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    private void parseUser(JSONObject userObj) throws JSONException {
        user = new User();
        user.name = userObj.optString("name");
        user.jpName = userObj.optString("jpName");
        user.level = userObj.optInt("level");
        user.xp = userObj.optInt("xp");
        user.maxXp = userObj.optInt("maxXp");
        user.streak = userObj.optInt("streak");
        user.wordsKnown = userObj.optInt("wordsKnown");
        user.accuracy = userObj.optInt("accuracy");
        user.email = userObj.optString("email");
    }

    private void parseCategories(JSONArray categoriesArray) throws JSONException {
        categories.clear();
        for (int i = 0; i < categoriesArray.length(); i++) {
            JSONObject catObj = categoriesArray.getJSONObject(i);
            Category cat = new Category();
            cat.id = catObj.optString("id");
            cat.jp = catObj.optString("jp");
            cat.en = catObj.optString("en");
            cat.ch = catObj.optString("char");
            cat.bg = catObj.optString("bg");
            cat.count = catObj.optInt("count");
            cat.img = catObj.optString("img");
            cat.progress = catObj.optInt("progress");
            cat.locked = catObj.optBoolean("locked", false);

            parseSubcategories(catObj, cat);
            categories.add(cat);
        }
    }

    private void parseSubcategories(JSONObject catObj, Category cat) throws JSONException {
        JSONArray subcatsArray = catObj.optJSONArray("subcategories");
        if (subcatsArray != null) {
            for (int i = 0; i < subcatsArray.length(); i++) {
                JSONObject subcatObj = subcatsArray.getJSONObject(i);
                Subcategory subcat = new Subcategory();
                subcat.id = subcatObj.optString("id");
                subcat.jp = subcatObj.optString("jp");
                subcat.en = subcatObj.optString("en");
                subcat.bg = subcatObj.optString("bg");
                subcat.total = subcatObj.optInt("total");
                subcat.mastered = subcatObj.optInt("mastered");
                subcat.locked = subcatObj.optBoolean("locked", false);

                parseWords(subcatObj, subcat);
                cat.subcategories.add(subcat);
            }
        }
    }

    private void parseWords(JSONObject subcatObj, Subcategory subcat) throws JSONException {
        JSONArray wordsArray = subcatObj.optJSONArray("words");
        if (wordsArray != null) {
            for (int i = 0; i < wordsArray.length(); i++) {
                JSONObject wordObj = wordsArray.getJSONObject(i);
                Word word = new Word();
                word.kanji = wordObj.optString("kanji");
                word.reading = wordObj.optString("reading");
                word.romaji = wordObj.optString("romaji");
                word.en = wordObj.optString("en");
                word.img = wordObj.optString("img");
                word.jlpt = wordObj.optString("jlpt");
                word.type = wordObj.optString("type");
                word.xp = wordObj.optInt("xp");
                word.example_jp = wordObj.optString("example_jp");
                word.example_romaji = wordObj.optString("example_romaji");
                word.example_en = wordObj.optString("example_en");
                word.mastery = wordObj.optInt("mastery");
                word.maxMastery = wordObj.optInt("maxMastery", 5);
                word.correct = wordObj.optBoolean("correct", false);
                word.favorite = wordObj.optBoolean("favorite", false);
                subcat.words.add(word);
            }
        }
    }

    public User getUser() {
        return user;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Category getCategoryById(String id) {
        for (Category cat : categories) {
            if (cat.id.equals(id)) {
                return cat;
            }
        }
        return null;
    }

    public JSONObject getLessonData() {
        return lessonData;
    }
}
