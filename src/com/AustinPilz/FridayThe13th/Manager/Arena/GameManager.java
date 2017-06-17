package com.AustinPilz.FridayThe13th.Manager.Arena;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Components.GameStatus;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Display.WaitingCountdownDisplayManager;
import com.AustinPilz.FridayThe13th.Runnable.GameStatusCheck;
import com.AustinPilz.FridayThe13th.Runnable.WaitingCountdown;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;

public class GameManager
{
    private Arena arena;

    //Game Variables
    public int gameTimeLeftInSeconds;
    private int waitingTimeLeftInSeconds;
    private int waitingTimeMax = 5;
    private GameStatus gameStatus;

    //Tasks
    //second countdown (only when in waiting and in progress)
    int gameStatusCheckTask = -1;
    int waitingCountdownTask = -1;


    //Managers
    protected WaitingCountdownDisplayManager waitingCountdownDisplayManager;

    /**
     * @param arena Arena object
     */
    public GameManager (Arena arena)
    {
        this.arena = arena;
        resetGameStatistics();

        //Change game status to empty
        gameStatus = GameStatus.Empty; //to void null pointer
        changeGameStatus(GameStatus.Empty);

        //Managers
        waitingCountdownDisplayManager = new WaitingCountdownDisplayManager(arena);

        //Start Tasks
        gameStatusCheckTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new GameStatusCheck(arena), 60, 20);
    }

    /**
     * Returns the countdown display manager
     */
    public WaitingCountdownDisplayManager getWaitingCountdownDisplayManager()
    {
        return waitingCountdownDisplayManager;
    }

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
     * Resets the games internal statistics
     */
    private void resetGameStatistics()
    {
        gameTimeLeftInSeconds = 0;
        waitingTimeLeftInSeconds = getWaitingTimeMax();
    }

    /**
     * Performs automated checks on the game to ensure status is always accurate
     */
    public void checkGameStatus()
    {
        if (isGameEmpty())
        {
            if (arena.getPlayerManager().getNumPlayers() >= 2)
            {
                //There are people waiting and we've reached the min, change to waiting
                changeGameStatus(GameStatus.Waiting);
            }
            else
            {
                //Need more players before waiting countdown will begin
                Iterator it = arena.getPlayerManager().getPlayers().entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    Player player = (Player) entry.getValue();
                    ActionBarAPI.sendActionBar(player, ChatColor.RED + "Waiting for 1 more player before waiting countdown begins...");
                }


            }
        }
        else if (isGameWaiting())
        {
            if (arena.getPlayerManager().getNumPlayers() >= 2)
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
            if (arena.getPlayerManager().getNumPlayers() < 2)
            {
                //TODO END GAME COMMAND HERE
                //TODO KICK ALL PLAYERS, ETC ^^^
                changeGameStatus(GameStatus.Empty);
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
            if (isGameWaiting())
            {
                Bukkit.getScheduler().cancelTask(waitingCountdownTask); //Cancel task
                arena.getPlayerManager().hideWaitingCountdown(); //Hide countdown from players
            }

            gameStatus = GameStatus.Empty; //Change mode
            resetGameStatistics();

        }
        else if (status.equals(GameStatus.Waiting)) //Changing to waiting (can only go from empty -> in waiting)
        {
            gameStatus = GameStatus.Waiting; //Change mode
            resetGameStatistics();

            //Start the timer task
            waitingCountdownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new WaitingCountdown(arena), 20, 20);

            //Reset players visuals (remove action bars)
            arena.getPlayerManager().resetPlayerActionBars();

            //Display waiting countdown
            getWaitingCountdownDisplayManager().updateCountdownValue();
            arena.getPlayerManager().displayWaitingCountdown();
        }
        else if (status.equals(GameStatus.InProgress)) //Changing to in progress (can only go from waiting -> in progress)
        {
            if (isGameWaiting())
            {
                Bukkit.getScheduler().cancelTask(waitingCountdownTask); //Cancel task
                arena.getPlayerManager().hideWaitingCountdown(); //Hide countdown from players
            }

            gameStatus = GameStatus.InProgress; //Change mode

            //Start the game
            beginGame();
        }
    }

    private void beginGame()
    {
        //Reset location manager spawn point availability
        arena.getLocationManager().resetAvailableStartingPoints();

        //Assign all players roles (maybe move these into the performInProgressActions() ?
        arena.getPlayerManager().assignGameRoles();
        arena.getPlayerManager().assignSpawnLocations(); //teleports players to spawn locations
        arena.getPlayerManager().performInProgressActions();

        //Generate chests and stuff here?!

    }

    private void endGame()
    {
        //cancel timers and tasks
    }
}
