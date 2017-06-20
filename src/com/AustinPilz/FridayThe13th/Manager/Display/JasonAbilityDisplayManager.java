package com.AustinPilz.FridayThe13th.Manager.Display;

import com.AustinPilz.FridayThe13th.Components.Jason;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class JasonAbilityDisplayManager
{
    Jason jason;

    //Bars
    BossBar stalkBar;
    BossBar senseBar;
    BossBar warpBar;

    public JasonAbilityDisplayManager(Jason jason)
    {
        this.jason = jason;

        //Bars
        stalkBar = Bukkit.createBossBar(ChatColor.GRAY + "Stalk", BarColor.GREEN, BarStyle.SOLID, BarFlag.DARKEN_SKY);
        senseBar = Bukkit.createBossBar(ChatColor.GRAY + "Sense", BarColor.RED, BarStyle.SOLID, BarFlag.DARKEN_SKY);
        warpBar = Bukkit.createBossBar(ChatColor.GRAY + "Warp", BarColor.PURPLE, BarStyle.SOLID, BarFlag.DARKEN_SKY);
    }

    public void updateLevels()
    {
        //Stalk
        stalkBar.setProgress(jason.getStalkLevelPercentage());
        if (jason.hasInitialStalkGenerationCompleted())
        {
            stalkBar.setTitle(ChatColor.WHITE + "Stalk");

            //Sense is dependent on stalk being done first
            senseBar.addPlayer(jason.getPlayer());
            senseBar.setProgress(jason.getSenseLevelPercentage());
        }

        //Sense
        if (jason.hasInitialSenseGenerationCompleted())
        {
            senseBar.setTitle(ChatColor.WHITE + "Sense");

            //Warp bar is dependent on sense bar being done first
            warpBar.addPlayer(jason.getPlayer());
            warpBar.setProgress(jason.getWarpLevelPercentage());
        }

        //Warp
        if (jason.hasInitialWarpGenerationCompleted())
        {
            warpBar.setTitle(ChatColor.WHITE + "Warp");
        }
    }

    public void showAbilities()
    {
        stalkBar.addPlayer(jason.getPlayer());
    }

    public void hideAbilities()
    {
        stalkBar.removeAll();
        senseBar.removeAll();
        warpBar.removeAll();
    }
}
