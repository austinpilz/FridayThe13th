package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Enum.CharacterType;
import com.AustinPilz.FridayThe13th.Components.Perk.F13Perk;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Shop_JasonPerksMenu {
    public static void openMenu(Player player) {

        F13Player f13Player = FridayThe13th.playerController.getPlayer(player);

        int count = 0;
        for (F13Perk perk : F13Perk.values())
        {
            if (perk.getCharacterType().equals(CharacterType.Jason))
            {
                count++;
            }
        }

        //Determine inventory size
        //9,18,27,36,45,54,63
        int size = 0;
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

        Inventory inventory = Bukkit.createInventory(null, size, ChatColor.RED + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.JasonPerksShopMenu", "Jason Perks") + ChatColor.WHITE + " [CP: " + f13Player.getFormattedCP() + "]");

        int place = 0;
        for (F13Perk perk : F13Perk.values())
        {
            if (perk.getCharacterType().equals(CharacterType.Jason))
            {
                ItemStack item = new ItemStack(perk.getDisplayMaterial(), 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(perk.getName());

                List<String> menuItemLore = new ArrayList<String>();
                menuItemLore.add(HiddenStringsUtil.encodeString("{\"PurchasePerk\": \"" + perk.getName() + "\"}"));

                if (!perk.getDescription().isEmpty())
                {
                    menuItemLore.add(ChatColor.WHITE + perk.getDescription());
                    menuItemLore.add("");
                }

                menuItemLore.add(ChatColor.WHITE + "Cost: " + ChatColor.AQUA + perk.getFormattedCost() + ChatColor.WHITE + " cp");
                menuItemLore.add("");

                if (f13Player.getCP() >= perk.getCost())
                {
                    //They have sufficient funds
                    menuItemLore.add(ChatColor.GREEN + "Click to purchase!");
                }
                else
                {
                    //Insufficient funds
                    menuItemLore.add(ChatColor.RED + "Insufficient funds");
                }

                meta.setLore(menuItemLore);
                item.setItemMeta(meta);
                inventory.setItem(place++, item);
            }
        }

        //Open it for player
        player.openInventory(inventory);
    }
}
