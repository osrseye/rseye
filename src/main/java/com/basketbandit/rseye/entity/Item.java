package com.basketbandit.rseye.entity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Item {
    public String id;
    public String name;
    public String urlName;
    public String icon;
    public int quantity = -1;
    public String quantityFormatted = "-1";

    public Item(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.urlName = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
        this.icon = icon;
    }

    /**
     * Item constructor that allows cloning of an item.
     * @param item {@link Item}
     */
    public Item(Item item) {
        this.id = item.id;
        this.name = item.name;
        this.urlName = item.urlName;
        this.icon = item.icon;
        this.quantity = item.quantity;
    }
}
