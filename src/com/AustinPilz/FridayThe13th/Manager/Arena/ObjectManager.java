package com.AustinPilz.FridayThe13th.Manager.Arena;


import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Components.ArenaChest;
import com.AustinPilz.FridayThe13th.Components.ArenaDoor;
import com.AustinPilz.FridayThe13th.Components.ArenaSwitch;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.ArenaDoorAction;
import com.AustinPilz.FridayThe13th.Runnable.ArenaSwitchAction;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class ObjectManager
{
    private Arena arena;
    private HashSet<ArenaChest> chestsWeapons;
    private HashSet<ArenaChest> chestsItems;

    //Per Game Objects
    private HashMap<Block, ArenaDoor> doors;
    private HashSet<Block> brokenDoors;
    private HashMap<Block, ArenaSwitch> brokenSwitches;
    private HashSet<Block> brokenWindows;

    /**
     * @param arena Arena object
     */
    public ObjectManager(Arena arena)
    {
        this.arena = arena;
        this.chestsItems = new HashSet<>();
        this.chestsWeapons = new HashSet<>();

        //Per Game Objects
        doors = new HashMap<>();
        brokenDoors = new HashSet<>();
        brokenSwitches = new HashMap<>();
        brokenWindows = new HashSet<>();
    }

    /**
     * Get the arena of this object manager
     * @return Arena
     */
    public Arena getArena()
    {
        return this.arena;
    }

    /**
     * Adds a new chest
     * @param chest
     */
    public void addChest(ArenaChest chest)
    {
        if (chest.isWeaponChest())
        {
            chestsWeapons.add(chest);
        }
        else if (chest.isItemChest())
        {
            chestsItems.add(chest);
        }
    }

    /**
     * Randomly regenerates chests
     */
    public void regenerateChests()
    {
        //Items: Healing Potion (Antiseptic), Redstone (for electrical repairs),
        //Weapons: Axes, Swords, Sticks (low durability)

        for (ArenaChest chest: chestsWeapons)
        {
            chest.randomlyGenerate();
        }

        for (ArenaChest chest: chestsItems)
        {
            chest.randomlyGenerate();
        }
    }


    /**
     * Restores all per game objects back to their default state
     */
    public void restorePerGameObjects()
    {
        //Restore doors to closed state
        fixBrokenDoors();
        brokenDoors.clear();

        //Restore switches
        fixBrokenSwitches();
        brokenSwitches.clear();

        //Restore windows?

    }

    /**
     * Returns all doors that are "broken" open
     * @return
     */
    public HashSet<Block> getBrokenDoors()
    {
        return brokenDoors;
    }

    /**
     * Returns all switches that are "broken" off
     * @return
     */

    public ArenaDoor getArenaDoor(Block block)
    {
        if (doors.containsKey(block))
        {
            return doors.get(block);
        }
        else
        {
            ArenaDoor newDoor = new ArenaDoor(block, arena);
            doors.put(block, newDoor);
            return newDoor;
        }
    }

    /**
     * Restores broken doors to closed
     */
    private void fixBrokenDoors()
    {
        for (Block block: brokenDoors)
        {
            Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new ArenaDoorAction(block, block.getRelative(BlockFace.DOWN), false), 1);
        }
    }

    /* SWITCHES */
    public HashMap<Block, ArenaSwitch> getBrokenSwitches() { return brokenSwitches; }
    /**
     * Gets arena door, adds and returns new one if didn't exist already
     * @param block
     * @return
     */

    /**
     * Breaks supplied switch
     * @param block
     */
    public void breakSwitch(Block block)
    {
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

        //Add to hash set
        brokenSwitches.put(block, new ArenaSwitch(block, arena));
    }

    /**
     * Restores broken switches to ON
     */
    private void fixBrokenSwitches()
    {
        Iterator it = getBrokenSwitches().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            ArenaSwitch arenaSwitch = (ArenaSwitch) entry.getValue();
            arenaSwitch.repairSwitch();
            it.remove();
        }
    }
}
