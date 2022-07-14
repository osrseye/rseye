package com.basketbandit.rseye.entity.event;

public record SkullEvent(String username, String usernameEncoded, String skull) implements Event {
}
