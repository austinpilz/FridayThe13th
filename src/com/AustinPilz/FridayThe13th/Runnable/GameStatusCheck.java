package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Arena;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class GameStatusCheck extends BukkitRunnable
{
    private Arena arena;

    public GameStatusCheck(Arena arena)
    {
        this.arena = arena;
    }

    @Override
    public void run()
    {
        arena.getGameManager().checkGameStatus();
    }

}