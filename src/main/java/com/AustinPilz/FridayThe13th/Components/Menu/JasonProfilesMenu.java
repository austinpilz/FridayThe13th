package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Enum.F13Skin;
import com.AustinPilz.FridayThe13th.Components.Enum.JasonProfile;
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

public class JasonProfilesMenu {
    /**
     * Opens the menu for the supplied player
     *
     * @param player
     */
    public static void openMenu(Player player) {

        F13Player f13Player = FridayThe13th.playerController.getPlayer(player);

        Inventory inventory;

        inventory = Bukkit.createInventory(null, 18, ChatColor.RED + "" + ChatColor.BOLD + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.title.JasonProfileMenu", "Jason Profiles"));

        //Display all profiles
        for (JasonProfile profile : JasonProfile.values()) {
            ItemStack item;
            ItemMeta itemMeta;

            ItemStack glass;
            ItemMeta glassMeta = null;

            List<String> jasonItemLore = new ArrayList<String>();
            if (f13Player.getLevel().equals(profile.getRequiredLevel()) || f13Player.getLevel().isGreaterThan(profile.getRequiredLevel())) {
                //The player has access to the profile
                jasonItemLore.add(HiddenStringsUtil.encodeString("{\"JasonProfileSelect\": \"" + profile.getDisplayName() + "\"}"));
                jasonItemLore.add(ChatColor.DARK_GREEN + "Unlocked");
                jasonItemLore.add("");
                jasonItemLore.add(ChatColor.WHITE + "Stalk " + ChatColor.GREEN + profile.getStalkLevel().getLevelName());
                jasonItemLore.add(ChatColor.WHITE + "Sense " + ChatColor.RED + profile.getSenseLevel().getLevelName());
                jasonItemLore.add(ChatColor.WHITE + "Warp " + ChatColor.DARK_PURPLE + profile.getWarpLevel().getLevelName());
                jasonItemLore.add(ChatColor.WHITE + "Door Breaks: " + ChatColor.AQUA + profile.getRequiredDoorBreaks());

                //Add green glass to signify unlocked
                if (f13Player.getJasonProfile().equals(profile)) {
                    //They currently have this skin selected
                    glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4);
                    glassMeta = glass.getItemMeta();
                    glassMeta.setDisplayName(ChatColor.GOLD + "^ Selected");
                } else {
                    //This skin is available
                    glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
                    glassMeta = glass.getItemMeta();
                    glassMeta.setDisplayName(ChatColor.DARK_GREEN + "^ Unlocked");
                }
            } else {
                //This Jason is locked
                jasonItemLore.add(HiddenStringsUtil.encodeString("{\"JasonProfileSelect\": \"Locked\"}"));
                jasonItemLore.add(ChatColor.BOLD + "" + ChatColor.RED + "LOCKED");
                jasonItemLore.add("");
                jasonItemLore.add(ChatColor.WHITE + "Unlocks at level " + ChatColor.AQUA + profile.getRequiredLevel().getLevelNumber());

                //Set red glass to signify locked
                glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
                glassMeta = glass.getItemMeta();
                glassMeta.setDisplayName(ChatColor.RED + "^ Locked");
            }

            item = new SkullPreview(profile.getSkin(), profile.getDisplayName(), jasonItemLore);
            inventory.addItem(item);

            //Add glass to signify which skins are available
            List<String> glassLore = new ArrayList<String>();
            glassLore.add(HiddenStringsUtil.encodeString("{\"JasonProfileSelect\": \"Locked\"}"));
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
        menuItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"JasonProfiles\"}"));
        player.getInventory().setItem(8, new SkullPreview(F13Skin.JASON_Part1, ChatColor.RED + FridayThe13th.language.get(player, "game.menu.JasonProfile", "Jason Profiles"), menuItemLore));
    }
}
