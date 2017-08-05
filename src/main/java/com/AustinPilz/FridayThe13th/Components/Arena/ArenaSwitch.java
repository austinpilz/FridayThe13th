package com.AustinPilz.FridayThe13th.Components.Arena;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.ArenaSwitchAction;
import com.AustinPilz.FridayThe13th.Utilities.InventoryActions;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

public class ArenaSwitch
{
    private Block block;
    private Arena arena;
    private int repairAttempts;
    private int repairAttemptsRequired;
    private Hologram hologram;

    public ArenaSwitch(Block block, Arena arena)
    {
        this.block = block;
        this.arena = arena;
        repairAttempts = 0;
        repairAttemptsRequired = 75;

        //Add hologram
        hologram = HologramsAPI.createHologram(FridayThe13th.instance, block.getRelative(BlockFace.UP).getLocation());
        hologram.getVisibilityManager().hideTo(arena.getGameManager().getPlayerManager().getJason().getPlayer());
        hologram.appendTextLine(ChatColor.WHITE + "Repair: 0%");
    }

    /**
     * Attempt to repair the switch
     */
    public void repairSwitchAttempt(Player player)
    {
        repairAttempts++;

        if (getRepairProgressPercent() == 1)
        {
            //Fix the switch
            repairSwitch();
            arena.getObjectManager().getBrokenSwitches().remove(block);
            hologram.delete();

            //Remove redstone from players inventory
            InventoryActions.remove(player.getPlayer().getInventory(), Material.REDSTONE, 1, (short) -1);

            //Fire firework
            arena.getGameManager().getPlayerManager().fireFirework(player, Color.ORANGE);

            //Play Sound for Jason
            arena.getGameManager().getPlayerManager().getJason().getPlayer().playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1F, 2F);

            //Register switch fix for XP
            arena.getGameManager().getPlayerManager().getCounselor(player).getXPManager().addSwitchFix();
        }
        else
        {
            hologram.getLine(0).removeLine();

            float percentage = ((float) repairAttempts) / repairAttemptsRequired * 100;
            percentage = Math.round(percentage);
            String strPercent = String.format("%2.0f", percentage);

            String string = "Repair: " + String.valueOf(strPercent) + "%";
            hologram.insertTextLine(0, ChatColor.WHITE + string);
        }
    }

    /**
     * Returns the percentage of repair progress
     * @return
     */
    private double getRepairProgressPercent()
    {
        return repairAttempts/repairAttemptsRequired;
    }

    /**
     * Returns switch to original working condition
     */
    public void repairSwitch()
    {
        //Switch Fixed
        BlockState state = block.getState();
        Lever lever = (Lever)state.getData();

        if (lever.isPowered())
        {
            Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new ArenaSwitchAction(block, false), 1);
        }
        else
        {
            Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new ArenaSwitchAction(block, true), 1);
        }
    }
}
