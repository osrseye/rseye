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

    /**
     * Item constructor that allows cloning of an item.
     * @param item {@link Item}
     */
    public Item(Item item) {
        this.id = item.id;
        this.name = item.name;
        this.icon = item.icon;
        this.quantity = item.quantity;
    }
}
