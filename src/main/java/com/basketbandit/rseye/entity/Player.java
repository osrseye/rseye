package com.basketbandit.rseye.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    public record Information(String username, String usernameEncoded, HashMap<String, Integer> position, HashMap<String, Integer> offsetPosition) {
        public Information() {
            this("", "", new HashMap<>(), new HashMap<>());
        }
        public Information(String username, HashMap<String, Integer> position) {
            this(username, username.replace(" ", "_"), position, new HashMap<>() {{
                put("x", (position.get("x")-1152)*4); // mapOffsetX = 1152
                put("y", (256*178 - ((position.get("y")-1215)*4))); // mapPixelHeight = 256*178, mapOffsetY = 1215
                put("plane", position.get("plane"));
            }});
        }
    }
    public record Stats(Integer totalLevel, Integer combatLevel, HashMap<String, HashMap<String, Integer>> stats) {
        public Stats() {
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
    private Information information = new Information();
    private Stats stats = new Stats();
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

    public Information information() {
        return information;
    }

    public Stats stats() {
        return stats;
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
        this.loginState = loginState;
        this.lastUpdate = (loginState.equals("LOGGED_IN") || loginState.equals("HOPPING") || loginState.equals("LOADING")) ? System.currentTimeMillis() : System.currentTimeMillis() - 600000; // make the time 10 minutes in the past so not logged back in
    }

    public void setInformation(Information information) {
        this.information = information;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setStats(Stats stats) {
        this.stats = stats;
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
}
