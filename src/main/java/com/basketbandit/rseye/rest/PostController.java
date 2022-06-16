package com.basketbandit.rseye.rest;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.AssetManager;
import com.basketbandit.rseye.entity.Item;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.entity.fragment.*;
import com.basketbandit.rseye.socket.MapSocketHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class PostController {
    @PostMapping("/api/v1/login_state/")
    public void login(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            updatePlayerInfo("login_state", object);
        }
    }

    @PostMapping("/api/v1/level_change/")
    public void level(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player;
            if((player = updatePlayerInfo("level_change", object)) == null) {
                return;
            }

            JsonObject obj = object.get("data").getAsJsonObject();
            int totalLevel = obj.get("totalLevel").getAsInt();
            JsonObject statsObject = obj.get("levels").getAsJsonObject();
            Set<String> statKeySet = statsObject.keySet();
            HashMap<String, Integer> skills = new HashMap<>();
            statKeySet.forEach(stat -> skills.put(stat, statsObject.get(stat).getAsInt()));
            player.stats = new PlayerStats(totalLevel-1, skills);
        }
    }

    @PostMapping("/api/v1/bank/")
    public void bank(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player;
            if((player = updatePlayerInfo("bank", object)) == null) {
                return;
            }

            JsonObject obj = object.get("data").getAsJsonObject();
            Integer value = obj.get("value").getAsInt();
            JsonArray inventoryArray = obj.get("items").getAsJsonArray();
            ArrayList<Item> bank = new ArrayList<>();
            inventoryArray.forEach(i -> {
                JsonObject o = i.getAsJsonObject();
                Item item = AssetManager.items.get(o.get("id").getAsString());
                item.quantity = o.get("quantity").getAsInt();
                bank.add(item);
            });
            player.bank = new PlayerBank(value, bank);
            System.out.println(body);
        }
    }

    @PostMapping("/api/v1/equipped_items/")
    public void equipment(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player;
            if((player = updatePlayerInfo("equipped_items", object)) == null) {
                return;
            }

            JsonObject equippedItemObject = object.get("data").getAsJsonObject().get("equippedItems").getAsJsonObject();
            Set<String> equippedItemKeySet = equippedItemObject.keySet();
            HashMap<String, Item> equipped = new HashMap<>();
            equippedItemKeySet.forEach(slot -> {
                JsonObject equipmentItem = equippedItemObject.get(slot).getAsJsonObject();
                Item item = AssetManager.items.get(equipmentItem.get("id").getAsString());
                item.quantity = equipmentItem.get("quantity").getAsInt();
                equipped.put(slot, item);
            });
            player.equipment = new PlayerEquipment(equipped);
        }
    }

    @PostMapping("/api/v1/inventory_items/")
    public void inventory(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player;
            if((player = updatePlayerInfo("inventory_items", object)) == null) {
                return;
            }

            JsonArray inventoryArray = object.get("data").getAsJsonObject().get("inventory").getAsJsonArray();
            ArrayList<Item> inventory = new ArrayList<>();
            inventoryArray.forEach(slot -> {
                JsonObject o = slot.getAsJsonObject();
                Item item = AssetManager.items.get(o.get("id").getAsString());
                item.quantity = o.get("quantity").getAsInt();
                inventory.add(item);
            });
            player.inventory = new PlayerInventory(inventory);
        }
    }

    @PostMapping("/api/v1/quest_change/")
    public void quest(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player;
            if((player = updatePlayerInfo("quest_change", object)) == null) {
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
            player.quests = new PlayerQuests(questPoints, quests);
        }
    }

    @PostMapping("/api/v1/npc_kill/")
    public void kill(@RequestHeader("Authorization") String token, @RequestBody String body) {
        if(AssetManager.tokens.contains(token)) {
            JsonObject object = JsonParser.parseString(body).getAsJsonObject();
            Player player;
            if((player = updatePlayerInfo("npc_kill", object)) == null) {
                return;
            }
        }
    }

    /**
     * Updates players current info
     * @param object {@link JsonObject}
     */
    private Player updatePlayerInfo(String updateType, JsonObject object) {
        if(!object.has("playerInfo")) {
            return null;
        }

        JsonObject info = object.get("playerInfo").getAsJsonObject();
        JsonObject position = info.get("position").getAsJsonObject();

        if(!info.has("username")) {
            return null;
        }

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
        if(Application.players.containsKey(playerInfo.username())) {
            Application.players.get(playerInfo.username()).info = playerInfo;
        } else {
            Player player = new Player();
            player.info = playerInfo;
            Application.players.put(playerInfo.username(), player);
            MapSocketHandler.broadcastNewPlayer(playerInfo);
        }

        MapSocketHandler.updateBroadcast(updateType, playerInfo);
        return Application.players.get(playerInfo.username());
    }
}
