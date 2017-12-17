package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Characters.CharacterType;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Perk.F13Perk;
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

public class PurchasedPerksMenu {
    /**
     * Opens the menu for the supplied player
     *
     * @param player
     */
    public static void openMenu(Player player) {

        F13Player f13Player = FridayThe13th.playerController.getPlayer(player);
        int size = 0;
        int count = f13Player.getPurchasedPerks().size();
        if (count > 0 && count <= 9)
        {
            size = 9;
        }
        else if (count > 9  && count <= 18)
        {
            size = 18;
        }
        else if (count > 18  && count <= 27)
        {
            size = 27;
        }


        Inventory inventory = Bukkit.createInventory(null, 9, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.shopPurchasedPerks", "Purchased Perks"));

        for (F13Perk perk : f13Player.getPurchasedPerks())
        {
            ItemStack item = new ItemStack(perk.getDisplayMaterial(), 1);
            ItemMeta meta = item.getItemMeta();

            if (perk.getCharacterType().equals(CharacterType.Counselor))
            {
                meta.setDisplayName(ChatColor.DARK_GREEN + perk.getName());
            } else if (perk.getCharacterType().equals(CharacterType.Jason))
            {
                meta.setDisplayName(ChatColor.DARK_RED + perk.getName());
            }

            List<String> menuItemLore = new ArrayList<String>();
            menuItemLore.add(HiddenStringsUtil.encodeString("{\"ActivatePerk\": \"" + perk.getName() + "\"}"));

            if (!perk.getDescription().isEmpty())
            {
                menuItemLore.add(ChatColor.WHITE + perk.getDescription());
            }

            meta.setLore(menuItemLore);
            item.setItemMeta(meta);
            inventory.addItem(item);
        }

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
        menuItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"Purchased_Perks\"}"));

        ItemStack item = new ItemStack(Material.ENDER_CHEST, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + "Purchased Perks");
        itemMeta.setLore(menuItemLore);
        item.setItemMeta(itemMeta);

        player.getInventory().setItem(3, item);
    }
}
