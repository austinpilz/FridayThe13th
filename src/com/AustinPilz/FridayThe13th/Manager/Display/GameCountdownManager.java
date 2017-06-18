package com.AustinPilz.FridayThe13th.Manager.Display;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;

public class GameCountdownManager
{
    private Arena arena;
    private BossBar countdownBar;

    public GameCountdownManager (Arena arena)
    {
        this.arena = arena;
        countdownBar = Bukkit.createBossBar("Game Time", BarColor.WHITE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
    }

    public void updateCountdown()
    {
        if (arena.getGameManager().getGameTimeLeft() == 0)
        {
            arena.getGameManager().gameTimeUp(); //game ran out of time
        }


        int rem = arena.getGameManager().getGameTimeLeft()%3600;
        int mn = rem/60;
        int sec = rem%60;

        //MATH
        float value = ((((float)arena.getGameManager().getGameTimeLeft() - 0) * (1 - 0)) / (arena.getGameManager().getGameTimeMax() - 0)) + 0;
        countdownBar.setProgress(value);
        countdownBar.setTitle("Game Time - " + mn + ":" + sec);



    }

    public void displayCountdown()
    {
        Iterator it = arena.getGameManager().getPlayerManager().getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player player = (Player) entry.getValue();

            //ActionBarAPI.sendActionBar(player, minutes+":"+seconds, 20);
            //player.sendMessage(FridayThe13th.pluginPrefix + minutes+":"+seconds + " left in game.");
            countdownBar.addPlayer(player);
        }
    }

    public void hideCountdown()
    {
        Iterator it = arena.getGameManager().getPlayerManager().getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player player = (Player) entry.getValue();

            countdownBar.removeAll();
        }
    }
}
