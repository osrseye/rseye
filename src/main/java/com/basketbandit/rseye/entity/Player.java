package com.basketbandit.rseye.entity;

import com.basketbandit.rseye.AssetManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    public record Username(String natural, String encoded) {
        public Username() {
            this("", "");
        }
        public Username(String natural) {
            this(natural, natural.replace(" ", "_"));
        }
    }
    public record Position(Integer plane, Integer x, Integer y, Integer offx, Integer offy){
        public Position() {
            // varrock magic numbers
            this(0,3213, 3428, (3213-1024)*4, (256*178-((3428-1215)*4)));
        }
    }
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
            HashMap<String, Integer> skill = skills.get(key);
            String skillNameFormatted = key.charAt(0) + key.substring(1).toLowerCase();
            int currentXp = skill.get("xp");
            int nextXp = AssetManager.xpTable.get(skill.get("level")+1);
            return "%s XP: %,d\nNext level at: %,d\nRemaining XP: %,d".formatted(skillNameFormatted, currentXp, nextXp, (nextXp-currentXp));
        }
        public int xpForLevel(int level) {
            return AssetManager.xpTable.get(Math.max(1, Math.min(level, 99)));
        }
        public int xpForNext(String key) {
            HashMap<String, Integer> skill = skills.get(key);
            if(skill.get("level") == 99) {
                return 0;
            }
            return AssetManager.xpTable.get(skill.get("level")+1);
        }
        public int xpUntilNext(String key) {
            HashMap<String, Integer> skill = skills.get(key);
            if(skill.get("level") == 99) {
                return 0;
            }
            return AssetManager.xpTable.get(skill.get("level")+1) - skill.get("xp");
        }
    }

    public record Quests(Integer questPoints, HashMap<Integer, Quest> quests) {
        public Quests() {
            this(0, new HashMap<>());
        }
    }
    public record Equipment(HashMap<String, Item> equipped) {
        public Equipment() {
            this(new HashMap<>());
        }
    }
    public record Inventory(ArrayList<Item> slots) {
        public Inventory() {
            this(new ArrayList<>());
        }
    }
    public record Bank(Integer value, ArrayList<Item> slots) {
        public Bank() {
            this(0, new ArrayList<>());
        }
    }
    public record Skull(String skull) {
        public Skull() {
            this("null");
        }
    }
    public record Overhead(String overhead) {
        public Overhead() {
            this("null");
        }
    }

    private String loginState = "LOGGED_OUT";
    private Username username = new Username();
    private Position position = new Position();
    private Skills skills = new Skills();
    private Quests quests = new Quests();
    private Equipment equipment = new Equipment();
    private Inventory inventory = new Inventory();
    private Bank bank = new Bank();
    private Skull skull = new Skull();
    private Overhead overhead = new Overhead();
    private LootTracker lootTracker = new LootTracker();
    private long lastUpdate = System.currentTimeMillis();

    /**
     * Logs out players who haven't been updated in the last 5 minutes. (xlogging doesn't send event)
     * @return boolean
     */
    public boolean loginStateChanged() {
        if(loginState.equals("LOGGED_IN") && (System.currentTimeMillis() - lastUpdate) > 300000) {
            loginState = "LOGGED_OUT";
            return true;
        }
        if(loginState.equals("LOGGED_OUT") && (System.currentTimeMillis() - lastUpdate) < 300000) {
            loginState = "LOGGED_IN";
            return true;
        }
        return false;
    }

    public String loginState() {
        return loginState;
    }

    public Username username() {
        return username;
    }

    public Position position() {
        return position;
    }

    public Skills skills() {
        return skills;
    }

    public Quests quests() {
        return quests;
    }

    public Equipment equipment() {
        return equipment;
    }

    public Inventory inventory() {
        return inventory;
    }

    public Bank bank() {
        return bank;
    }

    public Overhead overhead() {
        return overhead;
    }

    public Skull skull() {
        return skull;
    }

    public LootTracker lootTracker() {
        return lootTracker;
    }

    public void setLoginState(String loginState) {
        this.loginState = (loginState.equals("LOGGED_IN") || loginState.equals("HOPPING") || loginState.equals("LOADING")) ? "LOGGED_IN" : loginState;
        this.lastUpdate = this.loginState.equals("LOGGED_IN") ? System.currentTimeMillis() : System.currentTimeMillis() - 600000; // make the time 10 minutes in the past so not logged back in
    }

    public void setUsername(Username username) {
        this.username = username;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setPosition(Integer plane, Integer x, Integer y, Integer offx, Integer offy) {
        this.position = new Position(plane, x, y, offx, offy);
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setQuests(Quests quests) {
        this.quests = quests;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setBank(Bank bank) {
        this.bank = bank;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setOverhead(Overhead overhead) {
        this.overhead = overhead;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setSkull(Skull skull) {
        this.skull = skull;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void resetLootTracker() {
        this.lootTracker = new LootTracker();
    }

    /**********/
    /**********/

    public record BasicInfo(Username username, Position position){}
    public BasicInfo basicInfo() {
        return new BasicInfo(username, position);
    }

    public record CombatInfo(Username username, Integer combatLevel){}
    public CombatInfo combatInfo() {
        return new CombatInfo(username, skills().combatLevel());
    }
}
