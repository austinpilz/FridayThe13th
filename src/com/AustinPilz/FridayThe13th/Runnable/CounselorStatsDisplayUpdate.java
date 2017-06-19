package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Counselor;

public class CounselorStatsDisplayUpdate implements Runnable {

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