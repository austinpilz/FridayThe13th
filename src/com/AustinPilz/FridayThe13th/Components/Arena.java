package com.AustinPilz.FridayThe13th.Components;

import com.AustinPilz.FridayThe13th.Manager.Arena.GameManager;
import com.AustinPilz.FridayThe13th.Manager.Arena.LocationManager;
import com.AustinPilz.FridayThe13th.Manager.Arena.ObjectManager;
import com.AustinPilz.FridayThe13th.Manager.Arena.PlayerManager;
import org.bukkit.Location;

import java.util.Arrays;

public class Arena
{
    private String arenaName;

    //Arena Boundaries
    private Location boundary1;
    private Location boundary2;
    private Location waitingLocation;
    private Location returnLocation;
    private Location jasonStartLocation;

    //Arena Managers
    private PlayerManager playerManager;
    private LocationManager locationManager;
    private ObjectManager objectManager;
    private GameManager gameManager;

    public Arena(String arenaName, Location boundary1, Location boundary2, Location waitingLocation, Location returnLocation, Location jasonStartLocation)
    {
        //Initialize
        playerManager = new PlayerManager(this);
        locationManager = new LocationManager(this);
        objectManager = new ObjectManager(this);
        gameManager = new GameManager(this);

        //Values
        this.arenaName = arenaName;
        this.boundary1 = boundary1;
        this.boundary2 = boundary2;
        this.waitingLocation = waitingLocation;
        this.returnLocation = returnLocation;
        this.jasonStartLocation = jasonStartLocation;
    }

    /**
     * Returns the arena's name
     * @return
     */
    public String getArenaName()
    {
        return arenaName;
    }

    /**
     * Returns point 1 of the arena boundary
     * @return
     */
    public Location getBoundary1()
    {
        return boundary1;
    }

    /**
     * Returns point 2 of the arena boundary
     * @return
     */
    public Location getBoundary2()
    {
        return boundary2;
    }

    /**
     * Returns wait location
     * @return
     */
    public Location getWaitingLocation()
    {
        return waitingLocation;
    }

    /**
     * Returns the return location
     * @return
     */
    public Location getReturnLocation()
    {
        return returnLocation;
    }

    /**
     * Returns jason's starting location
     * @return
     */
    public Location getJasonStartLocation()
    {
        return jasonStartLocation;
    }

    /**
     * Returns the arena's location manager
     * @return
     */
    public LocationManager getLocationManager()
    {
        return locationManager;
    }

    /**
     * Returns the arena's game manager
     * @return
     */
    public GameManager getGameManager() { return gameManager; }

    /**
     * Returns the arena's player manager
     * @return
     */
    public PlayerManager getPlayerManager()
    {
        return playerManager;
    }

    /**
     * Checks if supplied location is within the arena boundaries
     * @param inQuestion
     * @return
     */
    public boolean isLocationWithinArenaBoundaries(Location inQuestion)
    {
        double[] dim = new double[2];

        dim[0] = getBoundary1().getX();
        dim[1] = getBoundary2().getX();
        Arrays.sort(dim);
        if(inQuestion.getX() > dim[1] || inQuestion.getX() < dim[0])
            return false;

        dim[0] = getBoundary1().getY();
        dim[1] = getBoundary2().getY();
        Arrays.sort(dim);
        if(inQuestion.getY() > dim[1] || inQuestion.getY() < dim[0])
            return false;

        dim[0] = getBoundary1().getZ();
        dim[1] = getBoundary2().getZ();
        Arrays.sort(dim);
        if(inQuestion.getZ() > dim[1] || inQuestion.getZ() < dim[0])
            return false;

        return true;
    }
}
