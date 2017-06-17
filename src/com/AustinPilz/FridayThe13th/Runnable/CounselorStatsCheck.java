package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Counselor;
import org.bukkit.scheduler.BukkitRunnable;

public class CounselorStatsCheck extends BukkitRunnable {

    private Counselor counselor;

    public CounselorStatsCheck(Counselor counselor)
    {
        this.counselor = counselor;
    }

    @Override
    public void run()
    {
        //Movement and Speed (Regeneration)
        if (counselor.isMoving())
        {
            counselor.setMoving(false);
        }
        else
        {
            counselor.setStandingStill(true);
        }

        //Update the displays
        counselor.getStatsDisplayManager().updateStats(); //Update display of the stats


    }
}
