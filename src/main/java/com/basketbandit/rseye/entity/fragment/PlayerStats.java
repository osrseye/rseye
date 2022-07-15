package com.basketbandit.rseye.entity.fragment;

import java.util.HashMap;

public record PlayerStats(Integer totalLevel, Integer combatLevel, HashMap<String, HashMap<String, Integer>> stats) {
    public PlayerStats() {
        this(38,3, new HashMap<>(){{
            // initialising this map makes life easier
            put("ATTACK", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("HITPOINTS", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("MINING", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("STRENGTH", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("AGILITY", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("SMITHING", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("DEFENCE", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("HERBLORE", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("FISHING", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("RANGED", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("THIEVING", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("COOKING", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("PRAYER", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("CRAFTING", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("FIREMAKING", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("MAGIC", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("FLETCHING", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("WOODCUTTING", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("RUNECRAFT", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("SLAYER", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("FARMING", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("CONSTRUCTION", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
            put("HUNTER", new HashMap<>(){{
                put("level", 0);
                put("xp", 0);
                put("boostedLevel", 0);
            }});
        }});
    }
}
