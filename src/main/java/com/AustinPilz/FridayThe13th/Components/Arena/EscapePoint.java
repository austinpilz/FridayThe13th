package com.AustinPilz.FridayThe13th.Components.Arena;

import com.AustinPilz.FridayThe13th.Components.Enum.EscapePointType;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.EscapePointEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Arrays;

public class EscapePoint {

    private Arena arena;
    private Location boundary1;
    private Location boundary2;
    private EscapePointType type;
    private boolean isPoliceLocation;
    private int policeEffectTask;

    public EscapePoint(Arena arena, EscapePointType type, Location boundary1, Location boundary2) {
        this.arena = arena;
        this.type = type;
        this.boundary1 = boundary1;
        this.boundary2 = boundary2;
        isPoliceLocation = false;
        policeEffectTask = -1;
    }

    /**
     * @return Arena
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * @return Boundary 1
     */
    public Location getBoundary1() {
        return boundary1;
    }

    /**
     * @return Boundary 2
     */
    public Location getBoundary2() {
        return boundary2;
    }

    /**
     * @return Escape point type
     */
    public EscapePointType getType() {
        return type;
    }

    /**
     * @return If escape point is on land
     */
    public boolean isLandPoint() {
        return getType().equals(EscapePointType.Land);
    }

    /**
     * @return If escape point is on water
     */
    public boolean isWaterPoint() {
        return getType().equals(EscapePointType.Water);
    }

    /**
     * @return If escape point is where the police are
     */
    public boolean isPoliceLocation() {
        return isPoliceLocation;
    }

    /**
     * Sets the escape point as the police location
     *
     * @param value Boolean
     */
    public void setPoliceLocation(boolean value) {
        isPoliceLocation = value;

        if (value) {
            policeEffectTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new EscapePointEffect(this), 0, 200);
        } else {
            Bukkit.getScheduler().cancelTask(policeEffectTask);
        }
    }

    /**
     * @param inQuestion Location
     * @return If the location is within the escape point
     */
    public boolean isLocationWithinBoundaries(Location inQuestion) {
        double[] dim = new double[2];

        dim[0] = getBoundary1().getX();
        dim[1] = getBoundary2().getX();
        Arrays.sort(dim);
        if (inQuestion.getX() > dim[1] || inQuestion.getX() < dim[0])
            return false;

        dim[0] = getBoundary1().getY();
        dim[1] = getBoundary2().getY();
        Arrays.sort(dim);
        if (inQuestion.getY() > dim[1] || inQuestion.getY() < dim[0])
            return false;

        dim[0] = getBoundary1().getZ();
        dim[1] = getBoundary2().getZ();
        Arrays.sort(dim);
        return !(inQuestion.getZ() > dim[1] || inQuestion.getZ() < dim[0]);
    }

}
