package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.Player;

import java.util.HashMap;

public record ExperienceEvent(Player.Username username, HashMap<String, Integer> data) implements Event {
}
