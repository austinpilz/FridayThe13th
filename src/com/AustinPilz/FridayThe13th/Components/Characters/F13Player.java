package com.AustinPilz.FridayThe13th.Components.Characters;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

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

        getPlayer().closeInventory();
        getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(getPlayer(), "game.menu.spawnPrefSetJason", "Your spawn preference has been set as {0}jason{1}.", ChatColor.RED, ChatColor.WHITE));
    }

    /**
     * Sets the players spawn preference as Counselor
     */
    public void setSpawnPreferenceCounselor() {
        spawnPreferenceCounselor = true;
        spawnPreferenceJason = false;

        getPlayer().closeInventory();
        getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(getPlayer(), "game.menu.spawnPrefSetCounselor", "Your spawn preference has been set as a {0}counselor{1}.", ChatColor.DARK_GREEN, ChatColor.WHITE));
    }

    /**
     * Returns if the player prefers
     */
    public boolean isSpawnPreferenceJason() {
        return spawnPreferenceJason;
    }

    public boolean isSpawnPreferenceCounselor() {
        return spawnPreferenceCounselor;
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
