package com.scholarlyapps.pathlingo.models;

import java.util.ArrayList;
import java.util.List;

public class Subcategory {
    public String id;
    public String jp;
    public String en;
    public String bg;
    public int total;
    public int mastered;
    public boolean locked;
    public List<Word> words = new ArrayList<>();

    public Subcategory() {}

    public Subcategory(String id, String jp, String en, String bg, int total, int mastered) {
        this.id = id;
        this.jp = jp;
        this.en = en;
        this.bg = bg;
        this.total = total;
        this.mastered = mastered;
        this.locked = false;
    }
}
