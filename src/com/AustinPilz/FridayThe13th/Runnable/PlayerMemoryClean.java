package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.FridayThe13th;

public class PlayerMemoryClean implements Runnable {
    @Override
    public void run() {
        FridayThe13th.playerController.cleanupMemory();
    }
}
