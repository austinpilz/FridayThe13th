package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;



public class Shop_MainMenu {
    /**
     * Opens the menu for the supplied player
     *
     * @param player
     */
    public static void openMenu(Player player) {

        //
    }

    /**
     * Adds the menu open item to the supplied players inventory
     *
     * @param player
     */
    public static void addMenuOpenItem(Player player) {

        List<String> menuItemLore = new ArrayList<String>();
        menuItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"Shop_Main\"}"));
        menuItemLore.add(ChatColor.GRAY + "(Coming Soon)");

        ItemStack item = new ItemStack(Material.CHEST, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Store");
        itemMeta.setLore(menuItemLore);
        item.setItemMeta(itemMeta);

        player.getInventory().setItem(2, item);
    }
}
