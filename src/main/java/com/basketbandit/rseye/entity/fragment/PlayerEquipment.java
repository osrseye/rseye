package com.basketbandit.rseye.entity.fragment;

import com.basketbandit.rseye.entity.Item;

import java.util.HashMap;

public record PlayerEquipment(HashMap<String, Item> equipped) {
}
