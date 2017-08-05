package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Enum.CounselorProfile;
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

public class CounselorProfilesMenu {
    /**
     * Opens the menu for the supplied player
     *
     * @param player
     */
    public static void openMenu(Player player) {

        F13Player f13Player = FridayThe13th.playerController.getPlayer(player);

        Inventory inventory;

        inventory = Bukkit.createInventory(null, 18, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.CounselorProfileMenu", "Counselor Profiles"));

        //Display all profiles
        for (CounselorProfile profile : CounselorProfile.values()) {
            ItemStack item;
            ItemMeta itemMeta;

            ItemStack glass;
            ItemMeta glassMeta = null;

            List<String> jasonItemLore = new ArrayList<String>();
            if (f13Player.getLevel().equals(profile.getRequiredLevel()) || f13Player.getLevel().isGreaterThan(profile.getRequiredLevel())) {
                //The player has access to the profile
                jasonItemLore.add(HiddenStringsUtil.encodeString("{\"CounselorProfileSelect\": \"" + profile.getDisplayName() + "\"}"));
                jasonItemLore.add(ChatColor.DARK_GREEN + "Unlocked");
                jasonItemLore.add("");
                jasonItemLore.add(ChatColor.WHITE + "Composure    " + ChatColor.RED + profile.getComposure().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Luck         " + ChatColor.RED + profile.getLuck().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Intelligence " + ChatColor.RED + profile.getIntelligence().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Speed        " + ChatColor.RED + profile.getSpeed().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Stamina      " + ChatColor.RED + profile.getStamina().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Stealth      " + ChatColor.RED + profile.getStealth().getTraitLevel() + ChatColor.WHITE + "/10");
                jasonItemLore.add(ChatColor.WHITE + "Strength     " + ChatColor.RED + profile.getStrength().getTraitLevel() + ChatColor.WHITE + "/10");

                //Add green glass to signify unlocked
                if (f13Player.getCounselorProfile().equals(profile)) {
                    //They currently have this skin selected
                    glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4);
                    glassMeta = glass.getItemMeta();
                    glassMeta.setDisplayName(ChatColor.GOLD + "^ Selected");
                } else {
                    //This profile is available
                    glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
                    glassMeta = glass.getItemMeta();
                    glassMeta.setDisplayName(ChatColor.DARK_GREEN + "^ Unlocked");
                }
            } else {
                //This profile is locked
                jasonItemLore.add(HiddenStringsUtil.encodeString("{\"CounselorProfileSelect\": \"Locked\"}"));
                jasonItemLore.add(ChatColor.BOLD + "" + ChatColor.RED + "LOCKED");
                jasonItemLore.add("");
                jasonItemLore.add(ChatColor.WHITE + "Unlocks at level " + ChatColor.AQUA + profile.getRequiredLevel().getLevelNumber());

                //Set red glass to signify locked
                glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
                glassMeta = glass.getItemMeta();
                glassMeta.setDisplayName(ChatColor.RED + "^ Locked");
            }

            //Get skin preview for the skull
            //TODO CHANGE TO COUNSELOR PROFILE SKIN
            item = new SkullPreview(profile.getSkin(), profile.getDisplayName(), jasonItemLore);
            inventory.addItem(item);

            //Add glass to signify which skins are available
            List<String> glassLore = new ArrayList<String>();
            glassLore.add(HiddenStringsUtil.encodeString("{\"CounselorProfileSelect\": \"Locked\"}"));
            glassMeta.setLore(glassLore);
            glass.setItemMeta(glassMeta);
            inventory.setItem(profile.getOrder() + 8, glass);
        }


        //Open it for player
        player.openInventory(inventory);
    }

    /**
     * Adds the menu open item to the supplied players inventory
     *
     * @param player
     */
    public static void addMenuOpenItem(Player player) {
        List<String> menuItemLore = new ArrayList<String>();
        menuItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"CounselorProfiles\"}"));
        player.getInventory().setItem(0, new SkullPreview(F13Skin.COUNSELOR_BASE, ChatColor.DARK_GREEN + FridayThe13th.language.get(player, "game.menu.CounselorProfile", "Counselor Profiles"), menuItemLore));
    }
}
