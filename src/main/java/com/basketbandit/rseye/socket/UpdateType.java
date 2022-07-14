package com.basketbandit.rseye.socket;

public enum UpdateType {
    NEW_PLAYER("new_player"),
    LOGIN_UPDATE("login_update"),
    POSITION_UPDATE("position_update"),
    STATUS_UPDATE("status_update"),
    STAT_UPDATE("stat_update"),
    STAT_DATA("stat_data"),
    EXP_UPDATE("exp_update"),
    BANK_UPDATE("bank_update"),
    EQUIPMENT_UPDATE("equipment_update"),
    INVENTORY_UPDATE("inventory_update"),
    QUEST_UPDATE("quest_update"),
    QUEST_DATA("quest_data"),
    LOOT_UPDATE("loot_update"),
    OVERHEAD_UPDATE("overhead_update"),
    SKULL_UPDATE("skull_update"),
    DEATH_UPDATE("death_update");

    public final String value;
    UpdateType(String value) {
        this.value = value;
    }
}
