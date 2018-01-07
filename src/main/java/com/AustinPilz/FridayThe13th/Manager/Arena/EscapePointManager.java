package com.AustinPilz.FridayThe13th.Manager.Arena;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Arena.EscapePoint;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class EscapePointManager {

    private Arena arena;
    private HashSet<EscapePoint> escapePoints;


    EscapePointManager(Arena arena) {
        this.arena = arena;
        this.escapePoints = new HashSet<>();
    }

    /**
     * Adds escape point
     *
     * @param escapePoint
     */
    public void addEscapePoint(EscapePoint escapePoint) {
        escapePoints.add(escapePoint);
    }

    /**
     * Selects a random escape point for the police to show up at
     */
    public void selectRandomEscapePointForPolice() {
        EscapePoint[] escapePoints = getRandomizedEscapePointArray();

        int i = 0;
        boolean found = false;

        while (!found && i <= escapePoints.length - 1) {
            if (escapePoints[i].isLandPoint()) {
                found = true;
                escapePoints[i].setPoliceLocation(true);
            }

            i++;
        }
    }

    /**
     * @param location
     * @return If the location is within the escape point cuboid selection
     */
    public boolean isLocationWithinEscapePoint(Location location) {
        for (EscapePoint point : escapePoints) {
            if (point.isLocationWithinBoundaries(location)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param location
     * @return Escape point which the location is inside of
     */
    public EscapePoint getEscapePointFromLocation(Location location) {
        for (EscapePoint point : escapePoints) {
            if (point.isLocationWithinBoundaries(location)) {
                return point;
            }
        }

        return null;
    }

    /**
     * Resets escape points
     */
    public void resetEscapePoints() {
        //TODO: Restore vehicles, etc.
        for (EscapePoint point : escapePoints) {
            point.setPoliceLocation(false);
        }
    }

    /**
     * @return The number of escape points
     */
    public int getNumberOfEscapePoints() {
        return escapePoints.size();
    }

    /**
     * @return The number of land escape points
     */
    public int getNumberOfLandEscapePoints() {
        int count = 0;
        for (EscapePoint point : escapePoints) {
            if (point.isLandPoint()) {
                count++;
            }
        }

        return count;
    }

    /**
     * @return The number of water escape points
     */
    public int getNumberOfWaterEscapePoints() {
        int count = 0;
        for (EscapePoint point : escapePoints) {
            if (point.isWaterPoint()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Deletes all escape points from memory and database
     */
    public void deleteEscapePoints() {
        for (EscapePoint point : escapePoints) {
            FridayThe13th.inputOutput.deleteEscapePoint(point.getBoundary1().getX(), point.getBoundary1().getY(), point.getBoundary1().getZ(), point.getBoundary2().getX(), point.getBoundary2().getY(), point.getBoundary2().getZ(), point.getType().getFieldDescription(), point.getArena().getName());
        }

        escapePoints.clear();
    }

    /**
     * @return Random list of escape points
     */
    private EscapePoint[] getRandomizedEscapePointArray() {
        if (escapePoints.size() > 0) {
            EscapePoint[] pl = escapePoints.toArray(new EscapePoint[escapePoints.size()]);

            //Randomize possible escape points
            Random rnd = ThreadLocalRandom.current();
            for (int i = pl.length - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);

                // Simple swap
                EscapePoint a = pl[index];
                pl[index] = pl[i];
                pl[i] = a;
            }

            return pl;
        } else {
            return null;
        }
    }
}
