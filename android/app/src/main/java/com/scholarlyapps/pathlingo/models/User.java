package com.scholarlyapps.pathlingo.models;

public class User {

    public String name;
    public String jpName;
    public int level;
    public int xp;
    public int maxXp;
    public int streak;
    public int wordsKnown;
    public int accuracy;
    public String email;
    public String avatarUrl;
    public int coins;

    public User() {
    }

    public User(String name, String jpName, int level, int xp, int maxXp, int streak, int wordsKnown, int accuracy, String email) {
        this.name = name;
        this.jpName = jpName;
        this.level = level;
        this.xp = xp;
        this.maxXp = maxXp;
        this.streak = streak;
        this.wordsKnown = wordsKnown;
        this.accuracy = accuracy;
        this.email = email;
    }
}
