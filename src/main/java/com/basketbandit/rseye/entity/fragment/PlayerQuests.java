package com.basketbandit.rseye.entity.fragment;

import com.basketbandit.rseye.entity.Quest;

import java.util.HashMap;

public record PlayerQuests(Integer questPoints, HashMap<Integer, Quest> quests) {
}
