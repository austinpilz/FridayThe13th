package com.AustinPilz.FridayThe13th.Components.Enum;

import com.AustinPilz.FridayThe13th.Components.Characters.CharacterType;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;

public enum XPAward {

    Jason_WindowBreak(CharacterType.Jason, -1, 5, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.windowBreak", "Window Break")),
    Jason_CounselorKill(CharacterType.Jason, 9, 100, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.counselorKill", "Counselor Kill")),
    Jason_DoorBreak(CharacterType.Jason, -1, 30, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.doorBreak", "Door Break")),
    Jason_SwitchBreak(CharacterType.Jason, -1, 15, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.switchBreak", "Switch Break")),
    Jason_TrapEnsnare(CharacterType.Jason, -1, 75, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.trapEnsnare", "Trap Ensnarement")),
    Jason_NoEscapes(CharacterType.Jason, -1, 500, ""),

    Counselor_WindowSprint(CharacterType.Counselor, -1, 50, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.windowSprint", "Window Sprint")),
    Counselor_DoorClosed(CharacterType.Counselor, 1, 25, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.fortification", "Fortification")),
    Counselor_TommyCalled(CharacterType.Counselor, 1, 100, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.setTommyCalled", "Tommy Jarvis Called")),
    Counselor_PhoneRepaired(CharacterType.Counselor, 2, 50, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.phoneRepaired", "Phone Repaired")),
    Counselor_PoliceCalled(CharacterType.Counselor, 1, 150, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.setPoliceCalled", "Police Called")),
    Counselor_JasonKilled(CharacterType.Counselor, 1, 1000, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.jasonKilled", "Jason Killed")),
    Counselor_TrapActivated(CharacterType.Counselor, -1, 50, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.trapActivated", "Trap Activated")),
    Counselor_SwitchRepaired(CharacterType.Counselor, -1, 50, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.switchRepair", "Switch Repair")),
    Counselor_JasonStuns(CharacterType.Counselor, -1, 75, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.jasonStuns", "Jason Stunned")),
    Counselor_FriendlyHit(CharacterType.Counselor, -1, -1000, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.friendlyHit", "Betrayal:")),
    Counselor_MatchCompleted(CharacterType.Counselor, 1, 100, ""),
    Counselor_Escaped(CharacterType.Counselor, 1, 250, FridayThe13th.language.get(Bukkit.getConsoleSender(), "actionbar.xpAward.counselorEscape", "Escaped"));

    private CharacterType characterType;
    private int maxUses;
    private int xpAward;
    private String messageOnAward;

    XPAward(CharacterType characterType, int maxUses, int xpAward, String messageOnAward)
    {
        this.characterType = characterType;
        this.maxUses = maxUses;
        this.xpAward = xpAward;
        this.messageOnAward = messageOnAward;
    }

    /**
     * Returns the XP amount for this award
     * @return XP amount
     */
    public int getXPAward()
    {
        return xpAward;
    }

    /**
     * Returns the max number of uses for this xp award
     *
     * @return Max number of uses
     */
    public int getMaxUses() {
        return maxUses;
    }

    /**
     * Returns the message to be sent to the player upon award
     *
     * @return Award message
     */
    public String getMessageOnAward() {
        String returnString;

        if (getXPAward() >= 0) {
            returnString = messageOnAward + ": +" + getXPAward() + "xp";
        } else {
            returnString = messageOnAward + ": -" + getXPAward() + "xp";
        }

        return returnString;
    }

    /**
     * Returns if the XP award is for a counselor
     *
     * @return If XP award is for a counselor
     */
    public boolean isCounselorXPAward() {
        return characterType.equals(CharacterType.Counselor);
    }

    /**
     * Returns if the XP award is for Jason
     *
     * @return If the XP award is for Jason
     */
    public boolean isJasonXPAward() {
        return characterType.equals(CharacterType.Jason);
    }
}
