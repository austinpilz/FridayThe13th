package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Counselor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CounselorFearCheck extends BukkitRunnable {

    private Counselor counselor;

    public CounselorFearCheck(Counselor counselor)
    {
        this.counselor = counselor;
    }

    @Override
    public void run()
    {
        //Calculate fear
        counselor.updateFearLevel();
        //counselor.getPlayer().sendMessage("Your fear level: " + counselor.getFearLevel());
    }
}
