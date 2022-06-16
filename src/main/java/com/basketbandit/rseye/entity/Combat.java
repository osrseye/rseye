package com.basketbandit.rseye.entity;

import java.util.ArrayList;

public record Combat(String player, Item weapon, Monster monster, ArrayList<Item> loot) {
}
