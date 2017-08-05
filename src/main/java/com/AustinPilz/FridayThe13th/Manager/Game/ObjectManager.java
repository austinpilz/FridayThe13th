package com.AustinPilz.FridayThe13th.Manager.Game;


import com.AustinPilz.FridayThe13th.Components.Arena.*;
import com.AustinPilz.FridayThe13th.Components.Enum.TrapType;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.ArenaDoorAction;
import com.AustinPilz.FridayThe13th.Runnable.ArenaSwitchAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Lever;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ObjectManager
{
    private Arena arena;
    private HashSet<ArenaChest> weaponChests;
    private HashSet<ArenaChest> itemChests;
    private HashMap<Block, ArenaPhone> phones;

    //Globals
    private HashSet<BlockFace> windowBlockFaces;

    //Per Game Objects
    private HashMap<Block, ArenaDoor> doors;
    private HashSet<Block> brokenDoors;
    private HashMap<Block, ArenaSwitch> brokenSwitches;
    private HashSet<Block> brokenWindows;
    private HashMap<Block, Trap> traps;


    /**
     * @param arena Game object
     */
    public ObjectManager(Arena arena)
    {
        this.arena = arena;
        this.itemChests = new HashSet<>();
        this.weaponChests = new HashSet<>();
        this.phones = new HashMap<>();

        //Per Game Objects
        doors = new HashMap<>();
        brokenDoors = new HashSet<>();
        brokenSwitches = new HashMap<>();
        brokenWindows = new HashSet<>();
        traps = new HashMap<>();

        //Globals
        windowBlockFaces = new HashSet<>();
        generateWindowBlockFaces();
    }

    /**
     * Get the arena of this object manager
     * @return Game
     */
    public Arena getArena()
    {
        return this.arena;
    }

    /**
     * Populates internal list of block faces to check for broken windows
     */
    private void generateWindowBlockFaces()
    {
        windowBlockFaces.add(BlockFace.UP);
        windowBlockFaces.add(BlockFace.DOWN);
        windowBlockFaces.add(BlockFace.NORTH);
        windowBlockFaces.add(BlockFace.SOUTH);
        windowBlockFaces.add(BlockFace.EAST);
        windowBlockFaces.add(BlockFace.WEST);
        windowBlockFaces.add(BlockFace.SELF);
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

        //Restore traps
        removeTraps();
        traps.clear();
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
            weaponChests.add(chest);
        }
        else if (chest.isItemChest())
        {
            itemChests.add(chest);
        }
    }

    /**
     * Removes chest
     * @param chest
     */
    public void removeChest(ArenaChest chest)
    {
        weaponChests.remove(chest);
        itemChests.remove(chest);
    }

    /**
     * Returns the number of item chests
     * @return
     */
    public int getNumChestsItems()
    {
        return itemChests.size();
    }

    /**
     * Returns the number of weapon chests
     * @return
     */
    public int getNumChestsWeapon()
    {
        return weaponChests.size();
    }

    /**
     * Randomly regenerates chests
     */
    public void regenerateChests()
    {
        for (ArenaChest chest: weaponChests)
        {
            chest.randomlyGenerate();
        }

        for (ArenaChest chest: itemChests)
        {
            chest.randomlyGenerate();
        }

        //Place the walkie talkies
        placeRadios();
    }

    /**
     * Places walkies talkies randomly across the item chests
     */
    private void placeRadios()
    {
        //Teleport counselors to starting points
        ArenaChest[] chests = itemChests.toArray(new ArenaChest[itemChests.size()]);

        //Randomize starting points
        Random rnd = ThreadLocalRandom.current();
        for (int i = chests.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);

            // Simple swap
            ArenaChest a = chests[index];
            chests[index] = chests[i];
            chests[i] = a;
        }

        //We only place 1/2 as many radios as counselors - ensure we don't attempt to place more than we have chests
        double numnChestsToPlace = Math.min(Math.round((arena.getGameManager().getPlayerManager().getNumCounselors()+1)/2), chests.length);

        for (int i = 1; i <= numnChestsToPlace; i++)
        {
            chests[i].placeRadio();
        }
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

    /**
     * Returns all broken switches in current game
     * @return
     */
    public HashMap<Block, ArenaSwitch> getBrokenSwitches() { return brokenSwitches; }

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
        for (BlockFace blockface : windowBlockFaces)
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
    private void fixBrokenWindows()
    {
        for (Block b : brokenWindows)
        {
            b.setType(Material.THIN_GLASS);
        }
    }

    public HashMap<Block, Trap> getTraps() {
        return traps;
    }

    /**
     * Returns if the supplied block is a trap
     *
     * @param b
     * @return
     */
    public boolean isATrap(Block b) {
        return traps.containsKey(b);
    }

    public Trap getTrap(Block b) {
        return traps.get(b);
    }

    /**
     * Creates new Jason trap object and activates it
     *
     * @param b
     */
    public void placeJasonTrap(Block b, Material o) {
        traps.put(b, new Trap(arena, b, o, TrapType.Jason));
    }

    /**
     * Creates new Counselor trap object
     *
     * @param b
     */
    public void placeCounselorTrap(Block b, Material o) {
        traps.put(b, new Trap(arena, b, o, TrapType.Counselor));
    }

    /**
     * Removes all Jason traps from the arena
     */
    private void removeTraps() {
        Iterator it = traps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Trap trap = (Trap) entry.getValue();
            trap.remove();
        }
    }

}
