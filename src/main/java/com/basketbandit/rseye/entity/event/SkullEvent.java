package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.player.Username;

public record SkullEvent(Username username, String skull) implements Event {
}
