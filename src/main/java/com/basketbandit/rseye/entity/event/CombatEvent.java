package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.Utils;
import com.basketbandit.rseye.entity.Item;
import com.basketbandit.rseye.entity.Monster;
import com.basketbandit.rseye.entity.Player;

import java.util.ArrayList;

public record CombatEvent(Player.CombatInfo player, Item weapon, Monster monster, ArrayList<Item> loot) implements Event {
    public int combatLevelDifference() {
        return monster.combatLevel() - player.combatLevel();
    }

    public String combatLevelDifferenceTextColour() {
        return combatLevelDifference() > 9 ? "#ff0000" :
               combatLevelDifference() > 6 ? "#ff3000" :
               combatLevelDifference() > 3 ? "#ff7000" :
               combatLevelDifference() > 0 ? "#ffb000" :
               combatLevelDifference() == 0 ? "#ffff00" :
               combatLevelDifference() > -4 ? "#c0ff00" :
               combatLevelDifference() > -7 ? "#40ff00" : "#00ff00";
    }

    public String lootHighAlchValue() {
        int value = 0;
        for(Item item: loot) {
            value += (item.highAlchValue() * item.quantity());
        }
        return Utils.formatNumber(value);
    }
}
