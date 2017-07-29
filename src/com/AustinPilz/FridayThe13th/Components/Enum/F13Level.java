package com.AustinPilz.FridayThe13th.Components.Enum;

public enum F13Level {
    L1("1", 0, 500),
    L2("2", 501, 7500),
    L3("3", 7501, 15500),
    L4("4", 15501, 24500),
    L5("5", 24501, 34500),
    L6("6", 34501, 45500),
    L7("7", 45501, 57500),
    L8("8", 57501, 70500),
    L9("9", 70501, 84500),
    L10("10", 84501, 99500),
    L11("11", 99501, 115500),
    L12("12", 115501, 132500),
    L13("13", 132501, 150500),
    L14("14", 150501, 169500),
    L15("15", 169501, 189500),
    L16("16", 189501, 210500),
    L17("17", 210501, 232500),
    L18("18", 232501, 255500),
    L19("19", 255501, 279500),
    L20("20", 279501, 304500),;

    private final String displayName;
    private final int minXP;
    private final int maxXP;

    F13Level(String name, int min, int max) {
        displayName = name;
        minXP = min;
        maxXP = max;
    }

    /**
     * Get level's minimum XP
     *
     * @return
     */
    public int getMinXP() {
        return minXP;
    }

    /**
     * Get level's maximum XP
     *
     * @return
     */
    public int getMaxXP() {
        return maxXP;
    }

    /**
     * Returns integer representation of level name
     *
     * @return
     */
    public int getLevelNumber() {
        return Integer.parseInt(displayName);
    }
}

