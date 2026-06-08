package com.scholarlyapps.pathlingo.models;

import java.util.ArrayList;
import java.util.List;

public class Category {

    public String id;
    public String jp;
    public String en;
    public String ch;
    public String bg;
    public int count;
    public String img;
    public String iconUrl;
    public int progress;
    public boolean locked;
    public int coinPrice;
    public boolean isLocked;

    public List<Subcategory> subcategories = new ArrayList<>();

    public Category() {}
}
