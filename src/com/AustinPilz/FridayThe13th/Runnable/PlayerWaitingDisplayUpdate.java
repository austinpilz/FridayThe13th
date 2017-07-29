package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;

public class PlayerWaitingDisplayUpdate implements Runnable {
    private Arena arena;

    public PlayerWaitingDisplayUpdate(Arena a) {
        arena = a;
    }

    @Override
    public void run() {
        if ((arena.getGameManager().isGameEmpty() || arena.getGameManager().isGameWaiting()) && arena.getGameManager().getPlayerManager().getNumPlayers() > 0) {
            arena.getGameManager().getPlayerManager().updateWaitingStatsScoreboards();
        }
    }
}
