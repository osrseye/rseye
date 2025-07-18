package com.basketbandit.rseye.socket;

public enum UpdateType {
    NEW_PLAYER("new_player"),
    LOGIN_UPDATE("login_update"),
    POSITION_UPDATE("position_update"),
    STATUS_UPDATE("status_update"),
    SKILL_UPDATE("skill_update"),
    SKILL_DATA("skill_data"),
    EXP_UPDATE("exp_update"),
    BANK_UPDATE("bank_update"),
    EQUIPMENT_UPDATE("equipment_update"),
    INVENTORY_UPDATE("inventory_update"),
    QUEST_UPDATE("quest_update"),
    QUEST_DATA("quest_data"),
    COMBAT_LOOT_UPDATE("combat_loot_update"),
    RAID_LOOT_UPDATE("raid_loot_update"),
    OVERHEAD_UPDATE("overhead_update"),
    SKULL_UPDATE("skull_update"),
    DEATH_UPDATE("death_update");

    public final String value;
    UpdateType(String value) {
        this.value = value;
    }
}
