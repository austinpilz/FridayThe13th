package com.AustinPilz.FridayThe13th.Manager.Statistics;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Characters.Jason;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.connorlinfoot.actionbarapi.ActionBarAPI;

public class JasonXPManager {
    private Jason jason;
    private Arena arena;

    //Experience Points Statistics
    private int counselorKills = 0;
    private int windowBreaks = 0;
    private int doorBreak = 0;
    private int switchBreak = 0;
    private int trapEnsnarement = 0;
    private int timeLeftMinutes = 0;

    public JasonXPManager(Jason j, Arena a) {
        jason = j;
        arena = a;
    }

    /**
     * Adds counselor kill
     */
    public void addCounselorKill() {
        counselorKills++;
        ActionBarAPI.sendActionBar(jason.getPlayer(), FridayThe13th.language.get(jason.getPlayer(), "actionbar.xp.counselorKill", "Counselor Kill: +{0}xp", 100), 60);
    }

    /**
     * Adds window break
     */
    public void addWindowBreak() {
        windowBreaks++;
        ActionBarAPI.sendActionBar(jason.getPlayer(), FridayThe13th.language.get(jason.getPlayer(), "actionbar.xp.windowBreak", "Window Break: +{0}xp", 10), 60);
    }

    /**
     * Adds door break
     */
    public void addDoorBreak() {
        doorBreak++;
        ActionBarAPI.sendActionBar(jason.getPlayer(), FridayThe13th.language.get(jason.getPlayer(), "actionbar.xp.doorBreak", "Door Break: +{0}xp", 30), 60);
    }

    /**
     * Adds switch break
     */
    public void addSwitchBreak() {
        switchBreak++;
        ActionBarAPI.sendActionBar(jason.getPlayer(), FridayThe13th.language.get(jason.getPlayer(), "actionbar.xp.switchBreak", "Switch Break: +{0}xp", 50), 60);
    }

    /**
     * Adds trap ensnarement
     */
    public void addTrapEnsnarement() {
        trapEnsnarement++;
        ActionBarAPI.sendActionBar(jason.getPlayer(), FridayThe13th.language.get(jason.getPlayer(), "actionbar.xp.trapEnsnare", "Trap Ensnarement: +{0}xp", 75), 60);
    }

    /**
     * Sets the time left in game when it ended
     *
     * @param min
     */
    public void setTimeLeftMinutes(int min) {
        timeLeftMinutes = min;
    }

    /**
     * Calculates and returns Jason's XP for the game
     *
     * @return
     */
    public int calculateXP() {
        int xp = 0;

        //Calculate Counselor Kills
        xp += counselorKills * 100;

        //Calculate Window Breaks
        xp += windowBreaks * 10;

        //Calculate Door Breaks
        xp += doorBreak * 30;

        //Calculate Switch Breaks
        xp += switchBreak * 50;

        //Calculate Trap Ensnarement
        xp += trapEnsnarement * 75;

        //Calculate Time Left Bonus
        xp += timeLeftMinutes * 50;

        return xp;
    }
}
