package com.AustinPilz.FridayThe13th.Components.Arena;


import com.AustinPilz.FridayThe13th.Components.Enum.ChestType;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class ArenaChest
{
    private Arena arena;
    private Location location;
    private ChestType chestType;
    private boolean filled;

    public ArenaChest(Arena arena, Location location, ChestType chestType)
    {
        this.arena = arena;
        this.location = location;
        this.chestType = chestType;
        this.filled = false;
    }

    /**
     * Returns the chest's location
     * @return
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Returns the chest's arena
     * @return
     */
    public Arena getArena()
    {
        return arena;
    }

    /**
     * Returns the chest's type
     * @return
     */
    public ChestType getChestType()
    {
        return chestType;
    }

    /**
     * Returns if the chest is a weapon chest
     * @return
     */
    public boolean isWeaponChest()
    {
        return getChestType().equals(ChestType.Weapon);
    }

    /**
     * Returns if the chest is an item chest
     * @return
     */
    public boolean isItemChest()
    {
        return getChestType().equals(ChestType.Item);
    }

    /**
     * Returns chest object
     * @return
     */
    public Chest getChest()
    {
        return (Chest)getLocation().getBlock().getState();
    }

    /**
     * Randomly regenerates the chest's inventory based on its type
     */
    public void randomlyFill()
    {
        if (getLocation().getBlock().getType().equals(Material.CHEST))
        {
            if (!filled)
            {
                //The chest has not yet been filled this game, mark it as so and fill it.
                filled = true;

                if (isWeaponChest()) {
                    //Iron Sword (BLADE)
                    double ironSwordChance = Math.random() * 100;
                    if ((ironSwordChance -= 10) < 0) //10% of the time
                    {
                        ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
                        ItemMeta metaData = item.getItemMeta();
                        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.Blade", "Blade"));
                        item.setItemMeta(metaData);
                        item.setDurability((short) 250);
                        getChest().getBlockInventory().addItem(item);
                    }

                    //Iron axe (AXE)
                    double ironAxeChance = Math.random() * 100;
                    if ((ironAxeChance -= 1) < 0) //3% of the time
                    {
                        ItemStack item = new ItemStack(Material.IRON_AXE, 1);
                        ItemMeta metaData = item.getItemMeta();
                        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.HeavyAxe", "Heavy Axe"));
                        item.setItemMeta(metaData);
                        item.setDurability((short) 20);
                        getChest().getBlockInventory().addItem(item);
                    }

                    //Wooden axe (AXE)
                    double woodAxeChance = Math.random() * 100;
                    if ((woodAxeChance -= 50) < 0) //50% of the time
                    {
                        ItemStack item = new ItemStack(Material.WOOD_AXE, 1);
                        ItemMeta metaData = item.getItemMeta();
                        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.LightAxe", "Light Axe"));
                        item.setDurability((short) 60);
                        item.setItemMeta(metaData);
                        getChest().getBlockInventory().addItem(item);
                    }

                    //Bow & Arrow (GUN)
                    double bowChance = Math.random() * 100;
                    if ((bowChance -= 10) < 0) //3% of the time
                    {
                        ItemStack item = new ItemStack(Material.BOW, 1);
                        ItemMeta metaData = item.getItemMeta();
                        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.Gun", "Gun"));
                        item.setItemMeta(metaData);
                        getChest().getBlockInventory().addItem(item);

                        getChest().getBlockInventory().addItem(new ItemStack(Material.ARROW, 1));
                    }

                    //Always put a stick (BRANCH)
                    ItemStack stick = new ItemStack(Material.STICK, 1);
                    ItemMeta metaData = stick.getItemMeta();
                    metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.BrittleBranch", "Brittle Branch"));
                    stick.setItemMeta(metaData);
                    getChest().getBlockInventory().addItem(stick);
                } else if (isItemChest()) {
                    //Healing Potion
                    double healPotionChance = Math.random() * 100;
                    if ((healPotionChance -= 50) < 0) //50% of the time
                    {
                        ItemStack healthPotion = new ItemStack(Material.POTION, 1);
                        PotionMeta meta = (PotionMeta) healthPotion.getItemMeta();
                        meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
                        meta.setDisplayName(ChatColor.GREEN + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.Antiseptic", "Antiseptic Spray"));
                        healthPotion.setItemMeta(meta);
                        getChest().getBlockInventory().addItem(healthPotion);
                    }

                    //Slow Potion
                    double slowPotionChance = Math.random() * 100;
                    if ((slowPotionChance -= 10) < 0) //10% of the time
                    {
                        ItemStack healthPotion = new ItemStack(Material.SPLASH_POTION, 1);
                        PotionMeta meta = (PotionMeta) healthPotion.getItemMeta();
                        meta.setBasePotionData(new PotionData(PotionType.SLOWNESS));
                        healthPotion.setItemMeta(meta);
                        getChest().getBlockInventory().addItem(healthPotion);
                    }

                    //Redstone
                    double redstoneChance = Math.random() * 100;
                    if ((redstoneChance -= 40) < 0) //40% of the time
                    {
                        ItemStack item = new ItemStack(Material.REDSTONE, 1);
                        ItemMeta metaData = item.getItemMeta();
                        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.RepairWire", "Repair Wire"));
                        item.setItemMeta(metaData);
                        getChest().getBlockInventory().addItem(item);
                    }

                    //Map
                    double mapChance = Math.random() * 100;
                    if ((mapChance -= 20) < 0) {
                        ItemStack item = new ItemStack(Material.EMPTY_MAP, 1);
                        ItemMeta metaData = item.getItemMeta();
                        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.Map", "Camp Map"));
                        item.setItemMeta(metaData);
                        getChest().getBlockInventory().addItem(item);
                    }

                    //Trap
                    double trapChance = Math.random() * 100;
                    if ((trapChance -= 20) < 0) {
                        ItemStack counselorTrap = new ItemStack(Material.CARPET, 1, (byte) 8);
                        ItemMeta counselorTrapMeta = counselorTrap.getItemMeta();
                        counselorTrapMeta.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.counselorTraps", "Trap"));
                        List<String> counselorTrapLore = new ArrayList<>();
                        counselorTrapLore.add(HiddenStringsUtil.encodeString("{\"PlaceItem\": \"CounselorTrap\"}"));
                        counselorTrapMeta.setLore(counselorTrapLore);
                        counselorTrap.setItemMeta(counselorTrapMeta);

                        getChest().getBlockInventory().addItem(counselorTrap);
                    }
                }
            }
        }
        else
        {
            //It's no longer a chest
            FridayThe13th.inputOutput.deleteChest(getLocation().getBlockX(), getLocation().getBlockY(), getLocation().getBlockZ(), getLocation().getWorld().getName());
        }
    }

    /**
     * Places a radio in the chest
     */
    public void placeRadio() {
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.Radio", "Radio"));
        item.setItemMeta(metaData);
        getChest().getBlockInventory().addItem(item);
    }

    /**
     * Places the phone fuse in the chest
     */
    public void placePhoneFuse() {
        ItemStack item = new ItemStack(Material.BONE, 1);
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.PhoneFuse", "Phone Fuse"));
        item.setItemMeta(metaData);
        getChest().getBlockInventory().addItem(item);
    }

    /**
     * Clears the inventory of the chest
     */
    public void clear()
    {
        if (getLocation().getBlock().getType().equals(Material.CHEST))
        {
            getChest().getBlockInventory().clear();
            filled = false; //reset per-game filled indicator
        }
        else
        {
            //It's no longer a chest
            FridayThe13th.inputOutput.deleteChest(getLocation().getBlockX(), getLocation().getBlockY(), getLocation().getBlockZ(), getLocation().getWorld().getName());
        }
    }
}
