package com.AustinPilz.FridayThe13th.Manager.Display;

import com.AustinPilz.FridayThe13th.Components.Jason;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class JasonAbilityDisplayManager
{
    Jason jason;

    //Bars
    BossBar stalkBar;

    public JasonAbilityDisplayManager(Jason jason)
    {
        this.jason = jason;

        //Bars
        stalkBar = Bukkit.createBossBar("Stalking", BarColor.GREEN, BarStyle.SOLID, BarFlag.DARKEN_SKY);
    }

    public void updateLevels()
    {
        //
    }

    public void showAbilities()
    {
        //
    }

    public void hideAbilities()
    {
        //
    }
}
