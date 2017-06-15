package com.AustinPilz.FridayThe13th.Components;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;

public class Arena
{
    private String arenaName;

    //Arena Boundaries
    private Location boundary1;
    private Location boundary2;

    //Arena Objects
    private HashSet<Chest> chests;

    //Arena Managers

    public Arena()
    {
        //
    }

    /**
     * Returns the arena's name
     * @return
     */
    public String getArenaName()
    {
        return arenaName;
    }
}
