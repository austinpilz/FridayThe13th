package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.Components.Enum.XPAward;
import com.AustinPilz.FridayThe13th.Events.F13BlockPlacedEvent;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Game.SoundManager;
import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Door;
import org.bukkit.material.Lever;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BlockListener implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
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
                            FridayThe13th.inputOutput.storeSign(s, FridayThe13th.arenaController.getArena(lines[1]));
                            FridayThe13th.arenaController.getArena(lines[1]).getSignManager().addJoinSign(s);
                            FridayThe13th.arenaController.getArena(lines[1]).getSignManager().updateJoinSigns();
                        }
                        catch (ArenaDoesNotExistException exception)
                        {
                            //Game does not exist
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + lines[1] + ChatColor.WHITE));
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        try
        {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer());

            //Check to see if the item has hidden data
            if (event.getPlayer().getInventory().getItemInMainHand() != null && event.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
                //It has meta, so it must be one of the F13 selections
                ItemMeta meta = event.getPlayer().getInventory().getItemInMainHand().getItemMeta();
                List<String> lore = meta.getLore();

                if (lore != null && lore.size() > 0 && HiddenStringsUtil.hasHiddenString(lore.get(0))) {
                    //It's a F13 menu item - call custom event
                    F13BlockPlacedEvent newEvent = new F13BlockPlacedEvent(event.getPlayer(), arena, event.getBlock(), event.getBlockReplacedState().getType(), HiddenStringsUtil.extractHiddenString(lore.get(0)));
                    Bukkit.getServer().getPluginManager().callEvent(newEvent);

                    event.setCancelled(newEvent.isCancelled());
                } else {
                    //Has item, but no hidden data - must just be a special name
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }

            //Warn them if we cancelled the event
            if (event.isCancelled())
            {
                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.blockPlace", "You cannot place blocks while playing."));
            }
        }
        catch (PlayerNotPlayingException exception)
        {
            //Check to see if that block exists within the boundaries of any arena. If it does, check their perms. If they don't have f13.build, block the break
            Iterator arenaIterator = FridayThe13th.arenaController.getArenas().entrySet().iterator();
            while (arenaIterator.hasNext())
            {
                Map.Entry entry = (Map.Entry) arenaIterator.next();
                Arena arena = (Arena) entry.getValue();

                if (arena.isLocationWithinArenaBoundaries(event.getBlock().getLocation()) && !event.getPlayer().hasPermission("FridayThe13th.Build"))
                {
                    //They're trying to place a block within an arena boundary without having permissions, block the action
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.nonPlayingBlockPlace", "You don't have permissions to place blocks in F13 arena boundaries."));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event)
    {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer());
            event.setCancelled(true); //Can never break blocks while in-game

            if (arena.getGameManager().isGameInProgress())
            {
                if (arena.getGameManager().getPlayerManager().isJason(FridayThe13th.playerController.getPlayer(event.getPlayer())))
                {
                    //Physical object interactions
                    if (event.getBlock().getState().getData() instanceof Door)
                    {
                        BlockState state = event.getBlock().getState();
                        Door door = (Door) state.getData();

                        if (door.isTopHalf())
                        {
                            //Top 1/2
                            arena.getObjectManager().getArenaDoor(event.getBlock().getRelative(BlockFace.DOWN)).blockBreak();
                        }
                        else
                        {
                            arena.getObjectManager().getArenaDoor(event.getBlock()).blockBreak();
                        }
                    }
                    else if (event.getBlock().getState().getData() instanceof Lever)
                    {
                        //Lever
                        if (!arena.getObjectManager().getBrokenSwitches().containsKey(event.getBlock()))
                        {
                            arena.getObjectManager().breakSwitch(event.getBlock());
                            arena.getGameManager().getPlayerManager().getJason().getXpManager().registerXPAward(XPAward.Jason_SwitchBreak);
                        }
                    }
                    else if (event.getBlock().getType().equals(Material.THIN_GLASS)) {
                        //Window
                        arena.getObjectManager().getWindowManager().breakWindow(event.getBlock()); //Jason breaks window
                        arena.getGameManager().getPlayerManager().getJason().getXpManager().registerXPAward(XPAward.Jason_WindowBreak);

                        //Play sound for everyone
                        SoundManager.playSoundForNearbyPlayers(F13SoundEffect.GlassBreak, arena, event.getBlock().getLocation(), 10, false, true);
                    } else if (event.getBlock().getType().equals(Material.TRIPWIRE_HOOK) && arena.getObjectManager().getPhoneManager().isBlockARegisteredPhone(event.getBlock())) {
                        //Jason is breaking a registered phone
                        arena.getObjectManager().getPhoneManager().getPhone(event.getBlock()).breakPhone();
                    }
                }
            }
        }
        catch (PlayerNotPlayingException exception)
        {
            //Check to see if that block exists within the boundaries of any arena. If it does, check their perms. If they don't have f13.build, block the break
            Iterator arenaIterator = FridayThe13th.arenaController.getArenas().entrySet().iterator();
            while (arenaIterator.hasNext())
            {
                Map.Entry entry = (Map.Entry) arenaIterator.next();
                Arena arena = (Arena) entry.getValue();

                if (arena.isLocationWithinArenaBoundaries(event.getBlock().getLocation()) && !event.getPlayer().hasPermission("FridayThe13th.Build"))
                {
                    //They're trying to break a block within an arena boundary without having permissions, block the action
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.nonPlayingBlockBreak", "You don't have permissions to break blocks in F13 arena boundaries."));
                    event.setCancelled(true);
                }
            }

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
                            FridayThe13th.inputOutput.deleteSign(sign.getX(), sign.getY(), sign.getZ(), sign.getWorld().getName());
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

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPaintingBreak(HangingBreakByEntityEvent event)
    {
        if (event.getRemover() instanceof Player)
        {
            if (FridayThe13th.arenaController.isPlayerPlaying((Player)event.getRemover()))
            {
                if (event.getEntity().getType() == EntityType.PAINTING)
                {
                    //Players cannot break paintings while playing
                    event.setCancelled(true);
                }
            }
        }
    }

}
