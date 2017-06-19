package com.AustinPilz.FridayThe13th.Runnable;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Lever;

public class ArenaSwitchAction implements Runnable
{
    private Block block;
    private boolean powered;

    public ArenaSwitchAction(Block b, boolean p)
    {
        block = b;
        powered = p;
    }

    @Override
    public void run()
    {
        BlockState state = block.getState();
        Lever lever = (Lever) state.getData();
        lever.setPowered(powered);
        state.setData(lever);
        state.update();
    }
}
