package com.AustinPilz.FridayThe13th.Manager.Game;

import com.AustinPilz.CustomSoundManagerAPI.API.PlayerSoundAPI;
import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class SoundManager {

    /**
     * Plays sound effect for all players
     * @param soundEffect
     * @param arena
     */
    public static void playSoundForAllPlayers(F13SoundEffect soundEffect, Arena arena, boolean interrupt, boolean overlay, int transitionSeconds)
    {
        Iterator it = arena.getGameManager().getPlayerManager().getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player player = (Player) entry.getValue();
            PlayerSoundAPI.getPlayerSoundManager(player).playCustomSound(player.getLocation(), soundEffect.getResourcePackValue(), soundEffect.getLengthInSeconds(), soundEffect.getVolume(), interrupt, overlay, transitionSeconds);
        }
    }

    /**
     * Plays sounds effect for players within radius of provided location
     * @param soundEffect
     * @param arena
     * @param location
     * @param radius
     */
    public static void playSoundForNearbyPlayers(F13SoundEffect soundEffect, Arena arena, Location location, int radius, boolean interrupt, boolean overlay)
    {
        HashSet<Player> nearbyPlayers = new HashSet<>();

        //Determine which players are within the radius
        Iterator it = arena.getGameManager().getPlayerManager().getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player player = (Player) entry.getValue();
            if (location.distance(player.getLocation()) <= radius)
            {
                nearbyPlayers.add(player);
            }
        }

        //Play the sound
        for (Player player : nearbyPlayers)
        {
            PlayerSoundAPI.getPlayerSoundManager(player).playCustomSound(player.getLocation(), soundEffect.getResourcePackValue(), soundEffect.getLengthInSeconds(), 10, interrupt, overlay);
        }

    }

    /**
     * Plays sound effect for supplied player
     * @param player
     * @param soundEffect
     * @param interrupt
     * @param overlay
     */
    public static void playSoundForPlayer(Player player, F13SoundEffect soundEffect, boolean interrupt, boolean overlay, int transitionSeconds)
    {
        PlayerSoundAPI.getPlayerSoundManager(player).playCustomSound(player.getLocation(), soundEffect.getResourcePackValue(), soundEffect.getLengthInSeconds(), soundEffect.getVolume(), interrupt, overlay, transitionSeconds);
    }

    /**
     * Returns if the sound effect is already playing for the supplied player
     * @param player
     * @param soundEffect
     * @return
     */
    public static boolean isSoundAlreadyPlayingForPlayer(Player player, F13SoundEffect soundEffect)
    {
        return PlayerSoundAPI.getPlayerSoundManager(player).isSoundCurrentlyPlaying(soundEffect.getResourcePackValue());
    }

    /**
     * Returns if any sounds are currently playing for the player
     * @param player
     * @return
     */
    public static boolean areAnySoundsPlayingForPlayer(Player player)
    {
        return PlayerSoundAPI.getPlayerSoundManager(player).areAnySoundsPlaying();
    }
}
