package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.IO.Setting;
import com.AustinPilz.FridayThe13th.IO.Settings;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class BlockListener implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event)
    {
        //
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (FridayThe13th.arenaController.isPlayerPlaying(event.getPlayer()))
        {
            event.setCancelled(true);

            if (Settings.getGlobalBoolean(Setting.gameplayWarnOnPlace))
            {
                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You cannot place blocks while playing.");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (FridayThe13th.arenaController.isPlayerPlaying(event.getPlayer()))
        {
            event.setCancelled(true);

            if (Settings.getGlobalBoolean(Setting.gameplayWarnOnBreak))
            {
                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You cannot break blocks while playing.");
            }
        }
    }

}
