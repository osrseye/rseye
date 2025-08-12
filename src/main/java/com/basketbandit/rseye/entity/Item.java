package com.basketbandit.rseye.entity;

import com.basketbandit.rseye.Utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

public class Item {
    private final String id;
    private final String name;
    private final String icon;
    private final boolean stackable;
    private final int stacked;
    private final int highAlchValue;
    private int quantity = 0;
    private String quantityFormatted = "0";
    private final boolean isPlaceholder;
    private final HashMap<String, Item> linkedItems;

    /**
     * Default constructor - used on init to generate database of items.
     */
    public Item(String id, String name, String icon, boolean stackable, int stacked, int highAlchValue, boolean isPlaceholder) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.stackable = stackable;
        this.stacked = stacked;
        this.highAlchValue = highAlchValue;
        this.isPlaceholder = isPlaceholder;
        this.linkedItems = new HashMap<>();
    }

    /**
     * Item constructor that allows cloning of an item.
     * @param item {@link Item}
     */
    public Item(Item item) {
        this.id = item.id;
        this.name = item.name;
        this.icon = item.icon;
        this.stackable = item.stackable;
        this.stacked = item.stacked;
        this.quantity = item.quantity;
        this.quantityFormatted = item.quantityFormatted;
        this.highAlchValue = item.highAlchValue;
        this.isPlaceholder = item.isPlaceholder;
        this.linkedItems = item.linkedItems;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String nameEncoded() {
        return URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
    }

    public String icon() {
        if(!stackable || linkedItems.isEmpty()) {
            return icon;
        }
        if(quantity >= stacked) {
            int lastSuitableStack = 0;
            String mostSuitableIcon = icon;
            for(Item item : linkedItems.values()) {
                if(quantity >= item.stacked) {
                    if(lastSuitableStack < item.stacked) {
                        lastSuitableStack = item.stacked;
                        mostSuitableIcon = item.icon;
                    }
                }
            }
            return mostSuitableIcon;
        }
        return icon;
    }

    public boolean stackable() {
        return stackable;
    }

    public int stacked() {
        return stacked;
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

    public void addLinkedItem(Item item) {
        this.linkedItems.put(item.id, item);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return highAlchValue == item.highAlchValue && quantity == item.quantity && isPlaceholder == item.isPlaceholder && Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(icon, item.icon) && Objects.equals(quantityFormatted, item.quantityFormatted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, icon, stackable, stacked, highAlchValue, quantity, quantityFormatted, isPlaceholder);
    }
}
