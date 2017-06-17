package com.AustinPilz.FridayThe13th.Components;


import org.bukkit.Location;

public class ArenaChest
{
    private Arena arena;
    private Location location;
    private ChestType chestType;

    public ArenaChest(Arena arena, Location location, ChestType chestType)
    {
        this.arena = arena;
        this.location = location;
        this.chestType = chestType;
    }
}
