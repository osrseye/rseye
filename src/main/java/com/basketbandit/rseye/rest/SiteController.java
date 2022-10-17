package com.basketbandit.rseye.rest;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.entity.Player;
import com.basketbandit.rseye.entity.event.CombatEvent;
import com.basketbandit.rseye.entity.event.GrowthEvent;
import com.basketbandit.rseye.entity.event.QuestEvent;
import com.basketbandit.rseye.entity.event.RaidEvent;
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
        mv.addObject("loginState", player.loginState());
        mv.addObject("information", player.information());
        mv.addObject("stats", player.stats());
        mv.addObject("quests", player.quests());
        mv.addObject("equipment", player.equipment());
        mv.addObject("inventory", player.inventory());
        mv.addObject("bank", player.bank());
        mv.addObject("overhead", player.overhead());
        mv.addObject("skull", player.skull());
        return mv;
    }

    @GetMapping("/player/{username}/stats")
    public ModelAndView getPlayerStats(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/stats");
        mv.addObject("stats", player.stats());
        return mv;
    }

    @GetMapping("/player/{username}/status")
    public ModelAndView getPlayerStatus(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/status");
        mv.addObject("stats", player.stats());
        return mv;
    }

    @GetMapping("/player/{username}/equipment")
    public ModelAndView getPlayerEquipment(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/equipment");
        mv.addObject("equipment", player.equipment());
        return mv;
    }

    @GetMapping("/player/{username}/inventory")
    public ModelAndView getPlayerInventory(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/inventory");
        mv.addObject("inventory", player.inventory());
        return mv;
    }

    @GetMapping("/player/{username}/bank")
    public ModelAndView getPlayerBank(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/bank");
        mv.addObject("information", player.information());
        mv.addObject("bank", player.bank());
        return mv;
    }

    @GetMapping("/player/{username}/quests")
    public ModelAndView getPlayerQuests(@PathVariable("username") String username) {
        Player player = Application.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/quests");
        mv.addObject("quests", player.quests());
        return mv;
    }

    @GetMapping("/raid/latest")
    public ModelAndView getRaidFeedLatest() {
        int feedSize = Application.raidFeed.size();
        if(feedSize > 0) {
            RaidEvent raidEvent = Application.raidFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./event/raid/index");
            mv.addObject("raid", raidEvent);
            return mv;
        }
        return new ModelAndView("./event/raid/empty");
    }

    @GetMapping("/combat/latest")
    public ModelAndView getCombatFeedLatest() {
        int feedSize = Application.combatFeed.size();
        if(feedSize > 0) {
            CombatEvent combatEvent = Application.combatFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./event/combat/index");
            mv.addObject("combat", combatEvent);
            return mv;
        }
        return new ModelAndView("./event/combat/empty");
    }

    @GetMapping("/growth/latest")
    public ModelAndView getGrowthFeedLatest() {
        int feedSize = Application.growthFeed.size();
        if(feedSize > 0) {
            GrowthEvent growthEvent = Application.growthFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./event/growth/index");
            mv.addObject("growth", growthEvent);
            return mv;
        }
        return new ModelAndView("./event/growth/empty");
    }

    @GetMapping("/quest/latest")
    public ModelAndView getQuestFeedLatest() {
        int feedSize = Application.questFeed.size();
        if(feedSize > 0) {
            QuestEvent questEvent = Application.questFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./event/quest/index");
            mv.addObject("quest", questEvent);
            return mv;
        }
        return new ModelAndView("./event/quest/empty");
    }
}
