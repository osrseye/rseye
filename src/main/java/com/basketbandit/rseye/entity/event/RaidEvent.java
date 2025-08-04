package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.Utils;
import com.basketbandit.rseye.entity.Item;
import com.basketbandit.rseye.entity.Player;

import java.util.ArrayList;

public record RaidEvent(Player.CombatInfo player, String raid, ArrayList<Item> loot) implements Event {
    public String lootHighAlchValue() {
        int value = 0;
        for(Item item: loot) {
            value += (item.highAlchValue() * item.quantity());
        }
        return Utils.formatNumber(value);
    }
}
