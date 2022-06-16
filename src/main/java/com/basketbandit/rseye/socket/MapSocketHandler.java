package com.basketbandit.rseye.socket;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.entity.fragment.PlayerInfo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class MapSocketHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(MapSocketHandler.class);
    private static final ArrayList<WebSocketSession> clients = new ArrayList<>();
    private static final Gson gson = new Gson();

    public static void broadcastNewPlayer(PlayerInfo player) {
        synchronized(clients) {
            clients.forEach(client -> {
                try {
                    client.sendMessage(new TextMessage("newPlayer:" + gson.toJson(player)));
                } catch(IOException e) {
                    log.warn("There was a problem contacting client, reason: {}", e.getMessage(), e);
                }
            });
        }
    }

    public static void updateBroadcast(String updateType, PlayerInfo player) {
        synchronized(clients) {
            HashMap<String, PlayerInfo> playerLocations = new HashMap<>();
            Application.players.keySet().forEach(username -> {
                Player p = Application.players.get(username);
                playerLocations.put(username, p.info);
            });

            clients.forEach(client -> {
                try {
                    client.sendMessage(new TextMessage(updateType + ":" + gson.toJson(player)));
                    client.sendMessage(new TextMessage("updateLocation" + ":" + gson.toJson(playerLocations)));
                } catch(IOException e) {
                    log.warn("There was a problem contacting client, reason: {}", e.getMessage(), e);
                }
            });
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            if(!session.isOpen()) {
                session.close();
                return;
            }

            if(message.getPayload().equals("fetch")) {
                HashMap<String, PlayerInfo> hashMap = new HashMap<>();
                Application.players.keySet().forEach(username -> {
                    Player player = Application.players.get(username);
                    hashMap.put(username, player.info);
                });
                session.sendMessage(new TextMessage("fetch:" + gson.toJson(hashMap)));
                return;
            }

            if(message.getPayload().equals("ping")) {
                session.sendMessage(new TextMessage("pong"));
                return;
            }
        } catch(Exception e) {
            log.error("There was an error handling message: {}", e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        clients.add(session);
        clients.forEach(client -> {
            try {
                client.sendMessage(new TextMessage("clients:" + clients.size()));
            } catch(Exception e) {
                log.warn("There was a problem contacting client, reason: {}", e.getMessage(), e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        clients.remove(session);
        clients.forEach(client -> {
            try {
                client.sendMessage(new TextMessage("clients:" + clients.size()));
            } catch(Exception e) {
                log.warn("There was a problem contacting client, reason: {}", e.getMessage(), e);
            }
        });
    }
}