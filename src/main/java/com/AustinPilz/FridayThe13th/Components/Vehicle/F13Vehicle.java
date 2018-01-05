package com.AustinPilz.FridayThe13th.Components.Vehicle;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import org.bukkit.Location;

public class F13Vehicle {

    private Arena arena;
    private Location spawnLocation;
    private VehicleType type;

    public F13Vehicle(Arena arena, Location spawnLocation, VehicleType type) {
        this.arena = arena;
        this.spawnLocation = spawnLocation;
        this.type = type;
    }

    /**
     * @return The vehicle's Arena
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * @return Spawn location of the vehicle
     */
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    /**
     * @return Vehicle type
     */
    public VehicleType getType() {
        return type;
    }

    /**
     * Returns the display string for the hologram values
     *
     * @param actual   Actual value
     * @param required Required value
     * @return Display string
     */
    public String getDisplayPercentageString(double actual, int required) {
        float percentage = ((float) actual) / required * 100;
        percentage = Math.round(percentage);
        String strPercent = String.format("%2.0f", percentage);
        String string = String.valueOf(strPercent) + "%";
        return string;
    }
}
