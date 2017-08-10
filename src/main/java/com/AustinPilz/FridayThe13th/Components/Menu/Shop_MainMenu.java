package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Enum.F13Skin;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Skin.SkullPreview;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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

        F13Player f13Player = FridayThe13th.playerController.getPlayer(player);
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.DARK_PURPLE + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.shopMainMenuTitle", "Shop") + ChatColor.WHITE + " [CP: " + f13Player.getFormattedCP() + "]");

        //Counselor Perks
        List<String> counselorItemLore = new ArrayList<String>();
        counselorItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"Shop_Counselor\"}"));
        counselorItemLore.add(ChatColor.WHITE + "Purchase perks that enhance");
        counselorItemLore.add(ChatColor.WHITE + "the counselor play experience.");
        inventory.setItem(0, new SkullPreview(F13Skin.Counselor_Eric, ChatColor.DARK_GREEN + FridayThe13th.language.get(player, "game.menu.CounselorPerkItem", "Counselor Perks"), counselorItemLore));

        //Jason Perks
        List<String> jasonItemLore = new ArrayList<String>();
        jasonItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"Shop_Jason\"}"));
        jasonItemLore.add(ChatColor.WHITE + "Purchase perks that enhance");
        jasonItemLore.add(ChatColor.WHITE + "the Jason play experience.");
        inventory.setItem(1, new SkullPreview(F13Skin.JASON_Part4, ChatColor.RED + FridayThe13th.language.get(player, "game.menu.JasonPerkItem", "Jason Perks"), jasonItemLore));

        //Display for player
        player.openInventory(inventory);
    }

    /**
     * Adds the menu open item to the supplied players inventory
     *
     * @param player
     */
    public static void addMenuOpenItem(Player player) {

        List<String> menuItemLore = new ArrayList<String>();
        menuItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"Shop_Main\"}"));

        ItemStack item = new ItemStack(Material.CHEST, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_PURPLE + "Store");
        itemMeta.setLore(menuItemLore);
        item.setItemMeta(itemMeta);

        player.getInventory().setItem(2, item);
    }
}
