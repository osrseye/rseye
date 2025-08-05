package com.basketbandit.rseye.entity.player;

import com.basketbandit.rseye.DataManager;

import java.util.HashMap;
import java.util.Map;

public record Skills(Integer totalLevel, Integer combatLevel, HashMap<String, HashMap<String, Integer>> skills) {
    public Skills() {
        this(38, 3, new HashMap<>(Map.ofEntries(
                Map.entry("ATTACK", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("HITPOINTS", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("MINING", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("STRENGTH", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("AGILITY", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("SMITHING", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("DEFENCE", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("HERBLORE", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("FISHING", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("RANGED", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("THIEVING", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("COOKING", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("PRAYER", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("CRAFTING", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("FIREMAKING", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("MAGIC", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("FLETCHING", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("WOODCUTTING", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("RUNECRAFT", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("SLAYER", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("FARMING", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("CONSTRUCTION", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0))),
                Map.entry("HUNTER", new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0)))
        )));
    }

    public String summary(String key) {
        HashMap<String, Integer> skill = skills.getOrDefault(key, new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0)));
        String skillNameFormatted = key.charAt(0) + key.substring(1).toLowerCase();
        int currentXp = skill.get("xp");
        int xpForNext = xpForNext(skill.get("level"));
        int xpUntilNext = xpUntilNext(skill.get("level"), skill.get("xp"));
        return "%s XP: %,d\nNext level at: %,d\nRemaining XP: %,d".formatted(skillNameFormatted, currentXp, xpForNext, xpUntilNext);
    }

    public float percentageThroughLevel(String key) {
        HashMap<String, Integer> skill = skills.getOrDefault(key, new HashMap<>(Map.of("level", 0, "xp", 0, "boostedLevel", 0)));
        if(skill.get("xp") == 0) {
            return 0;
        }
        float difference = xpDifferenceBetween(xpFor(skill.get("level")), xpForNext(skill.get("level")));
        float xpPast = xpPastLevel(skill.get("level"), skill.get("xp"));
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
}
