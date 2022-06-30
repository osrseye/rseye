package com.basketbandit.rseye.entity;

import com.basketbandit.rseye.entity.fragment.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private String loginState;
    private PlayerInfo info;
    private PlayerStats stats;
    private PlayerQuests quests;
    private PlayerEquipment equipment;
    private PlayerInventory inventory;
    private PlayerBank bank;
    private long lastUpdate;

    public Player() {
        this.loginState = "LOGGED_OUT";
        this.info = new PlayerInfo("", "", "", new HashMap<>());
        this.stats = new PlayerStats(32, new HashMap<>(){{
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
        this.quests = new PlayerQuests(0, new ArrayList<>());
        this.equipment = new PlayerEquipment(new HashMap<>());
        this.inventory = new PlayerInventory(new ArrayList<>());
        this.bank = new PlayerBank(0, new ArrayList<>());
        this.lastUpdate = System.currentTimeMillis();
    }

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

    public PlayerInfo info() {
        return info;
    }

    public PlayerStats stats() {
        return stats;
    }

    public PlayerQuests quests() {
        return quests;
    }

    public PlayerEquipment equipment() {
        return equipment;
    }

    public PlayerInventory inventory() {
        return inventory;
    }

    public PlayerBank bank() {
        return bank;
    }

    public void setLoginState(String loginState) {
        this.loginState = loginState;
        this.lastUpdate = loginState.equals("LOGGED_IN") ? System.currentTimeMillis() : System.currentTimeMillis() - 600000; // make the time 10 minutes in the past so not logged back in
    }

    public void setInfo(PlayerInfo info) {
        this.info = info;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setStats(PlayerStats stats) {
        this.stats = stats;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setQuests(PlayerQuests quests) {
        this.quests = quests;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setEquipment(PlayerEquipment equipment) {
        this.equipment = equipment;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setInventory(PlayerInventory inventory) {
        this.inventory = inventory;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setBank(PlayerBank bank) {
        this.bank = bank;
        this.lastUpdate = System.currentTimeMillis();
    }
}
