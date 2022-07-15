package com.basketbandit.rseye.entity;

import com.basketbandit.rseye.entity.fragment.*;

public class Player {
    private String loginState;
    private PlayerInformation information;
    private PlayerStats stats;
    private PlayerQuests quests;
    private PlayerEquipment equipment;
    private PlayerInventory inventory;
    private PlayerBank bank;
    private PlayerOverhead overhead;
    private PlayerSkull skull;
    private long lastUpdate;

    public Player() {
        this.loginState = "LOGGED_OUT";
        this.information = new PlayerInformation();
        this.stats = new PlayerStats();
        this.quests = new PlayerQuests();
        this.equipment = new PlayerEquipment();
        this.inventory = new PlayerInventory();
        this.bank = new PlayerBank();
        this.overhead = new PlayerOverhead();
        this.skull = new PlayerSkull();
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

    public PlayerInformation information() {
        return information;
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

    public PlayerOverhead overhead() {
        return overhead;
    }

    public PlayerSkull skull() {
        return skull;
    }

    public void setLoginState(String loginState) {
        this.loginState = loginState;
        this.lastUpdate = (loginState.equals("LOGGED_IN") || loginState.equals("HOPPING")) ? System.currentTimeMillis() : System.currentTimeMillis() - 600000; // make the time 10 minutes in the past so not logged back in
    }

    public void setInformation(PlayerInformation information) {
        this.information = information;
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

    public void setOverhead(PlayerOverhead overhead) {
        this.overhead = overhead;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setSkull(PlayerSkull skull) {
        this.skull = skull;
        this.lastUpdate = System.currentTimeMillis();
    }
}
