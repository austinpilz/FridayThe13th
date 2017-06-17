package com.AustinPilz.FridayThe13th.Manager.Arena;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Components.Counselor;
import com.AustinPilz.FridayThe13th.Components.Jason;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerAlreadyPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager
{
    private Arena arena;

    //Arena Players
    private HashMap<String, Player> players;
    private HashMap<String, Counselor> counselors;
    private Jason jason;

    /**
     * @param arena Arena
     */
    public PlayerManager(Arena arena)
    {
        this.arena = arena;
        this.players = new HashMap<>();
        this.counselors = new HashMap<>();
    }

    /**
     * Returns the number of players in the game
     * @return
     */
    public int getNumPlayers()
    {
        return players.size();
    }

    /**
     * Returns hash map of current players
     * @return
     */
    public HashMap<String, Player> getPlayers()
    {
        return players;
    }

    /**
     * Returns hash map of current counselors
     * @return
     */
    public HashMap<String, Counselor> getCounselors() { return counselors; }

    /**
     * Returns if the supplied player is a counselor
     * @param player
     * @return
     */
    public boolean isCounselor(Player player)
    {
        return counselors.containsKey(player.getUniqueId().toString());
    }

    /**
     * Returns if the supplied payer is jason
     * @param player
     * @return
     */
    public boolean isJason(Player player)
    {
        if (jason.getPlayer().equals(player))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns the counselor object for the player
     * @param player
     * @return
     */
    public Counselor getCounselor (Player player)
    {
        return counselors.get(player.getUniqueId().toString());
    }


    /**
     * Returns the counselor object for the player
     * @param playerUUID
     * @return
     */
    public Counselor getCounselor (String playerUUID)
    {
        return counselors.get(playerUUID);
    }

    /**
     * Adds player to the player hash map
     * @param p
     */
    private void addPlayer(Player p)
    {
        players.put(p.getUniqueId().toString(), p);
    }

    /**
     * Removes player from the player hash map
     * @param playerUUID
     */
    private void removePlayer(String playerUUID)
    {
        players.remove(playerUUID);
    }

    /* Display */

    /**
     * Resets the action bars of all players to nothing
     */
    public void resetPlayerActionBars()
    {
        Iterator it = getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player player = (Player) entry.getValue();
            ActionBarAPI.sendActionBar(player, "");
        }
    }

    /**
     * Displays the waiting countdown for all players
     */
    public void displayWaitingCountdown()
    {
        Iterator it = getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player player = (Player) entry.getValue();
            arena.getGameManager().getWaitingCountdownDisplayManager().displayForPlayer(player);
        }
    }

    /**
     * Hides the waiting countdown from all players
     */
    public void hideWaitingCountdown()
    {
        arena.getGameManager().getWaitingCountdownDisplayManager().hideFromAllPlayers();
    }


    /* Game Join */

    /**
     * Adds player to the game, if room is available
     * @param player
     * @throws GameFullException
     */
    public synchronized void playerJoinGame(Player player) throws GameFullException, GameInProgressException
    {
        if (arena.getGameManager().isGameEmpty() || arena.getGameManager().isGameWaiting()) {
            //Determine if there's room for this user
            if (isRoomForPlayerToJoin()) {
                try {
                    //Add to arena controller global player list
                    FridayThe13th.arenaController.addPlayer(player.getUniqueId().toString(), arena);
                    addPlayer(player);

                    //Waiting actions
                    performWaitingActions(player);

                } catch (PlayerAlreadyPlayingException exception) {
                    //They're already in the controller global player list
                    player.sendMessage(FridayThe13th.pluginPrefix + "Failed to add you to game because you're already registered as playing a game.");
                }
            } else {
                throw new GameFullException();
            }
        }
        else
        {
            throw new GameInProgressException();
        }
    }

    /**
     * Calculates if there is room for the player to join the game
     * @return
     */
    private boolean isRoomForPlayerToJoin()
    {
        if ((arena.getLocationManager().getNumberStartingPoints() - getNumPlayers() + 1) > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /* Player Events */
    public void playerLeaveGame(String playerUUID)
    {
        //Remove player from hash maps (have to remove these first since listeners check to see if player has an arena)
        FridayThe13th.arenaController.removePlayer(playerUUID);
        removePlayer(playerUUID);

        //Clear any displays
        Counselor counselor = getCounselor(playerUUID);

        //Stop any individual counselor tasks
        counselor.cancelTasks();


        //Actions done only if they're online
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
        if (offlinePlayer.isOnline())
        {
            //Teleport them to the return point
            Bukkit.getPlayer(UUID.fromString(playerUUID)).teleport(arena.getReturnLocation());

            //Hide the stats bars
            counselor.getStatsDisplayManager().hideStats();
        }

        //Restore inventory?
    }

    /**
     * Assigns all players a role (counselor or jason)
     */
    protected void assignGameRoles()
    {
        Random generator = new Random();
        Object[] playerArray = players.values().toArray();
        int jasonCell = generator.nextInt(playerArray.length);

        //Select Jason
        Player jasonPlayer = (Player)playerArray[jasonCell];
        this.jason = new Jason(jasonPlayer, arena);

        //Make everyone else counselors
        for (int i = 0; i < playerArray.length; i++)
        {
            if (i != jasonCell)
            {
                Player counselorPlayer = (Player)playerArray[i];
                this.counselors.put(counselorPlayer.getUniqueId().toString(), new Counselor(counselorPlayer, arena));
            }
        }
    }

    /**
     * Assigns and teleports players and jason to their spawn locations
     */
    protected void assignSpawnLocations()
    {
        //Teleport jason to jason start point
        jason.getPlayer().teleport(arena.getJasonStartLocation());

        //Teleport counselors to starting points

        Location[] counselorLocations = arena.getLocationManager().getAvailableStartingPoints().toArray(new Location[arena.getLocationManager().getAvailableStartingPoints().size()]);

        Iterator it = getCounselors().entrySet().iterator();
        int i = 0;
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Counselor counselor = (Counselor) entry.getValue();
            counselor.getPlayer().teleport(counselorLocations[i++]);
        }
    }

    /* Player Preparation Actions */
    private void performWaitingActions(Player p)
    {
        //Teleport player to waiting location
        p.teleport(arena.getWaitingLocation());
    }

    /**
     * Transitions and prepares all players to play the game
     */
    protected void performInProgressActions()
    {
        //Display player bars
        Iterator it = getCounselors().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Counselor counselor = (Counselor) entry.getValue();

            //Display Status
            counselor.getStatsDisplayManager().displayStats();

            //Start All Counselor Tasks
            counselor.scheduleTasks();
        }
    }

    private void performEndGameActions()
    {
        //
    }
}
