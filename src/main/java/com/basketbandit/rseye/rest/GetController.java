package com.basketbandit.rseye.rest;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.entity.fragment.PlayerInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;

@RestController
public class GetController {

    @GetMapping("/api/v1/player/{username}")
    public Player getPlayer(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player());
    }

    @GetMapping("/api/v1/player/{username}/login_state")
    public String getPlayerLoginState(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player()).loginState;
    }

    @GetMapping("/api/v1/players")
    public Collection<Player> getPlayers() {
        return Application.players.values();
    }

    @GetMapping("/api/v1/position/all")
    public HashMap<String, PlayerInfo> getPositions() {
        HashMap<String, PlayerInfo> hashMap = new HashMap<>();
        Application.players.keySet().forEach(username -> {
            Player player = Application.players.get(username);
            hashMap.put(username, player.info);
        });
        return hashMap;
    }
}


