package com.basketbandit.rseye.rest;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.entity.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;

@RestController
public class GetController {
    @GetMapping("/api/v2/player/{username}")
    public Player getPlayer(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player());
    }

    @GetMapping("/api/v2/player/{username}/login_state")
    public String getPlayerLoginState(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player()).loginState();
    }

    @GetMapping("/api/v2/player/{username}/username")
    public Player.Username getPlayerUsername(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player()).username();
    }

    @GetMapping("/api/v2/player/{username}/position")
    public Player.Position getPlayerPosition(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player()).position();
    }

    @GetMapping("/api/v2/player/{username}/inventory")
    public Player.Inventory getPlayerInventory(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player()).inventory();
    }

    @GetMapping("/api/v2/player/{username}/equipment")
    public Player.Equipment getPlayerEquipment(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player()).equipment();
    }

    @GetMapping("/api/v2/player/{username}/quests")
    public Player.Quests getPlayerQuests(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player()).quests();
    }

    @GetMapping("/api/v2/player/{username}/skills")
    public Player.Skills getPlayerSkills(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player()).skills();
    }

    @GetMapping("/api/v2/player/{username}/bank")
    public Player.Bank getPlayerBank(@PathVariable("username") String username) {
        return Application.players.getOrDefault(username, new Player()).bank();
    }

    @GetMapping("/api/v2/players")
    public Collection<Player> getPlayers() {
        return Application.players.values().stream().toList();
    }

    @GetMapping("/api/v2/position/all")
    public HashMap<String, Player.Position> getPositions() {
        HashMap<String, Player.Position> hashMap = new HashMap<>();
        Application.players.keySet().forEach(username -> {
            Player player = Application.players.get(username);
            hashMap.put(username, player.position());
        });
        return hashMap;
    }
}


