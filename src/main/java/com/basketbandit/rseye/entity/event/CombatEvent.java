package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.Item;
import com.basketbandit.rseye.entity.Monster;
import com.basketbandit.rseye.entity.Player;

import java.util.ArrayList;

public record CombatEvent(Player.Username username, Item weapon, Monster monster, ArrayList<Item> loot) implements Event {
}
