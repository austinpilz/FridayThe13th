package com.AustinPilz.FridayThe13th.Components.Arena;

import com.AustinPilz.FridayThe13th.Components.Enum.EscapePointType;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Arrays;

public class EscapePoint {

    private Arena arena;
    private Location boundary1;
    private Location boundary2;
    private EscapePointType type;
    private boolean isPoliceLocation;

    public EscapePoint(Arena arena, EscapePointType type, Location boundary1, Location boundary2) {
        this.arena = arena;
        this.type = type;
        this.boundary1 = boundary1;
        this.boundary2 = boundary2;
        isPoliceLocation = false;
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
     * @param value
     */
    public void setPoliceLocation(boolean value) {
        isPoliceLocation = value;

        if (value) {
            Firework f = getBoundary1().getWorld().spawn(getBoundary1().getWorld().getHighestBlockAt(getBoundary1()).getLocation(), Firework.class);
            FireworkMeta fm = f.getFireworkMeta();
            fm.addEffect(FireworkEffect.builder()
                    .flicker(true)
                    .trail(true)
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(Color.RED)
                    .build());
            fm.setPower(1);
            f.setFireworkMeta(fm);

            Firework f2 = getBoundary1().getWorld().spawn(getBoundary1().getWorld().getHighestBlockAt(getBoundary2()).getLocation(), Firework.class);
            FireworkMeta fm2 = f2.getFireworkMeta();
            fm2.addEffect(FireworkEffect.builder()
                    .flicker(true)
                    .trail(true)
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(Color.BLUE)
                    .build());
            fm2.setPower(1);
            f2.setFireworkMeta(fm2);
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
