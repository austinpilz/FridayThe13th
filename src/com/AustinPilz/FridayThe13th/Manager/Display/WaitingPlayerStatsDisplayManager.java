package com.AustinPilz.FridayThe13th.Manager.Display;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class WaitingPlayerStatsDisplayManager {
    private F13Player player;
    private Sidebar statsScoreboard;

    public WaitingPlayerStatsDisplayManager(F13Player p) {
        player = p;
        statsScoreboard = new Sidebar(ChatColor.RED + "" + ChatColor.BOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.scoreboard.title", "Friday The 13th"), FridayThe13th.instance, 20);
    }

    /**
     * Updates stats scoreboard
     */
    public void updateStatsScoreboard() {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(player.getPlayerUUID());

            List<SidebarString> newList = new ArrayList<>(statsScoreboard.getEntries());
            for (SidebarString string : newList) {
                statsScoreboard.removeEntry(string);
            }

            //Game
            SidebarString arenaTitle = new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "waiting.sidebar.Game", "Game"));
            statsScoreboard.addEntry(arenaTitle);

            SidebarString arenaName = new SidebarString(arena.getArenaName());
            statsScoreboard.addEntry(arenaName);

            statsScoreboard.addEntry(new SidebarString("   "));

            SidebarString waitingPlayersTitle = new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "waiting.sidebar.WaitingPlayers", "Waiting Players"));
            statsScoreboard.addEntry(waitingPlayersTitle);

            SidebarString waitingPlayersValue = new SidebarString(arena.getGameManager().getPlayerManager().getNumPlayers() + " ");
            statsScoreboard.addEntry(waitingPlayersValue);

            statsScoreboard.addEntry(new SidebarString("    "));

            SidebarString playerLevelTitle = new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "waiting.sidebar.PlayerLevelTitle", "Your Level"));
            statsScoreboard.addEntry(playerLevelTitle);

            SidebarString playerLevelValue = new SidebarString(player.getLevel().getLevelNumber() + "  ");
            statsScoreboard.addEntry(playerLevelValue);

            statsScoreboard.addEntry(new SidebarString("     "));

            SidebarString xpTitle = new SidebarString(ChatColor.GOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "waiting.sidebar.XPTitle", "Your XP"));
            statsScoreboard.addEntry(xpTitle);

            SidebarString xpValue = new SidebarString(Integer.toString(player.getXP()) + "   ");
            statsScoreboard.addEntry(xpValue);

            statsScoreboard.update();
        } catch (PlayerNotPlayingException exception) {
            //They're not playing, so don't update
        }
    }

    /**
     * Displays stats scoreboard
     */
    public void displayStatsScoreboard() {
        statsScoreboard.showTo(player.getPlayer());
    }

    /**
     * Hides stats scoreboard
     */
    public void removeStatsScoreboard() {
        statsScoreboard.hideFrom(player.getPlayer());
    }
}
