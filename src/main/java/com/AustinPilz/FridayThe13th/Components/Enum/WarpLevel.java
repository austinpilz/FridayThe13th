package com.AustinPilz.FridayThe13th.Components.Enum;

public enum WarpLevel {
    One("I", .12, .02),
    Two("II", .1, .03),
    Three("III", .09, .04),
    Four("IV", .08, .05),
    Five("V", .07, .06);

    private String levelName;
    private double depletionRate; //default .1
    private double regenerationRate; //default .03

    WarpLevel(String name, double d, double r) {
        levelName = name;
        depletionRate = d;
        regenerationRate = r;
    }

    /**
     * Returns the level name
     *
     * @return
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Returns the depletion rate
     *
     * @return
     */
    public double getDepletionRate() {
        return depletionRate;
    }

    /**
     * Returns the regeneration rate
     *
     * @return
     */
    public double getRegenerationRate() {
        return regenerationRate;
    }
}
