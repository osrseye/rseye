package com.basketbandit.rseye;

import com.basketbandit.rseye.entity.*;
import com.basketbandit.rseye.entity.event.CombatEvent;
import com.basketbandit.rseye.entity.event.GrowthEvent;
import com.basketbandit.rseye.entity.event.QuestEvent;
import com.basketbandit.rseye.entity.event.RaidEvent;
import com.basketbandit.rseye.scheduler.ScheduleHandler;
import com.basketbandit.rseye.scheduler.jobs.PingJob;
import com.basketbandit.rseye.scheduler.jobs.SaveJob;
import com.basketbandit.rseye.scheduler.jobs.TrackerResetJob;
import com.basketbandit.rseye.scheduler.jobs.UpdateJob;
import com.basketbandit.rseye.scheduler.tasks.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataManager {
    public Logger log = LoggerFactory.getLogger(DataManager.class);
    public static ArrayList<String> tokens = new ArrayList<>();
    public static HashMap<String, Item> items = new HashMap<>();
    public static HashMap<String, Monster> monsters = new HashMap<>();
    public static HashMap<Integer, Integer> xpTable = new HashMap<>();
    public static ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
    public static CopyOnWriteArrayList<CombatEvent> combatFeed = new CopyOnWriteArrayList<>(); // inefficient but we aren't expecting high throughput
    public static CopyOnWriteArrayList<RaidEvent> raidFeed = new CopyOnWriteArrayList<>(); // inefficient but we aren't expecting high throughput
    public static CopyOnWriteArrayList<GrowthEvent> growthFeed = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<GrowthEvent> deathFeed = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<QuestEvent> questFeed = new CopyOnWriteArrayList<>();
    public static LootTracker globalLootTracker = new LootTracker();
    public static ExperienceTracker experienceTracker = new ExperienceTracker();

    public DataManager() {
        // load items
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

        // load monsters
        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/monsters-cache-data.json"), StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(r, JsonObject.class);
            obj.keySet().forEach(key -> monsters.put(key, new Monster(key, obj.get(key).getAsJsonObject().get("name").getAsString(), obj.get(key).getAsJsonObject().get("combatLevel").getAsInt())));
            log.info("Found {} monster(s)", monsters.size());
        } catch(Exception e) {
            log.error("There was an issue loading assets: {}", e.getMessage(), e);
        }

        // load skill xp table
        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/xp-table.json"), StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(r, JsonObject.class);
            JsonObject table = obj.get("xp_table").getAsJsonObject();
            table.keySet().forEach(key -> xpTable.put(Integer.valueOf(key), table.get(key).getAsInt()));
            log.info("Found {} level(s)", xpTable.size());
        } catch(Exception e) {
            log.error("There was an issue loading assets: {}", e.getMessage(), e);
        }

        // load players
        try(Stream<Path> stream = Files.list(Paths.get("./data/players/"))) {
            Set<String> playerJson = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
            playerJson.forEach(playerFile -> {
                try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/players/" + playerFile), StandardCharsets.UTF_8))) {
                    Gson gson = new Gson();
                    Player player = gson.fromJson(r, Player.class);
                    players.put(player.username().natural(), player);
                } catch(Exception e) {
                    log.error("There was an issue loading assets: {}", e.getMessage(), e);
                }
            });
            log.info("Found {} player(s)", players.size());
        } catch(IOException e) {
            log.error(e.getMessage(), e);
        }

        // load bearerTokens
        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("./data/token.txt"), StandardCharsets.UTF_8))) {
            r.lines().forEach(token -> tokens.add(token));
            log.info("Found {} token(s)", tokens.size());
        } catch(Exception e) {
            log.warn("There was an issue while reading the token data file, reason: {}", e.getMessage(), e);
        }

        ScheduleHandler.registerJob(new UpdateJob(new PlayerStateCheckTask(), new EventLogClearTask()));
        ScheduleHandler.registerJob(new PingJob(new PingWebSocketTask()));
        ScheduleHandler.registerJob(new TrackerResetJob(new TrackerResetTask()));
        ScheduleHandler.registerJob(new SaveJob(new SavePlayerDataTask()));
    }
}
