package com.AustinPilz.FridayThe13th.Components.Arena;

import com.AustinPilz.FridayThe13th.Components.Enum.XPAward;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.ArenaSwitchPowerAction;
import com.AustinPilz.FridayThe13th.Utilities.InventoryActions;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Lever;

import java.util.concurrent.ThreadLocalRandom;

public class ArenaSwitch
{
    private Block block;
    private Arena arena;
    private double repairAttempts;
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
    public void repairSwitchAttempt(F13Player player)
    {
        repairAttempts += player.getCounselorProfile().getIntelligence().getRegenerationRate();

        if (getRepairProgressPercent() >= 1)
        {
            //Fix the switch
            repairSwitch();
            arena.getObjectManager().getBrokenSwitches().remove(block);
            hologram.delete();

            //Remove redstone from players inventory
            InventoryActions.remove(player.getBukkitPlayer().getInventory(), Material.REDSTONE, 1, (short) -1);

            //Fire firework
            arena.getGameManager().getPlayerManager().fireFirework(player, Color.ORANGE);

            //Play Sound for Jason
            arena.getGameManager().getPlayerManager().getJason().getPlayer().playSound(player.getBukkitPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1F, 2F);

            //Register switch fix for XP
            arena.getGameManager().getPlayerManager().getCounselor(player).getXpManager().registerXPAward(XPAward.Counselor_SwitchRepaired);
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
     * @return Percentage of repair progress
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

        Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new ArenaSwitchPowerAction(block, !lever.isPowered()), ThreadLocalRandom.current().nextInt(2, 20 + 1));

    }
}
