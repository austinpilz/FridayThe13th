package com.AustinPilz.FridayThe13th.Factory;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class F13ItemFactory {

    /**
     * Create the phone fuse
     *
     * @return Phone fuse ItemStack
     */
    public static ItemStack getPhoneFuse() {
        ItemStack item = new ItemStack(Material.END_ROD, 1);
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.PhoneFuse", "Phone Fuse"));
        item.setItemMeta(metaData);
        return item;
    }

    /**
     * Create a radio
     *
     * @return Radio ItemStack
     */
    public static ItemStack getRadio() {
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.Radio", "Radio"));
        item.setItemMeta(metaData);
        return item;
    }

    /**
     * Create vehicle gas
     *
     * @return Vehicle gas
     */
    public static ItemStack getVehicleGas() {
        ItemStack item = new ItemStack(Material.BLAZE_POWDER, 1);
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.VehicleGas", "Vehicle Gas"));
        item.setItemMeta(metaData);
        return item;
    }

    /**
     * Create vehicle keys
     *
     * @return Vehicle keys
     */
    public static ItemStack getVehicleKeys() {
        ItemStack item = new ItemStack(Material.STONE_BUTTON, 1);
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.VehicleKeys", "Vehicle Key"));
        item.setItemMeta(metaData);
        return item;
    }

    /**
     * Create vehicle battery
     *
     * @return Vehicle battery
     */
    public static ItemStack getVehicleBattery() {
        ItemStack item = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.VehicleBattery", "Vehicle Battery"));
        item.setItemMeta(metaData);
        return item;
    }

    /**
     * Create vehicle propeller
     *
     * @return Vehicle propeller
     */
    public static ItemStack getVehiclePropeller() {
        ItemStack item = new ItemStack(Material.SHULKER_SHELL, 1);
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.VehiclePropeller", "Vehicle Propeller"));
        item.setItemMeta(metaData);
        return item;
    }

    /**
     * Create repair wire
     *
     * @return Repair Wire ItemStack
     */
    public static ItemStack getRepairWire() {
        ItemStack item = new ItemStack(Material.REDSTONE, 1);
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.item.RepairWire", "Repair Wire"));
        item.setItemMeta(metaData);
        return item;
    }

}
