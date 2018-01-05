package com.AustinPilz.FridayThe13th.Components.Vehicle;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import org.bukkit.Location;

public class F13Boat extends F13Vehicle {

    private boolean hasPropellar;
    private boolean hasGas;

    public F13Boat(Arena arena, Location spawnLocation) {
        super(arena, spawnLocation, VehicleType.Boat);
        hasGas = false;
        hasPropellar = false;
    }
}
