package com.basketbandit.rseye.scheduler.tasks;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.scheduler.Task;

public class EventLogClearTask implements Task {
    @Override
    public void run() {
        if(Application.combatFeed.size() > 10) {
            Application.combatFeed.removeFirst();
        }
        if(Application.raidFeed.size() > 10) {
            Application.raidFeed.removeFirst();
        }
        if(Application.questFeed.size() > 10) {
            Application.questFeed.removeFirst();
        }
        if(Application.growthFeed.size() > 10) {
            Application.growthFeed.removeFirst();
        }
    }
}
