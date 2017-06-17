package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Counselor;
import org.bukkit.scheduler.BukkitRunnable;

public class CounselorStatsDisplayUpdate extends BukkitRunnable {

    private Counselor counselor;

    public CounselorStatsDisplayUpdate(Counselor counselor)
    {
        this.counselor = counselor;
    }

    @Override
    public void run()
    {
        //Update the displays
        counselor.getStatsDisplayManager().updateStats(); //Update display of the stats
    }
}
