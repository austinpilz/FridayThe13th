package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnPreferenceMenu {
    /**
     * Opens the menu for the supplied player
     *
     * @param player
     */
    public static void openMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.SpawnPreferenceMenu", "F13 Spawn Preference"));

        //Add Jason

        //Blocks are grey if no preference selected

        player.openInventory(inventory);
    }

    /**
     * Adds the menu open item to the supplied players inventory
     *
     * @param player
     */
    public static void addMenuOpenItem(Player player) {
        ItemStack item = new ItemStack(Material.EMPTY_MAP, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + FridayThe13th.language.get(player, ChatColor.WHITE + "game.item.SpawnPreference", "Spawn Preference"));
        item.setItemMeta(itemMeta);
        player.getInventory().addItem(item);
    }
}
