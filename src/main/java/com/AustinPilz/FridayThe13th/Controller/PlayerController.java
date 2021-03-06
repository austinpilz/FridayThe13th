package com.AustinPilz.FridayThe13th.Controller;

import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PlayerController {
    private HashMap<String, F13Player> players;

    public PlayerController() {
        players = new HashMap<>();
    }

    /**
     * Gets F13 player object of supplied Bukkit player
     *
     * @param player Player
     * @return F13Player object of player
     */
    public F13Player getPlayer(Player player) {
        return getPlayer(player.getUniqueId().toString());
    }

    /**
     * Gets F13 player object of supplied player UUID
     *
     * @param uuid UUID of the player
     * @return F13Player object
     */
    public F13Player getPlayer(String uuid) {
        if (!doesPlayerExist(uuid)) {
            //They're not in memory, so let's check the database to see if they exist
            FridayThe13th.inputOutput.loadPlayer(uuid);

            if (!doesPlayerExist(uuid)) {
                //They weren't in the DB, so make new object and store them in DB
                F13Player player = new F13Player(uuid, "", "", 0, 0);
                player.storeToDB();
                addPlayer(player);
                return player;
            }
        }

        return players.get(uuid);
    }

    /**
     * Adds F13 player
     * @param player F13 Player Object
     */
    public void addPlayer(F13Player player) {
        players.put(player.getPlayerUUID(), player);
    }

    /**
     * Returns if the F13 is in memory
     *
     * @param uuid UUID of the player
     * @return If the player has played F13 before
     */
    private boolean doesPlayerExist(String uuid) {
        return players.containsKey(uuid);
    }

    /**
     * Returns if the player has ever played Friday the 13th before
     * @param uuid UUID of player
     * @return If the player has played F13 before
     */
    public boolean hasPlayerPlayed(String uuid)
    {
        if (doesPlayerExist(uuid))
        {
            return true;
        }
        else
        {
            FridayThe13th.inputOutput.loadPlayer(uuid);
            return doesPlayerExist(uuid);
        }
    }

    /**
     * @return Number of F13 players in memory
     */
    public int getNumPlayers() {
        return players.size();
    }

    /**
     * Removes
     */
    public void cleanupMemory() {
        Iterator it = players.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String playerUUID = (String) entry.getKey();
            F13Player player = (F13Player) entry.getValue();

            if (!Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).isOnline()) {
                player.updateDB();
                it.remove();
            }
        }
    }
}
