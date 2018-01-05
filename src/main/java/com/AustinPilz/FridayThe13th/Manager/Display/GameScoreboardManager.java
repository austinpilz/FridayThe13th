package com.AustinPilz.FridayThe13th.Manager.Display;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameScoreboardManager
{
    private Arena arena;
    private Sidebar gameScoreboard;

    private SidebarString timeLeftValue;
    private SidebarString jasonValue;

    public GameScoreboardManager(Arena a)
    {
        arena = a;
        gameScoreboard = new Sidebar(ChatColor.RED + "" + ChatColor.BOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.scoreboard.title", "Friday The 13th"), FridayThe13th.instance, 60);
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

        SidebarString arenaTitle = new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sidebar.Game", "Game"));
        gameScoreboard.addEntry(arenaTitle);

        SidebarString arenaName = new SidebarString(arena.getName());
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
        int alive = arena.getGameManager().getPlayerManager().getNumPlayersAlive()-1;
        gameScoreboard.addEntry(new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sidebar.AliveDead", "Alive / Dead")));
        gameScoreboard.addEntry(new SidebarString(ChatColor.GREEN + "" + alive + ChatColor.WHITE + " / " + ChatColor.RED + "" + arena.getGameManager().getPlayerManager().getNumPlayersDead()));

        if (arena.getGameManager().havePoliceBeenCalled()) {
            gameScoreboard.addEntry(new SidebarString("    "));
            gameScoreboard.addEntry(new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sidebar.Police", "Police")));

            if (arena.getGameManager().havePoliceArrived()) {
                gameScoreboard.addEntry(new SidebarString(ChatColor.GREEN + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sidebar.PoliceArrived", "Arrived")));
            } else {
                int rem2 = arena.getGameManager().getTimeUntilPoliceArrive() % 3600;
                int mn2 = rem2 / 60;
                int sec2 = rem2 % 60;

                gameScoreboard.addEntry(new SidebarString(mn2 + "m " + sec2 + "s"));
            }

        } else if (arena.getGameManager().getPlayerManager().getNumSpectators() > 0)
        {
            gameScoreboard.addEntry(new SidebarString("    "));
            gameScoreboard.addEntry(new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sidebar.Spectators", "Spectators")));
            gameScoreboard.addEntry(new SidebarString(ChatColor.WHITE + "" + arena.getGameManager().getPlayerManager().getNumSpectators()));
        }

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
