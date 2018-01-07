package com.AustinPilz.FridayThe13th.Components.Characters;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Menu.SpectateMenu;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;

public class Spectator extends F13Character {
    public Spectator(F13Player player, Arena arena)
    {
        super(player, arena);
    }

    /**
     * Puts the player into spectate mode
     */
    public void enterSpectatingMode()
    {
        //Show countdown bar & scoreboard
        arena.getGameManager().getGameCountdownManager().showForPlayer(getF13Player().getBukkitPlayer());
        arena.getGameManager().getGameScoreboardManager().displayForPlayer(getF13Player().getBukkitPlayer());

        //Enter flight
        makePlayerVisibleToEveryone(false);
        getF13Player().getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
        getF13Player().getBukkitPlayer().setAllowFlight(true);
        getF13Player().getBukkitPlayer().setFlying(true);
        getF13Player().getBukkitPlayer().setHealth(20);
        getF13Player().getBukkitPlayer().setWalkSpeed(0.2f);

        //Location
        getF13Player().getBukkitPlayer().teleport(arena.getLocationManager().getRandomSpawnLocations()[0]);
        getF13Player().getBukkitPlayer().getInventory().clear();

        //Give them the selector
        SpectateMenu.addMenuOpenItem(getF13Player().getBukkitPlayer());

        //Let them know
        ActionBarAPI.sendActionBar(getF13Player().getBukkitPlayer(), ChatColor.RED + FridayThe13th.language.get(getF13Player().getBukkitPlayer(), "actionBar.counselor.becomeSpectator", "You are now in spectating mode.", ChatColor.WHITE), 300);

        //Hide other spectators from this person
        for (Spectator existingSpectator : arena.getGameManager().getPlayerManager().getSpectators().values())
        {
            if (!getF13Player().getBukkitPlayer().equals(existingSpectator.getF13Player().getBukkitPlayer()))
            {
                getF13Player().getBukkitPlayer().hidePlayer(existingSpectator.getF13Player().getBukkitPlayer());
            }
        }
    }
}
