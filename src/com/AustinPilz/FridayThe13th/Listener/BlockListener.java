package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Components.Jason;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.IO.Setting;
import com.AustinPilz.FridayThe13th.IO.Settings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Door;
import org.bukkit.material.Lever;

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
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            if (arena.getGameManager().isGameInProgress())
            {
                //Physical object interactions
                if (event.getBlock().getState().getData() instanceof Door)
                {
                    //Door broken
                    arena.getObjectManager().getArenaDoor(event.getBlock()).blockBreak();
                    event.setCancelled(true);
                }
                else if (event.getBlock().getState().getData() instanceof Lever)
                {
                    //Lever
                    arena.getObjectManager().breakSwitch(event.getBlock());
                    event.setCancelled(true);
                }
                else if (event.getBlock().getType().equals(Material.THIN_GLASS) || event.getBlock().getType().equals(Material.STAINED_GLASS_PANE))
                {
                    //Window
                }
                else
                {
                    event.setCancelled(true);
                }
            }
            else if (arena.getGameManager().isGameWaiting() || arena.getGameManager().isGameEmpty())
            {
                event.setCancelled(true); //Disable interaction while in the waiting room
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

}
