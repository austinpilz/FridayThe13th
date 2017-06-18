package com.AustinPilz.FridayThe13th.Components;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Jason
{
    //Minecraft Objects
    private Player player;

    //Game
    private Arena arena;


    public Jason(Player p, Arena a)
    {
        arena = a;
        player = p;
    }

    /**
     * Return' the player object of the counselor
     */
    public Player getPlayer()
    {
        return this.player;
    }

    /**
     * Performs all necessary tasks when the game begins
     */
    public void prepapreForGameplay()
    {
        //Give jason his sword
        ItemStack sword = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta metaData = sword.getItemMeta();
        metaData.setUnbreakable(true);
        metaData.setDisplayName(ChatColor.RED + "Jason's Sword");
        sword.setItemMeta(metaData);

        //Give him a bow
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta bowMetaData = sword.getItemMeta();
        bowMetaData.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
        bowMetaData.setDisplayName(ChatColor.RED + "Jason's Bow");
        bow.setItemMeta(bowMetaData);

        getPlayer().getInventory().addItem(sword);
        getPlayer().getInventory().addItem(bow);
        getPlayer().getInventory().addItem(new ItemStack(Material.ARROW, 2));

        //He walks a little slower
        getPlayer().setWalkSpeed(0.12f);
    }
}
