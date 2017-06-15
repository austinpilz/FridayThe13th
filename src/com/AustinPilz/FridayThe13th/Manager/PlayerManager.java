package com.AustinPilz.FridayThe13th.Manager;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Components.Counselor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerManager
{
    private Arena arena;

    //Arena Players
    private HashMap<String, Player> players;
    private HashMap<String, Counselor> counselors;

    public PlayerManager(Arena a)
    {
        arena = a;
    }
}
