package com.AustinPilz.FridayThe13th.Manager.Game;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
import com.AustinPilz.FridayThe13th.FridayThe13th;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class WeatherManager
{
    private Arena arena;
    private int rainTime;
    private boolean rainEnabled;
    private boolean isRaining;

    public WeatherManager(Arena a)
    {
        this.arena = a;
        this.rainTime = 0;
        this.rainEnabled = false;
        this.isRaining = false;
    }

    /**
     * Starts the weather service at the beginning of the game
     */
    public void beginWeatherService()
    {
        //Determine the seconds at which the rain will begin - 15 seconds min
        rainTime = ThreadLocalRandom.current().nextInt(15, arena.getGameManager().getGameTimeLeft() + 1);

        //Determine if this match will have rain
        rainEnabled = getRandomBoolean();

        //Make it night
        arena.getBoundary1().getWorld().setTime(20000);

        FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Rain " + rainEnabled + " at " + rainTime);
    }

    /**
     * Performs time based weather services
     */
    public void weatherCheck()
    {
        //Rain Management
        if (rainEnabled && !isRaining && arena.getGameManager().getGameTimeLeft() <= rainTime)
        {
            //Begin the rain
            arena.getBoundary1().getWorld().setThundering(true);
            arena.getBoundary1().getWorld().setThunderDuration(arena.getGameManager().getGameTimeLeft()*20);
            arena.getBoundary1().getWorld().setStorm(true);

            //Set our flag
            isRaining = true;
        }
    }

    /**
     * Strikes lightning to players locations when sense is enabled by Jason
     */
    public void senseActivated()
    {
        if (rainEnabled && isRaining)
        {
            Iterator counselorIterator = arena.getGameManager().getPlayerManager().getCounselors().entrySet().iterator();
            while (counselorIterator.hasNext())
            {
                Map.Entry entry = (Map.Entry) counselorIterator.next();
                Counselor counselor = (Counselor) entry.getValue();
                counselor.getPlayer().getLocation().getWorld().strikeLightningEffect(counselor.getPlayer().getLocation());
            }
        }
    }

    /**
     * Performs cleanup actions when a game begins
     */
    public void endGame()
    {
        //Stop the rain
        arena.getBoundary1().getWorld().setStorm(false);
        arena.getBoundary1().getWorld().setThundering(false);

        //Reset things
        rainTime = 0;
        isRaining = false;
        rainEnabled = false;

    }

    /**
     * Returns a random boolean
     * @return
     */
    private boolean getRandomBoolean()
    {
        Random random = new Random();
        return random.nextBoolean();
    }
}
