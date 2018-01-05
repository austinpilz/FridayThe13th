package com.AustinPilz.FridayThe13th.Components.Profiles;

import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.Components.Level.F13PlayerLevel;
import com.AustinPilz.FridayThe13th.Components.Level.SenseLevel;
import com.AustinPilz.FridayThe13th.Components.Level.StalkLevel;
import com.AustinPilz.FridayThe13th.Components.Level.WarpLevel;
import com.AustinPilz.FridayThe13th.Components.Skin.F13Skin;

public enum JasonProfile {
    PartOne("Part I", "J-P1", 1, F13PlayerLevel.L1, F13Skin.JASON_Part1, 5, StalkLevel.One, SenseLevel.One, WarpLevel.One, F13SoundEffect.RegularChase, false, 1),
    PartTwo("Part II", "J-P2", 2, F13PlayerLevel.L3, F13Skin.JASON_Part2, 5, StalkLevel.One, SenseLevel.Two, WarpLevel.Two, F13SoundEffect.RegularChase, false, 1),
    PartThree("Part III", "J-P3", 3, F13PlayerLevel.L5, F13Skin.JASON_Part3, 5, StalkLevel.Two, SenseLevel.Two, WarpLevel.Two, F13SoundEffect.RegularChase, false, 2),
    PartFour("Part IV", "J-P4", 4, F13PlayerLevel.L7, F13Skin.JASON_Part4, 4, StalkLevel.Two, SenseLevel.Three, WarpLevel.Two, F13SoundEffect.RegularChase, false, 2),
    PartFive("Part V", "J-P5", 5, F13PlayerLevel.L9, F13Skin.JASON_Part5, 4, StalkLevel.Three, SenseLevel.Three, WarpLevel.Three, F13SoundEffect.RegularChase, false, 3),
    PartSix("Part VI", "J-P6", 6, F13PlayerLevel.L11, F13Skin.JASON_Part6, 3, StalkLevel.Three, SenseLevel.Three, WarpLevel.Three, F13SoundEffect.RegularChase, false, 3),
    PartSeven("Part VII", "J-P7", 7, F13PlayerLevel.L13, F13Skin.JASON_Part7, 3, StalkLevel.Four, SenseLevel.Three, WarpLevel.Three, F13SoundEffect.RegularChase, false, 4),
    PartEight("Part VIII", "J-P8", 8, F13PlayerLevel.L15, F13Skin.JASON_Part8, 3, StalkLevel.Four, SenseLevel.Four, WarpLevel.Four, F13SoundEffect.RegularChase, false, 4),
    PartNine("Part IX", "J-P9", 9, F13PlayerLevel.L17, F13Skin.JASON_Part9, 2, StalkLevel.Four, SenseLevel.Five, WarpLevel.Four, F13SoundEffect.RegularChase, false, 5),
    JasonX("Jason X", "J-X", 10, F13PlayerLevel.L18, F13Skin.Jason_X, 2, StalkLevel.Three, SenseLevel.Five, WarpLevel.Five, F13SoundEffect.RegularChase, false, 5),
    Retro("Retro", "J-Retro", 11, F13PlayerLevel.L20, F13Skin.Jason_Retro, 3, StalkLevel.Three, SenseLevel.Three, WarpLevel.Three, F13SoundEffect.RetroChase, false, 5),
    Savini("Savini", "J-Savini", 12, F13PlayerLevel.L20, F13Skin.Jason_Savini, 1, StalkLevel.Five, SenseLevel.Five, WarpLevel.Five, F13SoundEffect.RegularChase, false, 6),
    Pamela_Voorhees("Pamela Voorhees", "J-Pamela", 13, F13PlayerLevel.L10, F13Skin.Pamela_Voorhees, 2, StalkLevel.Five, SenseLevel.Five, WarpLevel.One, F13SoundEffect.RegularChase, true, 1),
    Christmas("Christmas Edition", "J-XMAS", 8, F13PlayerLevel.L15, F13Skin.Jason_Christmas, 3, StalkLevel.Four, SenseLevel.Four, WarpLevel.Four, F13SoundEffect.RegularChase, true, 4),
    ;

    private String displayName;
    private String internalIdentifier;
    private F13PlayerLevel requiredLevel;
    private StalkLevel stalkLevel;
    private SenseLevel senseLevel;
    private WarpLevel warpLevel;
    private F13Skin skin;
    private F13SoundEffect chaseMusic;
    private int order;
    private boolean f13Only;
    private int numStartingKnives;

    //In-Game Quantifiers
    private int requiredDoorBreaks;

    JasonProfile(String name, String internal, int o, F13PlayerLevel level, F13Skin s, int doorBreaks, StalkLevel stalk, SenseLevel sense, WarpLevel warp, F13SoundEffect chase, Boolean special, int numStartingKnives) {
        displayName = name;
        internalIdentifier = internal;
        requiredLevel = level;
        stalkLevel = stalk;
        senseLevel = sense;
        warpLevel = warp;
        skin = s;
        chaseMusic = chase;
        order = o;
        f13Only = special;
        this.numStartingKnives = numStartingKnives;

        //In-Game Quantifiers
        requiredDoorBreaks = doorBreaks;
    }

    /**
     * Returns the display name of the profile
     *
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the internal identifier
     * @return
     */
    public String getInternalIdentifier()
    {
        return internalIdentifier;
    }

    /**
     * Returns the required level for the profile
     *
     * @return
     */
    public F13PlayerLevel getRequiredLevel() {
        return requiredLevel;
    }

    /**
     * Returns the stalk level for the profile
     *
     * @return
     */
    public StalkLevel getStalkLevel() {
        return stalkLevel;
    }

    /**
     * Returns the sense level for the profile
     *
     * @return
     */
    public SenseLevel getSenseLevel() {
        return senseLevel;
    }

    /**
     * Returns the warp profile of the player
     *
     * @return
     */
    public WarpLevel getWarpLevel() {
        return warpLevel;
    }

    /**
     * Returns the skin
     *
     * @return
     */
    public F13Skin getSkin() {
        return skin;
    }

    /**
     * Returns the numerical representation of the profile order
     *
     * @return
     */
    public int getOrder() {
        return order;
    }

    /**
     * Returns the number of door breaks required for Jason to open a door
     *
     * @return
     */
    public int getRequiredDoorBreaks() {
        return requiredDoorBreaks;
    }

    /**
     * Returns the chase music for the profile
     * @return
     */
    public F13SoundEffect getChaseMusic() {
         return chaseMusic;
    }

    /**
     * Returns if the profile is only available on Friday the 13th
     * @return
     */
    public boolean isFridayThe13thOnly()
    {
        return f13Only;
    }

    /**
     * Returns the number of starting knives
     *
     * @return
     */
    public int getNumStartingKnives() {
        return numStartingKnives;
    }
}
