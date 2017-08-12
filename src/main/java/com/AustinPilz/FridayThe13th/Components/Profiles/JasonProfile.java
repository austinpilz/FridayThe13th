package com.AustinPilz.FridayThe13th.Components.Profiles;

import com.AustinPilz.FridayThe13th.Components.Enum.*;
import com.AustinPilz.FridayThe13th.Components.Level.F13Level;
import com.AustinPilz.FridayThe13th.Components.Level.SenseLevel;
import com.AustinPilz.FridayThe13th.Components.Level.StalkLevel;
import com.AustinPilz.FridayThe13th.Components.Level.WarpLevel;

public enum JasonProfile {
    PartOne("Part I", "J-P1", 1, F13Level.L1, F13Skin.JASON_Part1, 5, StalkLevel.One, SenseLevel.One, WarpLevel.One, F13SoundEffect.RegularChase),
    PartTwo("Part II", "J-P2", 2, F13Level.L3, F13Skin.JASON_Part2, 5, StalkLevel.One, SenseLevel.Two, WarpLevel.Two, F13SoundEffect.RegularChase),
    PartThree("Part III", "J-P3", 3, F13Level.L5, F13Skin.JASON_Part3, 5, StalkLevel.Two, SenseLevel.Two, WarpLevel.Two, F13SoundEffect.RegularChase),
    PartFour("Part IV", "J-P4", 4, F13Level.L7, F13Skin.JASON_Part4, 4, StalkLevel.Two, SenseLevel.Three, WarpLevel.Two, F13SoundEffect.RegularChase),
    PartFive("Part V", "J-P5", 5, F13Level.L9, F13Skin.JASON_Part5, 4, StalkLevel.Three, SenseLevel.Three, WarpLevel.Three, F13SoundEffect.RegularChase),
    PartSix("Part VI", "J-P6", 6, F13Level.L13, F13Skin.JASON_Part6, 4, StalkLevel.Three, SenseLevel.Three, WarpLevel.Three, F13SoundEffect.RegularChase),
    PartSeven("Part VII", "J-P7", 7, F13Level.L16, F13Skin.JASON_Part7, 3, StalkLevel.Four, SenseLevel.Four, WarpLevel.Three, F13SoundEffect.RegularChase),
    PartEight("Part VIII", "J-P8", 8, F13Level.L18, F13Skin.JASON_Part8, 2, StalkLevel.Four, SenseLevel.Four, WarpLevel.Four, F13SoundEffect.RegularChase),
    PartNine("Part IX", "J-P9", 9, F13Level.L20, F13Skin.JASON_Part9, 1, StalkLevel.Five, SenseLevel.Five, WarpLevel.Five, F13SoundEffect.RegularChase),
    Retro("Retro", "J-Retro", 10, F13Level.L20, F13Skin.Jason_Retro, 3, StalkLevel.Three, SenseLevel.Three, WarpLevel.Three, F13SoundEffect.RetroChase);

    private String displayName;
    private String internalIdentifier;
    private F13Level requiredLevel;
    private StalkLevel stalkLevel;
    private SenseLevel senseLevel;
    private WarpLevel warpLevel;
    private F13Skin skin;
    private F13SoundEffect chaseMusic;
    private int order;

    //In-Game Quantifiers
    private int requiredDoorBreaks;

    JasonProfile(String name, String internal, int o, F13Level level, F13Skin s, int doorBreaks, StalkLevel stalk, SenseLevel sense, WarpLevel warp, F13SoundEffect chase) {
        displayName = name;
        internalIdentifier = internal;
        requiredLevel = level;
        stalkLevel = stalk;
        senseLevel = sense;
        warpLevel = warp;
        skin = s;
        chaseMusic = chase;
        order = o;

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
    public F13Level getRequiredLevel() {
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
}
