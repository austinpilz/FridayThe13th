package com.AustinPilz.FridayThe13th.Components.Menu;

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

public class SpawnPreferenceMenu {
    /**
     * Opens the menu for the supplied player
     *
     * @param player
     */
    public static void openMenu(Player player) {

        Inventory inventory;

        if (FridayThe13th.playerController.getPlayer(player).isSpawnPreferenceJason()) {
            //Jason
            inventory = Bukkit.createInventory(null, 9, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.SpawnPreferenceMenuJasonSet", "Spawn Preference: {0}Jason", ChatColor.RED));
        } else if (FridayThe13th.playerController.getPlayer(player).isSpawnPreferenceCounselor()) {
            //Counselor
            inventory = Bukkit.createInventory(null, 9, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.SpawnPreferenceMenuCounselorSet", "Spawn Preference: {0}Counselor", ChatColor.DARK_GREEN));
        } else {
            //None set
            inventory = Bukkit.createInventory(null, 9, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.SpawnPreferenceMenu", "Spawn Preference"));
        }

        //Add Jason
        ItemStack jasonItem = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta jasonItemMeta = jasonItem.getItemMeta();
        jasonItemMeta.setDisplayName(ChatColor.RED + FridayThe13th.language.get(player, "game.item.SpawnPreferenceJason", "Jason"));

        List<String> jasonItemLore = new ArrayList<String>();
        jasonItemLore.add(HiddenStringsUtil.encodeString("{\"SpawnPrefSelect\": \"J\"}"));
        jasonItemMeta.setLore(jasonItemLore);

        jasonItem.setItemMeta(jasonItemMeta);
        inventory.addItem(jasonItem);

        //Add Counselor
        ItemStack counselorItem = new ItemStack(Material.COMPASS, 1);
        ItemMeta counselorItemMeta = counselorItem.getItemMeta();
        counselorItemMeta.setDisplayName(ChatColor.GREEN + FridayThe13th.language.get(player, "game.item.SpawnPreferenceCounselor", "Counselor"));

        List<String> counselorItemLore = new ArrayList<String>();
        counselorItemLore.add(HiddenStringsUtil.encodeString("{\"SpawnPrefSelect\": \"C\"}"));
        counselorItemMeta.setLore(counselorItemLore);

        counselorItem.setItemMeta(counselorItemMeta);
        inventory.addItem(counselorItem);

        //Open it for player
        player.openInventory(inventory);
    }

    /**
     * Adds the menu open item to the supplied players inventory
     *
     * @param player
     */
    public static void addMenuOpenItem(Player player) {
        ItemStack item = new ItemStack(Material.BED, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + FridayThe13th.language.get(player, "game.item.SpawnPreference", "Spawn Preference"));
        List<String> menuItemLore = new ArrayList<String>();
        menuItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"SpawnPref\"}"));
        itemMeta.setLore(menuItemLore);
        item.setItemMeta(itemMeta);

        if (FridayThe13th.playerController.getPlayer(player).isSpawnPreferenceJason() || FridayThe13th.playerController.getPlayer(player).isSpawnPreferenceCounselor()) {
            //They have already set their preference, so do not auto-open
            player.getInventory().setItem(1, item);
        } else {
            //They do not have their preference set, so put it in slot 0 so it'll most likely auto-open
            player.getInventory().setItem(0, item);
        }
    }
}
