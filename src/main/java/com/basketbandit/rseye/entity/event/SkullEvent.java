package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.Player;

public record SkullEvent(Player.Username username, String skull) implements Event {
}
