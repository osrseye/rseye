package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.Player;

public record QuestEvent(Player.Username username, String quest, String state) implements Event  {
}
