package com.AustinPilz.FridayThe13th.Components.Enum;

public enum JasonProfile {
    PartOne("Part I", 1, F13Level.L1, F13Skin.JASON_Part1),
    PartTwo("Part II", 2, F13Level.L3, F13Skin.JASON_Part2),
    PartThree("Part III", 3, F13Level.L5, F13Skin.JASON_Part3),
    PartFour("Part IV", 4, F13Level.L7, F13Skin.JASON_Part4),
    PartFive("Part V", 5, F13Level.L9, F13Skin.JASON_Part5),
    PartSix("Part VI", 6, F13Level.L13, F13Skin.JASON_Part6),
    PartSeven("Part VII", 7, F13Level.L16, F13Skin.JASON_Part7),
    PartEight("Part VIII", 8, F13Level.L18, F13Skin.JASON_Part8),
    PartNine("Part IX", 9, F13Level.L20, F13Skin.JASON_Part9);

    private String displayName;
    private F13Level requiredLevel;
    private F13Skin skin;
    private int order;

    JasonProfile(String name, int o, F13Level level, F13Skin s) {
        displayName = name;
        requiredLevel = level;
        skin = s;
        order = o;
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
     * Returns the skin
     *
     * @return
     */
    public F13Skin getSkin() {
        return skin;
    }

    /**
     * Returns the numerican representation of the profile order
     *
     * @return
     */
    public int getOrder() {
        return order;
    }
}
