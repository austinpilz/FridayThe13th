package com.AustinPilz.FridayThe13th.Components;

import org.bukkit.entity.Player;

public class Counselor
{
    //Minecraft Objects
    private Player player;

    //Statistics
    private double stamina;
    private double fear;
    private double walkSpeed;

    /**
     * Creates new counselor object
     * @param p Minecraft player object
     */
    public Counselor(Player p)
    {
        player = p;
    }
}
