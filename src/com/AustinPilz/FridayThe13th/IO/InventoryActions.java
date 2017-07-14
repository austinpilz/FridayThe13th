package com.AustinPilz.FridayThe13th.IO;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryActions
{
    /**
     * Removes a item from a inventory
     *
     * @param inventory The inventory to remove from.
     * @param mat      The material to remove .
     * @param amount    The amount to remove.
     * @param damage    The data value or -1 if this does not matter.
     * @return If the inventory has not enough items, this will return the amount of items which were not removed.
     */
    public static int remove(Inventory inventory, Material mat, int amount, short damage)
    {
        ItemStack[] contents = inventory.getContents();
        int removed = 0;
        for (int i = 0; i < contents.length; i++)
        {
            ItemStack item = contents[i];

            if (item == null || !item.getType().equals(mat)) {
                continue;
            }

            if (damage != (short) -1 && item.getDurability() != damage) {
                continue;
            }

            int remove = item.getAmount() - amount - removed;

            if (item.getAmount() <= amount)
            {
                inventory.remove(mat);
            }

            if (removed > 0) {
                removed = 0;
            }

            if (remove <= 0) {
                removed += Math.abs(remove);
                contents[i] = null;
            } else {
                item.setAmount(remove);
            }
        }
        return removed;
    }


    /**
     * Checks weather the inventory contains a item or not.
     *
     * @param inventory The inventory to check..
     * @param mat      The material to check .
     * @param amount    The amount to check.
     * @param damage    The data value or -1 if this does not matter.
     * @return The amount of items the player has not. If this return 0 then the check was successfull.
     */
    public static int contains(Inventory inventory, Material mat, int amount, short damage)
    {
        ItemStack[] contents = inventory.getContents();
        int searchAmount = 0;
        for (ItemStack item : contents) {

            if (item == null || !item.getType().equals(mat)) {
                continue;
            }

            if (damage != -1 && item.getDurability() == damage) {
                continue;
            }

            searchAmount += item.getAmount();
        }
        return searchAmount - amount;
    }

}