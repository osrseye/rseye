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
        this.info = new PlayerInfo("", "", new HashMap<>());
        this.stats = new PlayerStats(-1, new HashMap<>());
        this.quests = new PlayerQuests(-1, new ArrayList<>());
        this.equipment = new PlayerEquipment(new HashMap<>());
        this.inventory = new PlayerInventory(new ArrayList<>());
        this.bank = new PlayerBank(-1, new ArrayList<>());
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
