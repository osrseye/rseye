package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.Item;
import com.basketbandit.rseye.entity.Monster;

import java.util.ArrayList;

public record CombatEvent(String player, Item weapon, Monster monster, ArrayList<Item> loot) {
}
