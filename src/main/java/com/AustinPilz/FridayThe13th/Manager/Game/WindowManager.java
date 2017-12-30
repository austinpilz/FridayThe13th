package com.AustinPilz.FridayThe13th.Manager.Game;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.HashSet;

public class WindowManager {

    private Arena arena;
    private HashSet<BlockFace> windowBlockFaces;
    private HashSet<Block> brokenWindows;

    public WindowManager(Arena arena) {
        this.arena = arena;
        windowBlockFaces = new HashSet<>();
        brokenWindows = new HashSet<>();
        generateWindowBlockFaces();
    }

    /**
     * Populates internal list of block faces to check for broken windows
     */
    private void generateWindowBlockFaces() {
        windowBlockFaces.add(BlockFace.UP);
        windowBlockFaces.add(BlockFace.DOWN);
        windowBlockFaces.add(BlockFace.NORTH);
        windowBlockFaces.add(BlockFace.SOUTH);
        windowBlockFaces.add(BlockFace.EAST);
        windowBlockFaces.add(BlockFace.WEST);
        windowBlockFaces.add(BlockFace.SELF);
    }

    /**
     * Breaks window
     *
     * @param block
     */
    public void breakWindow(Block block) {
        //Break the window
        brokenWindows.add(block);
        block.setType(Material.IRON_FENCE);

        //Check to see if it's attached to any other glass panes, break them too
        for (BlockFace blockface : windowBlockFaces) {
            if (block.getRelative(blockface).getType().equals(Material.THIN_GLASS)) {
                breakWindow(block.getRelative(blockface));
            }
        }
    }

    /**
     * Repairs all broken windows
     */
    protected void fixBrokenWindows() {
        for (Block b : brokenWindows) {
            b.setType(Material.THIN_GLASS);
        }

        brokenWindows.clear();
    }
}
