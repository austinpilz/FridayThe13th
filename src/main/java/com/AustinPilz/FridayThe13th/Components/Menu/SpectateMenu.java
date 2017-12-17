package com.AustinPilz.FridayThe13th.Components.Menu;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
import com.AustinPilz.FridayThe13th.Components.Profiles.CounselorProfile;
import com.AustinPilz.FridayThe13th.Components.Skin.SkullPreview;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SpectateMenu {
    /**
     * Opens the spectate menu for the supplied player
     *
     * @param player
     * @param arena
     */
    public static void openMenu(Player player, Arena arena) {
        Inventory inventory = Bukkit.createInventory(null, 9, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.SpectateSelectionItem", "Spectate Selection"));

        int i = 0;
        Iterator it = arena.getGameManager().getPlayerManager().getCounselors().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Counselor counselor = (Counselor) entry.getValue();

            if (i <= 9 && arena.getGameManager().getPlayerManager().isAlive(counselor.getPlayer())) {
                ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwner(counselor.getPlayer().getName());
                meta.setDisplayName(counselor.getPlayer().getName());
                List<String> menuItemLore = new ArrayList<String>();
                menuItemLore.add(HiddenStringsUtil.encodeString("{\"SpectateTeleport\": \"" + counselor.getPlayer().getUniqueId().toString() + "\"}"));
                meta.setLore(menuItemLore);
                item.setItemMeta(meta);

                inventory.setItem(i++, item);
            }
        }

        player.openInventory(inventory);
    }

    /**
     * Adds the spectate menu item to the players inventory
     *
     * @param player
     */
    public static void addMenuOpenItem(Player player) {

        List<String> menuItemLore = new ArrayList<String>();
        menuItemLore.add(HiddenStringsUtil.encodeString("{\"Menu\": \"Spectate\"}"));
        ItemStack item = new SkullPreview(CounselorProfile.getRandomCounselorProfile().getSkin(), ChatColor.GREEN + FridayThe13th.language.get(player, "game.title.spectateMenu", "Select Player to Spectate"), menuItemLore);
        player.getInventory().setItem(0, item);
    }
}
