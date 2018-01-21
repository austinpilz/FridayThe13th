package com.AustinPilz.FridayThe13th.Controller;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Arena.ArenaPhone;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaAlreadyExistsException;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerAlreadyPlayingException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ArenaController
{
    private HashMap<String, Arena> arenas;
    private HashMap<F13Player, Arena> players;

    public ArenaController ()
    {
        arenas = new HashMap<>();
        players = new HashMap<>();
    }

    /**
     * Adds arena into the controller memory
     * @param arena Arena object
     * @throws ArenaAlreadyExistsException The arena already exists in memory
     */
    public void addArena(Arena arena) throws ArenaAlreadyExistsException
    {
        if (!arenas.containsValue(arena))
        {
            arenas.put(arena.getName(), arena);
        }
        else
        {
            throw new ArenaAlreadyExistsException();
        }
    }

    /**
     * Removes the arena from the controller memory
     * @param arena Arena object
     */
    public void removeArena(Arena arena)
    {
        arenas.remove(arena.getName());
    }

    /**
     * Returns the arena supplied by name
     * @param name Name of desired arena
     * @return Arena object
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
     * @return If the arena exists
     */
    public boolean doesArenaExist(String name)
    {
        return arenas.containsKey(name);
    }

    /**
     * Returns the number of arenas loaded in the controller memory
     * @return The number of arenas
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
     * @param player F13Player
     * @param arena Arena
     * @throws PlayerAlreadyPlayingException
     */
    public void addPlayer(F13Player player, Arena arena) throws PlayerAlreadyPlayingException
    {
        if (!isPlayerPlaying(player))
        {
            players.put(player, arena);
        }
        else
        {
            throw new PlayerAlreadyPlayingException();
        }
    }

    /**
     * Removes player from player hash map
     * @param player F13Player
     */
    public void removePlayer(F13Player player)
    {
        players.remove(player);
    }

    /**
     * Returns the arena which the player is in
     * @param player F13Player
     * @return Arena That the player is playing within
     * @throws PlayerNotPlayingException
     */
    public Arena getPlayerArena(F13Player player) throws PlayerNotPlayingException
    {
        if (isPlayerPlaying(player))
        {
            return players.get(player);
        }
        else
        {
            throw new PlayerNotPlayingException();
        }
    }

    /**
     * Returns the arena which the player is in
     *
     * @param player Bukkit player
     * @return Arena That the player is playing within
     * @throws PlayerNotPlayingException
     */
    public Arena getPlayerArena(Player player) throws PlayerNotPlayingException {
        return getPlayerArena(FridayThe13th.playerController.getPlayer(player));
    }

    /**
     * Returns if the player is actively playing within an arena
     * @param player F13Player
     * @return If the play is actively playing within an arena
     */
    public boolean isPlayerPlaying(F13Player player) {
        return players.containsKey(player);
    }

    /**
     * Returns if the player is actively playing within an arena
     * @param player Bukkit player
     * @return If the play is actively playing within an arena
     */
    public boolean isPlayerPlaying(Player player) {
        return isPlayerPlaying(FridayThe13th.playerController.getPlayer(player));
    }


    /**
     * Returns hashmap of player UUID string and Game objects
     * @return
     */
    public HashMap<F13Player, Arena> getPlayers()
    {
        return players;
    }

    /**
     * Auto-joins player to an available arena, if any.
     * @param player Bukkit player
     * @return If the player can join an arena
     */
    public boolean playerAutoJoin(Player player) {
        return playerAutoJoin(FridayThe13th.playerController.getPlayer(player));
    }

    /**
     * Auto-joins player to an available arena, if any.
     *
     * @param player F13Player
     * @return If the player can join an arena
     */
    public boolean playerAutoJoin(F13Player player) {
        if (FridayThe13th.arenaController.getNumberOfArenas() > 0) {
            //Iterate through waiting games to find the one with the most number of players

            Arena arenaWithMostPlayers = null;

            Iterator it = arenas.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Arena arena = (Arena) entry.getValue();

                if (arenaWithMostPlayers != null) {
                    if (arena.getGameManager().isGameWaiting() && arena.getGameManager().getPlayerManager().isRoomForPlayerToJoin() && arena.getGameManager().getPlayerManager().getNumberOfPlayers() > arenaWithMostPlayers.getGameManager().getPlayerManager().getNumberOfPlayers()) {
                        arenaWithMostPlayers = arena;
                    }
                } else if (arena.getGameManager().isGameWaiting() && arena.getGameManager().getPlayerManager().isRoomForPlayerToJoin()) {
                    arenaWithMostPlayers = arena;
                }
            }

            if (arenaWithMostPlayers != null) {
                try {
                    //Add the player to the arena that we found
                    arenaWithMostPlayers.getGameManager().getPlayerManager().playerJoinGame(player);
                    return true;
                } catch (GameFullException exception) {
                    //The game is full
                    return false;
                } catch (GameInProgressException exception) {
                    //The game is in progress
                    return false;
                }
            } else {
                //We didn't find any in progress arenas with room, so lets just stick them in a random arena that's empty (preferring an empty one with a player in)

                if (getEmptyArenas().size() > 0) {
                    Arena[] arenas;

                    if (getEmptyArenasWithOnePlayer().size() > 0) {
                        arenas = getEmptyArenasWithOnePlayer().toArray(new Arena[getEmptyArenasWithOnePlayer().size()]);
                    } else {
                        arenas = getEmptyArenas().toArray(new Arena[getEmptyArenas().size()]);
                    }


                    //Randomize possible arenas
                    Random rnd = ThreadLocalRandom.current();
                    for (int i = arenas.length - 1; i > 0; i--) {
                        int index = rnd.nextInt(i + 1);

                        // Simple swap
                        Arena a = arenas[index];
                        arenas[index] = arenas[i];
                        arenas[i] = a;
                    }

                    try {
                        //Add the player to the arena that we found
                        arenas[0].getGameManager().getPlayerManager().playerJoinGame(player);
                        return true;
                    } catch (GameFullException exception) {
                        //The game is full
                        return false;
                    } catch (GameInProgressException exception) {
                        //The game is in progress
                        return false;
                    }
                } else {
                    //There are no empty arenas to put them in
                    return false;
                }
            }
        } else {
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
            if (arena.getGameManager().isGameEmpty() && arena.getGameManager().getPlayerManager().getNumberOfPlayers() > 0) {
                emptyArenas.add(arena);
            }
        }

        return emptyArenas;
    }

    /**
     * Returns if the supplied location is within one of the F13 arenas
     *
     * @param location
     * @return If location is within an arena
     */
    public boolean isLocationWithinAnArena(Location location) {
        ArrayList<Arena> emptyArenas = new ArrayList<>();

        Iterator it = arenas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Arena arena = (Arena) entry.getValue();
            if (arena.isLocationWithinArenaBoundaries(location)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param location
     * @return The arena object that encompasses the supplied location
     */
    public Arena getArenaFromLocation(Location location) {
        ArrayList<Arena> emptyArenas = new ArrayList<>();

        Iterator it = arenas.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Arena arena = (Arena) entry.getValue();
            if (arena.isLocationWithinArenaBoundaries(location)) {
                return arena;
            }
        }

        return null;
    }

    /**
     * @return Random Arena
     */
    public Arena getRandomArena()
    {
        if (arenas.size() > 0) {
            Arena[] pl = arenas.values().toArray(new Arena[arenas.size()]);

            //Randomize possible phones
            Random rnd = ThreadLocalRandom.current();
            for (int i = pl.length - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);

                // Simple swap
                Arena a = pl[index];
                pl[index] = pl[i];
                pl[i] = a;
            }

            return pl[0];
        } else {
            return null;
        }
    }
}
