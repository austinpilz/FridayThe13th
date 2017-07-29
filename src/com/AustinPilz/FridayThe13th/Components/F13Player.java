package com.AustinPilz.FridayThe13th.Components;

import com.AustinPilz.FridayThe13th.Components.Enum.F13Level;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Display.WaitingPlayerStatsDisplayManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Level;

public class F13Player {
    private String playerUUID;

    //Spawn Preference
    private boolean spawnPreferenceJason;
    private boolean spawnPreferenceCounselor;

    //Statistics
    private F13Level level;
    private int experiencePoints;

    //Waiting Scoreboard
    private WaitingPlayerStatsDisplayManager waitingPlayerStatsDisplayManager;

    public F13Player(String uuid) {
        this.playerUUID = uuid;

        //Spawn Preferences
        spawnPreferenceJason = false;
        spawnPreferenceCounselor = false;
        experiencePoints = 0;
        determineLevel();

        //Display
        waitingPlayerStatsDisplayManager = new WaitingPlayerStatsDisplayManager(this);
    }

    /**
     * Returns the player's UUID
     *
     * @return
     */
    public String getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Returns the manager for the waiting player stats scoreboard
     *
     * @return
     */
    public WaitingPlayerStatsDisplayManager getWaitingPlayerStatsDisplayManager() {
        return waitingPlayerStatsDisplayManager;
    }

    /**
     * Sets the players spawn preference as Jason
     */
    public void setSpawnPreferenceJason() {
        spawnPreferenceCounselor = false;
        spawnPreferenceJason = true;
        updateDB();
    }

    /**
     * Sets the players spawn preference as Counselor
     */
    public void setSpawnPreferenceCounselor() {
        spawnPreferenceCounselor = true;
        spawnPreferenceJason = false;
        updateDB();
    }

    /**
     * Returns if the player prefers to play as Jason
     */
    public boolean isSpawnPreferenceJason() {
        return spawnPreferenceJason;
    }

    /**
     * Returns if the player prefers to play as a counselor
     */
    public boolean isSpawnPreferenceCounselor() {
        return spawnPreferenceCounselor;
    }

    /**
     * Stores the player in the database
     */
    public void storeToDB() {
        try {
            FridayThe13th.inputOutput.storePlayer(this);
        } catch (SaveToDatabaseException exception) {
            //Ruh-roh raggy. Couldn't save them, probably means they already exist
            FridayThe13th.log.log(Level.WARNING, "Encountered an unexpected error while attempting to save F13 player to database.");
        }
    }

    /**
     * Updates player in the database
     */
    public void updateDB() {
        FridayThe13th.inputOutput.updatePlayer(this);
    }

    /**
     * Returns the bukkit player object
     *
     * @return
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(getPlayerUUID()));
    }

    /**
     * Returns the player's experience points
     *
     * @return
     */
    public int getXP() {
        return experiencePoints;
    }

    /**
     * Adds XP to the player's XP balance
     *
     * @param value
     */
    public void addXP(int value) {
        experiencePoints += value;
        updateDB();
        determineLevel();
    }

    /**
     * Sets the player's XP
     *
     * @param value
     */
    public void setXP(int value) {
        experiencePoints = value;
        determineLevel();
    }

    /**
     * Returns the players level
     *
     * @return
     */
    public F13Level getLevel() {
        return level;
    }

    /**
     * Determines the players level
     */
    private void determineLevel() {
        for (F13Level l : F13Level.values()) {
            if (getXP() >= l.getMinXP() && getXP() < l.getMaxXP()) {
                this.level = l;
            }
        }
    }
}
