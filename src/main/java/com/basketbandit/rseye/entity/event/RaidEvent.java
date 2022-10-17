package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.Item;
import com.basketbandit.rseye.entity.Player;

import java.util.ArrayList;

public record RaidEvent(Player player, String raid, ArrayList<Item> loot) {
}
