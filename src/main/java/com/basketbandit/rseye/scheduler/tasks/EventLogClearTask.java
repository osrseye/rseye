package com.basketbandit.rseye.scheduler.tasks;

import com.basketbandit.rseye.DataManager;
import com.basketbandit.rseye.scheduler.Task;

public class EventLogClearTask implements Task {
    @Override
    public void run() {
        if(DataManager.combatFeed.size() > 10) {
            DataManager.combatFeed.removeFirst();
        }
        if(DataManager.raidFeed.size() > 10) {
            DataManager.raidFeed.removeFirst();
        }
        if(DataManager.questFeed.size() > 10) {
            DataManager.questFeed.removeFirst();
        }
        if(DataManager.growthFeed.size() > 10) {
            DataManager.growthFeed.removeFirst();
        }
        if(DataManager.deathFeed.size() > 10) {
            DataManager.deathFeed.removeFirst();
        }
    }
}
