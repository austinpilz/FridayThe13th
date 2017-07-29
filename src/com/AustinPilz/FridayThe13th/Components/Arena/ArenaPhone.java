package com.AustinPilz.FridayThe13th.Components.Arena;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * Created by austinpilz on 7/14/17.
 */
public class ArenaPhone
{
    private Arena arena;
    private Location location;
    private Hologram hologram;
    private boolean isVisible;

    private int callAttempts;
    private int callAttemptsRequired;



    public ArenaPhone(Arena a, Location l)
    {
        arena = a;
        location = l;
        isVisible = false;

        //Values
        callAttempts = 0;
        callAttemptsRequired = 75;

    }

    /**
     * Create's the phone hologram
     */
    public void createHologram()
    {
        hologram = HologramsAPI.createHologram(FridayThe13th.instance, location.getBlock().getRelative(BlockFace.UP).getLocation());
        hologram.appendTextLine(ChatColor.WHITE + "Call Tommy");
        //hologram.getVisibilityManager().hideTo(arena.getGameManager().getPlayerManager().getJason().getPlayer());
    }

    /**
     * Returns the phone's arena
     * @return
     */
    public Arena getArena()
    {
        return arena;
    }

    /**
     * Returns the phone's location
     * @return
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Returns the percentage of repair progress
     * @return
     */
    private double getCallProgressPercent()
    {
        return callAttempts/callAttemptsRequired;
    }

    /**
     * Attempt to repair the switch
     */
    public void callAttempt(Player player)
    {
        if (!arena.getGameManager().hasTommyBeenCalled() && isVisible) {
            if ((++callAttempts / callAttemptsRequired) == 1.0) {
                hologram.getLine(0).removeLine();
                hologram.insertTextLine(0, "Tommy: " + ChatColor.GREEN + "CALLED");

                //Fire firework
                arena.getGameManager().getPlayerManager().fireFirework(player, Color.GREEN);

                arena.getGameManager().getPlayerManager().sendMessageToAllPlayers(ChatColor.AQUA + player.getName() + ChatColor.WHITE + " has called Tommy Jarvis.");

                arena.getGameManager().tommyCalled();
                callAttempts++;

                //Register Tommy called for XP
                arena.getGameManager().getPlayerManager().getCounselor(player).getXPManager().addTommyCalled();
            } else {
                hologram.getLine(0).removeLine();

                float percentage = ((float) callAttempts) / callAttemptsRequired * 100;
                percentage = Math.round(percentage);
                String strPercent = String.format("%2.0f", percentage);

                String string = "Call Tommy: " + String.valueOf(strPercent) + "%";
                hologram.insertTextLine(0, ChatColor.WHITE + string);
            }
        }
    }

    /**
     * Hides phone
     */
    public void hidePhone()
    {
        if (hologram != null)
        {
            hologram.delete();
        }

        isVisible = false;
    }

    /**
     * Shows phone
     */
    public void showPhone()
    {
        createHologram();

        //Reset values
        callAttempts = 0;

        //Hide from Jason
        hologram.getVisibilityManager().hideTo(arena.getGameManager().getPlayerManager().getJason().getPlayer());

        //Set that this is the visible phone
        isVisible = true;
    }

    /**
     * Returns if the location of the phone is still a phone
     * @return
     */
    public boolean isStillPhone()
    {
        return location.getBlock().getType().equals(Material.TRIPWIRE_HOOK);
    }
}
