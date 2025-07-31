package com.basketbandit.rseye.socket;

import com.basketbandit.rseye.DataManager;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.entity.event.Event;
import com.google.gson.Gson;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MapSocketHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(MapSocketHandler.class);
    private static final CopyOnWriteArrayList<WebSocketSession> clients = new CopyOnWriteArrayList<>();
    private static final Gson gson = new Gson();

    public synchronized static void broadcastPing() {
        clients.forEach(client -> {
            try {
                client.sendMessage(new TextMessage("ping"));
            } catch(IOException e) {
                log.warn("There was a problem contacting client, reason: {}", e.getMessage(), e);
            }
        });
    }

    public synchronized static void broadcastUpdate(UpdateType updateType, Player player) {
        String payload = (updateType.value.equals("position_update") || updateType.value.equals("new_player"))
                ? gson.toJson(player.basicInfo())
                : gson.toJson(Map.of("username", player.username()));

        clients.forEach(client -> {
            try {
                client.sendMessage(new TextMessage(updateType.value + ":" + payload));
            } catch(IOException e) {
                log.warn("There was a problem contacting client, reason: {}", e.getMessage(), e);
            }
        });
    }

    public synchronized static void broadcastUpdate(UpdateType updateType, Event event) {
        String payload = gson.toJson(event);
        clients.forEach(client -> {
            try {
                client.sendMessage(new TextMessage(updateType.value + ":" + payload));
            } catch(IOException e) {
                log.warn("There was a problem contacting client, reason: {}", e.getMessage(), e);
            }
        });
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        try {
            if(!session.isOpen()) {
                session.close();
                return;
            }

            if(message.getPayload().equals("fetch")) {
                HashMap<String, Player.BasicInfo> hashMap = new HashMap<>();
                DataManager.players.keySet().forEach(username -> {
                    Player player = DataManager.players.get(username);
                    hashMap.put(username, player.basicInfo());
                });
                session.sendMessage(new TextMessage("fetch:" + gson.toJson(hashMap)));
            }
        } catch(Exception e) {
            log.error("There was an error handling message: {}", e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        clients.add(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        clients.remove(session);
    }
}
