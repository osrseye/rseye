package com.basketbandit.rseye.entity;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Item {
    public String id;
    public String name;
    public String urlName;
    public String icon;
    public int highAlchValue = 0;
    public int quantity = 0;
    public String quantityFormatted = "0";
    public boolean isPlaceholder;

    public Item(String id, String name, String icon, int highAlchValue, boolean isPlaceholder) {
        this.id = id;
        this.name = name;
        this.urlName = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
        this.icon = icon;
        this.highAlchValue = highAlchValue;
        this.isPlaceholder = isPlaceholder;
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
        this.highAlchValue = item.highAlchValue;
        this.isPlaceholder = item.isPlaceholder;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return highAlchValue == item.highAlchValue && quantity == item.quantity && isPlaceholder == item.isPlaceholder && Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(urlName, item.urlName) && Objects.equals(icon, item.icon) && Objects.equals(quantityFormatted, item.quantityFormatted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, urlName, icon, highAlchValue, quantity, quantityFormatted, isPlaceholder);
    }
}
