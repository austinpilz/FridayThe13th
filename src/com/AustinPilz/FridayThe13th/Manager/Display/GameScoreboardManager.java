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

    private String prevTimeSlot = "";

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
        Score emptySpace1 = obj.getScore("   ");
        emptySpace1.setScore(15);

        Score areaTitle = obj.getScore(ChatColor.RED + "Arena");
        areaTitle.setScore(14);

        Score arenaName = obj.getScore(arena.getArenaName());
        arenaName.setScore(13);

        Score emptySpace2 = obj.getScore("   ");
        emptySpace2.setScore(12);

        Score timeLeftTitle = obj.getScore(ChatColor.RED + "Time Left");
        timeLeftTitle.setScore(11);

        int rem = arena.getGameManager().getGameTimeLeft() % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        Score timeLeftValue = obj.getScore(mn + "m " + sec + "s");
        timeLeftValue.setScore(10);

        Score emptySpace3 = obj.getScore("   ");
        emptySpace3.setScore(9);

        Score aliveTitle = obj.getScore(ChatColor.RED + "Alive");
        aliveTitle.setScore(8);

        Score aliveValue = obj.getScore(arena.getGameManager().getPlayerManager().getNumPlayersAlive()+"");
        aliveValue.setScore(7);

        Score emptySpace4 = obj.getScore("   ");
        emptySpace4.setScore(6);

        Score deadTitle = obj.getScore(ChatColor.RED + "Dead");
        deadTitle.setScore(5);

        Score deadValue = obj.getScore(arena.getGameManager().getPlayerManager().getNumPlayersDead()+"");
        deadValue.setScore(4);

        Score emptySpace5 = obj.getScore("   ");
        emptySpace5.setScore(3);

        Score jasonTitle = obj.getScore(ChatColor.RED + "Jason");
        jasonTitle.setScore(2);

        Score jasonValue = obj.getScore(arena.getGameManager().getPlayerManager().getJason().getPlayer().getName()+"");
        jasonValue.setScore(1);
    }

    public void displayForPlayer(Player p)
    {
        //p.setScoreboard(gameScoreboard);
    }

    public void hideFromPlayer(Player p)
    {
        p.setScoreboard(dummyScoreboard);
    }


}
