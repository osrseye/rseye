package com.basketbandit.rseye.rest;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.AssetManager;
import com.basketbandit.rseye.Utils;
import com.basketbandit.rseye.entity.Item;
import com.basketbandit.rseye.entity.Monster;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.entity.Quest;
import com.basketbandit.rseye.entity.event.*;
import com.basketbandit.rseye.socket.MapSocketHandler;
import com.basketbandit.rseye.socket.UpdateType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class PostController {
    @PostMapping("/api/v1/login_update/")
    public void loginUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        player.setLoginState(data.get("state").getAsString());
        MapSocketHandler.broadcastUpdate(UpdateType.LOGIN_UPDATE, player);
    }

    @PostMapping("/api/v1/position_update/")
    public void positionUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        JsonObject p = data.get("position").getAsJsonObject();
        player.setPosition(
            p.get("plane").getAsInt(),
            p.get("x").getAsInt(),
            p.get("y").getAsInt(),
            (p.get("x").getAsInt()-1024)*4,
            (256*178) - ((p.get("y").getAsInt()-1215)*4)
        );
        MapSocketHandler.broadcastUpdate(UpdateType.POSITION_UPDATE, player);
    }

    @PostMapping("/api/v1/stat_update/") // mapping remains until runeline plugin changed
    public void skillUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        HashMap<String, HashMap<String, Integer>> skills = player.skills().skills();
        HashMap<String, Integer> diff = new HashMap<>();
        AtomicBoolean emitStatData = new AtomicBoolean(false);
        data.get("statChanges").getAsJsonArray().forEach(s -> {
            JsonObject skillUpdate = s.getAsJsonObject();
            String skill = skillUpdate.get("skill").getAsString();
            HashMap<String, Integer> statCurrent = skills.get(skill);

            // determine if the player has leveled-up a stat
            if(statCurrent.get("level") != 0 && statCurrent.get("level") < skillUpdate.get("level").getAsInt()) {
                Application.growthFeed.add(new GrowthEvent(player.username(), Utils.toPascal(skillUpdate.get("skill").getAsString()), skillUpdate.get("level").getAsString()));
                MapSocketHandler.broadcastUpdate(UpdateType.SKILL_UPDATE, player);
                emitStatData.set(true);
            }

            // if the current level is 0, it's safe to assume we're about to update it - so emit.
            if(statCurrent.get("level") == 0) {
                emitStatData.set(true);
            }

            // determine if the players current prayer or hitpoints have changed
            if((skill.equals("HITPOINTS") || skill.equals("PRAYER")) && statCurrent.get("boostedLevel") != skillUpdate.get("boostedLevel").getAsInt()) {
                MapSocketHandler.broadcastUpdate(UpdateType.STATUS_UPDATE, player);
            }

            // calculate exp differences
            if(statCurrent.get("level") != 0) {
                int diffInt = skillUpdate.get("xp").getAsInt() - statCurrent.get("xp");
                if(diffInt > 0) {
                    diff.put(skill, skillUpdate.get("xp").getAsInt() - statCurrent.get("xp"));
                }
            }

            // update skills with new data
            skills.put(skillUpdate.get("skill").getAsString(), new HashMap<>(){{
                put("level", skillUpdate.get("level").getAsInt());
                put("xp", skillUpdate.get("xp").getAsInt());
                put("boostedLevel", skillUpdate.get("boostedLevel").getAsInt());
            }});
        });

        int totalLevel = 0;
        for(HashMap<String, Integer> skill: skills.values()) {
            totalLevel += skill.get("level");
        }

        player.setSkills(new Player.Skills(totalLevel, data.get("combatLevel").getAsInt(), skills));

        // submit exp update
        if(!diff.isEmpty()) {
            MapSocketHandler.broadcastUpdate(UpdateType.EXP_UPDATE, new ExperienceEvent(player.username(), diff));
        }

        // submit stat data
        if(emitStatData.get()) {
            MapSocketHandler.broadcastUpdate(UpdateType.SKILL_DATA, player);
        }
    }

    @PostMapping("/api/v1/bank_update/")
    public void bankUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        Integer value = 0;
        JsonArray inventoryArray = data.get("items").getAsJsonArray();
        ArrayList<Item> bank = new ArrayList<>();
        inventoryArray.forEach(i -> {
            JsonObject o = i.getAsJsonObject();
            Item item = new Item(AssetManager.items.getOrDefault(o.get("id").getAsString(), AssetManager.items.get("0")));
            item.setQuantity(item.isPlaceholder() ? 0 : o.get("quantity").getAsInt());
            if(!item.equals(AssetManager.items.get("0"))) {
                bank.add(item);
            }
        });

        player.setBank(new Player.Bank(value, bank));
        MapSocketHandler.broadcastUpdate(UpdateType.BANK_UPDATE, player);
    }

    @PostMapping("/api/v1/equipment_update/")
    public void equipmentUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        JsonObject equippedItemObject = data.get("items").getAsJsonObject();
        Set<String> equippedItemKeySet = equippedItemObject.keySet();
        HashMap<String, Item> equipped = new HashMap<>();
        equippedItemKeySet.forEach(slot -> {
            JsonObject obj = equippedItemObject.get(slot).getAsJsonObject();
            Item item = new Item(AssetManager.items.getOrDefault(obj.get("id").getAsString(), AssetManager.items.get("0")));
            item.setQuantity(obj.get("quantity").getAsInt());
            equipped.put(slot, item);
        });

        player.setEquipment(new Player.Equipment(equipped));
        MapSocketHandler.broadcastUpdate(UpdateType.EQUIPMENT_UPDATE, player);
    }

    @PostMapping("/api/v1/inventory_update/")
    public void inventoryUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        JsonArray inventoryArray = data.get("items").getAsJsonArray();
        ArrayList<Item> inventory = new ArrayList<>();
        inventoryArray.forEach(slot -> {
            JsonObject o = slot.getAsJsonObject();
            Item item = new Item(AssetManager.items.getOrDefault(o.get("id").getAsString(), AssetManager.items.get("0")));
            item.setQuantity(o.get("quantity").getAsInt());
            inventory.add(item);
        });

        player.setInventory(new Player.Inventory(inventory));
        MapSocketHandler.broadcastUpdate(UpdateType.INVENTORY_UPDATE, player);
    }

    @PostMapping("/api/v1/quest_update/")
    public void questUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        JsonArray questArray = data.get("questChanges").getAsJsonArray();
        if(questArray.size() < 3) {
            for(int i = 0; i < questArray.size(); i++) {
                JsonObject quest = questArray.get(i).getAsJsonObject();
                Application.questFeed.add(new QuestEvent(player.username(), quest.get("name").getAsString(), quest.get("state").getAsString()));
                MapSocketHandler.broadcastUpdate(UpdateType.QUEST_UPDATE, player);
            }
        }

        HashMap<Integer, Quest> quests = player.quests().quests();
        questArray.forEach(q -> {
            JsonObject quest = q.getAsJsonObject();
            quests.put(quest.get("id").getAsInt(), new Quest(quest.get("id").getAsInt(), quest.get("name").getAsString(), quest.get("state").getAsString()));
        });

        player.setQuests(new Player.Quests(data.get("questPoints").getAsInt(), quests));
        MapSocketHandler.broadcastUpdate(UpdateType.QUEST_DATA, player);
    }

    @PostMapping("/api/v1/loot_update/")
    public void lootUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        ArrayList<Item> loot = new ArrayList<>();

        data.get("items").getAsJsonArray().forEach(slot -> {
            JsonObject o = slot.getAsJsonObject();
            Item item = new Item(AssetManager.items.getOrDefault(o.get("id").getAsString(), AssetManager.items.get("0")));
            item.setQuantity(o.get("quantity").getAsInt());
            loot.add(item);
        });

        switch(data.get("lootType").getAsString()) {
            case "NPC", "Player" -> {
                Item weapon = player.equipment().equipped().getOrDefault("WEAPON", null);
                Monster monster = AssetManager.monsters.getOrDefault(data.get("entityId").getAsString(), AssetManager.monsters.get("0"));
                //player.lootTracker().trackLoot(monster, loot); // update loot trackers
                Application.globalLootTracker.trackLoot(monster, loot); // update loot trackers
                Application.combatFeed.add(new CombatEvent(player.username(), weapon, monster, loot));
                MapSocketHandler.broadcastUpdate(UpdateType.COMBAT_LOOT_UPDATE, player);
            }
            case "Barrows", "Theatre of Blood", "Chambers of Xeric", "Tombs of Amascut" -> {
                Application.raidFeed.add(new RaidEvent(player.username(), data.get("lootType").getAsString(), loot));
                MapSocketHandler.broadcastUpdate(UpdateType.RAID_LOOT_UPDATE, player);
            }
        }
    }

    @PostMapping("/api/v1/overhead_update/")
    public void overheadUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        String overhead = data.has("overhead") ? data.get("overhead").getAsString() : "null";
        player.setOverhead(new Player.Overhead(overhead));
        MapSocketHandler.broadcastUpdate(UpdateType.OVERHEAD_UPDATE, new OverheadEvent(player.username(), player.overhead().overhead()));
    }

    @PostMapping("/api/v1/skull_update/")
    public void skullUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        String skull = data.has("skull") ? data.get("skull").getAsString() : "null";
        player.setSkull(new Player.Skull(skull));
        MapSocketHandler.broadcastUpdate(UpdateType.SKULL_UPDATE, new SkullEvent(player.username(), player.skull().skull()));
    }

    @PostMapping("/api/v1/death_update/")
    public void deathUpdate(@RequestAttribute("player") Player player, @RequestAttribute("object") JsonObject data) {
        MapSocketHandler.broadcastUpdate(UpdateType.DEATH_UPDATE, player);
    }
}
