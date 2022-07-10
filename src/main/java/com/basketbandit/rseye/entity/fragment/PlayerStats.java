package com.basketbandit.rseye.entity.fragment;

import java.util.HashMap;

public record PlayerStats(Integer totalLevel, HashMap<String, HashMap<String, Integer>> stats) {
}
