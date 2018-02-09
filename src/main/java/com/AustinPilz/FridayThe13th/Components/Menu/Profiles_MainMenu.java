package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Skin.F13Skin;
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

public class Profiles_MainMenu {

    public static void openMenu(Player player) {

        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.ProfileMenu", "Profiles"));

        List<String> counselorItemLore = new ArrayList<String>();
        counselorItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"CounselorProfiles\"}"));
        inventory.setItem(0, new SkullPreview(F13Skin.Counselor_Chad, ChatColor.DARK_GREEN + FridayThe13th.language.get(player, "game.menu.CounselorProfile", "Counselor Profiles"), counselorItemLore));

        List<String> jasonItemLore = new ArrayList<String>();
        jasonItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"JasonProfiles\"}"));
        inventory.setItem(1, new SkullPreview(F13Skin.JASON_Part1, ChatColor.RED + FridayThe13th.language.get(player, "game.menu.JasonProfile", "Jason Profiles"), jasonItemLore));

        player.openInventory(inventory);
    }


    /**
     * Adds the menu open item to the supplied players inventory
     *
     * @param player
     */
    public static void addMenuOpenItem(Player player) {
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_GREEN + FridayThe13th.language.get(player, "game.menu.Profiles", "Profiles"));

        List<String> menuItemLore = new ArrayList<String>();
        menuItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"Profiles\"}"));
        itemMeta.setLore(menuItemLore);
        item.setItemMeta(itemMeta);

        player.getInventory().setItem(0, item);
    }
}
