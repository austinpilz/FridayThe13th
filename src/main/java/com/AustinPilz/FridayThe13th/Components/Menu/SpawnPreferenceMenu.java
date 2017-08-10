package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Enum.F13Skin;
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
            inventory = Bukkit.createInventory(null, 9, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.SpawnPreferenceMenuJasonSet", "Spawn Preference: {0}Jason", ChatColor.DARK_RED));
        } else if (FridayThe13th.playerController.getPlayer(player).isSpawnPreferenceCounselor()) {
            //Counselor
            inventory = Bukkit.createInventory(null, 9, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.SpawnPreferenceMenuCounselorSet", "Spawn Preference: {0}Counselor", ChatColor.DARK_GREEN));
        } else {
            //None set
            inventory = Bukkit.createInventory(null, 9, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.SpawnPreferenceMenu", "Spawn Preference"));
        }


        //Add counselor
        List<String> counselorItemLore = new ArrayList<String>();
        counselorItemLore.add(HiddenStringsUtil.encodeString("{\"SpawnPrefSelect\": \"C\"}"));
        inventory.setItem(0, new SkullPreview(F13Skin.Counselor_Tiffany, ChatColor.DARK_GREEN + FridayThe13th.language.get(player, "game.item.SpawnPreferenceCounselor", "Counselor"), counselorItemLore));

        //Add Jason
        List<String> jasonItemLore = new ArrayList<String>();
        jasonItemLore.add(HiddenStringsUtil.encodeString("{\"SpawnPrefSelect\": \"J\"}"));
        inventory.setItem(1, new SkullPreview(F13Skin.JASON_Part1, ChatColor.DARK_RED + FridayThe13th.language.get(player, "game.item.SpawnPreferenceJason", "Jason"), jasonItemLore));

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

        player.getInventory().setItem(1, item);
    }
}
