package com.basketbandit.rseye.entity.player;

import com.basketbandit.rseye.entity.Item;

import java.util.ArrayList;

public record Bank(Integer value, ArrayList<Item> slots) {
    public Bank() {
        this(0, new ArrayList<>());
    }
}
