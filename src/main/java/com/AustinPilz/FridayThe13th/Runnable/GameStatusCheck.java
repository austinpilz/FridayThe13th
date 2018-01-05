package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;

public class GameStatusCheck implements Runnable
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
        arena.getSignManager().updateJoinSigns(); //Update signs
    }

}