package com.basketbandit.rseye.entity.event;

public record OverheadEvent(String username, String usernameEncoded, String overhead) implements Event {
}
