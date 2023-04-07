package com.basketbandit.rseye.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class LootTracker {
    private final ConcurrentHashMap<String, Record> records = new ConcurrentHashMap<>(); // uses ConcurrentHashMap for use in global loot tracking

    public void trackLoot(Monster monster, ArrayList<Item> loot) {
        Record record = this.records.getOrDefault(monster.name(), new Record(monster));
        record.trackLoot(loot);
        this.records.put(monster.name(), record);
    }

    public Collection<Record> records() {
        return records.values();
    }

    public boolean hasRecord() {
        return !records.isEmpty();
    }

    public class Record {
        private final Monster monster;
        private int kills = 0;
        private final HashMap<String, Item> loot = new HashMap<>();

        public Record(Monster monster) {
            this.monster = monster;
        }

        public Monster monster() {
            return monster;
        }

        public int kills() {
            return kills;
        }

        public HashMap<String, Item> loot() {
            return loot;
        }

        public void trackLoot(ArrayList<Item> loot) {
            kills++;
            loot.forEach(item -> {
                if(this.loot.containsKey(item.name())) {
                    Item oldItem = this.loot.get(item.name());
                    oldItem.setQuantity(oldItem.quantity() + item.quantity());
                    this.loot.put(item.name(), oldItem);
                } else {
                    this.loot.put(item.name(), item);
                }
            });
        }
    }
}
