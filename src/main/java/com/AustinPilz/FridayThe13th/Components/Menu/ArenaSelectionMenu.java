package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Profiles.CounselorProfile;
import com.AustinPilz.FridayThe13th.Components.Skin.F13Skin;
import com.AustinPilz.FridayThe13th.Components.Skin.SkullPreview;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import com.AustinPilz.FridayThe13th.Utilities.InventoryActions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ArenaSelectionMenu {
    /**
     * Opens the menu for the supplied player
     *
     * @param player
     */
    public static void openMenu(Player player) {

        Inventory inventory;

        inventory = Bukkit.createInventory(null, InventoryActions.calculateMenuSize(FridayThe13th.arenaController.getNumberOfArenas()), ChatColor.DARK_RED + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.ArenaSelectionMenu", "F13 Arenas"));

        for (Arena arena : FridayThe13th.arenaController.getArenas().values())
        {
            List<String> arenaItemLore = new ArrayList<String>();
            Material material = Material.DIAMOND;
            arenaItemLore.add(HiddenStringsUtil.encodeString("{\"Play\": \"" + arena.getName() + "\"}"));

            if (arena.getGameManager().isGameInProgress())
            {
                material = Material.REDSTONE_BLOCK;

                int rem = arena.getGameManager().getGameTimeLeft() % 3600;
                int mn = rem / 60;
                int sec = rem % 60;

                arenaItemLore.add(ChatColor.WHITE + "Status: " + ChatColor.RED + "In Progress" + ChatColor.WHITE + " ~" + mn + ":" + sec + " left");
            }
            else if (arena.getGameManager().isGameWaiting())
            {
                material = Material.DIAMOND_BLOCK;

                int rem = arena.getGameManager().getWaitingTimeLeft() % 3600;
                int mn = rem / 60;
                int sec = rem % 60;

                arenaItemLore.add(ChatColor.WHITE + "Status: " + ChatColor.AQUA + "Waiting" + ChatColor.WHITE + " ~" + mn + ":" + sec + " until start");
            }
            else
            {
                material = Material.EMERALD_BLOCK;

                if (arena.getGameManager().getPlayerManager().getNumberOfPlayers() > 0)
                {
                    arenaItemLore.add(ChatColor.WHITE + "Status: " + ChatColor.RED + "Waiting for 1 more player");
                }
                else
                {
                    arenaItemLore.add(ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Empty");
                }
            }

            arenaItemLore.add("");
            if (arena.isTommyJarvisEnabled())
            {
                arenaItemLore.add(ChatColor.DARK_AQUA + "Tommy Jarvis");
            }

            if (arena.arePoliceEnabled())
            {
                arenaItemLore.add(ChatColor.DARK_AQUA + "Police");
            }

            if (arena.isCarEscapeEnabled())
            {
                arenaItemLore.add(ChatColor.DARK_AQUA + "Car Escape");
            }

            if (arena.isBoatEscapeEnabled())
            {
                arenaItemLore.add(ChatColor.DARK_AQUA + "Boat Escape");
            }

            arenaItemLore.add("");
            if (arena.getGameManager().isGameInProgress())
            {
                arenaItemLore.add(ChatColor.GREEN + "Click to spectate!");
            }
            else
            {
                if (arena.getGameManager().isGameWaiting() && arena.getGameManager().getPlayerManager().getNumberOfSpotsLeft() <= 0)
                {
                    arenaItemLore.add(ChatColor.RED + "Game Full");
                }
                else
                {
                    arenaItemLore.add(ChatColor.GREEN + "Click to play!");
                }
            }

            ItemStack item = new ItemStack(material, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(arenaItemLore);
            meta.setDisplayName(ChatColor.GOLD + arena.getName());
            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        //Open it for player
        player.openInventory(inventory);
    }
}
