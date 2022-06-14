package com.basketbandit.rseye.entity.fragment;

import java.util.HashMap;

public record PlayerInfo(String username, String combatLevel, HashMap<String, String> position) {
}
