package com.AustinPilz.FridayThe13th.Manager.Display;

import com.AustinPilz.FridayThe13th.Components.Counselor;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.CounselorStatsDisplayUpdate;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class CounselorStatsDisplayManager
{
    private Counselor counselor;
    private BossBar staminaBar;
    private BossBar fearBar;
    private int updaterTask = -1;

    public CounselorStatsDisplayManager(Counselor counselor)
    {
        this.counselor = counselor;
        staminaBar = Bukkit.createBossBar("Stamina", BarColor.GREEN, BarStyle.SOLID, BarFlag.DARKEN_SKY);
        fearBar = Bukkit.createBossBar("Fear", BarColor.RED, BarStyle.SOLID, BarFlag.CREATE_FOG);
        updateStats();
    }

    /**
     * Displays counselor stats
     */
    public void displayStats()
    {
        staminaBar.addPlayer(counselor.getPlayer());
        fearBar.addPlayer(counselor.getPlayer());
    }

    /**
     * Hides the stats from the counselor
     */
    public void hideStats()
    {
        staminaBar.removeAll();
        fearBar.removeAll();
    }

    public void updateStats()
    {
        fearBar.setProgress(convertToBossBarValue(counselor.getFearLevel(), counselor.getMaxFearLevel()));
        staminaBar.setProgress(convertToBossBarValue(counselor.getStamina(), counselor.getMaxStamina()));
    }

    /**
     * Converts value and max into boss bar acceptable value
     * @param current
     * @param max
     * @return
     */
    private double convertToBossBarValue(double current, double max)
    {
        return (((current - 0) * (1 - 0)) / (max - 0)) + 0;
    }

    public void startUpdaterTask()
    {
        if (updaterTask != -1)
        {
            //Cancel existing one to prevent memory leaks
            endUpdaterTask();
        }

        updaterTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FridayThe13th.instance, new CounselorStatsDisplayUpdate(counselor), 0, 20);

    }

    /**
     * Cancels updater task
     */
    public void endUpdaterTask()
    {
        Bukkit.getScheduler().cancelTask(updaterTask);
    }
}
