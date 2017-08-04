package com.AustinPilz.FridayThe13th.Components.Enum;

public enum StalkLevel {
    One("I", .1, .03),
    Two("II", .07, .07),
    Three("III", .05, .09),
    Four("IV", .03, .11),
    Five("V", .01, .15);

    private String levelName;
    private double depletionRate; //default .01
    private double regenerationRate; //default .06

    StalkLevel(String name, double d, double r) {
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
