package com.AustinPilz.FridayThe13th.Manager.Statistics;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
import com.AustinPilz.FridayThe13th.Components.Enum.XPAward;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.connorlinfoot.actionbarapi.ActionBarAPI;

public class CounselorXPManager {
    private Counselor counselor;
    private Arena arena;

    //Experience Points Statistics
    private int statsSwitchesRepaired = 0;
    private int statsTrapActivated = 0;
    private int statsFriendlyHits = 0;
    private int statsWindowSprints = 0;
    private int statsJasonStuns = 0;
    private boolean statsJasonKilled = false;
    private boolean statsCalledTommy = false;
    private boolean statsDoorClosed = false;
    private boolean statsCalledPolice = false;
    private int escapes = 0;

    public CounselorXPManager(Counselor c, Arena a) {
        counselor = c;
        arena = a;
    }

    /**
     * Registers the counselor inflicting a friendly hit to another counselor
     */
    public void addFriendlyHit() {
        statsFriendlyHits++;

        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.friendlyHit", "Betrayal: -{0}xp", XPAward.Counselor_FriendlyHit.getXPAward()), 60);
    }

    /**
     * Registers a window sprint
     */
    public void addWindowSprint() {
        statsWindowSprints++;

        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.windowSprint", "Window Sprint: +{0}xp", XPAward.Counselor_WindowSprint.getXPAward()), 60);
    }

    /**
     * Registers that the counselor activated a placed trap
     */
    public void addTrapActivated() {
        statsTrapActivated++;

        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.trapActivated", "Trap Activated: +{0}xp", XPAward.Counselor_TrapActivated.getXPAward()), 60);
    }

    /**
     * Registers that the counselor closed a door
     */
    public void addDoorClosed() {

        if (!statsDoorClosed) {
            statsDoorClosed = true;
            ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.fortification", "Fortification: +{0}xp", XPAward.Counselor_DoorClosed.getXPAward()), 60);
        }
    }

    /**
     * Registers that the counselor called Tommy Jarvis
     */
    public void addTommyCalled() {
        statsCalledTommy = true;
        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.setTommyCalled", "Tommy Jarvis Called: +{0}xp", XPAward.Counselor_TommyCalled.getXPAward()), 60);
    }

    /**
     * Registers that the counselor called the police
     */
    public void addPoliceCalled() {
        statsCalledPolice = true;
        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.setPoliceCalled", "Police Called: +{0}xp", XPAward.Counselor_PoliceCalled.getXPAward()), 60);
    }

    /**
     * Registers that the counselor repaired a broken switch
     */
    public void addSwitchFix() {
        statsSwitchesRepaired++;

        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.switchRepair", "Switch Repair: +{0}xp", statsSwitchesRepaired * XPAward.Counselor_SwitchRepaired.getXPAward()), 60);
    }

    /**
     * Registers that the counselor stunned Jason
     */
    public void addJasonStuns() {
        statsJasonStuns++;
        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.jasonStuns", "Jason Stunned: +{0}xp", statsJasonStuns * XPAward.Counselor_JasonStuns.getXPAward()), 60);
    }

    /**
     * Registers that the counselor killed jason
     */
    public void addJasonKill() {
        statsJasonKilled = true;
    }

    /**
     * Registers that the counselor stunned Jason
     */
    public void addEscape() {
        escapes++;
        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.counselorEscape", "Escaped: +{0}xp", statsJasonStuns * XPAward.Counselor_Escaped.getXPAward()), 60);
    }

    /**
     * Calculates and returns the counselors XP for the game
     *
     * @return
     */
    public int calculateXP() {
        int xp = 0;

        //Calculate window sprints
        if (statsWindowSprints > 0) {
            xp += XPAward.Counselor_WindowSprint.getXPAward();
        }

        //Calculate door closed
        if (statsDoorClosed) {
            xp += XPAward.Counselor_DoorClosed.getXPAward();
        }

        //Calculate Tommy Jarvis called
        if (statsCalledTommy) {
            xp += XPAward.Counselor_TommyCalled.getXPAward();
        }

        //Calculate Tommy Jarvis called
        if (statsCalledPolice) {
            xp += XPAward.Counselor_PoliceCalled.getXPAward();
        }

        if (statsJasonKilled) {
            xp += XPAward.Counselor_JasonKilled.getXPAward();
        }

        //Calculate activated traps
        xp += statsTrapActivated * XPAward.Counselor_TrapActivated.getXPAward();

        //Calculate repaired switches
        xp += statsSwitchesRepaired * XPAward.Counselor_SwitchRepaired.getXPAward();

        //Calculate Jason stuns
        xp += statsJasonStuns * XPAward.Counselor_JasonStuns.getXPAward();

        //Remove friendly hits
        xp -= statsFriendlyHits * XPAward.Counselor_FriendlyHit.getXPAward();

        //For completing a match and staying
        xp += XPAward.Counselor_MatchCompleted.getXPAward();

        //Escapes
        xp += escapes * XPAward.Counselor_Escaped.getXPAward();

        return xp;
    }
}
