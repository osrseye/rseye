package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.player.Username;

import java.util.HashMap;

public record ExperienceEvent(Username username, HashMap<String, Integer> data) implements Event {
}
