package com.AustinPilz.FridayThe13th.Components;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Display.CounselorStatsDisplayManager;
import com.AustinPilz.FridayThe13th.Runnable.CounselorStatsUpdate;
import com.AustinPilz.FridayThe13th.Structures.LightLevelList;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    private boolean spectatingMode = false;

    //Fear
    private double fearLevel;
    private double maxFearLevel;
    private LightLevelList lightHistory;
    private int lightHistoryMax = 20; //how many light histories are a part of the average

    //Managers
    private CounselorStatsDisplayManager statsDisplayManager;

    //Tasks
    int statsUpdateTask = -1;

    //Potions
    private PotionEffect potionOutOfBreath;
    private PotionEffect potionFearBlind;
    private PotionEffect potionSpectatingInvisibility;

    //Warnings
    private boolean shownStaminaWarning = false;
    private boolean shownFearWarning = false;

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

        //Potions
        potionOutOfBreath = new PotionEffect(PotionEffectType.CONFUSION, 300, 1);
        potionFearBlind = new PotionEffect(PotionEffectType.BLINDNESS, 400, 1);
        potionSpectatingInvisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1);
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
     * Performs all necessary tasks when the game begins
     */
    public void prepareForGameplay()
    {
        ///Display Status
        getStatsDisplayManager().displayStats();

        //Start All Counselor Tasks
        scheduleTasks();
    }

    /**
     * Schedules all counselor specific tasks
     */
    public void scheduleTasks()
    {
        //Start the stats updater
        getStatsDisplayManager().startUpdaterTask();

        //Fear Check Task
        statsUpdateTask =  Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new CounselorStatsUpdate(this), 0, 20);
    }

    /**
     * Cancels all counselor specific tasks
     */
    public void cancelTasks()
    {
        //End the stats updater
        getStatsDisplayManager().endUpdaterTask();

        //Cancel fearLevel check task
        Bukkit.getScheduler().cancelTask(statsUpdateTask);
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
            setMoving(true);
            updateStaminaEffects();
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
            updateStaminaEffects();
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
            updateStaminaEffects();
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
            updateStaminaEffects();
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
     * Updates the effects of stamina on the player
     */
    public void updateStaminaEffects()
    {
        //If they're totally out of breath, apply the potion
        if (getStamina() == 0)
        {
            //Add confusion effect
            getPlayer().addPotionEffect(potionOutOfBreath);
        }

        if (getStamina() < (getMaxStamina()*.05))
        {
            //Stamina is very low
            getPlayer().setWalkSpeed(0.15f); //Make them walk slow

            //Give them a warning
            if (!shownStaminaWarning)
            {
                ActionBarAPI.sendActionBar(getPlayer(), ChatColor.RED + "Warning:" + ChatColor.WHITE + " Stamina low, you're almost out of energy.", 300);
                shownStaminaWarning = true;
            }
        }
        else
        {
            //Stamina is within acceptable limits
            getPlayer().setWalkSpeed(0.2f);
            getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
        }
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

        //Add new light history
        Block block = player.getLocation().getBlock().getRelative(0, 1, 0);
        Double lightLevel = (Double)(double)block.getLightLevel();
        lightHistory.addLevel(lightLevel);

        //Light level goes from 0-15
        Double fearLevel = ((((15-lightHistory.getAverage()) - 0) * (getMaxFearLevel() - 0)) / (15 - 0)) + 0;

        //See how far they are from jason
        double distanceFromJason = getPlayer().getLocation().distance(arena.getGameManager().getPlayerManager().getJason().getPlayer().getLocation());

        if (distanceFromJason <= 5)
        {
            //Increase if within certain distance
            double increase = getMaxFearLevel() * (distanceFromJason / 10);
            if (getFearLevel()-fearLevel > 0)
            {
                //fear level would be decreasing
                fearLevel = getFearLevel() + increase;
            }
            else
            {
                fearLevel += increase;
            }
        }

        //Ensure within boundaries
        setFearLevel(Math.min(getMaxFearLevel(), fearLevel));

        //Update effects
        updateFearLevelEffects();

    }

    /**
     * Updates the effects on the counselor as a result of fear
     */
    private void updateFearLevelEffects()
    {
        if ((fearLevel/getMaxFearLevel()) >= .9)
        {
            //They're scared, add some blindness
            getPlayer().addPotionEffect(potionFearBlind);

            //Reduce stamina since scared
            setSprinting(true);

            if (!shownFearWarning)
            {
                ActionBarAPI.sendActionBar(getPlayer(), ChatColor.RED + "Warning:" + ChatColor.WHITE + " You are scared. Find light sources to feel safe again.", 300);
                shownFearWarning = true;
            }
        }
        else
        {
            //They're not very scared, remove blindness
            getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
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

    /**
     * Returns if the counselor is in spectating mode
     * @return
     */
    public boolean isInSpectatingMode()
    {
        return spectatingMode;
    }

    /**
     * Enters the counselor into spectating mode
     */
    public void enterSpectatingMode()
    {
        spectatingMode = true;

        //Make them invisible
        getPlayer().addPotionEffect(potionSpectatingInvisibility);

        //Enter flight
        getPlayer().setAllowFlight(true);

        Location currentLocation = player.getLocation();
        getPlayer().teleport(new Location(currentLocation.getWorld(), currentLocation.getX(), currentLocation.getY()+10, currentLocation.getZ()));
        getPlayer().setFlying(true);
        getPlayer().getInventory().clear();

        //Stop stats since they're dead
        getStatsDisplayManager().hideStats();
        cancelTasks();

        //Let them know
        ActionBarAPI.sendActionBar(getPlayer(), ChatColor.RED + "You died. " + ChatColor.GREEN + "You are now in spectating mode. You are invisible and can fly.", 400);
    }

    /**
     * Removes the counselor from spectating mode
     */
    public void leaveSpectatingMode()
    {
        //removes abilities and stuff
        getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        getPlayer().setAllowFlight(false);
        spectatingMode = false;
    }

    public void removeAllPotionEffects()
    {
        getPlayer().removePotionEffect(PotionEffectType.BLINDNESS); //Scared
        getPlayer().removePotionEffect(PotionEffectType.CONFUSION); //Out of breath
    }
}
