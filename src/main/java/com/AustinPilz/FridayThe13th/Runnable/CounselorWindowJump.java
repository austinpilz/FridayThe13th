package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class CounselorWindowJump implements Runnable
{
    private Counselor counselor;
    private Location initialLocation;
    private Block block;

    public CounselorWindowJump(Counselor c, Location l, Block b)
    {
        counselor = c;
        initialLocation = l;
        block = b;
    }

    @Override
    public void run()
    {
        if (counselor.getPlayer().getLocation().distance(initialLocation) < 1)
        {
            counselor.teleportThroughWindow(block, false);
        }
        else
        {
            ActionBarAPI.sendActionBar(counselor.getPlayer(), ChatColor.RED + FridayThe13th.language.get(counselor.getPlayer(), "actionBar.counselor.windowJumpFail", "Jump cancelled! {0}You moved before the jump was complete.", ChatColor.WHITE), 60);
        }

        counselor.setAwaitingWindowJump(false); //Restore their ability to window jump
    }

}
