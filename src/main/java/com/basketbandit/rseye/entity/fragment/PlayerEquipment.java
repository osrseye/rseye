package com.basketbandit.rseye.entity.fragment;

import java.util.HashMap;
import java.util.Map;

public record PlayerEquipment(HashMap<String, Map<Integer, Integer>> equipped,  HashMap<Integer, String> icons) {
}
