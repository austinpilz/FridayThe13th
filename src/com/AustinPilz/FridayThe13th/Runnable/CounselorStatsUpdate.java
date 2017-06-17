package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Counselor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CounselorStatsUpdate extends BukkitRunnable {

    private Counselor counselor;

    public CounselorStatsUpdate(Counselor counselor)
    {
        this.counselor = counselor;
    }

    @Override
    public void run()
    {
        //Calculate fear
        counselor.updateFearLevel();

        //Movement and Speed (Regeneration)
        if (counselor.isMoving())
        {
            counselor.setMoving(false);
        }
        else if (!counselor.isMoving() && (counselor.getFearLevel()/counselor.getMaxFearLevel()) < .9)
        {
            counselor.setStandingStill(true);
        }
    }
}
