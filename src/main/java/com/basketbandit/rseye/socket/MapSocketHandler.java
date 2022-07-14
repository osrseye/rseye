package com.basketbandit.rseye.socket;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.entity.event.Event;
import com.basketbandit.rseye.entity.fragment.PlayerInformation;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MapSocketHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(MapSocketHandler.class);
    private static final CopyOnWriteArrayList<WebSocketSession> clients = new CopyOnWriteArrayList<>();
    private static final Gson gson = new Gson();
    private record Username(String username, String usernameEncoded){}; // using records facilitates the creation of serializable objects without any boilerplate

    public synchronized static void broadcastUpdate(UpdateType updateType, Player player) {
        String payload = updateType.value.equals("position_update") ? gson.toJson(player.information()) : gson.toJson(new Username(player.information().username(), player.information().usernameEncoded()));
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
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            if(!session.isOpen()) {
                session.close();
                return;
            }

            if(message.getPayload().equals("fetchLatestData")) {
                HashMap<String, PlayerInformation> hashMap = new HashMap<>();
                Application.players.keySet().forEach(username -> {
                    Player player = Application.players.get(username);
                    hashMap.put(username, player.information());
                });
                session.sendMessage(new TextMessage("fetchLatestData:" + gson.toJson(hashMap)));
                return;
            }

            if(message.getPayload().equals("ping")) {
                session.sendMessage(new TextMessage("pong"));
            }
        } catch(Exception e) {
            log.error("There was an error handling message: {}", e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        clients.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        clients.remove(session);
    }
}
