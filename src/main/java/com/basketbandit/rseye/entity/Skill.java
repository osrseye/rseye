package com.basketbandit.rseye.entity;

public class Skill {
    private final String name;
    private int level;
    private int boostedLevel;
    private int xp;

    public Skill(String name, int level, int boostedLevel, int xp) {
        this.name = name;
        this.level = level;
        this.boostedLevel = boostedLevel;
        this.xp = xp;
    }

    public String name() {
        return name;
    }

    public int level() {
        return level;
    }

    public int boostedLevel() {
        return boostedLevel;
    }

    public int xp() {
        return xp;
    }

    public String xpFormatted() {
        return String.format("%,d", xp);
    }

    public String icon() {
        return String.format("/data/icons/skill/%s.png", name.toUpperCase());
    }

    public void addExperience(int experience) {
        this.xp += experience;
    }

    public void update(int level, int boostedLevel, int xp) {
        this.level = level;
        this.boostedLevel = boostedLevel;
        this.xp = xp;
    }
};