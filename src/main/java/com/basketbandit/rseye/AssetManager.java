package com.basketbandit.rseye;

import com.basketbandit.rseye.entity.Item;
import com.basketbandit.rseye.entity.Monster;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class AssetManager {
    public Logger log = LoggerFactory.getLogger(AssetManager.class);
    public static ArrayList<String> tokens = new ArrayList<>();
    public static HashMap<String, Item> items = new HashMap<>();
    public static HashMap<String, Monster> monsters = new HashMap<>();

    public AssetManager() {
        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/items-cache-data.json"), StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(r, JsonObject.class);
            // non-linked items
            obj.keySet().forEach(id -> {
                JsonObject object = obj.get(id).getAsJsonObject();
                Item item = new Item(
                    id,
                    object.get("name").getAsString(),
                    "/data/icons/items/" + id + ".png",
                    object.get("stackable").getAsBoolean(),
                    object.get("stacked").isJsonNull() ? 0 : object.get("stacked").getAsInt(),
                    object.get("highalch").getAsInt(),
                    object.get("placeholder").getAsBoolean()
                );
                items.put(id, item);
            });
            // linked items
            obj.keySet().forEach(id -> {
                JsonObject object = obj.get(id).getAsJsonObject();
                if(!object.get("linked_id_item").isJsonNull()) {
                    Item item = new Item(
                        id,
                        object.get("name").getAsString(),
                        "/data/icons/items/" + id + ".png",
                        object.get("stackable").getAsBoolean(),
                        object.get("stacked").isJsonNull() ? 0 : object.get("stacked").getAsInt(),
                        object.get("highalch").getAsInt(),
                        object.get("placeholder").getAsBoolean()
                    );
                    String linkedItemID = object.get("linked_id_item").getAsString();
                    items.get(linkedItemID).addLinkedItem(item);
                }
            });
            log.info("Found {} item(s)", items.size());
        } catch(Exception e) {
            log.error("There was an issue loading assets: {}", e.getMessage(), e);
        }

        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/monsters-cache-data.json"), StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(r, JsonObject.class);
            obj.keySet().forEach(key -> monsters.put(key, new Monster(key, obj.get(key).getAsJsonObject().get("name").getAsString(), obj.get(key).getAsJsonObject().get("combatLevel").getAsInt())));
            log.info("Found {} monster(s)", monsters.size());
        } catch(Exception e) {
            log.error("There was an issue loading assets: {}", e.getMessage(), e);
        }

        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/token.txt"), StandardCharsets.UTF_8))) {
            r.lines().forEach(token -> tokens.add(token));
            log.info("Found {} token(s)", tokens.size());
        } catch(Exception e) {
            log.warn("There was an issue while reading the token data file, reason: {}", e.getMessage(), e);
        }
    }
}
