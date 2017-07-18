package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;

public class CounselorStatsUpdate implements Runnable {

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
