package com.AustinPilz.FridayThe13th.Manager.Game;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Enum.GameStatus;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Display.GameCountdownManager;
import com.AustinPilz.FridayThe13th.Manager.Display.GameScoreboardManager;
import com.AustinPilz.FridayThe13th.Manager.Display.WaitingCountdownDisplayManager;
import com.AustinPilz.FridayThe13th.Runnable.*;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.util.List;
import java.util.logging.Level;

public class
GameManager {
    private Arena arena;

    //Game Variables
    private int gameTimeLeftInSeconds;
    private int gameTimeMax;
    private int waitingTimeLeftInSeconds;
    private GameStatus gameStatus;

    //Tasks
    //second countdown (only when in waiting and in progress)
    private int gameStatusCheckTask = -1;
    private int gameCountdownTask = -1;
    private int waitingCountdownTask = -1;
    private int policeArriveCountdownTask = -1;
    private int waitingPlayerUpdateTask = -1;

    //Tommy Jarvis
    private boolean tommyCalled;
    private boolean tommySpawned;

    //Police
    private boolean policeCalled;
    private boolean policeArrived;
    private int timeUntilPoliceArrive;
    private int maxTimeUntilPoliceArrive;


    //Managers
    private PlayerManager playerManager;
    private GameCountdownManager gameCountdownManager;
    private WaitingCountdownDisplayManager waitingCountdownDisplayManager; //Game-wide waiting room countdown
    private GameScoreboardManager gameScoreboardManager;
    private WeatherManager weatherManager;

    /**
     * @param arena Game object
     */
    public GameManager(Arena arena) {
        this.arena = arena;
        resetGameStatistics();

        //Get max times
        gameTimeMax = 0; //Since game time is calculated based on number of counselors during every game
        maxTimeUntilPoliceArrive = 0;
        timeUntilPoliceArrive = 0;

        //Managers
        playerManager = new PlayerManager(arena);
        gameCountdownManager = new GameCountdownManager(arena);
        waitingCountdownDisplayManager = new WaitingCountdownDisplayManager(arena);
        gameScoreboardManager = new GameScoreboardManager(arena);
        weatherManager = new WeatherManager(arena);

        //Change game status to empty
        gameStatus = GameStatus.Empty; //to avoid null pointer
        changeGameStatus(GameStatus.Empty);

        //Start Tasks
        gameStatusCheckTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new GameStatusCheck(arena), 60, 20);
    }

    /**
     * Returns the arena's player manager
     *
     * @return
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * Returns the game countdown display manager
     *
     * @return
     */
    public GameCountdownManager getGameCountdownManager() {
        return gameCountdownManager;
    }

    /**
     * Returns the waiting countdown display manager
     */
    public WaitingCountdownDisplayManager getWaitingCountdownDisplayManager() {
        return waitingCountdownDisplayManager;
    }

    /**
     * Returns the game scoreboard display manager
     *
     * @return
     */
    public GameScoreboardManager getGameScoreboardManager() {
        return gameScoreboardManager;
    }

    /**
     * Returns the game's weather manager
     *
     * @return
     */
    public WeatherManager getWeatherManager() {
        return weatherManager;
    }

    /**
     * Returns the seconds left in the waiting countdown
     *
     * @return
     */
    public int getWaitingTimeLeft() {
        return waitingTimeLeftInSeconds;
    }

    /**
     * Sets the seconds left in the waiting countdown
     *
     * @param value
     */
    public void setWaitingTimeLeft(int value) {
        waitingTimeLeftInSeconds = value;
    }


    /**
     * Returns the number of seconds left in the game
     *
     * @return
     */
    public int getGameTimeLeft() {
        return gameTimeLeftInSeconds;
    }

    /**
     * Returns the maximum number of seconds per game
     *
     * @return
     */
    public int getGameTimeMax() {
        return gameTimeMax;
    }

    /**
     * Sets the time left in the game in seconds
     *
     * @param value
     */
    public void setGameTimeLeft(int value) {
        gameTimeLeftInSeconds = Math.max(0, value); //make sure it doesn't go below 0
    }

    /**
     * Marks that Tommy Jarvis was called
     */
    public void setTommyCalled() {
        tommyCalled = true;
    }

    /**
     * Returns if Tommy Jarvis has been called
     *
     * @return
     */
    public boolean hasTommyBeenCalled() {
        return tommyCalled;
    }

    /**
     * Marks that Tommy Jarvis was spawned
     */
    public void setTommySpawned() {
        tommySpawned = true;
    }

    /**
     * Returns if Tommy Jarvis has been spawned
     *
     * @return
     */
    public boolean hasTommyBeenSpawned() {
        return tommySpawned;
    }

    /**
     * @return If the police have been called
     */
    public boolean havePoliceBeenCalled() {
        return policeCalled;
    }

    /**
     * Sets if the police have been called
     *
     * @param value
     */
    public void setPoliceCalled(boolean value) {
        policeCalled = value;

        if (value) {
            calculatePoliceArrivalTime();
            policeArriveCountdownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new PoliceArrivalCountdown(arena), 20, 20);
        }
    }

    /**
     * @return Seconds until police arrive
     */
    public int getTimeUntilPoliceArrive() {
        return timeUntilPoliceArrive;
    }

    /**
     * @return Max seconds before the police arrive
     */
    public int getMaxTimeUntilPoliceArrive() {
        return maxTimeUntilPoliceArrive;
    }

    /**
     * Sets the number of seconds until the police arrive
     *
     * @param seconds
     */
    public void setTimeUntilPoliceArrive(int seconds) {
        timeUntilPoliceArrive = Math.max(seconds, 0);
    }

    /**
     * @return If the police have arrived
     */
    public boolean havePoliceArrived() {
        return policeArrived;
    }

    /**
     * Sets if the police have arrived
     *
     * @param value
     */
    public void setPoliceArrived(boolean value) {
        policeArrived = value;

        if (value) {
            arena.getLocationManager().getEscapePointManager().selectRandomEscapePointForPolice();
            getPlayerManager().sendActionBarMessageToAllPlayers(ChatColor.DARK_AQUA + "The police have arrived!", 80);
        }
    }

    /**
     * Resets the games internal statistics
     */
    private void resetGameStatistics() {
        setGameTimeLeft(getGameTimeMax());
        waitingTimeLeftInSeconds = arena.getSecondsWaitingRoom();
        tommyCalled = false;
        tommySpawned = false;
        policeCalled = false;
        policeArrived = false;
    }

    /**
     * Performs automated checks on the game to ensure status is always accurate
     */
    public void checkGameStatus() {
        if (isGameEmpty()) {
            if (getPlayerManager().getNumberOfPlayers() >= 2) {
                //There are people waiting and we've reached the min, change to waiting
                changeGameStatus(GameStatus.Waiting);
            } else {
                //Need more players before waiting countdown will begin

                for (F13Player player : getPlayerManager().getPlayers()) {
                    ActionBarAPI.sendActionBar(player.getBukkitPlayer(), ChatColor.RED + FridayThe13th.language.get(player.getBukkitPlayer(), "actionBar.waitingForMorePlayers", "Waiting for 1 more player before waiting countdown begins..."));
                }
            }
        } else if (isGameWaiting()) {
            if (getPlayerManager().getNumberOfPlayers() >= 2) {
                if (waitingTimeLeftInSeconds <= 0) {
                    //BEGIN THE GAME
                    changeGameStatus(GameStatus.InProgress);
                } else {
                    getPlayerManager().displayWaitingCountdown();
                }
            } else {
                //Minimum player requirement no longer met - Cancel waiting countdown task and go back to empty status
                changeGameStatus(GameStatus.Empty);
            }
        } else if (isGameInProgress()) {
            if (getPlayerManager().getNumberOfPlayers() < 2) {
                endGame(); //End the game since there aren't enough players
            }
        }
    }

    /**
     * Returns if the game is empty
     *
     * @return
     */
    public boolean isGameEmpty() {
        return gameStatus.equals(GameStatus.Empty);
    }

    /**
     * Returns if the game is waiting
     *
     * @return
     */
    public boolean isGameWaiting() {
        return gameStatus.equals(GameStatus.Waiting);
    }

    /**
     * Returns if the game is in progress
     *
     * @return
     */
    public boolean isGameInProgress() {
        return gameStatus.equals(GameStatus.InProgress);
    }

    /**
     * Changes the game status
     *
     * @param status
     */
    private void changeGameStatus(GameStatus status) {
        //Changing to empty
        if (status.equals(GameStatus.Empty)) {
            //Tasks
            Bukkit.getScheduler().cancelTask(waitingCountdownTask);
            Bukkit.getScheduler().cancelTask(gameCountdownTask);
            Bukkit.getScheduler().cancelTask(policeArriveCountdownTask);
            waitingPlayerUpdateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new PlayerWaitingDisplayUpdate(arena), 60, 60);

            if (isGameWaiting() && getPlayerManager().getNumberOfPlayers() == 0) {
                getPlayerManager().hideWaitingCountdown(); //Hide countdown from players
                //Resets all data structures with players since there are none left
            }

            if (isGameInProgress()) {
                getPlayerManager().resetPlayerStorage();
            }

            gameStatus = GameStatus.Empty; //Change mode
            resetGameStatistics();
        } else if (status.equals(GameStatus.Waiting)) //Changing to waiting (can only go from empty -> in waiting)
        {
            gameStatus = GameStatus.Waiting; //Change mode
            resetGameStatistics();

            //Start the tasks
            waitingCountdownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new GameWaitingCountdown(arena), 20, 20);

            //Reset players visuals (remove action bars)
            getPlayerManager().resetPlayerActionBars();

            //Display waiting countdown
            getWaitingCountdownDisplayManager().updateCountdownValue();
            getPlayerManager().displayWaitingCountdown();
        } else if (status.equals(GameStatus.InProgress)) //Changing to in progress (can only go from waiting -> in progress)
        {


            if (isGameWaiting()) {
                Bukkit.getScheduler().cancelTask(waitingCountdownTask); //Cancel waiting countdown
                Bukkit.getScheduler().cancelTask(waitingPlayerUpdateTask); //Cancel waiting sidebar display for players
                getPlayerManager().hideWaitingCountdown(); //Hide countdown from players
            }

            gameStatus = GameStatus.InProgress; //Change mode

            //Start the game
            beginGame();

            //Schedule game countdown
            gameCountdownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new GameCountdown(arena), 0, 20);
        }

        arena.getSignManager().updateJoinSigns(); //update the join signs
    }

    /**
     * Performs actions to begin the game
     */
    private void beginGame() {
        //Reset location manager spawn point availability
        arena.getLocationManager().resetAvailableStartingPoints();
        arena.getLocationManager().getEscapePointManager().resetEscapePoints();

        //Assign all players roles
        getPlayerManager().beginGame();

        //Calculate the game time
        calculateGameTime();

        //Display game phones
        arena.getObjectManager().getPhoneManager().displayGamePhones();

        //Radios
        arena.getObjectManager().placePerItemGames();

        //Vehicles
        arena.getObjectManager().getVehicleManager().prepareVehicles();

        //Start the weather service
        weatherManager.beginWeatherService();

        FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Game in " + arena.getName() + " beginning...");
    }

    /**
     * Ends the game
     */
    protected void endGame() {
        if (isGameInProgress()) {
            //Remove all players
            getPlayerManager().endGame();

            //Make countdown bar for any counselors disappear
            getGameCountdownManager().hideCountdownBar();

            //Replace any items changed during gameplay
            arena.getObjectManager().restorePerGameObjects();
            arena.getLocationManager().getEscapePointManager().resetEscapePoints();
            arena.getObjectManager().getVehicleManager().resetVehicles();

            //Stop the weather service
            weatherManager.endGame();

            //Remove any holograms from the previous game
            for (Hologram hologram : HologramsAPI.getHolograms(FridayThe13th.instance)) {
                if (arena.isLocationWithinArenaBoundaries(hologram.getLocation())) {
                    hologram.delete();
                }
            }

            //Remove any dropped items on the ground
            List<Entity> entList = arena.getBoundary1().getWorld().getEntities();//get all entities in the world

            for (Entity current : entList) {
                if (current instanceof Item && arena.isLocationWithinArenaBoundaries(current.getLocation())) {
                    current.remove();//remove it
                }
            }

            //Don't need to worry about tasks and timers here, handled automatically
            changeGameStatus(GameStatus.Empty);

            //Increment arena lifetime game counter
            arena.incrementLifetimeGames();
        }
    }

    /**
     * Ends the game when the time expires
     */
    public void gameTimeUp() {
        //need to pass that the counselors who are alive won.
        endGame();
    }

    /**
     * Calculates the game time based on the number of counselors
     */
    private void calculateGameTime() {
        double minutesPer = Math.max(arena.getMinutesPerCounselor(), 1.8); //1.8 is the min TPC allowed
        gameTimeLeftInSeconds = (int) Math.ceil(((minutesPer * arena.getGameManager().getPlayerManager().getNumberOfCounselors())) * 60);
        gameTimeMax = gameTimeLeftInSeconds;
    }

    /**
     * Calculates seconds until the police arrive
     */
    private void calculatePoliceArrivalTime() {
        maxTimeUntilPoliceArrive = Math.min(getGameTimeLeft() / 2, 300);
        setTimeUntilPoliceArrive(maxTimeUntilPoliceArrive);
    }

    /**
     * Clears the arena of all players - does not award XP
     */
    public void clearArena() {
        if (isGameEmpty() || isGameWaiting()) {
            for (F13Player player : getPlayerManager().getPlayers()) {
                getPlayerManager().onplayerQuit(player);
            }
        } else if (isGameInProgress()) {
            endGame();
        }
    }
}
