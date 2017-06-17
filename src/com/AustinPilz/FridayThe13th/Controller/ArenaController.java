package com.AustinPilz.FridayThe13th.Controller;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaAlreadyExistsException;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerAlreadyPlayingException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ArenaController
{
    private HashMap<String, Arena> arenas;
    private HashMap<String, Arena> players;

    public ArenaController ()
    {
        arenas = new HashMap<>();
        players = new HashMap<>();
    }

    /**
     * Adds arena into the controller memory
     * @param arena
     * @throws ArenaAlreadyExistsException
     */
    public void addArena(Arena arena) throws ArenaAlreadyExistsException
    {
        if (!arenas.containsValue(arena))
        {
            arenas.put(arena.getArenaName(), arena);
        }
        else
        {
            throw new ArenaAlreadyExistsException();
        }
    }

    /**
     * Removes the arena from the controller memory
     * @param arena
     */
    public void removeArena(Arena arena)
    {
        arenas.remove(arena.getArenaName());
    }

    /**
     * Returns the arena supplied by name
     * @param name Name of desired arena
     * @return
     */
    public Arena getArena(String name) throws ArenaDoesNotExistException
    {
        if (doesArenaExist(name))
        {
            return arenas.get(name);
        }
        else
        {
            throw new ArenaDoesNotExistException("Arena " + name + " does not exist in the controller memory");
        }
    }

    /**
     * Returns if the supplied arena by name exists in the controller memory
     * @param name Name of the arena
     * @return
     */
    public boolean doesArenaExist(String name)
    {
        return arenas.containsKey(name);
    }

    /**
     * Returns the number of arenas loaded in the controller memory
     * @return
     */
    public int getNumberOfArenas()
    {
        return arenas.size();
    }

    /**
     * Returns all arenas
     * @return
     */
    public HashMap<String, Arena> getArenas()
    {
        return arenas;
    }


    /* Players */

    /**
     * Adds player to global players list
     * @param playerUUID
     * @param arena
     * @throws PlayerAlreadyPlayingException
     */
    public void addPlayer(String playerUUID, Arena arena) throws PlayerAlreadyPlayingException
    {
        if (!isPlayerPlaying(playerUUID))
        {
            players.put(playerUUID, arena);
        }
        else
        {
            throw new PlayerAlreadyPlayingException();
        }
    }

    /**
     * Removes player from player hash map
     * @param playerUUID
     */
    public void removePlayer(String playerUUID)
    {
        players.remove(playerUUID);
    }

    /**
     * Returns the arena which the player is in
     * @param playerUUID
     * @return
     * @throws PlayerNotPlayingException
     */
    public Arena getPlayerArena(String playerUUID) throws PlayerNotPlayingException
    {
        if (isPlayerPlaying(playerUUID))
        {
            return players.get(playerUUID);
        }
        else
        {
            throw new PlayerNotPlayingException();
        }
    }

    /**
     * Returns if the player is actively playing within an arena
     * @param playerUUID
     * @return
     */
    public boolean isPlayerPlaying(String playerUUID)
    {
        if (players.containsKey(playerUUID))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
