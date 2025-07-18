package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.Player;

public record OverheadEvent(Player.Username username, String overhead) implements Event {
}
