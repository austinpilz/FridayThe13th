package com.AustinPilz.FridayThe13th.Manager;

import com.AustinPilz.FridayThe13th.Components.Arena;
import org.bukkit.Location;

import java.util.HashSet;

public class LocationManager
{
    private Arena arena;
    private HashSet<Location> startingPoints;

    public LocationManager(Arena a)
    {
        arena = a;

        //Initialize Objects
        startingPoints = new HashSet<>();
    }

}
