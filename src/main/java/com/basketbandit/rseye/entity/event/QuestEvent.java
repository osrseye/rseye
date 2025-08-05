package com.basketbandit.rseye.entity.event;

import com.basketbandit.rseye.entity.player.Username;

public record QuestEvent(Username username, String quest, String state) implements Event  {
}
