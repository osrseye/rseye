package com.basketbandit.rseye.entity.event;

import java.util.HashMap;

public record ExperienceEvent(String username, String usernameEncoded, HashMap<String, Integer> data) {
}
