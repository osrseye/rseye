package com.basketbandit.rseye.rest;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.AssetManager;
import com.basketbandit.rseye.Utils;
import com.basketbandit.rseye.entity.event.Combat;
import com.basketbandit.rseye.entity.Item;
import com.basketbandit.rseye.entity.Monster;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.entity.event.Growth;
import com.basketbandit.rseye.entity.fragment.*;
import com.basketbandit.rseye.socket.MapSocketHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@RestController
public class PostController {
    @PostMapping("/api/v1/login_state/")
    public void login(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player = computePlayer(object);
            if(player == null) {
                return;
            }

            player.setLoginState(object.get("data").getAsJsonObject().get("state").getAsString());

            broadcastUpdate("login_state", player);
        }
    }

    @PostMapping("/api/v1/level_change/")
    public void level(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player = computePlayer(object);
            if(player == null) {
                return;
            }

            object = object.get("data").getAsJsonObject();
            if(object.has("updatedSkillName")) {
                Application.growthFeed.add(new Growth(player.info().username(), object.get("updatedSkillName").getAsString(), object.get("updatedSkillLevel").getAsString()));
                broadcastUpdate("level_change", player);
            }

            int totalLevel = object.get("totalLevel").getAsInt();
            JsonObject statsObject = object.get("levels").getAsJsonObject();
            Set<String> statKeySet = statsObject.keySet();
            HashMap<String, Integer> skills = new HashMap<>();
            statKeySet.forEach(stat -> skills.put(stat, statsObject.get(stat).getAsInt()));
            player.setStats(new PlayerStats(totalLevel-1, skills));

            broadcastUpdate("level_data", player);
        }
    }

    @PostMapping("/api/v1/bank/")
    public void bank(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player = computePlayer(object);
            if(player == null) {
                return;
            }

            JsonObject obj = object.get("data").getAsJsonObject();
            Integer value = obj.get("value").getAsInt();
            JsonArray inventoryArray = obj.get("items").getAsJsonArray();
            ArrayList<Item> bank = new ArrayList<>();
            inventoryArray.forEach(i -> {
                JsonObject o = i.getAsJsonObject();
                Item item = new Item(AssetManager.items.getOrDefault(o.get("id").getAsString(), AssetManager.items.get("0")));
                item.quantity = o.get("quantity").getAsInt();
                item.quantityFormatted = Utils.formatNumber(o.get("quantity").getAsInt());
                bank.add(item);
            });
            player.setBank(new PlayerBank(value, bank));

            broadcastUpdate("bank", player);
        }
    }

    @PostMapping("/api/v1/equipped_items/")
    public void equipment(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player = computePlayer(object);
            if(player == null) {
                return;
            }

            JsonObject equippedItemObject = object.get("data").getAsJsonObject().get("equippedItems").getAsJsonObject();
            Set<String> equippedItemKeySet = equippedItemObject.keySet();
            HashMap<String, Item> equipped = new HashMap<>();
            equippedItemKeySet.forEach(slot -> {
                JsonObject obj = equippedItemObject.get(slot).getAsJsonObject();
                Item item = new Item(AssetManager.items.getOrDefault(obj.get("id").getAsString(), AssetManager.items.get("0")));
                item.quantity = obj.get("quantity").getAsInt();
                item.quantityFormatted = Utils.formatNumber(obj.get("quantity").getAsInt());
                equipped.put(slot, item);
            });
            player.setEquipment(new PlayerEquipment(equipped));

            broadcastUpdate("equipped_items", player);
        }
    }

    @PostMapping("/api/v1/inventory_items/")
    public void inventory(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player = computePlayer(object);
            if(player == null) {
                return;
            }

            JsonArray inventoryArray = object.get("data").getAsJsonObject().get("inventory").getAsJsonArray();
            ArrayList<Item> inventory = new ArrayList<>();
            inventoryArray.forEach(slot -> {
                JsonObject o = slot.getAsJsonObject();
                Item item = new Item(AssetManager.items.getOrDefault(o.get("id").getAsString(), AssetManager.items.get("0")));
                item.quantity = o.get("quantity").getAsInt();
                item.quantityFormatted = Utils.formatNumber(o.get("quantity").getAsInt());
                inventory.add(item);
            });
            player.setInventory(new PlayerInventory(inventory));

            broadcastUpdate("inventory_items", player);
        }
    }

    @PostMapping("/api/v1/quest_change/")
    public void quest(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player = computePlayer(object);
            if(player == null) {
                return;
            }

            JsonObject obj = object.get("data").getAsJsonObject();
            Integer questPoints = obj.get("qp").getAsInt();
            ArrayList<Quest> quests = new ArrayList<>();
            JsonArray questArray = obj.get("quests").getAsJsonArray();
            questArray.forEach(q -> {
                JsonObject quest = q.getAsJsonObject();
                quests.add(new Quest(quest.get("id").getAsInt(), quest.get("name").getAsString(), quest.get("state").getAsString()));
            });
            player.setQuests(new PlayerQuests(questPoints, quests));

            broadcastUpdate("quest_change", player);
        }
    }

    @PostMapping("/api/v1/npc_kill/")
    public void combat(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player = computePlayer(object);
            if(player == null) {
                return;
            }

            JsonObject obj = object.get("data").getAsJsonObject();
            JsonArray lootArray = obj.get("items").getAsJsonArray();
            Item weapon = player.equipment().equipped().getOrDefault("WEAPON", null);
            Monster monster = AssetManager.monsters.get(obj.get("npcId").getAsString());
            ArrayList<Item> loot = new ArrayList<>();
            lootArray.forEach(slot -> {
                JsonObject o = slot.getAsJsonObject();
                Item item = new Item(AssetManager.items.getOrDefault(o.get("id").getAsString(), AssetManager.items.get("0")));
                item.quantity = o.get("quantity").getAsInt();
                item.quantityFormatted = Utils.formatNumber(o.get("quantity").getAsInt());
                loot.add(item);
            });

            Application.combatFeed.add(new Combat(player.info().username() + " (level-" + player.info().combatLevel() + ")", weapon, monster, loot));
            if(Application.combatFeed.size() > 100) {
                Application.combatFeed.remove(0);
            }

            broadcastUpdate("npc_kill", player);
        }
    }

    /**
     * Computes {@link Player}
     * @param object {@link JsonObject}
     */
    private Player computePlayer(JsonObject object) {
        if(!object.has("playerInfo") || !object.get("playerInfo").getAsJsonObject().has("username")) {
            return null;
        }

        JsonObject info = object.get("playerInfo").getAsJsonObject();
        JsonObject position = info.get("position").getAsJsonObject();
        PlayerInfo playerInfo = new PlayerInfo(
                info.get("username").getAsString(),
                info.get("combatLevel").getAsString(),
                new HashMap<>() {{
                    put("x", position.get("x").getAsString());
                    put("y", position.get("y").getAsString());
                    put("plane", position.get("plane").getAsString());
                }}
        );

        // can this be done better? p'sure it can!
        Player player;
        if(Application.players.containsKey(playerInfo.username())) {
            player = Application.players.get(playerInfo.username());
            player.setInfo(playerInfo);
        } else {
            player = new Player();
            player.setInfo(playerInfo);
            Application.players.put(playerInfo.username(), player);
            broadcastUpdate("new_player", player);
        }

        return player;
    }

    private void broadcastUpdate(String type, Player player){
        MapSocketHandler.broadcastUpdate(type, player.info());
    }
}
