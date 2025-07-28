package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.Player;

public record DeathEvent(Player.CombatInfo player) implements Event {
}
