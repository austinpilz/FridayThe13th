package com.AustinPilz.FridayThe13th.Components.Enum;

public enum WarpLevel {
    One("I", .12, .02),
    Two("II", .12, .022),
    Three("III", .12, .025),
    Four("IV", .12, .027),
    Five("V", .12, .035);

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
