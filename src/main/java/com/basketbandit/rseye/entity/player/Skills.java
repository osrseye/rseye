package com.basketbandit.rseye.entity.player;

import com.basketbandit.rseye.DataManager;
import com.basketbandit.rseye.entity.Skill;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record Skills(Integer totalLevel, Integer combatLevel, ConcurrentHashMap<String, Skill> skills) {
    public Skills() {
        this(38, 3, new ConcurrentHashMap<>(Map.ofEntries(
            Map.entry("ATTACK", new Skill("Attack", 0, 0, 0)),
            Map.entry("HITPOINTS", new Skill("Hitpoints", 0, 0, 0)),
            Map.entry("MINING", new Skill("Mining", 0, 0, 0)),
            Map.entry("STRENGTH", new Skill("Strength", 0, 0, 0)),
            Map.entry("AGILITY", new Skill("Agility", 0, 0, 0)),
            Map.entry("SMITHING", new Skill("Smithing", 0, 0, 0)),
            Map.entry("DEFENCE", new Skill("Defence", 0, 0, 0)),
            Map.entry("HERBLORE", new Skill("Herblore", 0, 0, 0)),
            Map.entry("FISHING", new Skill("Fishing", 0, 0, 0)),
            Map.entry("RANGED", new Skill("Ranged", 0, 0, 0)),
            Map.entry("THIEVING", new Skill("Thieving", 0, 0, 0)),
            Map.entry("COOKING", new Skill("Cooking", 0, 0, 0)),
            Map.entry("PRAYER", new Skill("Prayer", 0, 0, 0)),
            Map.entry("CRAFTING", new Skill("Crafting", 0, 0, 0)),
            Map.entry("FIREMAKING", new Skill("Firemaking", 0, 0, 0)),
            Map.entry("MAGIC", new Skill("Magic", 0, 0, 0)),
            Map.entry("FLETCHING", new Skill("Fletching", 0, 0, 0)),
            Map.entry("WOODCUTTING", new Skill("Woodcutting", 0, 0, 0)),
            Map.entry("RUNECRAFT", new Skill("Runecraft", 0, 0, 0)),
            Map.entry("SLAYER", new Skill("Slayer", 0, 0, 0)),
            Map.entry("FARMING", new Skill("Farming", 0, 0, 0)),
            Map.entry("CONSTRUCTION", new Skill("Construction", 0, 0, 0)),
            Map.entry("HUNTER", new Skill("Hunter", 0, 0, 0))
        )));
    }

    public Collection<Skill> skillsAsCollection() {
        return skills.values();
    }

    public String summary(String key) {
        Skill skill = skills.getOrDefault(key, new Skill("Missing", 0, 0, 0));
        int xpForNext = xpForNext(skill.level());
        int xpUntilNext = xpUntilNext(skill.level(), skill.xp());
        return "%s XP: %,d\nNext level at: %,d\nRemaining XP: %,d".formatted(skill.name(), skill.xp(), xpForNext, xpUntilNext);
    }

    public float percentageThroughLevel(String key) {
        Skill skill = skills.getOrDefault(key, new Skill("Missing", 0, 0, 0));
        if(skill.xp() == 0) {
            return 0;
        }
        float difference = xpDifferenceBetween(xpFor(skill.level()), xpForNext(skill.level()));
        float xpPast = xpPastLevel(skill.level(), skill.xp());
        return (xpPast / difference) * 100;
    }

    public int xpFor(int level) {
        return DataManager.xpTable.get(Math.min(99, Math.max(1, level)));
    }

    public int xpPastLevel(int level, int currentXp) {
        return currentXp - DataManager.xpTable.get(Math.min(99, Math.max(1, level)));
    }

    public int xpForNext(int level) {
        return level == 99 ? 0 : DataManager.xpTable.get(level+1);
    }

    public int xpUntilNext(int level, int currentXp) {
        return level == 99 ? 0 : DataManager.xpTable.get(level+1) - currentXp;
    }

    public int xpDifferenceBetween(int levelSmall, int levelBig) {
        levelSmall = Math.min(200000000, Math.max(0, levelSmall));
        levelBig = Math.min(200000000, Math.max(0, levelBig));
        return levelBig - levelSmall;
    }

    public int levelAtXp(int xp) {
        int level = 1;
        for(Integer key : DataManager.xpTable.keySet()) {
            if(xp > DataManager.xpTable.get(key)) {
                level = key;
                continue;
            }
            break;
        }
        return level;
    }
}
