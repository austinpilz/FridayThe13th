package com.AustinPilz.FridayThe13th.Runnable;


import com.AustinPilz.FridayThe13th.Components.Arena;
import org.bukkit.scheduler.BukkitRunnable;

public class GameScoreboardUpdate extends BukkitRunnable {

    private Arena arena;

    public GameScoreboardUpdate(Arena arena)
    {
        this.arena = arena;
    }

    @Override
    public void run()
    {
        if (arena.getGameManager().isGameInProgress()) {
            arena.getGameManager().getScoreboardManager().updateStats();
        }
    }
}
