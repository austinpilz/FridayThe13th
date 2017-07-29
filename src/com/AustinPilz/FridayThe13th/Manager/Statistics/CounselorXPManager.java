package com.AustinPilz.FridayThe13th.Manager.Statistics;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
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
    private boolean statsCalledTommy = false;
    private boolean statsDoorClosed = false;

    public CounselorXPManager(Counselor c, Arena a) {
        counselor = c;
        arena = a;
    }

    /**
     * Registers the counselor inflicting a friendly hit to another counselor
     */
    public void addFriendlyHit() {
        statsFriendlyHits++;

        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.friendlyHit", "Betrayal: -{0}xp", 1000), 60);
    }

    /**
     * Registers a window sprint
     */
    public void addWindowSprint() {
        statsWindowSprints++;

        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.windowSprint", "Window Sprint: +{0}xp", 50), 60);
    }

    /**
     * Registers that the counselor activated a placed trap
     */
    public void addTrapActivated() {
        statsTrapActivated++;

        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.trapActivated", "Trap Activated: +{0}xp", 50), 60);
    }

    /**
     * Registers that the counselor closed a door
     */
    public void addDoorClosed() {

        if (!statsDoorClosed) {
            statsDoorClosed = true;
            ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.fortification", "Fortification: +{0}xp", 25), 60);
        }
    }

    /**
     * Registers that the counselor called Tommy Jarvis
     */
    public void addTommyCalled() {
        statsCalledTommy = true;

        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.tommyCalled", "Tommy Jarvis Called: +{0}xp", 100), 60);
    }

    /**
     * Registers that the counselor repaired a broken switch
     */
    public void addSwitchFix() {
        statsSwitchesRepaired++;

        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.switchRepair", "Switch Repair: +{0}xp", 100), 60);
    }

    /**
     * Registers that the counselor stunned Jason
     */
    public void addJasonStuns() {
        statsJasonStuns++;

        ActionBarAPI.sendActionBar(counselor.getPlayer(), FridayThe13th.language.get(counselor.getPlayer(), "actionbar.xp.jasonStuns", "Jason Stunned: +{0}xp", 50), 60);
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
            xp += 50;
        }

        //Calculate door closed
        if (statsDoorClosed) {
            xp += 25;
        }

        //Calculate Tommy Jarvis called
        if (statsCalledTommy) {
            xp += 100;
        }

        //Calculate activated traps
        xp += statsTrapActivated * 50;

        //Calculate repaired switches
        xp += statsSwitchesRepaired * 100;

        //Calculate Jason stuns
        xp += statsJasonStuns * 50;

        //Remove friendly hits
        xp -= statsFriendlyHits * 1000;

        return xp;
    }
}
