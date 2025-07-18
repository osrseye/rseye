package com.basketbandit.rseye.rest.intercept;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.AssetManager;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.socket.MapSocketHandler;
import com.basketbandit.rseye.socket.UpdateType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class PostInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
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
        Player player = Application.players.getOrDefault(username, new Player());
        if(!username.equals(player.username().natural())) {
            player.setUsername(new Player.Username(username));
            Application.players.put(username, player);
            MapSocketHandler.broadcastUpdate(UpdateType.NEW_PLAYER, player);
        }

        request.setAttribute("player", player);
        request.setAttribute("object", object);
        return true;
    }
}