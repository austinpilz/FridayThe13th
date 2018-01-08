package com.AustinPilz.FridayThe13th.Manager.Arena;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LocationManager {
    private Arena arena;
    private HashSet<Location> startingPoints;
    private HashSet<Location> availableStartingPoints;

    private EscapePointManager escapePointManager;

    /**
     * @param arena Game object
     */
    public LocationManager(Arena arena) {
        this.arena = arena;

        //Initialize Objects
        startingPoints = new HashSet<>();
        availableStartingPoints = new HashSet<>();
        escapePointManager = new EscapePointManager(arena);
    }

    /**
     * Adds starting point location
     *
     * @param location
     */
    public void addStartingPoint(Location location) {
        startingPoints.add(location);
    }

    /**
     * Resets available starting points to original set
     */
    public void resetAvailableStartingPoints() {
        availableStartingPoints.clear(); //Clear just in case
        availableStartingPoints.addAll(startingPoints);
    }

    /**
     * Return's the arena's total number of starting points
     */
    public int getNumberStartingPoints() {
        return startingPoints.size();
    }

    /**
     * Returns available starting points
     *
     * @return
     */
    public HashSet<Location> getAvailableStartingPoints() {
        return availableStartingPoints;
    }

    /**
     * @return Randomized spawn point array
     */
    public Location[] getRandomSpawnLocations() {
        Location[] spawnPoints = getAvailableStartingPoints().toArray(new Location[getAvailableStartingPoints().size()]);

        //Randomize starting points
        Random rnd = ThreadLocalRandom.current();
        for (int i = spawnPoints.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);

            // Simple swap
            Location a = spawnPoints[index];
            spawnPoints[index] = spawnPoints[i];
            spawnPoints[i] = a;
        }

        return spawnPoints;
    }

    /**
     * @return Escape Point Manager
     */
    public EscapePointManager getEscapePointManager() {
        return escapePointManager;
    }
}
