package com.AustinPilz.FridayThe13th.Runnable;


import com.AustinPilz.FridayThe13th.Components.Arena;

public class GameCountdown implements Runnable
{
    private Arena arena;

    public GameCountdown(Arena arena)
    {
        this.arena = arena;
    }

    @Override
    public void run()
    {
        if (arena.getGameManager().isGameInProgress())
        {
            arena.getGameManager().setGameTimeLeft(arena.getGameManager().getGameTimeLeft() - 1); //Decrement a second
            arena.getGameManager().getGameCountdownManager().updateCountdown(); //Update the display
            arena.getSignManager().updateJoinSigns(); //Update signs
        }
    }
}