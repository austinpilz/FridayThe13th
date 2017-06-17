package com.AustinPilz.FridayThe13th.Manager.Display;

import com.AustinPilz.FridayThe13th.Components.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.UUID;

public class GameScoreboardManager
{
    private Arena arena;

    private BoardManager playerSidebarBoard;
    private BoardManager blankBoard;


    public GameScoreboardManager(Arena arena)
    {
        this.arena = arena;
        this.blankBoard = new BoardManager("", "", DisplaySlot.SIDEBAR);
    }

    private void updatePlayerSidebarBoard()
    {
        playerSidebarBoard = new BoardManager("PlayerSidebar", ChatColor.RED + "-- Friday The 13th --", DisplaySlot.SIDEBAR);

        //Alive players
        for (String playerUUID: arena.getPlayerManager().getAlivePlayers())
        {
            Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
            if (arena.getPlayerManager().isJason(player))
            {
                playerSidebarBoard.setObjectiveScore(player.getName() + ChatColor.GOLD + " J " + ChatColor.GREEN + " ALIVE", 1);
            }
            else
            {
                playerSidebarBoard.setObjectiveScore(player.getName() + ChatColor.GREEN + " ALIVE", 0);
            }

            playerSidebarBoard.setScoreboard(player);
        }

        //Now the dead ones
        for (String playerUUID: arena.getPlayerManager().getDeadPlayers())
        {
            Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
            if (arena.getPlayerManager().isJason(player))
            {
                playerSidebarBoard.setObjectiveScore(player.getName() + ChatColor.GOLD + " J " + ChatColor.RED + " DEAD", 1);
            }
            else
            {
                playerSidebarBoard.setObjectiveScore(player.getName() + ChatColor.RED + " DEAD", 0);
            }

            playerSidebarBoard.setScoreboard(player);
        }
    }

    public void updateStats()
    {
        //Make sure all players can see it
        updatePlayerSidebarBoard();
    }

    /**
     * Hides all F13 scoreboards from player
     * @param player
     */
    public void hideSidebars(Player player)
    {
        blankBoard.setScoreboard(player);
    }

}
