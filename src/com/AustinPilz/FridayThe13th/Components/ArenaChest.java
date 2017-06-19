package com.AustinPilz.FridayThe13th.Components;


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

public class ArenaChest
{
    private Arena arena;
    private Location location;
    private ChestType chestType;

    public ArenaChest(Arena arena, Location location, ChestType chestType)
    {
        this.arena = arena;
        this.location = location;
        this.chestType = chestType;
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
        if (getChestType().equals(ChestType.Weapon))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns if the chest is an item chest
     * @return
     */
    public boolean isItemChest()
    {
        if (getChestType().equals(ChestType.Item))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns chest object
     * @return
     */
    public Chest getChest()
    {
        return (Chest)getLocation().getBlock().getState();
    }

    public void randomlyGenerate()
    {
        //Clear existing inventory
        getChest().getBlockInventory().clear();

        if (isWeaponChest())
        {
            //Diamond Sword (BLADE)
            double diamondSwordChance = Math.random() * 100;
            if ((diamondSwordChance -= 10) < 0) //10% of the time
            {
                ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
                ItemMeta metaData = item.getItemMeta();
                metaData.setDisplayName("Blade");
                item.setItemMeta(metaData);
                item.setDurability((short) 250);
                getChest().getBlockInventory().addItem(item);
            }

            //Iron axe (AXE)
            double ironAxeChance = Math.random() * 100;
            if ((ironAxeChance -= 10) < 0) //3% of the time
            {
                ItemStack item = new ItemStack(Material.IRON_AXE, 1);
                ItemMeta metaData = item.getItemMeta();
                metaData.setDisplayName("Heavy Axe");
                item.setItemMeta(metaData);
                item.setDurability((short) 60);
                getChest().getBlockInventory().addItem(item);
            }

            //Wooden axe (AXE)
            double woodAxeChance = Math.random() * 100;
            if ((woodAxeChance -= 50) < 0) //50% of the time
            {
                ItemStack item = new ItemStack(Material.WOOD_AXE, 1);
                ItemMeta metaData = item.getItemMeta();
                metaData.setDisplayName("Light Axe");
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
                metaData.setDisplayName("Gun");
                item.setItemMeta(metaData);
                getChest().getBlockInventory().addItem(item);

                getChest().getBlockInventory().addItem(new ItemStack(Material.ARROW, 1));
            }

            //Always put a stick (BRANCH)
            ItemStack stick = new ItemStack(Material.STICK, 1);
            ItemMeta metaData = stick.getItemMeta();
            metaData.setDisplayName("Brittle Branch");
            stick.setItemMeta(metaData);
            getChest().getBlockInventory().addItem(stick);
        }
        else if (isItemChest())
        {
            //Healing Potion
            double healPotionChance = Math.random() * 100;
            if ((healPotionChance -= 50) < 0) //50% of the time
            {
                ItemStack healthPotion = new ItemStack(Material.POTION, 1);
                PotionMeta meta = (PotionMeta) healthPotion.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
                meta.setDisplayName(ChatColor.GREEN + "Antiseptic Spray");
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
                metaData.setDisplayName("Repair Wire");
                item.setItemMeta(metaData);
                getChest().getBlockInventory().addItem(item);
            }
        }


    }
}
