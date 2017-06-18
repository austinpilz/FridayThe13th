package com.AustinPilz.FridayThe13th.Components;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Display.JasonAbilityDisplayManager;
import com.AustinPilz.FridayThe13th.Runnable.CounselorStatsUpdate;
import com.AustinPilz.FridayThe13th.Runnable.JasonAbilitiesDisplayUpdate;
import com.AustinPilz.FridayThe13th.Runnable.JasonAbilitiesRegeneration;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Jason
{
    //Minecraft Objects
    private Player player;

    //Game
    private Arena arena;

    //Display
    private JasonAbilityDisplayManager abilityDisplayManager;

    //Tasks
    int taskAbilityDisplay = -1;
    int taskAbilityRegeneration = -1;

    //Stalk
    private double stalkLevel;
    private double stalkLevelMax;
    private double stalkLevelDepletionRate;
    private double stalkLevelRegenerationRate;
    private boolean stalkInitialGenerationCompleted;
    private PotionEffect stalkPotion;

    //Sense

    //Warp



    public Jason(Player p, Arena a)
    {
        arena = a;
        player = p;

        //Display
        abilityDisplayManager = new JasonAbilityDisplayManager(this);

        //Stalk Values
        stalkLevel = 0;
        stalkLevelMax = 30;
        stalkLevelDepletionRate = 0.01;
        stalkLevelRegenerationRate = 0.06;
        stalkInitialGenerationCompleted = false;
        stalkPotion = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1);
    }

    /**
     * Return the player object of the counselor
     */
    public Player getPlayer()
    {
        return this.player;
    }

    /**
     * Returns Jason's ability display manager
     * @return
     */
    public JasonAbilityDisplayManager getAbilityDisplayManager()
    {
        return abilityDisplayManager;
    }

    /**
     * Schedules all Jason runnable tasks
     */
    public void scheduleTasks()
    {
        taskAbilityDisplay = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new JasonAbilitiesDisplayUpdate(this), 0, 20);
        taskAbilityRegeneration = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new JasonAbilitiesRegeneration(this), 0, 20);
    }

    /**
     * Cancels all Jason runnable tasks
     */
    public void cancelTasks()
    {
        Bukkit.getScheduler().cancelTask(taskAbilityDisplay);
        Bukkit.getScheduler().cancelTask(taskAbilityRegeneration);
    }


    /**
     * Performs all necessary tasks when the game begins
     */
    public void prepapreForGameplay()
    {
        //Give jason his sword
        ItemStack sword = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta metaData = sword.getItemMeta();
        metaData.setUnbreakable(true);
        metaData.setDisplayName(ChatColor.RED + "Jason's Axe");
        sword.setItemMeta(metaData);

        //Give him a bow
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta bowMetaData = sword.getItemMeta();
        bowMetaData.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
        bowMetaData.setDisplayName(ChatColor.RED + "Jason's Bow");
        bow.setItemMeta(bowMetaData);

        getPlayer().getInventory().addItem(sword);
        getPlayer().getInventory().addItem(bow);
        getPlayer().getInventory().addItem(new ItemStack(Material.ARROW, 2));

        //He walks a little slower
        getPlayer().setWalkSpeed(0.12f);

        //Show his abilities
        getAbilityDisplayManager().showAbilities();

        //Schedule tasks
        scheduleTasks();
    }

    /* STALKING */

    /**
     * Returns Jason's current stalk level
     * @return
     */
    private double getStalkLevel()
    {
        return stalkLevel;
    }

    /**
     * Returns Jason's max stalk level
     * @return
     */
    private double getStalkLevelMax()
    {
        return stalkLevelMax;
    }

    /**
     * Returns percentage of Jason's stalk level
     * @return
     */
    public double getStalkLevelPercentage()
    {
        return getStalkLevel()/getStalkLevelMax();
    }

    /**
     * Sets Jason's stalk level
     * @param level
     */
    private void setStalkLevel(double level)
    {
        stalkLevel = level;
    }

    /**
     * Returns if Jason is able to stalk
     * @return
     */
    public boolean canStalk()
    {
        if (getStalkLevel() > 0 && hasInitialStalkGenerationCompleted())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets if Jason is able to stalk
     * @param value
     */
    public void setStalking(boolean value)
    {
        if (value)
        {
            setStalkLevel(Math.max(0, getStalkLevel() - (getStalkLevelMax() * stalkLevelDepletionRate)));
            getPlayer().addPotionEffect(stalkPotion);

            setWalking(false);
            setSprinting(false);
            setFlying(false);
        }
        else
        {
            //Remove invisibility potion
            getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);

        }
    }

    public void setWalking(boolean value)
    {
        if (value)
        {
            setStalking(false);
            setSprinting(false);
            setFlying(false);
        }
        else
        {

        }
    }

    public void setSprinting(boolean value)
    {
        if (value)
        {
            setStalking(false);
            setWalking(false);
            setFlying(false);
        }
        else
        {

        }
    }

    public void setFlying(boolean value)
    {
        if (value)
        {
            setStalking(false);
            setWalking(false);
            setSprinting(false);
        }
        else
        {

        }
    }

    /**
     * Regenerates Jason's stalking level if he's not sneaking
     */
    public void regenerateStalking()
    {
        if (!getPlayer().isSneaking())
        {
            //Regen
            setStalkLevel(Math.min(getStalkLevelMax(), getStalkLevel() + (getStalkLevelMax() * stalkLevelRegenerationRate)));
        }
    }

    /**
     * Returns if the initial stalk generation has completed
     * @return
     */
    public boolean hasInitialStalkGenerationCompleted()
    {
        return stalkInitialGenerationCompleted;
    }

    /**
     * Sets if the initial stalk generation has completed
     * @param value
     */
    public void setInitialStalkGenerationCompleted(boolean value)
    {
       stalkInitialGenerationCompleted = value;

       if (value)
       {
           //When it becomes available, send message
           ActionBarAPI.sendActionBar(getPlayer(), "Stalk ability is now available.", 60);
       }
    }



}
