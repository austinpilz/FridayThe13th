package com.AustinPilz.FridayThe13th.Controller;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaAlreadyExistsException;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerAlreadyPlayingException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
            throw new ArenaDoesNotExistException("Game " + name + " does not exist in the controller memory");
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
        return players.containsKey(playerUUID);
    }

    /**
     * Returns if the player is actively playing within an arena
     * @param player
     * @return
     */
    public boolean isPlayerPlaying(Player player)
    {
        return isPlayerPlaying(player.getUniqueId().toString());
    }

    /**
     * Returns hashmap of player UUID string and Game objects
     * @return
     */
    public HashMap<String, Arena> getPlayers()
    {
        return players;
    }

    /**
     * Auto-joins player to an available arena, if any.
     * @param player
     * @return
     */
    public boolean playerAutoJoin(Player player)
    {
        if (FridayThe13th.arenaController.getNumberOfArenas() > 0)
        {
            //Iterate through waiting games to find the one with the most number of players

            Arena arenaWithMostPlayers = null;

            Iterator it = arenas.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry) it.next();
                Arena arena = (Arena) entry.getValue();

                if (arenaWithMostPlayers != null)
                {
                    if (arena.getGameManager().isGameWaiting() && arena.getGameManager().getPlayerManager().isRoomForPlayerToJoin() && arena.getGameManager().getPlayerManager().getNumPlayers() > arenaWithMostPlayers.getGameManager().getPlayerManager().getNumPlayers())
                    {
                        arenaWithMostPlayers = arena;
                    }
                }
                else if (arena.getGameManager().isGameWaiting() && arena.getGameManager().getPlayerManager().isRoomForPlayerToJoin())
                {
                   arenaWithMostPlayers = arena;
                }
            }

            if (arenaWithMostPlayers != null)
            {
                try
                {
                    //Add the player to the arena that we found
                    arenaWithMostPlayers.getGameManager().getPlayerManager().playerJoinGame(player);
                    return true;
                }
                catch (GameFullException exception)
                {
                    //The game is full
                    return false;
                }
                catch (GameInProgressException exception)
                {
                    //The game is in progress
                    return false;
                }
            }
            else
            {
                //We didn't find any in progress arenas with room, so lets just stick them in a random arena that's empty (preferring an empty one with a player in)

                if (getEmptyArenas().size() > 0)
                {
                    Arena[] arenas;

                    if (getEmptyArenasWithOnePlayer().size() > 0)
                    {
                        arenas = getEmptyArenasWithOnePlayer().toArray(new Arena[getEmptyArenasWithOnePlayer().size()]);
                    }
                    else
                    {
                        arenas = getEmptyArenas().toArray(new Arena[getEmptyArenas().size()]);
                    }


                    //Randomize possible arenas
                    Random rnd = ThreadLocalRandom.current();
                    for (int i = arenas.length - 1; i > 0; i--)
                    {
                        int index = rnd.nextInt(i + 1);

                        // Simple swap
                        Arena a = arenas[index];
                        arenas[index] = arenas[i];
                        arenas[i] = a;
                    }

                    try
                    {
                        //Add the player to the arena that we found
                        arenas[0].getGameManager().getPlayerManager().playerJoinGame(player);
                        return true;
                    }
                    catch (GameFullException exception)
                    {
                        //The game is full
                        return false;
                    }
                    catch (GameInProgressException exception)
                    {
                        //The game is in progress
                        return false;
                    }
                }
                else
                {
                    //There are no empty arenas to put them in
                    return false;
                }
            }
        }
        else
        {
            //There are no arenas
            return false;
        }
    }

    /**
     * Returns a list of all empty arenas
     * @return
     */
    private ArrayList<Arena> getEmptyArenas()
    {
        ArrayList<Arena> emptyArenas = new ArrayList<>();

        Iterator it = arenas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Arena arena = (Arena) entry.getValue();
            if (arena.getGameManager().isGameEmpty()) {
                emptyArenas.add(arena);
            }
        }

        return emptyArenas;
    }

    /**
     * Returns a list of all empty arenas who have one player waiting in them
     * @return
     */
    private ArrayList<Arena> getEmptyArenasWithOnePlayer()
    {
        ArrayList<Arena> emptyArenas = new ArrayList<>();

        Iterator it = arenas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Arena arena = (Arena) entry.getValue();
            if (arena.getGameManager().isGameEmpty() && arena.getGameManager().getPlayerManager().getNumPlayers() > 0) {
                emptyArenas.add(arena);
            }
        }

        return emptyArenas;
    }
}
