package com.AustinPilz.FridayThe13th.Utilities;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Cone {

    /**
     * @param entities  List of nearby entities
     * @param startPos  starting position
     * @param radius    distance cone travels
     * @param degrees   angle of cone
     * @param direction direction of the cone
     * @return All entities inside the cone
     */
    public static List<Entity> getEntitiesInCone(List<Entity> entities, Vector startPos, float radius, float degrees, Vector direction) {

        List<Entity> newEntities = new ArrayList<>();        //    Returned list
        float squaredRadius = radius * radius;                     //    We don't want to use square root

        for (Entity e : entities) {
            Vector relativePosition = e.getLocation().toVector();                            //    Position of the entity relative to the cone origin
            relativePosition.subtract(startPos);
            if (relativePosition.lengthSquared() > squaredRadius)
                continue;                    //    First check : distance
            if (getAngleBetweenVectors(direction, relativePosition) > degrees) continue;    //    Second check : angle
            newEntities.add(e);                                                                //    The entity e is in the cone
        }
        return newEntities;
    }

    /**
     * @param startPos  starting position
     * @param radius    distance cone travels
     * @param degrees   angle of cone
     * @param direction direction of the cone
     * @return All block positions inside the cone
     */
    public static List<Vector> getPositionsInCone(Vector startPos, float radius, float degrees, Vector direction) {

        List<Vector> positions = new ArrayList<Vector>();        //    Returned list
        float squaredRadius = radius * radius;                     //    We don't want to use square root

        for (float x = startPos.getBlockX() - radius; x < startPos.getBlockX() + radius; x++)
            for (float y = startPos.getBlockY() - radius; y < startPos.getBlockY() + radius; y++)
                for (float z = startPos.getBlockZ() - radius; z < startPos.getBlockZ() + radius; z++) {
                    Vector relative = new Vector(x, y, z);
                    relative.subtract(startPos);
                    if (relative.lengthSquared() > squaredRadius) continue;            //    First check : distance
                    if (getAngleBetweenVectors(direction, relative) > degrees) continue;    //    Second check : angle
                    positions.add(new Vector(x, y, z));                                                //    The position v is in the cone
                }
        return positions;
    }


    public static float getAngleBetweenVectors(Vector v1, Vector v2) {
        return Math.abs((float) Math.toDegrees(v1.angle(v2)));
    }
}