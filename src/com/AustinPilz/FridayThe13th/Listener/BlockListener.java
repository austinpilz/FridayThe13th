package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.IO.Setting;
import com.AustinPilz.FridayThe13th.IO.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Door;
import org.bukkit.material.Lever;

import java.util.Iterator;
import java.util.Map;

public class BlockListener implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event)
    {
        if (event.getBlock().getType().equals(Material.WALL_SIGN) || event.getBlock().getType().equals(Material.SIGN_POST))
        {
            Sign s = (Sign)event.getBlock().getState();
            String[] lines = event.getLines();

            if (lines[0].equalsIgnoreCase("[F13]"))
            {
                if (event.getPlayer().hasPermission("FridayThe13th.admin"))
                {
                    if (!lines[1].isEmpty() && lines[1] != "")
                    {
                        try
                        {
                            event.setCancelled(true);
                            FridayThe13th.inputOutput.newSign(s, FridayThe13th.arenaController.getArena(lines[1]));
                            FridayThe13th.arenaController.getArena(lines[1]).getSignManager().addJoinSign(s);
                            FridayThe13th.arenaController.getArena(lines[1]).getSignManager().updateJoinSigns();
                        }
                        catch (ArenaDoesNotExistException exception)
                        {
                            //Arena does not exist
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "command.error.arenaDoesNotExist", "Arena {0} does not exist.", ChatColor.RED + lines[1] + ChatColor.WHITE));
                        }
                        catch (SaveToDatabaseException exception)
                        {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.signDatabaseError", "There was an error while attempting to save sign to the database. See console for full error."));
                        }
                    }
                    else
                    {
                        //They didn't supply the sign name
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.signArenaError", "You need to supply the arena name on the second line of the sign."));
                    }
                }
                else
                {
                    //No permissions
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.signAddPermissionError", "You don't have permission to add signs."));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (FridayThe13th.arenaController.isPlayerPlaying(event.getPlayer()))
        {
            event.setCancelled(true);

            if (Settings.getGlobalBoolean(Setting.gameplayWarnOnPlace))
            {
                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.blockPlace", "You cannot place blocks while playing."));
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
                    event.setCancelled(true);
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
        } catch (PlayerNotPlayingException exception)
        {
            //They're not playing
            if (event.getBlock().getType().equals(Material.WALL_SIGN) || event.getBlock().getType().equals(Material.SIGN_POST))
            {
                Sign sign = (Sign)event.getBlock().getState();

                Iterator it = FridayThe13th.arenaController.getArenas().entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    Arena arena = (Arena) entry.getValue();
                    if (arena.getSignManager().isJoinSign(sign))
                    {
                        if (event.getPlayer().hasPermission("FridayThe13th.Admin"))
                        {
                            arena.getSignManager().removeJoinSign(sign);
                            FridayThe13th.inputOutput.deleteSign(sign.getX(), sign.getY(), sign.getZ(), sign.getWorld().toString());
                            event.getPlayer().sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(event.getPlayer(), "block.confirm.signBreak", "Sign removed successfully."));
                        }
                        else
                        {
                            //Don't have permission to break the sign
                            event.getPlayer().sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(event.getPlayer(), "block.error.signBreakNoPermission", "You don't have permission to break Friday the 13th signs."));
                        }
                    }
                }
            }
        }
    }

}
