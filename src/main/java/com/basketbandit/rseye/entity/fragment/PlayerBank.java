package com.basketbandit.rseye.entity.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public record PlayerBank(Integer value, ArrayList<Map<Integer, Integer>> slots, HashMap<Integer, String> icons) {
}
