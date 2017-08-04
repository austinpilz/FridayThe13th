package com.AustinPilz.FridayThe13th.Components.Enum;

public enum SenseLevel {
    One("I", .12, .02),
    Two("II", .1, .04),
    Three("III", .09, .06),
    Four("IV", .07, .07),
    Five("V", .05, .1);

    private String levelName;
    private double depletionRate; //default .1
    private double regenerationRate; //default .04

    SenseLevel(String name, double d, double r) {
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