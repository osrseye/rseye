package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.player.Username;

public record OverheadEvent(Username username, String overhead) implements Event {
}
