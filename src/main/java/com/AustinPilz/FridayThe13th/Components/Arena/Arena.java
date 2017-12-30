package com.AustinPilz.FridayThe13th.Components.Arena;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Game.GameManager;
import com.AustinPilz.FridayThe13th.Manager.Game.LocationManager;
import com.AustinPilz.FridayThe13th.Manager.Game.ObjectManager;
import com.AustinPilz.FridayThe13th.Manager.Game.SignManager;
import org.bukkit.Location;

import java.util.Arrays;

public class Arena
{
    private String arenaName;

    //Game Boundaries
    private Location boundary1;
    private Location boundary2;
    private Location waitingLocation;
    private Location returnLocation;
    private Location jasonStartLocation;
    private double minutesPerCounselor;
    private int secondsWaitingRoom;

    //Game Managers
    private LocationManager locationManager; //Manages important locations such as counselor spawn locations
    private ObjectManager objectManager;
    private GameManager gameManager; //Manages the active game, players, etc.
    private SignManager signManager;

    public Arena(String arenaName, Location boundary1, Location boundary2, Location waitingLocation, Location returnLocation, Location jasonStartLocation, double minPerCounselor, int secWaitingRoom)
    {
        //Values
        this.arenaName = arenaName;
        this.boundary1 = boundary1;
        this.boundary2 = boundary2;
        this.waitingLocation = waitingLocation;
        this.returnLocation = returnLocation;
        this.jasonStartLocation = jasonStartLocation;
        this.minutesPerCounselor = minPerCounselor;
        this.secondsWaitingRoom = secWaitingRoom;

        //Initialize
        locationManager = new LocationManager(this);
        objectManager = new ObjectManager(this);
        signManager = new SignManager(this);
        gameManager = new GameManager(this);
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
     * Returns the arena's object manager
     * @return
     */
    public ObjectManager getObjectManager() { return objectManager; }

    /**
     * Returns the arena's sign manager
     * @return
     */
    public SignManager getSignManager() { return signManager; }

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
        return !(inQuestion.getZ() > dim[1] || inQuestion.getZ() < dim[0]);
    }

    /**
     * Sets the number of minutes per counselor in the arena
     *
     * @param value
     */
    public void setMinutesPerCounselor(double value) {
        minutesPerCounselor = value;
        updateInDB();
    }

    /**
     * Returns the number of minutes per counselor in the arena
     *
     * @return
     */
    public double getMinutesPerCounselor() {
        return minutesPerCounselor;
    }

    /**
     * Returns the number of minutes in the waiting room once the minimum number of players have joined
     * @return
     */
    public int getSecondsWaitingRoom() { return secondsWaitingRoom; }

    /**
     * Updates the number of seconds in the waiting room
     * @param seconds
     */
    public void setSecondsWaitingRoom(int seconds)
    {
        secondsWaitingRoom = seconds;
        updateInDB();
    }

    /**
     * Returns the max number of players that can play in the arena
     *
     * @return
     */
    public int getMaxNumberOfPlayers() {
        if (getLocationManager().getNumberStartingPoints() >= 8) {
            //We don't have to worry about spawn points
            //Games capped at 8 counselors + Jason.
            return 9;
        } else {
            //They're are less than 8 spawn points for counselors
            return getLocationManager().getNumberStartingPoints() + 1;
        }
    }

    /**
     * Updates the arena values in the database
     */
    private void updateInDB() {
        FridayThe13th.inputOutput.updateArena(this);
    }
}
