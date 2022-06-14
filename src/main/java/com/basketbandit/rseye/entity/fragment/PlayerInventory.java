package com.basketbandit.rseye.entity.fragment;

import java.util.ArrayList;
import java.util.Map;

public record PlayerInventory(ArrayList<Map<Integer, Integer>> slots) {
}
