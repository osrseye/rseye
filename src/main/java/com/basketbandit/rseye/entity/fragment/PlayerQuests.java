package com.basketbandit.rseye.entity.fragment;

import com.basketbandit.rseye.entity.Quest;

import java.util.ArrayList;

public record PlayerQuests(Integer questPoints, ArrayList<Quest> quests) {
}
