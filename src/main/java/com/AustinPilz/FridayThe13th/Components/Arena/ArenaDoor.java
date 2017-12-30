package com.AustinPilz.FridayThe13th.Components.Arena;

import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Game.SoundManager;
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
    private boolean broken;

    public ArenaDoor(Block block, Arena arena)
    {
        hits = 0;
        requiredHits = arena.getGameManager().getPlayerManager().getJason().getF13Player().getJasonProfile().getRequiredDoorBreaks();
        doorBlock = block;
        this.arena = arena;
        broken = false;
    }

    /**
     * Called whenever block break attempt is made by Jason
     */
    public void blockBreak() {
        hits++; //Register hit

        if (getNumHits() >= getNumHitsRequired() && !broken)
        {
            BlockState state = doorBlock.getState();
            Door door = (Door) state.getData();

            Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new ArenaDoorAction(doorBlock, doorBlock.getRelative(BlockFace.DOWN), true), 1);

            if (door.isTopHalf())
            {
                //Top 1/2
                arena.getObjectManager().getBrokenDoors().add(doorBlock.getRelative(BlockFace.DOWN));
            }
            else
            {
                arena.getObjectManager().getBrokenDoors().add(doorBlock);
            }

            //Door is broken, add XP to Jason
            arena.getGameManager().getPlayerManager().getJason().getXPManager().addDoorBreak();

            //Play broken sound
            SoundManager.playSoundForNearbyPlayers(F13SoundEffect.DoorBreak, arena, doorBlock.getLocation(), 5, false, true);

            broken = true;
        }
        else
        {
            //Play hitting sound
            SoundManager.playSoundForNearbyPlayers(F13SoundEffect.DoorHit, arena, doorBlock.getLocation(), 5, false, true);
        }
    }

    private int getNumHits()
    {
        return hits;
    }

    private int getNumHitsRequired()
    {
        return requiredHits;
    }
}
