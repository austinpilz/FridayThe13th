package com.AustinPilz.FridayThe13th.Manager.Arena;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import java.util.HashSet;

public class SignManager
{
    private Arena arena;
    private HashSet<Sign> joinSigns;

    public SignManager(Arena arena)
    {
        this.arena = arena;
        this.joinSigns = new HashSet<>();
    }

    /**
     * Returns if the supplied sign is a join sign
     * @param sign
     * @return
     */
    public boolean isJoinSign(Sign sign)
    {
        return joinSigns.contains(sign);
    }

    /**
     * Adds join sign
     * @param sign
     */
    public void addJoinSign(Sign sign)
    {
        joinSigns.add(sign);
    }

    /**
     * Updates all arena join signs
     */
    public void updateJoinSigns()
    {
        for (Sign sign : joinSigns)
        {
            sign.setLine(0, ChatColor.RED + FridayThe13th.signPrefix);
            sign.setLine(1, arena.getArenaName());

            if (arena.getGameManager().isGameEmpty())
            {
                sign.setLine(2, ChatColor.GREEN + "Empty");
                sign.setLine(3, "Click to join!");
            }
            else if (arena.getGameManager().isGameWaiting())
            {
                sign.setLine(2, ChatColor.AQUA + "Waiting" + ChatColor.BLACK + " - " + arena.getGameManager().getWaitingTimeLeft() + "s");
                sign.setLine(3, "Click to join!");
            }
            else if (arena.getGameManager().isGameInProgress())
            {
                sign.setLine(2, ChatColor.RED + "In Progress");

                int rem = arena.getGameManager().getGameTimeLeft() % 3600;
                int mn = rem / 60;
                int sec = rem % 60;

                sign.setLine(3, mn + " m " + sec + " sec");
            }

            sign.update();
        }
    }

    /**
     * Marks all signs as deleted
     */
    public void markDeleted()
    {
        for (Sign sign : joinSigns)
        {
            sign.setLine(0, ChatColor.RED + FridayThe13th.signPrefix);
            sign.setLine(1, "[Deleted]");
            sign.update();
        }
    }
}
