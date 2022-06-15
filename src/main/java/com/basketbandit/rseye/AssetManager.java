package com.basketbandit.rseye;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class AssetManager {
    public Logger log = LoggerFactory.getLogger(AssetManager.class);
    public static HashMap<String, String> itemIcons = new HashMap<>();

    public AssetManager() {
        try(BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(ResourceUtils.getFile("classpath:static/img/icons-items-complete.json")), StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(r, JsonObject.class);
            obj.keySet().forEach(key -> itemIcons.put(key, "data:image/png;base64, " + obj.get(key).getAsString()));
            log.info("Successfully parsed " + itemIcons.keySet().size() + " item icons");
        } catch(IOException e) {
            log.error("There was an issue loading assets: {}", e.getMessage(), e);
        }
    }
}
