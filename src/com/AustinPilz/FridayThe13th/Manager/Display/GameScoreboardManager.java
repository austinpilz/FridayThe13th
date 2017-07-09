package com.AustinPilz.FridayThe13th.Manager.Display;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class GameScoreboardManager
{
    private Arena arena;
    private Sidebar gameScoreboard;

    private SidebarString timeLeftValue;
    private SidebarString jasonValue;
    private SidebarString space;

    public GameScoreboardManager(Arena a)
    {
        arena = a;
        gameScoreboard = new Sidebar(ChatColor.RED + "" + ChatColor.BOLD + "Friday the 13th", FridayThe13th.instance, 60);
        timeLeftValue = new SidebarString("");
        jasonValue = new SidebarString("");
    }

    /**
     * Updates the scoreboard values
     */
    public void updateScoreboard()
    {
        List<SidebarString> newList = new ArrayList<>(gameScoreboard.getEntries());
        for (SidebarString string : newList)
        {
            gameScoreboard.removeEntry(string);
        }

        SidebarString arenaTitle = new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sidebar.Arena", "Arena"));
        gameScoreboard.addEntry(arenaTitle);

        SidebarString arenaName = new SidebarString(arena.getArenaName());
        gameScoreboard.addEntry(arenaName);

        gameScoreboard.addEntry(new SidebarString(" "));

        //Time Left
        SidebarString timeLeftTitle = new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sidebar.TimeLeft", "Time Left"));
        gameScoreboard.addEntry(timeLeftTitle);

        int rem = arena.getGameManager().getGameTimeLeft() % 3600;
        int mn = rem / 60;
        int sec = rem % 60;

        timeLeftValue = new SidebarString(mn + "m " + sec + "s");
        gameScoreboard.addEntry(timeLeftValue);

        gameScoreboard.addEntry(new SidebarString("  "));

        SidebarString jasonTitle = new SidebarString(ChatColor.GOLD + "Jason");
        gameScoreboard.addEntry(jasonTitle);

        jasonValue = new SidebarString(arena.getGameManager().getPlayerManager().getJason().getPlayer().getName());
        gameScoreboard.addEntry(jasonValue);

        //Space
        gameScoreboard.addEntry(new SidebarString("   "));

        //ALIVE
        gameScoreboard.addEntry(new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sidebar.AliveDead", "Alive / Dead")));
        gameScoreboard.addEntry(new SidebarString(ChatColor.GREEN + "" + arena.getGameManager().getPlayerManager().getNumPlayersAlive() + ChatColor.WHITE + " / " + ChatColor.RED + "" + arena.getGameManager().getPlayerManager().getNumPlayersDead()));

        gameScoreboard.update();
    }

    public void displayForPlayer(Player p)
    {
        gameScoreboard.showTo(p);
    }

    public void hideFromPlayer(Player p)
    {
        gameScoreboard.hideFrom(p);
    }


}