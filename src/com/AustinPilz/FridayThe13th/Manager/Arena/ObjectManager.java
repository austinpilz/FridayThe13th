package com.AustinPilz.FridayThe13th.Manager.Arena;


import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Components.ArenaChest;

import java.util.HashSet;

public class ObjectManager
{
    private Arena arena;
    private HashSet<ArenaChest> chestsWeapons;
    private HashSet<ArenaChest> chestsItems;

    /**
     * @param arena Arena object
     */
    public ObjectManager(Arena arena)
    {
        this.arena = arena;
        this.chestsItems = new HashSet<>();
        this.chestsWeapons = new HashSet<>();
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
}
