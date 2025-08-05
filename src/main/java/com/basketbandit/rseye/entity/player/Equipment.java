package com.basketbandit.rseye.entity.player;

import com.basketbandit.rseye.entity.Item;

import java.util.HashMap;

public record Equipment(HashMap<String, Item> equipped) {
    public Equipment() {
        this(new HashMap<>());
    }
}
