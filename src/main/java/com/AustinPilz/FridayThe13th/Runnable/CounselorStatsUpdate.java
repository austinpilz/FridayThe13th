package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
import org.golde.bukkit.corpsereborn.nms.Corpses;

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

        //Check to see if they can see any corpses
        for(Corpses.CorpseData corpse : counselor.getArena().getObjectManager().getCorpses())
        {
            if (counselor.canSee(corpse.getTrueLocation()))
            {
                counselor.corpseSeen(corpse);
            }
        }
    }
}
