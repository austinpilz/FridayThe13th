package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Profiles.CounselorProfile;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Skin.SkullPreview;
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

public class Profiles_CounselorMenu {
    /**
     * Opens the menu for the supplied player
     *
     * @param player
     */
    public static void openMenu(Player player) {

        F13Player f13Player = FridayThe13th.playerController.getPlayer(player);

        Inventory inventory;

        inventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GREEN + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.CounselorProfileMenu", "Counselor Profiles") + ": " + ChatColor.WHITE + f13Player.getCounselorProfile().getShortName());

        //Display all profiles
        for (CounselorProfile profile : CounselorProfile.values()) {
            ItemStack item;
            ItemMeta itemMeta;

            List<String> jasonItemLore = new ArrayList<String>();
            if (f13Player.getLevel().equals(profile.getRequiredLevel()) || f13Player.getLevel().isGreaterThan(profile.getRequiredLevel()) || f13Player.hasPurchasedCounselorProfile(profile)) {
                //The player has access to the profile
                jasonItemLore.add(HiddenStringsUtil.encodeString("{\"CounselorProfileSelect\": \"" + profile.getInternalIdentifier() + "\"}"));

                //Add green glass to signify unlocked
                if (f13Player.getCounselorProfile().equals(profile)) {
                    //They currently have this skin selected
                    jasonItemLore.add(ChatColor.GOLD + "Selected");
                } else {
                    //This profile is available
                    jasonItemLore.add(ChatColor.DARK_GREEN + "Unlocked");
                }

                jasonItemLore.add("");
                jasonItemLore.add(ChatColor.WHITE + "Composure    " + ChatColor.RED + profile.getComposure().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Luck         " + ChatColor.RED + profile.getLuck().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Intelligence " + ChatColor.RED + profile.getIntelligence().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Speed        " + ChatColor.RED + profile.getSpeed().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Stamina      " + ChatColor.RED + profile.getStamina().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Stealth      " + ChatColor.RED + profile.getStealth().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Strength     " + ChatColor.RED + profile.getStrength().getTraitLevel() + ChatColor.WHITE + "/10");


            } else {
                //This profile is locked
                jasonItemLore.add(HiddenStringsUtil.encodeString("{\"CounselorProfileSelect\": \"Locked\"}"));
                jasonItemLore.add(ChatColor.BOLD + "" + ChatColor.RED + "LOCKED");
                jasonItemLore.add("");
                jasonItemLore.add(ChatColor.WHITE + "Unlocks at level " + ChatColor.AQUA + profile.getRequiredLevel().getLevelNumber());
            }

            //Get skin preview for the skull
            item = new SkullPreview(profile.getSkin(), profile.getDisplayName(), jasonItemLore);
            inventory.addItem(item);
        }


        //Open it for player
        player.openInventory(inventory);
    }
}
