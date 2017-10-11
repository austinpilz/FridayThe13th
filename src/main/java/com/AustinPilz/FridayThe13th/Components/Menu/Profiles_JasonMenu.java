package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Profiles.JasonProfile;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Skin.SkullPreview;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Profiles_JasonMenu {
    /**
     * Opens the menu for the supplied player
     *
     * @param player
     */
    public static void openMenu(Player player) {

        F13Player f13Player = FridayThe13th.playerController.getPlayer(player);

        Inventory inventory;

        inventory = Bukkit.createInventory(null, 18, ChatColor.RED + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.JasonProfileMenu", "Jason Profiles") + ": " + ChatColor.WHITE + f13Player.getJasonProfile().getDisplayName());

        //Display all profiles
        for (JasonProfile profile : JasonProfile.values())
        {
            if (!profile.isFridayThe13thOnly() || (profile.isFridayThe13thOnly() && FridayThe13th.isItFridayThe13th()))
            {
                ItemStack item;

                List<String> jasonItemLore = new ArrayList<String>();
                if (f13Player.getLevel().equals(profile.getRequiredLevel()) || f13Player.getLevel().isGreaterThan(profile.getRequiredLevel()) || f13Player.hasPurchasedJasonProfile(profile)) {
                    //The player has access to the profile
                    jasonItemLore.add(HiddenStringsUtil.encodeString("{\"JasonProfileSelect\": \"" + profile.getInternalIdentifier() + "\"}"));

                    if (f13Player.getJasonProfile().equals(profile))
                    {
                        jasonItemLore.add(ChatColor.GOLD + "Selected");
                    }
                    else
                    {
                        jasonItemLore.add(ChatColor.DARK_GREEN + "Unlocked");
                    }


                    jasonItemLore.add("");
                    jasonItemLore.add(ChatColor.WHITE + "Stalk " + ChatColor.GREEN + profile.getStalkLevel().getLevelName());
                    jasonItemLore.add(ChatColor.WHITE + "Sense " + ChatColor.RED + profile.getSenseLevel().getLevelName());
                    jasonItemLore.add(ChatColor.WHITE + "Warp " + ChatColor.DARK_PURPLE + profile.getWarpLevel().getLevelName());
                    jasonItemLore.add(ChatColor.WHITE + "Door Breaks: " + ChatColor.AQUA + profile.getRequiredDoorBreaks());


                }
                else
                {
                    //This Jason is locked
                    jasonItemLore.add(HiddenStringsUtil.encodeString("{\"JasonProfileSelect\": \"Locked\"}"));
                    jasonItemLore.add(ChatColor.BOLD + "" + ChatColor.RED + "LOCKED");
                    jasonItemLore.add("");
                    jasonItemLore.add(ChatColor.WHITE + "Unlocks at level " + ChatColor.AQUA + profile.getRequiredLevel().getLevelNumber());
                }

                item = new SkullPreview(profile.getSkin(), profile.getDisplayName(), jasonItemLore);
                inventory.addItem(item);
            }
        }


        //Open it for player
        player.openInventory(inventory);
    }
}
