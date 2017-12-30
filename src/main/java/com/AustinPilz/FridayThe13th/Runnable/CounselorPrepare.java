package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;

public class CounselorPrepare implements Runnable {

    private Counselor counselor;

    public CounselorPrepare(Counselor counselor) {
        this.counselor = counselor;
    }

    @Override
    public void run() {
        counselor.prepareForGameplay();
    }
}
