package com.basketbandit.rseye.entity;

import com.basketbandit.rseye.Utils;

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

    public void trackLoot(Activity activity, ArrayList<Item> loot) {
        Record record = this.records.getOrDefault(activity.name(), new Record(activity));
        record.trackLoot(loot);
        this.records.put(activity.name(), record);
    }

    public Collection<Record> records() {
        return records.values();
    }

    public boolean hasRecord() {
        return !records.isEmpty();
    }

    public static class Record {
        private final Monster monster;
        private final Activity activity;
        private int kills = 0;
        private final HashMap<String, Item> loot = new HashMap<>();
        private int lootHighAlchValue = 0;

        public Record(Monster monster) {
            this.monster = monster;
            this.activity = null;
        }

        public Record(Activity activity) {
            this.monster = null;
            this.activity = activity;
        }

        public Monster monster() {
            return monster;
        }

        public Activity activity() {
            return activity;
        }

        public String recordType() {
            return monster == null ? "Activity" : "Monster";
        }

        public int kills() {
            return kills;
        }

        public int lootHighAlchValue() {
            return lootHighAlchValue;
        }

        public String lootHighAlchValueFormatted() {
            return Utils.formatNumber(lootHighAlchValue);
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
                lootHighAlchValue += (item.highAlchValue() * item.quantity());
            });
        }
    }
}
