package com.AustinPilz.FridayThe13th.Components;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Display.CounselorStatsDisplayManager;
import com.AustinPilz.FridayThe13th.Runnable.CounselorStatsUpdate;
import com.AustinPilz.FridayThe13th.Structures.LightLevelList;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

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
    private PotionEffect potionSenseByJason;

    //Warnings
    private boolean shownStaminaWarning = false;
    private boolean shownFearWarning = false;
    private boolean isTommyJarvis = false;

    //Restore values
    private float originalWalkSpeed;
    private float originalFlySpeed;
    private boolean originalAllowFly;

    //Etc
    private boolean awaitingWindowJump;

    /**
     * Creates new counselor object
     * @param p Minecraft player object
     */
    public Counselor(Player p, Arena a)
    {
        player = p;
        arena = a;

        //Stamina
        stamina = 100;
        maxStamina = 100;
        staminaDepletionRate = 0.1;
        staminaRegenerationRate = .1;

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
        potionSenseByJason = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 10);

        //Restore Values
        originalWalkSpeed = player.getWalkSpeed();
        originalFlySpeed = player.getFlySpeed();
        originalAllowFly = player.getAllowFlight();

        //Etc
        awaitingWindowJump = false;
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

        //Display game-wide scoreboard
        arena.getGameManager().getGameScoreboardManager().displayForPlayer(getPlayer());

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
        statsUpdateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new CounselorStatsUpdate(this), 0, 20);
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
            setStamina(Math.min(getMaxStamina(), stamina + getMaxStamina()*staminaRegenerationRate));
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
     * Returns the counselor's stamina percentage
     * @return
     */
    public double getStaminaPercentage()
    {
        return getStamina() / getMaxStamina();
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
                ActionBarAPI.sendActionBar(getPlayer(), ChatColor.RED + FridayThe13th.language.get(player, "actionBar.counselor.staminaLow", "Warning: {0}Stamina low. You're almost out of energy.", ChatColor.WHITE), 300);
                shownStaminaWarning = true;
            }
        }
        else
        {
            //Stamina is within acceptable limits

            if (getPlayer().getHealth() / getPlayer().getHealthScale() <= .25)
            {
                //They're injured, so change walk speed
                getPlayer().setWalkSpeed(0.13f);
            }
            else
            {
                //Their health is fine, so make walk speed normal
                getPlayer().setWalkSpeed(0.2f);
            }

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
     * Returns the counselor's fear percentage
     * @return
     */
    public double getFearLevelPercentage()
    {
        return getFearLevel() / getMaxFearLevel();
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

        if (distanceFromJason <= 5 && !arena.getGameManager().getPlayerManager().getJason().getPlayer().isSneaking())
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
        if (isTommyJarvis)
        {
            setFearLevel(0.0); //They're Tommy, so keep fear at 0
        }
        else
        {
            //They're not Tommy, so set fear level
            setFearLevel(Math.min(getMaxFearLevel(), fearLevel));
        }

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
                ActionBarAPI.sendActionBar(getPlayer(), ChatColor.RED + FridayThe13th.language.get(player, "actionBar.counselor.fearLevelHigh", "You are scared. {0}Find a well lit area.", ChatColor.WHITE), 300);
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

        //Remove any potions from in game
        removeAllPotionEffects();

        //Set counselor stats to full to avoid messages
        setFearLevel(0.0);
        setStamina(getMaxStamina());

        //Make them invisible
        getPlayer().addPotionEffect(potionSpectatingInvisibility);

        //Enter flight
        getPlayer().setAllowFlight(true);
        getPlayer().setHealth(20);

        Location currentLocation = player.getLocation();
        getPlayer().teleport(new Location(currentLocation.getWorld(), currentLocation.getX(), currentLocation.getY()+10, currentLocation.getZ()));
        getPlayer().setFlying(true);
        getPlayer().getInventory().clear();

        //Stop stats since they're dead
        getStatsDisplayManager().hideStats();
        cancelTasks();

        //Give them the compass
        ItemStack compass = new ItemStack(Material.EMERALD, 1);
        ItemMeta compassMetaData = compass.getItemMeta();
        compassMetaData.setDisplayName(ChatColor.GREEN + "Spectate Selector");
        compass.setItemMeta(compassMetaData);
        getPlayer().getInventory().addItem(compass);

        //Let them know
        ActionBarAPI.sendActionBar(getPlayer(), ChatColor.RED + FridayThe13th.language.get(player, "actionBar.counselor.enterSpectatingMode", "You Died! {0}You are now in spectating mode.", ChatColor.WHITE), 300);

        //Hide this player from everyone else
        Iterator it = arena.getGameManager().getPlayerManager().getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player hideFrom = (Player) entry.getValue();
            hideFrom.hidePlayer(getPlayer());
        }
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
        ActionBarAPI.sendActionBar(getPlayer(), "");
        arena.getGameManager().getGameCountdownManager().hideFromPlayer(getPlayer());

        //Make visible to all players
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.showPlayer(getPlayer());
        }

    }

    /**
     * Removes potion effects from counselor
     */
    public void removeAllPotionEffects()
    {
        getPlayer().removePotionEffect(PotionEffectType.BLINDNESS); //Scared
        getPlayer().removePotionEffect(PotionEffectType.CONFUSION); //Out of breath
        getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        getPlayer().removePotionEffect(PotionEffectType.GLOWING);
        getPlayer().closeInventory(); //Close the spectate inventory if they happen to have it open
    }

    /* SENSE BY JASON */

    /**
     * Returns if counselor can be sensed by Jason
     * @return
     */
    private boolean canBeSensedByJason()
    {
        if (getFearLevelPercentage() > .25 && !isInSpectatingMode())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets sense mode and applies effects accordingly
     * @param value
     */
    public void setSenseMode(Boolean value)
    {
        if (value && canBeSensedByJason())
        {
            getPlayer().addPotionEffect(potionSenseByJason);
        }
        else
        {
            getPlayer().removePotionEffect(PotionEffectType.GLOWING);
        }
    }

    /**
     * Restores the players original speed values
     */
    public void restoreOriginalSpeeds()
    {
        if (player.isOnline())
        {
            player.setFlySpeed(originalFlySpeed);
            player.setWalkSpeed(originalWalkSpeed);
            player.setAllowFlight(originalAllowFly);
        }
    }

    /**
     * Sets this counselor as Tommy Jarvis
     */
    public void setTommyJarvis()
    {
        setFearLevel(0.0);
        setStamina(getMaxStamina());
        isTommyJarvis = true;
        getPlayer().getInventory().clear();

        getPlayer().getInventory().addItem(new ItemStack(Material.BOW, 1));
        getPlayer().getInventory().addItem(new ItemStack(Material.SPECTRAL_ARROW, 1));
    }

    /**
     * Teleports player through window
     */
    public void teleportThroughWindow(Block block, boolean damage)
    {
        //Teleport them
        int direction = 1; //front
        float newZ = (float)(block.getLocation().getZ() + (2 * Math.sin(Math.toRadians(player.getLocation().getYaw() + 90 * direction))));
        float newX = (float)(block.getLocation().getX() + (2 * Math.cos(Math.toRadians(player.getLocation().getYaw() + 90 * direction))));
        getPlayer().teleport(new Location(player.getLocation().getWorld(), (double)newX, player.getLocation().getY(), newZ));

        //Damage the player
        if (damage)
        {
            getPlayer().damage(6);
        }
    }

    /**
     * Sets if the counselor is awaiting a window jump
     * @param value
     */
    public void setAwaitingWindowJump(boolean value)
    {
        awaitingWindowJump = value;
    }

    /**
     * Returns if the counselor is awai
     * @return
     */
    public boolean isAwaitingWindowJump()
    {
        return awaitingWindowJump;
    }

}
