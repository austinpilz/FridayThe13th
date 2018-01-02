package com.AustinPilz.FridayThe13th.Manager.Game;


import com.AustinPilz.FridayThe13th.Components.Arena.*;
import com.AustinPilz.FridayThe13th.Components.Enum.TrapType;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.ArenaDoorAction;
import com.AustinPilz.FridayThe13th.Runnable.ArenaSwitchAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;
import org.golde.bukkit.corpsereborn.CorpseAPI.CorpseAPI;
import org.golde.bukkit.corpsereborn.nms.Corpses;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ObjectManager
{
    private Arena arena;
    private WindowManager windowManager;
    private PhoneManager phoneManager;

    private HashSet<ArenaChest> weaponChests;
    private HashSet<ArenaChest> itemChests;


    //Per Game Objects
    private HashMap<Block, ArenaDoor> doors;
    private HashSet<Block> brokenDoors;
    private HashMap<Block, ArenaSwitch> brokenSwitches;
    private HashMap<Block, Trap> traps;
    private HashSet<Corpses.CorpseData> corpses;


    /**
     * @param arena Game object
     */
    public ObjectManager(Arena arena)
    {
        this.arena = arena;
        this.windowManager = new WindowManager(arena);
        this.phoneManager = new PhoneManager(arena);
        this.itemChests = new HashSet<>();
        this.weaponChests = new HashSet<>();

        //Per Game Objects
        doors = new HashMap<>();
        brokenDoors = new HashSet<>();
        brokenSwitches = new HashMap<>();
        traps = new HashMap<>();
        corpses = new HashSet<>();
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
     * Restores all per game objects back to their default state
     */
    public void restorePerGameObjects()
    {
        //Restore doors to closed state
        fixBrokenDoors();
        brokenDoors.clear();
        doors.clear();

        //Restore switches
        fixBrokenSwitches();
        brokenSwitches.clear();

        //Phones
        getPhoneManager().hideAllPhones();

        //Restore windows
        getWindowManager().fixBrokenWindows();

        //Restore traps
        removeTraps();
        traps.clear();

        //Remove corpses
        cleanupCorpses();
        corpses.clear();

        //Empties all chests
        clearChests();
    }

    /**
     * Returns the window manager
     * @return Window Manager
     */
    public WindowManager getWindowManager() {
        return windowManager;
    }

    /**
     * Returns the phone manager
     * @return Phone Manager
     */
    public PhoneManager getPhoneManager() {
        return phoneManager;
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
     * Empties all chests
     */
    private void clearChests()
    {
        for (ArenaChest chest: weaponChests)
        {
            chest.clear();
        }

        for (ArenaChest chest: itemChests)
        {
            chest.clear();
        }
    }

    /**
     * Returns if the location is one of the arena chests s
     * @param location
     * @return
     */
    public boolean isLocationAChest(Location location)
    {
        ArrayList<ArenaChest> chests = new ArrayList<>();
        chests.addAll(weaponChests);
        chests.addAll(itemChests);

        for (ArenaChest chest : chests)
        {
            if (chest.getLocation().equals(location))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the arena chest based on the supplied location
     * @param location
     * @return
     */
    public ArenaChest getChest(Location location)
    {
        ArrayList<ArenaChest> chests = new ArrayList<>();
        chests.addAll(weaponChests);
        chests.addAll(itemChests);

        for (ArenaChest chest : chests)
        {
            if (chest.getLocation().equals(location))
            {
                return chest;
            }
        }

        return null;
    }

    /**
     * Places per item games randomly
     */
    protected void placePerItemGames() {
        //RADIOS
        ArenaChest[] radioChests = getRandomizedItemChests();
        double numChestsToPlaceForRadios = Math.min(Math.round((arena.getGameManager().getPlayerManager().getNumCounselors() + 1) / 2), radioChests.length);
        int i;
        for (i = 1; i <= numChestsToPlaceForRadios; i++) {
            radioChests[i].placeRadio();
        }

        //Place the phone fuse
        ArenaChest[] fuseChests = getRandomizedItemChests();
        if (fuseChests.length > 0) {
            fuseChests[0].placePhoneFuse();
        }

    }

    /**
     * @return Ranomized item chest array
     */
    private ArenaChest[] getRandomizedItemChests()
    {
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

        return chests;
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
     * Returns arena door
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
        int offset = 0;
        for (Block block: brokenDoors)
        {
            Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new ArenaDoorAction(block, block.getRelative(BlockFace.DOWN), false), 1 + offset++);
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

        Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new ArenaSwitchAction(block, !lever.isPowered()), 2);

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

    /**
     * Gets all corpses
     * @return
     */
    public HashSet<Corpses.CorpseData> getCorpses() {
        return corpses;
    }

    /**
     * Spawns a corpse where the player died
     * @param player
     */
    public void spawnCorpse(Player player)
    {
        Corpses.CorpseData corpse = CorpseAPI.spawnCorpse(player, player.getLocation());
        corpses.add(corpse);

        corpse.resendCorpseToEveryone();
    }

    /**
     * Removes all corpses
     */
    public void cleanupCorpses()
    {
        for (Corpses.CorpseData corpse : corpses)
        {
            corpse.destroyCorpseFromEveryone();
            CorpseAPI.removeCorpse(corpse);
        }
    }

}
