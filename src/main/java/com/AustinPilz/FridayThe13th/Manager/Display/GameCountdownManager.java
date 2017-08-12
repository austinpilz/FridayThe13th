package com.AustinPilz.FridayThe13th.Manager.Display;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Game.SoundManager;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;

public class GameCountdownManager
{
    private Arena arena;
    private BossBar gameCountdownBar;

    public GameCountdownManager (Arena arena)
    {
        this.arena = arena;
        gameCountdownBar = Bukkit.createBossBar(FridayThe13th.language.get(Bukkit.getConsoleSender(), "bossBar.counselor.timeLeft", "Time Left"), BarColor.WHITE, BarStyle.SOLID, BarFlag.CREATE_FOG);
        gameCountdownBar.setProgress(1);
    }

    public void updateCountdown()
    {
        Iterator counselorIterator = arena.getGameManager().getPlayerManager().getCounselors().entrySet().iterator();
        while (counselorIterator.hasNext())
        {
            Map.Entry entry = (Map.Entry) counselorIterator.next();
            Counselor counselor = (Counselor) entry.getValue();
        }



        if (arena.getGameManager().getGameTimeLeft() == 0)
        {
            arena.getGameManager().gameTimeUp(); //game ran out of time
        }
        else
        {
            //Update the bar
            float percentage = ((float) arena.getGameManager().getGameTimeLeft()) / arena.getGameManager().getGameTimeMax();
            gameCountdownBar.setProgress(percentage);

            int rem = arena.getGameManager().getGameTimeLeft() % 3600;
            int mn = rem / 60;
            int sec = rem % 60;

            if (mn > 1 && sec == 0)
            {
                //Every whole minute
                Iterator it = arena.getGameManager().getPlayerManager().getPlayers().entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    Player player = (Player) entry.getValue();
                    ActionBarAPI.sendActionBar(player, FridayThe13th.language.get(player, "actionBar.counselor.timeLeft", "Time Left") + ": " + mn + " " + FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionBar.counselor.minutes", "minutes"), 60);
                }

                //Spawn tommy
                if (arena.getGameManager().hasTommyBeenCalled() && !arena.getGameManager().hasTommyBeenSpawned())
                {
                    arena.getGameManager().getPlayerManager().spawnTommyJarvis();
                }

                //Play 2 minute left music
                if (mn == 2)
                {
                    SoundManager.playSoundForAllPlayers(F13SoundEffect.Music_2Minute, arena, true, false, 2);
                }
            }
            else if (mn == 0 && (sec == 30 || sec == 20 || sec == 10 || sec == 5))
            {
                //Special
                Iterator it = arena.getGameManager().getPlayerManager().getPlayers().entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    Player player = (Player) entry.getValue();
                    ActionBarAPI.sendActionBar(player, FridayThe13th.language.get(player, "actionBar.counselor.timeLeft", "Time Left") + ": " + sec + " " +  FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionBar.counselor.seconds", "seconds"), 60);
                }
            }
        }
    }

    /**
     * Shows the time left bar for the specified player
     * @param p
     */
    public void showForPlayer(Player p) { gameCountdownBar.addPlayer(p); }

    /**
     * Removes the time left bar from the specified player
     * @param p
     */
    public void hideFromPlayer(Player p) { gameCountdownBar.removePlayer(p); }

    /**
     * Hides the time left bar from everyone
     */
    public void hideCountdownBar()
    {
        gameCountdownBar.removeAll();
    }
}
