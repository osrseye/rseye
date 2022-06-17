package com.basketbandit.rseye.rest;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.entity.event.Combat;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.entity.event.Growth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SiteController {
    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/player/{username}")
    public ModelAndView getPlayer(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/index");
        mv.addObject("playerInfo", player.info);
        mv.addObject("playerStats", player.stats);
        mv.addObject("playerQuests", player.quests);
        mv.addObject("playerEquipment", player.equipment);
        mv.addObject("playerInventory", player.inventory);
        mv.addObject("playerBank", player.bank);
        return mv;
    }

    @GetMapping("/player/{username}/stats")
    public ModelAndView getPlayerStats(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/stats");
        mv.addObject("playerStats", player.stats);
        return mv;
    }

    @GetMapping("/player/{username}/equipment")
    public ModelAndView getPlayerEquipment(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/equipment");
        mv.addObject("playerEquipment", player.equipment);
        return mv;
    }

    @GetMapping("/player/{username}/inventory")
    public ModelAndView getPlayerInventory(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/inventory");
        mv.addObject("playerInventory", player.inventory);
        return mv;
    }

    @GetMapping("/player/{username}/bank")
    public ModelAndView getPlayerBank(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/bank");
        mv.addObject("playerBank", player.bank);
        return mv;
    }

    @GetMapping("/player/{username}/quests")
    public ModelAndView getPlayerQuests(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/quests");
        mv.addObject("playerQuests", player.quests);
        return mv;
    }

    @GetMapping("/combat/latest")
    public ModelAndView getCombatFeedLatest() {
        int feedSize = Application.combatFeed.size();
        if(feedSize > 0) {
            Combat combat = Application.combatFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./combat/index");
            mv.addObject("combat", combat);
            return mv;
        }
        return new ModelAndView("./combat/empty");
    }

    @GetMapping("/growth/latest")
    public ModelAndView getGrowthFeedLatest() {
        int feedSize = Application.growthFeed.size();
        if(feedSize > 0) {
            Growth growth = Application.growthFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./growth/index");
            mv.addObject("growth", growth);
            return mv;
        }
        return new ModelAndView("./growth/empty");
    }
}
