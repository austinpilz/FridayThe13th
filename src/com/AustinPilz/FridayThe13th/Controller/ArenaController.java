package com.AustinPilz.FridayThe13th.Controller;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.ArenaAlreadyExistsException;
import com.AustinPilz.FridayThe13th.Exceptions.ArenaDoesNotExistException;

import java.util.HashMap;

public class ArenaController
{
    private HashMap<String, Arena> arenas;

    public ArenaController ()
    {
        arenas = new HashMap<>();
    }

    /**
     * Adds arena into the controller memory
     * @param arena
     * @throws ArenaAlreadyExistsException
     */
    public void addArena(Arena arena) throws ArenaAlreadyExistsException
    {
        if (!arenas.containsValue(arena))
        {
            arenas.put(arena.getArenaName(), arena);
        }
        else
        {
            throw new ArenaAlreadyExistsException();
        }
    }

    /**
     * Removes the arena from the controller memory
     * @param arena
     */
    public void removeArena(Arena arena)
    {
        arenas.remove(arena.getArenaName());
    }

    /**
     * Returns the arena supplied by name
     * @param name Name of desired arena
     * @return
     */
    public Arena getArena(String name) throws ArenaDoesNotExistException
    {
        if (doesArenaExist(name))
        {
            return arenas.get(name);
        }
        else
        {
            throw new ArenaDoesNotExistException("Arena " + name + " does not exist in the controller memory");
        }
    }

    /**
     * Returns if the supplied arena by name exists in the controller memory
     * @param name Name of the arena
     * @return
     */
    public boolean doesArenaExist(String name)
    {
        return arenas.containsKey(name);
    }
}
