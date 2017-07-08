package com.AustinPilz.FridayThe13th.Manager.Arena;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Components.GameStatus;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.IO.Setting;
import com.AustinPilz.FridayThe13th.IO.Settings;
import com.AustinPilz.FridayThe13th.Manager.Display.GameCountdownManager;
import com.AustinPilz.FridayThe13th.Manager.Display.GameScoreboardManager;
import com.AustinPilz.FridayThe13th.Manager.Display.WaitingCountdownDisplayManager;
import com.AustinPilz.FridayThe13th.Runnable.GameCountdown;
import com.AustinPilz.FridayThe13th.Runnable.GameStatusCheck;
import com.AustinPilz.FridayThe13th.Runnable.WaitingCountdown;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameManager
{
    private Arena arena;

    //Game Variables
    private int gameTimeLeftInSeconds;
    private int gameTimeMax;
    private int waitingTimeLeftInSeconds;
    private int waitingTimeMax;
    private GameStatus gameStatus;

    //Tasks
    //second countdown (only when in waiting and in progress)
    int gameStatusCheckTask = -1;
    int gameCountdownTask = -1;
    int waitingCountdownTask = -1;


    //Managers
    public PlayerManager playerManager;
    private GameCountdownManager gameCountdownManager;
    private WaitingCountdownDisplayManager waitingCountdownDisplayManager; //Game-wide waiting room countdown
    private GameScoreboardManager gameScoreboardManager;

    /**
     * @param arena Arena object
     */
    public GameManager (Arena arena)
    {
        this.arena = arena;
        resetGameStatistics();

        //Get max times
        waitingTimeMax = Settings.getGlobalInt(Setting.gameplayWaitingTime);
        gameTimeMax = Settings.getGlobalInt(Setting.gameplayGameTime);

        //Managers
        playerManager = new PlayerManager(arena);
        gameCountdownManager = new GameCountdownManager(arena);
        waitingCountdownDisplayManager = new WaitingCountdownDisplayManager(arena);
        gameScoreboardManager = new GameScoreboardManager(arena);

        //Change game status to empty
        gameStatus = GameStatus.Empty; //to void null pointer
        changeGameStatus(GameStatus.Empty);

        //Start Tasks
        gameStatusCheckTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new GameStatusCheck(arena), 60, 20);
    }

    /**
     * Returns the arena's player manager
     * @return
     */
    public PlayerManager getPlayerManager()
    {
        return playerManager;
    }

    /**
     * Returns the game countdown display manager
     * @return
     */
    public GameCountdownManager getGameCountdownManager()
    {
        return gameCountdownManager;
    }

    /**
     * Returns the waiting countdown display manager
     */
    public WaitingCountdownDisplayManager getWaitingCountdownDisplayManager()
    {
        return waitingCountdownDisplayManager;
    }

    /**
     * Returns the game scoreboard display manager
     * @return
     */
    public GameScoreboardManager getGameScoreboardManager() { return gameScoreboardManager; }

    /**
     * Returns the seconds left in the waiting countdown
     * @return
     */
    public int getWaitingTimeLeft()
    {
        return waitingTimeLeftInSeconds;
    }

    /**
     * Sets the seconds left in the waiting countdown
     * @param value
     */
    public void setWaitingTimeLeft(int value)
    {
        waitingTimeLeftInSeconds = value;
    }

    /**
     * Returns the maximum number of seconds in the waiting countdown
     * @return
     */
    public int getWaitingTimeMax()
    {
        return waitingTimeMax;
    }

    /**
     * Returns the number of seconds left in the game
     * @return
     */
    public int getGameTimeLeft()
    {
        return gameTimeLeftInSeconds;
    }

    /**
     * Returns the maximum number of seconds per game
     * @return
     */
    public int getGameTimeMax()
    {
        return gameTimeMax;
    }

    /**
     * Sets the time left in the game in seconds
     * @param value
     */
    public void setGameTimeLeft(int value)
    {
        gameTimeLeftInSeconds = Math.max(0, value); //make sure it doesn't go below 0
    }
    /**
     * Resets the games internal statistics
     */
    private void resetGameStatistics()
    {
        setGameTimeLeft(getGameTimeMax());
        waitingTimeLeftInSeconds = getWaitingTimeMax();
    }

    /**
     * Performs automated checks on the game to ensure status is always accurate
     */
    public void checkGameStatus()
    {
        if (isGameEmpty())
        {
            if (getPlayerManager().getNumPlayers() >= 2)
            {
                //There are people waiting and we've reached the min, change to waiting
                changeGameStatus(GameStatus.Waiting);
            }
            else
            {
                //Need more players before waiting countdown will begin
                Iterator it = getPlayerManager().getPlayers().entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    Player player = (Player) entry.getValue();
                    ActionBarAPI.sendActionBar(player, ChatColor.RED + FridayThe13th.language.get(player, "actionBar.waitingForMorePlayers", "Waiting for 1 more player before waiting countdown begins..."));
                }


            }
        }
        else if (isGameWaiting())
        {
            if (getPlayerManager().getNumPlayers() >= 2)
            {
                if (waitingTimeLeftInSeconds <= 0)
                {
                    //BEGIN THE GAME
                    changeGameStatus(GameStatus.InProgress);
                }
            }
            else
            {
                //Cancel waiting countdown task and go back to empty status
                changeGameStatus(GameStatus.Empty);
            }
        }
        else if (isGameInProgress())
        {
            if (getPlayerManager().getNumPlayers() < 2)
            {
                endGame(); //End the game since there aren't enough players
            }
        }
    }

    /**
     * Returns if the game is empty
     * @return
     */
    public boolean isGameEmpty()
    {
        if (gameStatus.equals(GameStatus.Empty))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns if the game is waiting
     * @return
     */
    public boolean isGameWaiting()
    {
        if (gameStatus.equals(GameStatus.Waiting))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns if the game is in progress
     * @return
     */
    public boolean isGameInProgress()
    {
        if (gameStatus.equals(GameStatus.InProgress))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Changes the game status
     * @param status
     */
    private void changeGameStatus(GameStatus status)
    {
        //Changing to empty
        if (status.equals(GameStatus.Empty))
        {
            //Cancel tasks
            Bukkit.getScheduler().cancelTask(waitingCountdownTask); //Cancel task
            Bukkit.getScheduler().cancelTask(gameCountdownTask);

            if (isGameWaiting())
            {
                getPlayerManager().hideWaitingCountdown(); //Hide countdown from players
            }


            gameStatus = GameStatus.Empty; //Change mode
            resetGameStatistics();
            getPlayerManager().resetPlayerStorage(); //Resets all data structures with players

        }
        else if (status.equals(GameStatus.Waiting)) //Changing to waiting (can only go from empty -> in waiting)
        {
            gameStatus = GameStatus.Waiting; //Change mode
            resetGameStatistics();

            //Start the tasks
            waitingCountdownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new WaitingCountdown(arena), 20, 20);

            //Reset players visuals (remove action bars)
            getPlayerManager().resetPlayerActionBars();

            //Display waiting countdown
            getWaitingCountdownDisplayManager().updateCountdownValue();
            getPlayerManager().displayWaitingCountdown();
        }
        else if (status.equals(GameStatus.InProgress)) //Changing to in progress (can only go from waiting -> in progress)
        {
            if (isGameWaiting())
            {
                Bukkit.getScheduler().cancelTask(waitingCountdownTask); //Cancel task
                getPlayerManager().hideWaitingCountdown(); //Hide countdown from players
            }

            gameStatus = GameStatus.InProgress; //Change mode

            //Schedule game countdown
            gameCountdownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new GameCountdown(arena),0, 20);


            //Start the game
            beginGame();
        }

        arena.getSignManager().updateJoinSigns(); //update the join signs
    }

    private void beginGame()
    {
        //Reset location manager spawn point availability
        arena.getLocationManager().resetAvailableStartingPoints();

        //Regenerate tests
        arena.getObjectManager().regenerateChests();

        //Assign all players roles (maybe move these into the performInProgressActions() ?
        getPlayerManager().performInProgressActions();

    }

    /**
     * Ends the game
     */
    protected void endGame()
    {
        //Remove all players
        getPlayerManager().performEndGameActions();

        //Make countdown bar for any counselors disappear
        getGameCountdownManager().hideCountdownBar();

        //Replace any items changed during gameplay
        arena.getObjectManager().restorePerGameObjects();

        //Remove any holograms from the previous game
        //Remove Hologram
        for (Hologram hologram: HologramsAPI.getHolograms(FridayThe13th.instance))
        {
            hologram.delete();
        }

        //Remove any dropped items on the ground
        List<Entity> entList = arena.getBoundary1().getWorld().getEntities();//get all entities in the world

        for(Entity current : entList) {
            if (current instanceof Item && arena.isLocationWithinArenaBoundaries(current.getLocation())) {
                current.remove();//remove it
            }
        }

        //Don't need to worry about tasks and timers here, handled automatically
        changeGameStatus(GameStatus.Empty);
    }

    public void gameTimeUp()
    {
        //need to pass that the counselors who are alive won.
        endGame();
    }
}
