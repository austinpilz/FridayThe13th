package com.AustinPilz.FridayThe13th.Components.Enum.Level;

public enum CounselorTraitLevel {

    Composure_1("Composure", 1, 12, 20, 0),
    Composure_2("Composure", 2, 11, 22, 0),
    Composure_3("Composure", 3, 10, 24, 0),
    Composure_4("Composure", 4, 9, 26, 0),
    Composure_5("Composure", 5, 8, 28, 0),
    Composure_6("Composure", 6, 7, 30, 0),
    Composure_7("Composure", 7, 6, 32, 0),
    Composure_8("Composure", 8, 5, 34, 0),
    Composure_9("Composure", 9, 4, 36, 0),
    Composure_10("Composure", 10, 3, 38, 0),

    Luck_1("Luck", 1, 0, 0, 0),
    Luck_2("Luck", 2, 0, 0, 0),
    Luck_3("Luck", 3, 0, 0, 0),
    Luck_4("Luck", 4, 0, 0, 0),
    Luck_5("Luck", 5, 0, 0, 0),
    Luck_6("Luck", 6, 0, 0, 0),
    Luck_7("Luck", 7, 0, 0, 0),
    Luck_8("Luck", 8, 0, 0, 0),
    Luck_9("Luck", 9, 0, 0, 0),
    Luck_10("Luck", 10, 0, 0, 0),

    Intelligence_1("Intelligence", 1, 0, 0, .5),
    Intelligence_2("Intelligence", 2, 0, 0, .65),
    Intelligence_3("Intelligence", 3, 0, 0, .8),
    Intelligence_4("Intelligence", 4, 0, 0, .92),
    Intelligence_5("Intelligence", 5, 0, 0, 1),
    Intelligence_6("Intelligence", 6, 0, 0, 1.35),
    Intelligence_7("Intelligence", 7, 0, 0, 1.75),
    Intelligence_8("Intelligence", 8, 0, 0, 2),
    Intelligence_9("Intelligence", 9, 0, 0, 2.25),
    Intelligence_10("Intelligence", 10, 0, 0, 2.5),

    Speed_1("Speed", 1, .16, 0, 0),
    Speed_2("Speed", 2, 0.17,0, 0),
    Speed_3("Speed", 3, .18, 0, 0),
    Speed_4("Speed", 4, .19, 0, 0),
    Speed_5("Speed", 5, .2, 0, 0),
    Speed_6("Speed", 6, .21, 0, 0),
    Speed_7("Speed", 7, .22, 0, 0),
    Speed_8("Speed", 8, .23, 0, 0),
    Speed_9("Speed", 9, .24, 0, 0),
    Speed_10("Speed", 10, .25, 0, 0),

    Stamina_1("Stamina", 1, 0, 0.25, 0.05),
    Stamina_2("Stamina", 2, 0, 0.19, 0.05),
    Stamina_3("Stamina", 3, 0, 0.18, 0.05),
    Stamina_4("Stamina", 4, 0, 0.17, 0.05),
    Stamina_5("Stamina", 5, 0, 0.16, 0.05),
    Stamina_6("Stamina", 6, 0, 0.15, 0.05),
    Stamina_7("Stamina", 7, 0, 0.14, 0.05),
    Stamina_8("Stamina", 8, 0, 0.13, 0.05),
    Stamina_9("Stamina", 9, 0, 0.12, 0.05),
    Stamina_10("Stamina", 10, 0, 0.1, 0.05),

    Stealth_1("Stealth", 1, 0, 0, 0),
    Stealth_2("Stealth", 2, 0, 0, 0),
    Stealth_3("Stealth", 3, 0, 0, 0),
    Stealth_4("Stealth", 4, 0, 0, 0),
    Stealth_5("Stealth", 5, 0, 0, 0),
    Stealth_6("Stealth", 6, 0, 0, 0),
    Stealth_7("Stealth", 7, 0, 0, 0),
    Stealth_8("Stealth", 8, 0, 0, 0),
    Stealth_9("Stealth", 9, 0, 0, 0),
    Stealth_10("Stealth", 10, 0, 0, 0),

    Strength_1("Strength", 1, 0, .5, 0),
    Strength_2("Strength", 2, 0, .6, 0),
    Strength_3("Strength", 3, 0, .8, 0),
    Strength_4("Strength", 4, 0, .9, 0),
    Strength_5("Strength", 5, 0, 1, 0),
    Strength_6("Strength", 6, 0, 1.1, 0),
    Strength_7("Strength", 7, 0, 1.2, 0),
    Strength_8("Strength", 8, 0, 1.3, 0),
    Strength_9("Strength", 9, 0, 1.4, 0),
    Strength_10("Strength", 10, 0, 1.5, 0);

    private String traitName;
    private int traitLevel;
    private double dataValue;
    private double depletionRate;
    private double regenerationRate;

    CounselorTraitLevel(String n, int l, double d, double dr, double rr) {
        traitName = n;
        traitLevel = l;
        dataValue = d;
        depletionRate = dr;
        regenerationRate = rr;
    }

    public String getTraitName() {
        return traitName;
    }

    public int getTraitLevel() {
        return traitLevel;
    }

    public double getDataValue() {
        return dataValue;
    }

    public double getDepletionRate() { return depletionRate; }

    public double getRegenerationRate() { return regenerationRate; }
}
