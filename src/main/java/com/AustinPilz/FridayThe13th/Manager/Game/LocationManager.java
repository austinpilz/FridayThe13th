package com.AustinPilz.FridayThe13th.Manager.Game;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import org.bukkit.Location;

import java.util.HashSet;

public class LocationManager
{
    private Arena arena;
    private HashSet<Location> startingPoints;
    private HashSet<Location> availableStartingPoints;

    /**
     * @param arena Game object
     */
    public LocationManager(Arena arena)
    {
        this.arena = arena;

        //Initialize Objects
        startingPoints = new HashSet<>();
        availableStartingPoints = new HashSet<>();
    }

    /**
     * Adds starting point location
     * @param location
     */
    public void addStartingPoint(Location location)
    {
        startingPoints.add(location);
    }

    /**
     * Resets available starting points to original set
     */
    public void resetAvailableStartingPoints()
    {
        availableStartingPoints.clear(); //Clear just in case
        availableStartingPoints.addAll(startingPoints);
    }

    /**
     * Return's the arena's total number of starting points
     */
    public int getNumberStartingPoints()
    {
        return startingPoints.size();
    }

    /**
     * Returns available starting points
     * @return
     */
    public HashSet<Location> getAvailableStartingPoints()
    {
        return availableStartingPoints;
    }
}
