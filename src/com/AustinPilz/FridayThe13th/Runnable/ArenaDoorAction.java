package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;
import org.bukkit.scheduler.BukkitRunnable;


public class ArenaDoorAction implements Runnable
{
    private Block door;
    private Block relative;
    private boolean action;

    public ArenaDoorAction(Block b, Block r, boolean open)
    {
        this.door = b;
        this.relative = r;
        this.action = open;
    }


    @Override
    public void run()
    {

        BlockState state = door.getState();
        Door door = (Door) state.getData();

        if (door.isTopHalf())
        {

            BlockState state2 = relative.getState();
            Door setDoor = (Door) state2.getData();

            if (action == true)
            {
                setDoor.setOpen(true);
            }
            else
            {
                setDoor.setOpen(false);
            }

            state2.update();
        }
        else
        {
            if (action == true)
            {
                door.setOpen(true);
            }
            else
            {
                door.setOpen(false);
            }

            state.update();
        }
    }

}

