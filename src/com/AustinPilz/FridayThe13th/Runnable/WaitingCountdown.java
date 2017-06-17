package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Arena;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by austinpilz on 6/15/17.
 */
public class WaitingCountdown extends BukkitRunnable
{
    private Arena arena;

    public WaitingCountdown(Arena arena)
    {
        this.arena = arena;
    }

    @Override
    public void run()
    {
        if (arena.getGameManager().getWaitingTimeLeft() > 0)
        {
            arena.getGameManager().setWaitingTimeLeft(arena.getGameManager().getWaitingTimeLeft() - 1);
            arena.getGameManager().getWaitingCountdownDisplayManager().updateCountdownValue();
        }
    }
}
