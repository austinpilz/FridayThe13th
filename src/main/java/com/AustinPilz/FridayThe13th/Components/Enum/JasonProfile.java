package com.AustinPilz.FridayThe13th.Components.Enum;

public enum JasonProfile {
    PartOne("Part I", 1, F13Level.L1, F13Skin.JASON_Part1, 5, StalkLevel.One, SenseLevel.One, WarpLevel.One),
    PartTwo("Part II", 2, F13Level.L3, F13Skin.JASON_Part2, 5, StalkLevel.One, SenseLevel.Two, WarpLevel.Two),
    PartThree("Part III", 3, F13Level.L5, F13Skin.JASON_Part3, 5, StalkLevel.Two, SenseLevel.Two, WarpLevel.Two),
    PartFour("Part IV", 4, F13Level.L7, F13Skin.JASON_Part4, 4, StalkLevel.Two, SenseLevel.Three, WarpLevel.Two),
    PartFive("Part V", 5, F13Level.L9, F13Skin.JASON_Part5, 4, StalkLevel.Three, SenseLevel.Three, WarpLevel.Three),
    PartSix("Part VI", 6, F13Level.L13, F13Skin.JASON_Part6, 4, StalkLevel.Three, SenseLevel.Three, WarpLevel.Three),
    PartSeven("Part VII", 7, F13Level.L16, F13Skin.JASON_Part7, 3, StalkLevel.Four, SenseLevel.Four, WarpLevel.Three),
    PartEight("Part VIII", 8, F13Level.L18, F13Skin.JASON_Part8, 2, StalkLevel.Four, SenseLevel.Four, WarpLevel.Four),
    PartNine("Part IX", 9, F13Level.L20, F13Skin.JASON_Part9, 1, StalkLevel.Five, SenseLevel.Five, WarpLevel.Five);

    private String displayName;
    private F13Level requiredLevel;
    private StalkLevel stalkLevel;
    private SenseLevel senseLevel;
    private WarpLevel warpLevel;
    private F13Skin skin;
    private int order;

    //In-Game Quantifiers
    private int requiredDoorBreaks;

    JasonProfile(String name, int o, F13Level level, F13Skin s, int doorBreaks, StalkLevel stalk, SenseLevel sense, WarpLevel warp) {
        displayName = name;
        requiredLevel = level;
        stalkLevel = stalk;
        senseLevel = sense;
        warpLevel = warp;
        skin = s;
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
}
