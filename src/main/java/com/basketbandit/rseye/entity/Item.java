package com.basketbandit.rseye.entity;

import com.basketbandit.rseye.Utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Item {
    private final String id;
    private final String name;
    private final String nameEncoded;
    private final String icon;
    private final int highAlchValue;
    private int quantity = 0;
    private String quantityFormatted = "0";
    private final boolean isPlaceholder;

    /**
     * Default constructor - used on init to generate database of items.
     */
    public Item(String id, String name, String icon, int highAlchValue, boolean isPlaceholder) {
        this.id = id;
        this.name = name;
        this.nameEncoded = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
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
        this.nameEncoded = item.nameEncoded;
        this.icon = item.icon;
        this.quantity = item.quantity;
        this.quantityFormatted = item.quantityFormatted;
        this.highAlchValue = item.highAlchValue;
        this.isPlaceholder = item.isPlaceholder;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String nameEncoded() {
        return nameEncoded;
    }

    public String icon() {
        return icon;
    }

    public int highAlchValue() {
        return highAlchValue;
    }

    public int quantity() {
        return quantity;
    }

    public String quantityFormatted() {
        return quantityFormatted;
    }

    public boolean isPlaceholder() {
        return isPlaceholder;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.quantityFormatted = Utils.formatNumber(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return highAlchValue == item.highAlchValue && quantity == item.quantity && isPlaceholder == item.isPlaceholder && Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(nameEncoded, item.nameEncoded) && Objects.equals(icon, item.icon) && Objects.equals(quantityFormatted, item.quantityFormatted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameEncoded, icon, highAlchValue, quantity, quantityFormatted, isPlaceholder);
    }
}
