package com.basketbandit.rseye.entity.player;

import com.basketbandit.rseye.entity.Item;

import java.util.ArrayList;

public record Inventory(ArrayList<Item> slots) {
    public Inventory() {
        this(new ArrayList<>());
    }
}
