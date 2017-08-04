package com.AustinPilz.FridayThe13th.Components.Arena;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.ArenaDoorAction;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Door;

public class ArenaDoor
{
    private int hits;
    private int requiredHits;
    private Block doorBlock;
    private Arena arena;

    public ArenaDoor(Block block, Arena arena)
    {
        hits = 0;
        requiredHits = arena.getGameManager().getPlayerManager().getJason().getF13Player().getJasonProfile().getRequiredDoorBreaks();
        doorBlock = block;
        this.arena = arena;
    }

    /**
     * Called whenever block break attempt is made by Jason
     */
    public void blockBreak() {
        hits++; //Register hit

        if (getNumHits() >= getNumHitsRequired())
        {
            BlockState state = doorBlock.getState();
            Door door = (Door) state.getData();

            Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new ArenaDoorAction(doorBlock, doorBlock.getRelative(BlockFace.DOWN), true), 1);

            if (door.isTopHalf())
            {
                //Top 1/2
                Block set = doorBlock.getRelative(BlockFace.DOWN);
                BlockState state2 = set.getState();
                Door setDoor = (Door) state2.getData();
                arena.getObjectManager().getBrokenDoors().add(set);

            }
            else
            {
                arena.getObjectManager().getBrokenDoors().add(doorBlock);
            }

            //Door is broken, add XP to Jason
            arena.getGameManager().getPlayerManager().getJason().getXPManager().addDoorBreak();
        }
    }

    public int getNumHits()
    {
        return hits;
    }

    public int getNumHitsRequired()
    {
        return requiredHits;
    }
}
