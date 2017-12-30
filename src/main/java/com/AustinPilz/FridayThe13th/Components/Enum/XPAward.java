package com.AustinPilz.FridayThe13th.Components.Enum;

public enum XPAward {

    Jason_WindowBreak(5),
    Jason_CounselorKill(100),
    Jason_DoorBreak(30),
    Jason_SwitchBreak(50),
    Jason_TrapEnsnare(75),
    Jason_MinuteLeft(50),
    Jason_NoEscapes(500),
    Counselor_WindowSprint(50),
    Counselor_DoorClosed(25),
    Counselor_TommyCalled(100),
    Counselor_PoliceCalled(150),
    Counselor_JasonKilled(1000),
    Counselor_TrapActivated(50),
    Counselor_SwitchRepaired(100),
    Counselor_JasonStuns(50),
    Counselor_FriendlyHit(1000),
    Counselor_MatchCompleted(100),
    Counselor_Escaped(250);


    private int xp;

    XPAward(int num)
    {
        this.xp = num;
    }

    /**
     * Returns the XP amount for this award
     * @return XP amount
     */
    public int getXPAward()
    {
        return xp;
    }
}
