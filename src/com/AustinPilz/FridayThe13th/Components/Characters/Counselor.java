package com.AustinPilz.FridayThe13th.Components.Characters;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Enum.GameSkin;
import com.AustinPilz.FridayThe13th.Components.SkinChange;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Display.CounselorStatsDisplayManager;
import com.AustinPilz.FridayThe13th.Manager.Statistics.CounselorXPManager;
import com.AustinPilz.FridayThe13th.Runnable.CounselorStatsUpdate;
import com.AustinPilz.FridayThe13th.Structures.LightLevelList;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    private boolean moving = false;

    //Fear
    private double fearLevel;
    private double maxFearLevel;
    private LightLevelList lightHistory;

    //Managers
    private CounselorStatsDisplayManager statsDisplayManager;
    private CounselorXPManager counselorXPManager;
    private SkinChange skin;

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
        counselorXPManager = new CounselorXPManager(this, arena);

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
        skin = new SkinChange(getPlayer());
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
     * Returns counselor's XP manager
     *
     * @return
     */
    public CounselorXPManager getXPManager() {
        return counselorXPManager;
    }

    /**
     * Performs all necessary tasks when the game begins
     */
    public void prepareForGameplay()
    {
        //Clear their inventory of any waiting room goodies
        getPlayer().getInventory().clear();

        ///Display Status
        getStatsDisplayManager().displayStats();

        //Display game-wide scoreboard
        arena.getGameManager().getGameScoreboardManager().displayForPlayer(getPlayer());

        //Start All Counselor Tasks
        scheduleTasks();

        //Skin
        skin.apply(GameSkin.COUNSELOR_BASE);
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
     * Transitions the counselor to spectating mode
     */
    public void transitionToSpectatingMode()
    {
        //Remove any potions from in game
        removePotionEffects();

        //Set counselor stats to full to avoid messages
        setFearLevel(0.0);
        setStamina(getMaxStamina());

        //Stop stats since they're dead
        getStatsDisplayManager().hideStats();
        cancelTasks();
    }

    /**
     * Removes potion effects from counselor
     */
    public void removePotionEffects()
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
        return (getFearLevelPercentage() > .25 || isTommyJarvis) && !arena.getGameManager().getPlayerManager().isSpectator(getPlayer().getUniqueId().toString());
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

        //Give them radio
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.Radio", "Radio"));
        item.setItemMeta(metaData);
        getPlayer().getInventory().addItem(item);
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

    /**
     * Updates skin based on health level
     */
    public void updateSkin() {
        double percentage = getHealthPercentage();

        if (percentage == 1) {
            //Full Health
            skin.apply(GameSkin.COUNSELOR_BASE);
        } else if (percentage < 1 && percentage > 0.5) {
            //Minor Damage
            skin.apply(GameSkin.COUSENLOR_BLOOD_1);
        } else if (percentage <= 0.5 && percentage > 0.3) {
            //Some damage
            skin.apply(GameSkin.COUSENLOR_BLOOD_2);
        } else {
            //Major damage
            skin.apply(GameSkin.COUSENLOR_BLOOD_3);
        }
    }


    /**
     * Reverts counselor skin back to the players default skin
     */
    public void removeSkin() {
        skin.revert();
    }

    /**
     * Returns player health percentage
     *
     * @return
     */
    private double getHealthPercentage() {
        return getPlayer().getHealth() / getPlayer().getMaxHealth();
    }


    /**
     * Awards the counselor their XP
     */
    public void awardXP() {
        int gameXP = getXPManager().calculateXP();
        int currentXP = FridayThe13th.playerController.getPlayer(getPlayer()).getXP();
        int newXP = Math.max(currentXP, currentXP + gameXP);

        getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(getPlayer(), "message.gameEarnedXP", "You earned {0} xp from this round and now have a total of {1} xp.", ChatColor.GREEN + "" + gameXP + ChatColor.WHITE, ChatColor.GREEN + "" + ChatColor.BOLD + "" + newXP + ChatColor.RESET));

        FridayThe13th.playerController.getPlayer(getPlayer()).addXP(Math.max(0, gameXP));
    }
}
