package com.basketbandit.rseye.scheduler.tasks;

import com.basketbandit.rseye.Application;
import com.basketbandit.rseye.scheduler.Task;

public class EventLogClearTask implements Task {
    @Override
    public void run() {
        if(Application.combatFeed.size() > 50) {
            Application.combatFeed.remove(0);
        }
        if(Application.questFeed.size() > 50) {
            Application.questFeed.remove(0);
        }
        if(Application.growthFeed.size() > 50) {
            Application.growthFeed.remove(0);
        }
    }
}
