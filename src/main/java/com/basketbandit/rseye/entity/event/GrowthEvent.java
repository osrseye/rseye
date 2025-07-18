package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.Player;

public record GrowthEvent(Player.Username username, String skill, String level) implements Event {
}
