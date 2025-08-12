package com.basketbandit.rseye.rest;

import com.basketbandit.rseye.DataManager;
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
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("lootTracker", DataManager.globalLootTracker);
        mv.addObject("xpTracker", DataManager.experienceTracker);
        return mv;
    }

    @GetMapping("/loot-tracker")
    public ModelAndView getGlobalLootTracker() {
        ModelAndView mv = new ModelAndView("./global/loot-tracker");
        mv.addObject("lootTracker", DataManager.globalLootTracker);
        return mv;
    }

    @GetMapping("/xp-tracker")
    public ModelAndView getExperienceTracker() {
        ModelAndView mv = new ModelAndView("./global/xp-tracker");
        mv.addObject("xpTracker", DataManager.experienceTracker);
        return mv;
    }

    @GetMapping("/player/{username}")
    public ModelAndView getPlayer(@PathVariable("username") String username) {
        Player player = DataManager.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/index");
        mv.addObject("loginState", player.loginState());
        mv.addObject("username", player.username());
        mv.addObject("position", player.position());
        mv.addObject("combatLevel", player.skills().combatLevel());
        mv.addObject("skills", player.skills());
        mv.addObject("quests", player.quests());
        mv.addObject("equipment", player.equipment());
        mv.addObject("inventory", player.inventory());
        mv.addObject("bank", player.bank());
        mv.addObject("overhead", player.overhead());
        mv.addObject("skull", player.skull());
        mv.addObject("lootTracker", player.lootTracker());
        return mv;
    }

    @GetMapping("/player/{username}/combat")
    public ModelAndView getPlayerCombat(@PathVariable("username") String username) {
        Player player = DataManager.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/combat");
        mv.addObject("combatLevel", player.skills().combatLevel());
        return mv;
    }

    @GetMapping("/player/{username}/skills")
    public ModelAndView getPlayerSkills(@PathVariable("username") String username) {
        Player player = DataManager.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/skills");
        mv.addObject("skills", player.skills());
        return mv;
    }

    @GetMapping("/player/{username}/status")
    public ModelAndView getPlayerStatus(@PathVariable("username") String username) {
        Player player = DataManager.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/status");
        mv.addObject("skills", player.skills());
        return mv;
    }

    @GetMapping("/player/{username}/equipment")
    public ModelAndView getPlayerEquipment(@PathVariable("username") String username) {
        Player player = DataManager.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/equipment");
        mv.addObject("equipment", player.equipment());
        return mv;
    }

    @GetMapping("/player/{username}/inventory")
    public ModelAndView getPlayerInventory(@PathVariable("username") String username) {
        Player player = DataManager.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/inventory");
        mv.addObject("inventory", player.inventory());
        return mv;
    }

    @GetMapping("/player/{username}/bank")
    public ModelAndView getPlayerBank(@PathVariable("username") String username) {
        Player player = DataManager.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/bank");
        mv.addObject("username", player.username());
        mv.addObject("bank", player.bank());
        return mv;
    }

    @GetMapping("/player/{username}/quests")
    public ModelAndView getPlayerQuests(@PathVariable("username") String username) {
        Player player = DataManager.players.getOrDefault(username, new Player());
        ModelAndView mv = new ModelAndView("./player/quests");
        mv.addObject("quests", player.quests());
        return mv;
    }

    @GetMapping("/raid/latest")
    public ModelAndView getRaidFeedLatest() {
        int feedSize = DataManager.raidFeed.size();
        if(feedSize > 0) {
            RaidEvent raidEvent = DataManager.raidFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./event/raid/index");
            mv.addObject("raid", raidEvent);
            return mv;
        }
        return new ModelAndView("./event/raid/empty");
    }

    @GetMapping("/combat/latest")
    public ModelAndView getCombatFeedLatest() {
        int feedSize = DataManager.combatFeed.size();
        if(feedSize > 0) {
            CombatEvent combatEvent = DataManager.combatFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./event/combat/index");
            mv.addObject("combat", combatEvent);
            return mv;
        }
        return new ModelAndView("./event/combat/empty");
    }

    @GetMapping("/growth/latest")
    public ModelAndView getGrowthFeedLatest() {
        int feedSize = DataManager.growthFeed.size();
        if(feedSize > 0) {
            GrowthEvent growthEvent = DataManager.growthFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./event/growth/index");
            mv.addObject("growth", growthEvent);
            return mv;
        }
        return new ModelAndView("./event/growth/empty");
    }

    @GetMapping("/death/latest")
    public ModelAndView getDeathFeedLatest() {
        int feedSize = DataManager.deathFeed.size();
        if(feedSize > 0) {
            GrowthEvent deathFeed = DataManager.deathFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./event/death/index");
            mv.addObject("death", deathFeed);
            return mv;
        }
        return new ModelAndView("./event/death/empty");
    }

    @GetMapping("/quest/latest")
    public ModelAndView getQuestFeedLatest() {
        int feedSize = DataManager.questFeed.size();
        if(feedSize > 0) {
            QuestEvent questEvent = DataManager.questFeed.get(feedSize - 1);
            ModelAndView mv = new ModelAndView("./event/quest/index");
            mv.addObject("quest", questEvent);
            return mv;
        }
        return new ModelAndView("./event/quest/empty");
    }
}
