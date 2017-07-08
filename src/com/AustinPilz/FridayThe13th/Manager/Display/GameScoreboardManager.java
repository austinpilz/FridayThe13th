package com.AustinPilz.FridayThe13th.Manager.Display;

import com.AustinPilz.FridayThe13th.Components.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class GameScoreboardManager
{
    private Arena arena;
    private Scoreboard gameScoreboard;
    private Scoreboard dummyScoreboard;
    private Objective obj;

    public GameScoreboardManager(Arena a)
    {
        arena = a;
        gameScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        dummyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = gameScoreboard.registerNewObjective("scoreboard", "dummy");
        obj.setDisplayName(ChatColor.RED + "--Friday the 13th--");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Updates the scoreboard values
     */
    public void updateScoreboard()
    {
        Score areaTitle = obj.getScore(ChatColor.RED + "Arena");
        areaTitle.setScore(15);

        Score arenaName = obj.getScore(arena.getArenaName());
        arenaName.setScore(14);
    }

    public void displayForPlayer(Player p)
    {
        p.setScoreboard(gameScoreboard);
    }

    public void hideFromPlayer(Player p)
    {
        p.setScoreboard(dummyScoreboard);
    }


}
