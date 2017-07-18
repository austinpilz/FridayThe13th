package com.AustinPilz.FridayThe13th.Components;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Iterator;
import java.util.Map;

public class Spectator
{
    private Player player;
    private Arena arena;

    public Spectator(Player p, Arena a)
    {
        player = p;
        arena = a;
    }

    /**
     * Returns player object of the spectator
     * @return
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Enters the counselor into spectating mode
     */
    public void enterSpectatingMode()
    {
        //Show countdown bar
        arena.getGameManager().getGameCountdownManager().showForPlayer(getPlayer());

        //Enter flight
        getPlayer().setAllowFlight(true);
        getPlayer().setHealth(20);

        //Location
        getPlayer().teleport(arena.getJasonStartLocation());
        getPlayer().setFlying(true);
        getPlayer().getInventory().clear();

        //Give them the selector
        ItemStack compass = new ItemStack(Material.EMERALD, 1);
        ItemMeta compassMetaData = compass.getItemMeta();
        compassMetaData.setDisplayName(ChatColor.GREEN + FridayThe13th.language.get(player, "game.item.spectateSelector", "Spectate Selector", ChatColor.WHITE));
        compass.setItemMeta(compassMetaData);
        getPlayer().getInventory().addItem(compass);

        //Let them know
        ActionBarAPI.sendActionBar(getPlayer(), ChatColor.RED + FridayThe13th.language.get(player, "actionBar.counselor.becomeSpectator", "You are now in spectating mode.", ChatColor.WHITE), 300);

        //Hide this player from everyone else
        Iterator it = arena.getGameManager().getPlayerManager().getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player hideFrom = (Player) entry.getValue();
            hideFrom.hidePlayer(getPlayer());
        }
    }

    /**
     * Removes the counselor from spectating mode
     */
    public void leaveSpectatingMode()
    {
        //Restore their defaults
        getPlayer().setAllowFlight(false);
        getPlayer().getInventory().clear();

        //Clear the action bar and hide spectator displays
        ActionBarAPI.sendActionBar(getPlayer(), "");
        arena.getGameManager().getGameCountdownManager().hideFromPlayer(getPlayer());

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

    /**
     * Returns the spectator selector inventory
     * @return
     */
    public Inventory getSpectateMenuInventory()
    {
        //Determine size
        int size = 9;
        if (arena.getGameManager().getPlayerManager().getNumCounselors() > 9 && arena.getGameManager().getPlayerManager().getNumCounselors() <= 18)
        {
            size = 18;
        }
        else if (arena.getGameManager().getPlayerManager().getNumCounselors() > 18 && arena.getGameManager().getPlayerManager().getNumCounselors() <= 27)
        {
            size = 27;
        }
        else if (arena.getGameManager().getPlayerManager().getNumCounselors() > 27 && arena.getGameManager().getPlayerManager().getNumCounselors() <= 36)
        {
            size = 36;
        }
        else if (arena.getGameManager().getPlayerManager().getNumCounselors() > 36 && arena.getGameManager().getPlayerManager().getNumCounselors() <= 45)
        {
            size = 45;
        }
        else if (arena.getGameManager().getPlayerManager().getNumCounselors() > 45 && arena.getGameManager().getPlayerManager().getNumCounselors() <= 54)
        {
            size = 54;
        }
        else if (arena.getGameManager().getPlayerManager().getNumCounselors() > 27 && arena.getGameManager().getPlayerManager().getNumCounselors() <= 63)
        {
            size = 63;
        }


        Inventory inventory = Bukkit.createInventory(null, size, ChatColor.GREEN + "" + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.SpectateSelectionItem", "Spectate Selection"));
        int i = 0;

        Iterator it = arena.getGameManager().getPlayerManager().getCounselors().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Counselor counselor = (Counselor) entry.getValue();

            if (i <= size && arena.getGameManager().getPlayerManager().isAlive(counselor.getPlayer()))
            {
                counselor.getPlayer().getName();
                ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
                SkullMeta meta = (SkullMeta)item.getItemMeta();
                meta.setOwner(counselor.getPlayer().getName());
                meta.setDisplayName(counselor.getPlayer().getName());
                item.setItemMeta(meta);

                inventory.setItem(i++, item);
            }
        }

        return inventory;
    }
}
