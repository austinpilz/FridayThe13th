package com.AustinPilz.FridayThe13th.Controller;

import com.AustinPilz.FridayThe13th.Components.Characters.F13Player;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerController {
    private HashMap<String, F13Player> players;

    public PlayerController() {
        players = new HashMap<>();
    }

    /**
     * Gets F13 player object of supplied bukkit player
     *
     * @param player
     * @return
     */
    public F13Player getPlayer(Player player) {
        return getPlayer(player.getUniqueId().toString());
    }

    /**
     * Gets F13 player object of supplied player UUID
     *
     * @param uuid
     * @return
     */
    public F13Player getPlayer(String uuid) {
        if (!doesPlayerExist(uuid)) {
            addPlayer(new F13Player(uuid));
        }

        return players.get(uuid);
    }

    /**
     * Adds F13 player
     *
     * @param player
     */
    public void addPlayer(F13Player player) {
        players.put(player.getPlayerUUID(), player);
    }

    /**
     * Returns if the F13 is in memory
     *
     * @param uuid
     * @return
     */
    private boolean doesPlayerExist(String uuid) {
        return players.containsKey(uuid);
    }
}
