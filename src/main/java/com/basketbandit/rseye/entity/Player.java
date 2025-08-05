package com.basketbandit.rseye.entity;

import com.basketbandit.rseye.entity.player.*;

public class Player {
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
