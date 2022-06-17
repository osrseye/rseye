package com.basketbandit.rseye.entity;

import com.basketbandit.rseye.entity.fragment.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    public String loginState;
    public PlayerInfo info;
    public PlayerStats stats;
    public PlayerQuests quests;
    public PlayerEquipment equipment;
    public PlayerInventory inventory;
    public PlayerBank bank;

    public Player() {
        this.loginState = "LOGGED_OUT";
        this.info = new PlayerInfo("", "", new HashMap<>());
        this.stats = new PlayerStats(-1, new HashMap<>());
        this.quests = new PlayerQuests(-1, new ArrayList<>());
        this.equipment = new PlayerEquipment(new HashMap<>());
        this.inventory = new PlayerInventory(new ArrayList<>());
        this.bank = new PlayerBank(-1, new ArrayList<>());
    }
}
