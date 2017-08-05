package com.AustinPilz.FridayThe13th.Manager.Game;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
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
     * Removes join sign
     * @param sign
     */
    public void removeJoinSign(Sign sign) { joinSigns.remove(sign); }

    /**
     * Updates all arena join signs
     */
    public void updateJoinSigns()
    {
        for (Sign sign : joinSigns)
        {
            updateSign(sign);
        }
    }

    private void updateSign(Sign sign)
    {
        sign.setLine(0, ChatColor.RED + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.signPrefix", FridayThe13th.signPrefix));
        sign.setLine(1, arena.getArenaName());

        if (arena.getGameManager().isGameEmpty())
        {
            if (arena.getGameManager().getPlayerManager().getNumPlayers() == 0)
            {
                //Display empty
                sign.setLine(2, ChatColor.GREEN + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sign.Empty", "Empty"));
            }
            else
            {
                //Display counter
                if (arena.getLocationManager().getNumberStartingPoints() >= 8) {
                    sign.setLine(2, arena.getGameManager().getPlayerManager().getNumPlayers() + " / 9");
                } else {
                    sign.setLine(2, arena.getGameManager().getPlayerManager().getNumPlayers() + " / " + (arena.getLocationManager().getNumberStartingPoints() + 1));
                }
            }

            sign.setLine(3, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sign.ClickToJoin", "Click To Join!"));
        }
        else if (arena.getGameManager().isGameWaiting())
        {
            sign.setLine(2, ChatColor.AQUA + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sign.Waiting", "Waiting") + ChatColor.BLACK + " - " + arena.getGameManager().getWaitingTimeLeft() + "s");
            sign.setLine(3, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sign.ClickToJoin", "Click To Join!"));
        }
        else if (arena.getGameManager().isGameInProgress())
        {
            int rem = arena.getGameManager().getGameTimeLeft() % 3600;
            int mn = rem / 60;
            int sec = rem % 60;

            sign.setLine(2, ChatColor.DARK_RED + "" + mn + " m " + sec + " sec");
            sign.setLine(3, FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sign.ClickToSpectate", "Click To Spectate!"));
        }

        sign.update();
    }

    /**
     * Marks all signs as deleted
     */
    public void markDeleted()
    {
        for (Sign sign : joinSigns)
        {
            sign.setLine(0, ChatColor.RED + FridayThe13th.signPrefix);
            sign.setLine(1, "["+FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.sign.Deleted", "Deleted")+"]");
            sign.setLine(2, "");
            sign.setLine(3, "");
            sign.update();
        }
    }
}
