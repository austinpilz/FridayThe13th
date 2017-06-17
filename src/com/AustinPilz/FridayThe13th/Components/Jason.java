package com.AustinPilz.FridayThe13th.Components;

import org.bukkit.entity.Player;

public class Jason
{
    //Minecraft Objects
    private Player player;

    //Game
    private Arena arena;


    public Jason(Player p, Arena a)
    {
        arena = a;
        player = p;
    }

    /**
     * Return' the player object of the counselor
     */
    public Player getPlayer()
    {
        return this.player;
    }

    /**
     * Performs all necessary tasks when the game begins
     */
    public void prepapreForGameplay()
    {
        //Give jason his sword

    }
}
