package com.AustinPilz.FridayThe13th.Components.Characters;

import com.AustinPilz.CustomSoundManagerAPI.API.PlayerSoundAPI;
import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Perk.F13Perk;
import com.AustinPilz.FridayThe13th.Components.Profiles.JasonProfile;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Display.JasonAbilityDisplayManager;
import com.AustinPilz.FridayThe13th.Manager.Game.SoundManager;
import com.AustinPilz.FridayThe13th.Runnable.JasonAbilitiesDisplayUpdate;
import com.AustinPilz.FridayThe13th.Runnable.JasonAbilitiesRegeneration;
import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class Jason extends F13Character
{

    //Display
    private JasonAbilityDisplayManager abilityDisplayManager;

    //Tasks
    private int taskAbilityDisplay = -1;
    private int taskAbilityRegeneration = -1;

    //Stalk
    private double stalkLevel;
    private double stalkLevelMax;
    private double stalkLevelDepletionRate;
    private double stalkLevelRegenerationRate;
    private boolean stalkInitialGenerationCompleted;
    private PotionEffect stalkPotion;

    //Sense
    private double senseLevel;
    private double senseLevelMax;
    private double senseLevelDepletionRate;
    private double senseLevelRegenerationRate;
    private boolean senseInitialGenerationComplete;
    private boolean senseActive;
    private PotionEffect sensePotion;

    //Warp
    private double warpLevel;
    private double warpLevelMax;
    private double warpLevelDepletionRate;
    private double warpLevelRegenerationRate;
    private boolean warpInitialGenerationComplete;
    private boolean warpActive;

    //Stun Potions
    private PotionEffect stun1;
    private PotionEffect stun2;
    private PotionEffect stun3;
    private PotionEffect stun4;

    public Jason(F13Player player, Arena arena)
    {
        super(player, arena);

        //Display
        abilityDisplayManager = new JasonAbilityDisplayManager(this);

        //Stalk Values
        stalkLevel = 0;
        stalkLevelMax = 30;
        stalkLevelDepletionRate = getF13Player().getJasonProfile().getStalkLevel().getDepletionRate();
        stalkLevelRegenerationRate = getF13Player().getJasonProfile().getStalkLevel().getRegenerationRate();
        stalkInitialGenerationCompleted = false;
        stalkPotion = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1);

        //Sense Values
        senseLevel = 0;
        senseLevelMax = 30;
        senseLevelDepletionRate = getF13Player().getJasonProfile().getSenseLevel().getDepletionRate();
        senseLevelRegenerationRate = getF13Player().getJasonProfile().getSenseLevel().getRegenerationRate();
        senseInitialGenerationComplete = false;
        senseActive = false;
        sensePotion = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1);

        //Warp Values
        warpLevel = 0;
        warpLevelMax = 30;
        warpLevelDepletionRate = getF13Player().getJasonProfile().getWarpLevel().getDepletionRate();
        warpLevelRegenerationRate = getF13Player().getJasonProfile().getWarpLevel().getRegenerationRate();
        warpInitialGenerationComplete = false;
        warpActive = false;

        //Stun Potions
        stun1 = new PotionEffect(PotionEffectType.WEAKNESS, 100, 20);
        stun2 = new PotionEffect(PotionEffectType.CONFUSION, 100, 20);
        stun3 = new PotionEffect(PotionEffectType.GLOWING, 100, 20);
        stun4 = new PotionEffect(PotionEffectType.SLOW, 100, 20);
    }

    /**
     * Returns Jason's ability display manager
     * @return Jason's display manager
     */
    public JasonAbilityDisplayManager getAbilityDisplayManager()
    {
        return abilityDisplayManager;
    }


    /**
     * Schedules all Jason runnable tasks
     */
    private void scheduleTasks()
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
    public void prepareforGameplay()
    {
        teleportToJasonStartingPoint();


        //Give jason his sword
        Material weaponMaterial;

        if (getF13Player().hasPerk(F13Perk.Jason_Part2Pickaxe) && getF13Player().getJasonProfile().equals(JasonProfile.PartTwo))
        {
            weaponMaterial = Material.IRON_PICKAXE;
        }
        else
        {
            weaponMaterial = Material.IRON_AXE;
        }

        ItemStack sword = new ItemStack(weaponMaterial, 1);
        ItemMeta metaData = sword.getItemMeta();
        metaData.setUnbreakable(true);
        metaData.setDisplayName(ChatColor.RED + "Jason's " + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.JasonsWeapon", "Weapon"));
        sword.setItemMeta(metaData);

        //Give him a bow
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta bowMetaData = bow.getItemMeta();
        bowMetaData.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
        bowMetaData.setDisplayName(ChatColor.RED + "Jason's " + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.JasonsBow", "Bow"));
        bow.setItemMeta(bowMetaData);

        //Sense Ability Potion
        ItemStack sensePotion = new ItemStack(Material.POTION, 1);
        PotionMeta sensePotionMeta = (PotionMeta) sensePotion.getItemMeta();
        sensePotionMeta.setBasePotionData(new PotionData(PotionType.NIGHT_VISION));
        sensePotionMeta.setDisplayName(ChatColor.GREEN + "Sense " + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.SenseAbility", "Ability"));
        sensePotion.setItemMeta(sensePotionMeta);

        //Jason's Traps
        ItemStack jasonTraps = new ItemStack(Material.CARPET, 5, (byte) 12);
        ItemMeta jasonTrapsMeta = jasonTraps.getItemMeta();
        jasonTrapsMeta.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.JasonsTraps", "Trap"));
        List<String> jasonTrapLore = new ArrayList<>();
        jasonTrapLore.add(HiddenStringsUtil.encodeString("{\"PlaceItem\": \"JasonTrap\"}"));
        jasonTrapsMeta.setLore(jasonTrapLore);
        jasonTraps.setItemMeta(jasonTrapsMeta);

        //Put them in inventory
        getF13Player().getBukkitPlayer().getInventory().clear(); //clear inventory before hand
        getF13Player().getBukkitPlayer().getInventory().addItem(sword);
        getF13Player().getBukkitPlayer().getInventory().addItem(bow);
        getF13Player().getBukkitPlayer().getInventory().addItem(new ItemStack(Material.ARROW, getF13Player().getJasonProfile().getNumStartingKnives()));
        getF13Player().getBukkitPlayer().getInventory().addItem(sensePotion);
        getF13Player().getBukkitPlayer().getInventory().addItem(jasonTraps);

        //He walks a little slower
        getF13Player().getBukkitPlayer().setWalkSpeed(0.12f);
        getF13Player().getBukkitPlayer().setFlySpeed(0.1f);

        getF13Player().getBukkitPlayer().setAllowFlight(true);

        //Show his abilities
        getAbilityDisplayManager().showAbilities();

        //Display game-wide scoreboard
        getF13Player().getWaitingPlayerStatsDisplayManager().removeStatsScoreboard();
        arena.getGameManager().getGameScoreboardManager().displayForPlayer(getF13Player().getBukkitPlayer());

        //Skin
        skin.apply(getF13Player().getJasonProfile().getSkin());

        //Stop all previous sounds
        PlayerSoundAPI.getPlayerSoundManager(getF13Player().getBukkitPlayer()).stopAllSounds();

        //Schedule tasks
        scheduleTasks();
    }

    /* MOVEMENTS */
    /**
     * Sets if Jason is stalking
     * @param value If Jason is currently stalking
     */
    public void setStalking(boolean value)
    {
        if (value)
        {
            setStalkLevel(Math.max(0, getStalkLevel() - (getStalkLevelMax() * stalkLevelDepletionRate)));
            getF13Player().getBukkitPlayer().addPotionEffect(stalkPotion);

            setWalking(false);
            setSprinting(false);
            setFlying(false);
        }
        else
        {
            //Remove invisibility potion
            getF13Player().getBukkitPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);

        }
    }

    /**
     * Sets if Jason is walking
     * @param value If Jason is currently walking
     */
    public void setWalking(boolean value)
    {
        if (value)
        {
            setStalking(false);
            setSprinting(false);
            setFlying(false);
        }
    }

    /**
     * Sets if Jason is sprinting
     * @param value If Jason is currently sprinting
     */
    public void setSprinting(boolean value)
    {
        if (value)
        {
            setStalking(false);
            setWalking(false);
            setFlying(false);
        }
    }

    /**
     * Sets if Jason is flying
     * @param value If Jason is currently flying
     */
    public void setFlying(boolean value)
    {
        if (value)
        {
            setWarpActive(true);

            setStalking(false);
            setWalking(false);
            setSprinting(false);
        }
        else
        {
            setWarpActive(false);
        }
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
        return getStalkLevel() > 0 && hasInitialStalkGenerationCompleted();
    }

    /**
     * Regenerates Jason's stalking level if he's not sneaking
     */
    public void regenerateStalking()
    {
        setStalkLevel(Math.min(getStalkLevelMax(), getStalkLevel() + (getStalkLevelMax() * stalkLevelRegenerationRate)));
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
            ActionBarAPI.sendActionBar(getF13Player().getBukkitPlayer(), FridayThe13th.language.get(getF13Player().getBukkitPlayer(), "actionBar.jason.stalkAvailable", "Stalk ability is now available"), 60);
        }

        //Play sound effect for all players
        SoundManager.playSoundForAllPlayers(F13SoundEffect.ChiChiChi, arena, false, true, 0);

    }


    /* SENSE */

    /**
     * Gets Jason's sense level
     * @return
     */
    public double getSenseLevel()
    {
        return senseLevel;
    }

    /**
     * Sets Jason's sense level
     */
    private void setSenseLevel(double level)
    {
        senseLevel = level;
    }

    /**
     * Gets Jason's sense level max
     * @return
     */
    private double getSenseLevelMax()
    {
        return senseLevelMax;
    }

    /**
     * Gets Jason's sense level percentage
     * @return
     */
    public double getSenseLevelPercentage()
    {
        return getSenseLevel()/getSenseLevelMax();
    }

    /**
     * Returns if Jason can use sense ability
     * @return
     */
    private boolean canSense()
    {
        return getSenseLevelPercentage() == 1 && hasInitialSenseGenerationCompleted();
    }

    /**
     * Regenerates sense level
     */
    public void regenerateSense()
    {
        setSenseLevel(Math.min(getSenseLevelMax(), getSenseLevel() + (getSenseLevelMax() * senseLevelRegenerationRate)));
    }

    /**
     * Returns if the sense mode is currently in use
     * @return
     */
    public boolean isSenseActive()
    {
        return senseActive;
    }

    /**
     * Sets if the sense mode if currently is use
     * @param value
     */
    public void setSenseActive(boolean value)
    {
        senseActive = value;

        if (value)
        {
            //Apply potion to jason
            getF13Player().getBukkitPlayer().addPotionEffect(sensePotion);

            //Apply potion effect to players
            arena.getGameManager().getPlayerManager().jasonSensing(true);
        }
        else
        {
            //Remove potion from jason
            getF13Player().getBukkitPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);

            //Remove potion effect from players
            arena.getGameManager().getPlayerManager().jasonSensing(false);
        }
    }

    /**
     * Returns if initial sense generation has completed
     * @return
     */
    public boolean hasInitialSenseGenerationCompleted()
    {
        return senseInitialGenerationComplete;
    }

    /**
     * Sets if initial sense generation has completed
     * @param value
     */
    public void setInitialSenseGenerationCompleted(boolean value)
    {
        senseInitialGenerationComplete = value;

        if (value)
        {
            //When it becomes available, send message
            ActionBarAPI.sendActionBar(getF13Player().getBukkitPlayer(), FridayThe13th.language.get(getF13Player().getBukkitPlayer(), "actionBar.jason.senseAvailable", "Sense ability is now available"), 60);
        }

        //Play sound effect for all players
        SoundManager.playSoundForAllPlayers(F13SoundEffect.ChiChiChi, arena, false, true, 0);
    }

    /**
     * Jason requests to activate sense
     */
    public void senseActivationRequest(boolean value)
    {
        if (value && canSense())
        {
            setSenseActive(true);

            //Strike lightning once
            arena.getGameManager().getWeatherManager().senseActivated();
        }
        else
        {
            setSenseActive(false);
        }

    }

    /**
     * Sets if Jason is currently sensing
     * @param value If Jason is currently sensing
     */
    public void setSensing(boolean value)
    {
        if (value)
        {
            setSenseLevel(Math.max(0, getSenseLevel() - (getSenseLevelMax() * senseLevelDepletionRate)));
            setSenseActive(true); //Constantly updates players
        }
    }

    /* WARP */

    public boolean canWarp()
    {
        return getWarpLevelPercentage() > 0 && hasInitialWarpGenerationCompleted();
    }

    private double getWarpLevel()
    {
        return warpLevel;
    }

    private void setWarpLevel(Double level)
    {
        warpLevel = level;
    }

    private double getWarpLevelMax()
    {
        return warpLevelMax;
    }

    public double getWarpLevelPercentage()
    {
        return warpLevel/warpLevelMax;
    }

    public boolean hasInitialWarpGenerationCompleted()
    {
        return warpInitialGenerationComplete;
    }

    public void setInitialWarpGenerationCompleted(boolean value)
    {
        warpInitialGenerationComplete = value;

        if (value)
        {
            //When it becomes available, send message
            ActionBarAPI.sendActionBar(getF13Player().getBukkitPlayer(), FridayThe13th.language.get(getF13Player().getBukkitPlayer(), "actionBar.jason.warpAvailable", "Warp ability is now available"), 60);
        }

        //Play sound effect for all players
        SoundManager.playSoundForAllPlayers(F13SoundEffect.ChiChiChi, arena, false, true, 0);
    }

    public void regenerateWarp()
    {
        setWarpLevel(Math.min(getWarpLevelMax(), getWarpLevel() + (getWarpLevelMax() * warpLevelRegenerationRate)));
    }

    public boolean isWarpActive()
    {
        return warpActive;
    }

    public void setWarpActive(boolean value)
    {
        warpActive = value;

        if (!value)
        {
            getF13Player().getBukkitPlayer().setFlying(false);
        }
    }

    public void setWarping(boolean value)
    {
        if (value)
        {
            setWarpLevel(Math.max(0, getWarpLevel() - (getWarpLevelMax() * warpLevelDepletionRate)));
        }
    }


    /**
     * Removes Jason's potion effects
     */
    public void removePotionEffects()
    {
        getF13Player().getBukkitPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        getF13Player().getBukkitPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
        getF13Player().getBukkitPlayer().removePotionEffect(PotionEffectType.SLOW);
    }

    /**
     * Removes Jason skin
     */
    public void removeSkin()
    {
        skin.revert();
    }

    /**
     * Stuns Jason
     */
    public void stun() {
        getF13Player().getBukkitPlayer().addPotionEffect(stun1);
        getF13Player().getBukkitPlayer().addPotionEffect(stun2);
        getF13Player().getBukkitPlayer().addPotionEffect(stun3);
        getF13Player().getBukkitPlayer().addPotionEffect(stun4);
    }
}
