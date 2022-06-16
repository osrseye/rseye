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
            obj.keySet().forEach(key -> items.put(key, new Item(key, obj.get(key).getAsJsonObject().get("name").getAsString(), null)));
            log.info("Successfully parsed " + items.keySet().size() + " items");
        } catch(Exception e) {
            log.error("There was an issue loading assets: {}", e.getMessage(), e);
        }

        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/icons-items-complete.json"), StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(r, JsonObject.class);
            obj.keySet().forEach(key -> {
                Item item = items.getOrDefault(key, new Item(key, "", ""));
                item.icon = "data:image/png;base64, " + obj.get(key).getAsString();
                items.put(key, item);
            });
            log.info("Successfully parsed " + items.keySet().size() + " item icons");
        } catch(Exception e) {
            log.error("There was an issue loading assets: {}", e.getMessage(), e);
        }

        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/monsters-cache-data.json"), StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(r, JsonObject.class);
            obj.keySet().forEach(key -> monsters.put(key, new Monster(key, obj.get(key).getAsJsonObject().get("name").getAsString())));
            log.info("Successfully parsed " + monsters.keySet().size() + " monsters");
        } catch(Exception e) {
            log.error("There was an issue loading assets: {}", e.getMessage(), e);
        }

        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/token.txt"), StandardCharsets.UTF_8))) {
            log.info("Parsing whitelist from ./data/token.txt");
            r.lines().forEach(token -> {
                tokens.add(token);
            });
            log.info("Found " + tokens.size() + " token(s)");
        } catch(Exception e) {
            log.warn("There was an issue while reading the token data file, reason: {}", e.getMessage(), e);
        }
    }
}
