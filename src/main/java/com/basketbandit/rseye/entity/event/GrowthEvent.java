package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.player.Username;

public record GrowthEvent(Username username, String skill, String level) implements Event {
}
