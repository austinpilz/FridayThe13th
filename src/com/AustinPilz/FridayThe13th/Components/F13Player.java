package com.AustinPilz.FridayThe13th.Components;

import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Level;

public class F13Player {
    private String playerUUID;

    //Spawn Preference
    private boolean spawnPreferenceJason;
    private boolean spawnPreferenceCounselor;

    public F13Player(String uuid) {
        this.playerUUID = uuid;

        //Spawn Preferences
        spawnPreferenceJason = false;
        spawnPreferenceCounselor = false;
    }

    public String getPlayerUUID() {
        return playerUUID;
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
     *
     * @return
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
}
