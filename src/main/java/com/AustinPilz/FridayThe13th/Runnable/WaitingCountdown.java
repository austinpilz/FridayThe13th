package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;

public class WaitingCountdown implements Runnable
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

            //Update the signs
            arena.getSignManager().updateJoinSigns();
        }
    }
}
