package com.basketbandit.rseye.rest.intercept;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.AssetManager;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.socket.MapSocketHandler;
import com.basketbandit.rseye.socket.UpdateType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

@Component
public class PostInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // If "Authorization" is null, doesn't start with "Bearer: ", or isn't a valid token.
        if(request.getHeader("Authorization") == null || !request.getHeader("Authorization").startsWith("Bearer: ")
                || !AssetManager.tokens.contains(request.getHeader("Authorization").substring(8))) {
            return false;
        }

        String body = request.getReader().lines().collect(Collectors.joining());
        JsonObject object = JsonParser.parseString(body).getAsJsonObject();

        if(!object.has("username")) {
            return false;
        }

        String username = object.get("username").getAsString();
        Player player = Application.players.containsKey(username) ? Application.players.get(username) : new Player();
        if(!player.information().username().equals(username)) {
            player.setInformation(
                new Player.Information(username,
                        new HashMap<>(){{
                            // set xy to varrock square
                            put("x", 3213);
                            put("y", 3428);
                        }}
                )
            );
            Application.players.put(username, player);
            MapSocketHandler.broadcastUpdate(UpdateType.NEW_PLAYER, player);
        }

        request.setAttribute("player", player);
        request.setAttribute("object", object);
        return true;
    }
}