package com.AustinPilz.FridayThe13th.Components.Enum.Level;

public enum StalkLevel {
    One("I", .035, .03),
    Two("II", .025, .035),
    Three("III", .02, .04),
    Four("IV", .015, .045),
    Five("V", .01, .06);

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
