package com.basketbandit.rseye.entity.fragment;

import java.util.HashMap;

public record PlayerInformation(String username, String usernameEncoded, String combatLevel, HashMap<String, String> position) {
}
