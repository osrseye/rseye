package com.basketbandit.rseye.entity;

public record Monster(String id, String name, int combatLevel) {
    public String identifier() {
        return "%s (level-%s)".formatted(name, combatLevel);
    }
}
