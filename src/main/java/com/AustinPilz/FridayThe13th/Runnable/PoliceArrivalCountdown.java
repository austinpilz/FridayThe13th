package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;

public class PoliceArrivalCountdown implements Runnable {
    private Arena arena;

    public PoliceArrivalCountdown(Arena arena) {
        this.arena = arena;
    }

    @Override
    public void run() {
        if (arena.getGameManager().havePoliceBeenCalled() && !arena.getGameManager().havePoliceArrived()) {
            if (arena.getGameManager().getTimeUntilPoliceArrive() <= 0) {
                arena.getGameManager().setPoliceArrived(true);
            } else {
                arena.getGameManager().setTimeUntilPoliceArrive(arena.getGameManager().getTimeUntilPoliceArrive() - 1);
            }
        }
    }
}
