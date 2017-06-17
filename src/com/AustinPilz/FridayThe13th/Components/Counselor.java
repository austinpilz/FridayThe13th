package com.AustinPilz.FridayThe13th.Components;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Display.CounselorStatsDisplayManager;
import com.AustinPilz.FridayThe13th.Runnable.CounselorFearCheck;
import com.AustinPilz.FridayThe13th.Structures.LightLevelList;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Counselor
{
    //Minecraft Objects
    private Player player;

    //Game
    private Arena arena;

    //Statistics
    private double stamina;
    private double maxStamina;
    private double staminaDepletionRate;
    private double staminaRegenerationRate; //Has to be higher since done every second, not every movement
    private double walkSpeed;
    private boolean moving = false;

    //Fear
    private double fearLevel;
    private double maxFearLevel;
    private LightLevelList lightHistory;
    private int lightHistoryMax = 20; //how many light histories are a part of the average

    //Managers
    private CounselorStatsDisplayManager statsDisplayManager;

    //Tasks
    int fearCheckTask = -1;

    /**
     * Creates new counselor object
     * @param p Minecraft player object
     */
    public Counselor(Player p, Arena a)
    {
        player = p;
        arena = a;

        //Stamina
        stamina = 60;
        maxStamina = 60;
        staminaDepletionRate = 0.1;
        staminaRegenerationRate = 1.5;

        //Fear
        fearLevel = 5;
        maxFearLevel = 60;

        //Initialize Manager
        statsDisplayManager = new CounselorStatsDisplayManager(this);

        //Fear
        lightHistory = new LightLevelList(20);
    }

    /**
     * Return' the player object of the counselor
     */
    public Player getPlayer()
    {
        return this.player;
    }

    /**
     * Returns counselor's stats display manager
     * @return
     */
    public CounselorStatsDisplayManager getStatsDisplayManager()
    {
        return statsDisplayManager;
    }

    /**
     * Schedules all counselor specific tasks
     */
    public void scheduleTasks()
    {
        //Start the stats updater
        getStatsDisplayManager().startUpdaterTask();

        //Fear Check Task
        fearCheckTask =  Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new CounselorFearCheck(this), 0, 20);
    }

    /**
     * Cancels all counselor specific tasks
     */
    public void cancelTasks()
    {
        //End the stats updater
        getStatsDisplayManager().endUpdaterTask();

        //Cancel fearLevel check task
        Bukkit.getScheduler().cancelTask(fearCheckTask);
    }

    /**
     * Returns if the counselor's moving flag has been set
     * @return
     */
    public boolean isMoving()
    {
        return moving;
    }

    /**
     * Sets if the counselor is moving
     * @param value
     */
    public void setMoving(Boolean value)
    {
        moving = value;
    }

    /**
     * Sets if the counselor is walking
     * @param value
     */
    public void setWalking(boolean value)
    {
        if (value)
        {
            setStamina(Math.max(0,stamina - (staminaDepletionRate)));
        }
    }

    /**
     * Sets if the counselor is running
     * @param value
     */
    public void setSprinting(boolean value)
    {
        if (value)
        {
            setStamina(Math.max(0,stamina - (staminaDepletionRate*2)));
            setMoving(true);
        }
    }

    /**
     * Sets if the counselor is sneaking
     * @param value
     */
    public void setSneaking(boolean value)
    {
        if (value)
        {
            setStamina(Math.max(0, stamina - staminaDepletionRate/2));
            setMoving(true);
        }
    }

    /**
     * Sets if the counselor is standing still
     * @param value
     */
    public void setStandingStill(boolean value)
    {
        if (value)
        {
            setStamina(Math.min(getMaxStamina(), stamina + staminaRegenerationRate));
            setMoving(false);
        }
    }

    /**
     * Returns the counselor's stamina level
     * @return
     */
    public double getStamina()
    {
        return stamina;
    }

    /**
     * Returns the counselor's max stamina level
     * @return
     */
    public double getMaxStamina()
    {
        return maxStamina;
    }

    /**
     * Set's the counselors stamina level
     * @param value
     */
    public void setStamina(Double value)
    {
        stamina = value;
    }

    /**
     * Returns the counselor's fearLevel level
     * @return
     */
    public double getFearLevel()
    {
        return fearLevel;
    }

    /**
     * Returns the counselor's max fearLevel level
     * @return
     */
    public double getMaxFearLevel()
    {
        return this.maxFearLevel;
    }

    /**
     * Calculates and updates the counselors fear level
     */
    public void updateFearLevel()
    {
        //Light level goes from 1 - 15, where lower is higher fear

        //Add new light history
        Block block = player.getLocation().getBlock().getRelative(0, 1, 0);
        Double lightLevel = (Double)(double)block.getLightLevel();
        lightHistory.addLevel(lightLevel);

        //Light level goes from 0-15
        Double fearLevel = ((((15-lightHistory.getAverage()) - 0) * (getMaxFearLevel() - 0)) / (15 - 0)) + 0;

        if (fearLevel > getFearLevel())
        {
            setFearLevel(fearLevel);
        }
        else
        {
            setFearLevel(fearLevel);
        }
    }

    /**
     * Sets the counselors fear level
     * @param value
     */
    public void setFearLevel(Double value)
    {
        fearLevel = Math.min(getMaxFearLevel(), value); //Can't exceed max or will cause boss bar issues
    }


}
