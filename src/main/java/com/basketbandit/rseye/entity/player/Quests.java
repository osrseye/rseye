package com.basketbandit.rseye.entity.player;

import com.basketbandit.rseye.entity.Quest;

import java.util.HashMap;

public record Quests(Integer questPoints, HashMap<Integer, Quest> quests) {
    public Quests() {
        this(0, new HashMap<>());
    }
}