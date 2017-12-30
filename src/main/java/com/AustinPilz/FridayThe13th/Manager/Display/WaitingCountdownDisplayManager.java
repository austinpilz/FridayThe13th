package com.AustinPilz.FridayThe13th.Manager.Display;


import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class WaitingCountdownDisplayManager
{
    private Arena arena;

    //Visuals
    private BossBar waitingBar;

    public WaitingCountdownDisplayManager(Arena arena)
    {
        this.arena = arena;

        //Visuals
        waitingBar = Bukkit.createBossBar("Friday the 13th: " + FridayThe13th.language.get(Bukkit.getConsoleSender(), "bossBar.waitingTitle", "Time Until Game"), BarColor.RED, BarStyle.SOLID, BarFlag.DARKEN_SKY);
    }

    /**
     * Displays countdown for supplied player
     * @param p
     */
    public void displayForPlayer(Player p)
    {
        waitingBar.addPlayer(p);
    }

    /**
     * Hides countdown from supplied player
     *
     * @param p
     */
    public void hideForPlayer(Player p) { waitingBar.removePlayer(p); }

    /**
     * Hides countdown from all players
     */
    public void hideFromAllPlayers()
    {
        waitingBar.removeAll();
    }

    /**
     * Updates the bar's countdown progress
     */
    public void updateCountdownValue()
    {
        float value = ((((float)arena.getGameManager().getWaitingTimeLeft() - 0) * (1 - 0)) / (arena.getGameManager().getWaitingTimeMax() - 0)) + 0;
        waitingBar.setProgress(value);

        int rem = arena.getGameManager().getWaitingTimeLeft() % 3600;
        int sec = rem % 60;

        if (sec < 5)
        {
            //Play tick/tock sound for all players

        }
    }
}
