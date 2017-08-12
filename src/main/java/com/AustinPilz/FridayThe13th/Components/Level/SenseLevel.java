package com.AustinPilz.FridayThe13th.Components.Level;

public enum SenseLevel {
    One("I", .12, .02),
    Two("II", .11, .025),
    Three("III", .11, .03),
    Four("IV", .10, .035),
    Five("V", .09, .04);

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