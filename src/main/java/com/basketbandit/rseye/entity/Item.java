package com.basketbandit.rseye.entity;

public class Item {
    public String id;
    public String name;
    public String icon;
    public int quantity = -1;

    public Item(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }
}
