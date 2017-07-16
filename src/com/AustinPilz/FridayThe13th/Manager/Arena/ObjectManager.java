package com.AustinPilz.FridayThe13th.Manager.Arena;


import com.AustinPilz.FridayThe13th.Components.*;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.ArenaDoorAction;
import com.AustinPilz.FridayThe13th.Runnable.ArenaSwitchAction;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ObjectManager
{
    private Arena arena;
    private HashSet<ArenaChest> chestsWeapons;
    private HashSet<ArenaChest> chestsItems;
    private HashMap<Block, ArenaPhone> phones;

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
        this.phones = new HashMap<>();

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
     * Adds phone
     * @param phone
     */
    public void addPhone(ArenaPhone phone)
    {
        phones.put(phone.getLocation().getBlock(), phone);
    }

    /**
     * Removes phone
     * @param phone
     */
    public void removePhone(ArenaPhone phone)
    {
        phones.remove(phone.getLocation().getBlock());
    }

    /**
     * Returns all phones
     * @return
     */
    public HashMap<Block, ArenaPhone> getPhones()
    {
        return phones;
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
     * Removes chest
     * @param chest
     */
    public void removeChest(ArenaChest chest)
    {
        chestsWeapons.remove(chest);
        chestsItems.remove(chest);
    }

    /**
     * Returns the number of item chests
     * @return
     */
    public int getNumChestsItems()
    {
        return chestsItems.size();
    }

    /**
     * Returns the number of weapon chests
     * @return
     */
    public int getNumChestsWeapon()
    {
        return chestsWeapons.size();
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

        //Phones
        hideAllPhones();

        //Restore windows
        fixBrokenWindows();
        brokenWindows.clear();

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

    /**
     * Hides all phones
     */
    private void hideAllPhones()
    {
        Iterator it = phones.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            ArenaPhone phone = (ArenaPhone) entry.getValue();
            phone.hidePhone();
        }
    }

    /**
     * Activates one random phone in the arena
     */
    public void displayRandomPhone()
    {
        if (phones.size() > 0)
        {
            ArenaPhone[] pl = phones.values().toArray(new ArenaPhone[phones.size()]);

            //Randomize starting points
            Random rnd = ThreadLocalRandom.current();
            for (int i = pl.length - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);

                // Simple swap
                ArenaPhone a = pl[index];
                pl[index] = pl[i];
                pl[i] = a;
            }

            pl[0].showPhone();
        }

    }

    /**
     * Breaks window
     * @param block
     */
    public void breakWindow(Block block)
    {
        //Break the window
        brokenWindows.add(block);
        block.setType(Material.IRON_FENCE);

        //Check to see if it's attached to any other glass panes, break them too
        for (BlockFace blockface : BlockFace.values())
        {
            if (block.getRelative(blockface).getType().equals(Material.THIN_GLASS))
            {
                breakWindow(block.getRelative(blockface));
            }
        }
    }

    /**
     * Repairs all broken windows
     */
    public void fixBrokenWindows()
    {
        for (Block b : brokenWindows)
        {
            b.setType(Material.THIN_GLASS);
        }
    }

}
