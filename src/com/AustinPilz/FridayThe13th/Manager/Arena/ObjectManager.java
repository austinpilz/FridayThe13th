package com.AustinPilz.FridayThe13th.Manager.Arena;


import com.AustinPilz.FridayThe13th.Components.Arena;

public class ObjectManager
{
    private Arena arena;

    //Objects

    /*
    1.) Chests
    2.) Weapons?
    3.) Objects (keys, healing, etc)
     */

    /**
     * @param arena Arena object
     */
    public ObjectManager(Arena arena)
    {
        this.arena = arena;
    }

    /**
     * Get the arena of this object manager
     * @return Arena
     */
    public Arena getArena()
    {
        return this.arena;
    }
}
