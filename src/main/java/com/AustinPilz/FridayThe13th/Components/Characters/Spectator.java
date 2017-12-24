package com.AustinPilz.FridayThe13th.Components.Characters;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Menu.SpectateMenu;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;

public class Spectator extends F13Character {
    public Spectator(Player player, Arena arena)
    {
        super(player, arena);
    }

    /**
     * Puts the player into spectate mode
     */
    public void enterSpectatingMode()
    {
        //Show countdown bar & scoreboard
        arena.getGameManager().getGameCountdownManager().showForPlayer(getPlayer());
        arena.getGameManager().getGameScoreboardManager().displayForPlayer(getPlayer());

        //Enter flight
        getPlayer().setGameMode(GameMode.SURVIVAL);
        getPlayer().setAllowFlight(true);
        getPlayer().setFlying(true);
        getPlayer().setHealth(20);
        getPlayer().setWalkSpeed(0.2f);

        //Location
        getPlayer().teleport(arena.getLocationManager().getAvailableStartingPoints().iterator().next()); //Random starting point
        getPlayer().getInventory().clear();

        //Give them the selector
        SpectateMenu.addMenuOpenItem(getPlayer());

        //Let them know
        ActionBarAPI.sendActionBar(getPlayer(), ChatColor.RED + FridayThe13th.language.get(player, "actionBar.counselor.becomeSpectator", "You are now in spectating mode.", ChatColor.WHITE), 300);

        //Hide this player from everyone else
        for (Object o : arena.getGameManager().getPlayerManager().getPlayers().entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Player hideFrom = (Player) entry.getValue();
            hideFrom.hidePlayer(getPlayer());
        }

        //Hide existing spectators from this person
        Iterator spectatorIterator = arena.getGameManager().getPlayerManager().getSpectators().entrySet().iterator();
        while (spectatorIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) spectatorIterator.next();
            Spectator toHide = (Spectator) entry.getValue();
            if (!getPlayer().equals(toHide)) {
                getPlayer().hidePlayer(toHide.getPlayer());
            }
        }

    }

    /**
     * Removes the player from spectating mode
     */
    public void leaveSpectatingMode()
    {
        //Restore their defaults
        getPlayer().setAllowFlight(false);
        getPlayer().getInventory().clear();
        restoreOriginalSpeeds();

        //Clear the action bar and hide spectator displays
        ActionBarAPI.sendActionBar(getPlayer(), "");
        arena.getGameManager().getGameCountdownManager().hideFromPlayer(getPlayer());
        arena.getGameManager().getGameScoreboardManager().hideFromPlayer(getPlayer());

        //Make them visible to everyone again
        if (getPlayer().isOnline())
        {
            //Make visible to all players
            for (Player player : Bukkit.getOnlinePlayers())
            {
                player.showPlayer(getPlayer());
            }
        }
    }
}
