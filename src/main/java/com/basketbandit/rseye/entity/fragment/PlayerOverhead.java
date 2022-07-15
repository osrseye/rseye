package com.basketbandit.rseye.entity.fragment;

public record PlayerOverhead(String overhead) {
    public PlayerOverhead() {
        this("null");
    }
}
