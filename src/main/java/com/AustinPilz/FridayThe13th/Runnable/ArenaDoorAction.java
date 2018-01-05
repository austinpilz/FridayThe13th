package com.AustinPilz.FridayThe13th.Runnable;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Door;


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
            //Door is the top half, so let's get the bottom door
            state = relative.getState();
            door = (Door) state.getData();
        }

        if (action)
        {
            door.setOpen(true);
        } else {
            door.setOpen(false);
        }

        state.update();
    }

}

