package com.basketbandit.rseye.entity.player;

public record Username(String natural, String encoded) {
    public Username() {
        this("", "");
    }
    public Username(String natural) {
        this(natural, natural.replace(" ", "_"));
    }
}
