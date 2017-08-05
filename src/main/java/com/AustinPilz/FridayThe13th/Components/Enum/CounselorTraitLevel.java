package com.AustinPilz.FridayThe13th.Components.Enum;

public enum CounselorTraitLevel {

    Composure_1("Composure", 1, 0),
    Composure_2("Composure", 2, 0),
    Composure_3("Composure", 3, 0),
    Composure_4("Composure", 4, 0),
    Composure_5("Composure", 5, 0),
    Composure_6("Composure", 6, 0),
    Composure_7("Composure", 7, 0),
    Composure_8("Composure", 8, 0),
    Composure_9("Composure", 9, 0),
    Composure_10("Composure", 10, 0),

    Luck_1("Luck", 1, 0),
    Luck_2("Luck", 2, 0),
    Luck_3("Luck", 3, 0),
    Luck_4("Luck", 4, 0),
    Luck_5("Luck", 5, 0),
    Luck_6("Luck", 6, 0),
    Luck_7("Luck", 7, 0),
    Luck_8("Luck", 8, 0),
    Luck_9("Luck", 9, 0),
    Luck_10("Luck", 10, 0),

    Intelligence_1("Intelligence", 1, 0),
    Intelligence_2("Intelligence", 2, 0),
    Intelligence_3("Intelligence", 3, 0),
    Intelligence_4("Intelligence", 4, 0),
    Intelligence_5("Intelligence", 5, 0),
    Intelligence_6("Intelligence", 6, 0),
    Intelligence_7("Intelligence", 7, 0),
    Intelligence_8("Intelligence", 8, 0),
    Intelligence_9("Intelligence", 9, 0),
    Intelligence_10("Intelligence", 10, 0),

    Speed_1("Speed", 1, 0),
    Speed_2("Speed", 2, 0),
    Speed_3("Speed", 3, 0),
    Speed_4("Speed", 4, 0),
    Speed_5("Speed", 5, 0),
    Speed_6("Speed", 6, 0),
    Speed_7("Speed", 7, 0),
    Speed_8("Speed", 8, 0),
    Speed_9("Speed", 9, 0),
    Speed_10("Speed", 10, 0),

    Stamina_1("Stamina", 1, 0),
    Stamina_2("Stamina", 2, 0),
    Stamina_3("Stamina", 3, 0),
    Stamina_4("Stamina", 4, 0),
    Stamina_5("Stamina", 5, 0),
    Stamina_6("Stamina", 6, 0),
    Stamina_7("Stamina", 7, 0),
    Stamina_8("Stamina", 8, 0),
    Stamina_9("Stamina", 9, 0),
    Stamina_10("Stamina", 10, 0),

    Stealth_1("Stealth", 1, 0),
    Stealth_2("Stealth", 2, 0),
    Stealth_3("Stealth", 3, 0),
    Stealth_4("Stealth", 4, 0),
    Stealth_5("Stealth", 5, 0),
    Stealth_6("Stealth", 6, 0),
    Stealth_7("Stealth", 7, 0),
    Stealth_8("Stealth", 8, 0),
    Stealth_9("Stealth", 9, 0),
    Stealth_10("Stealth", 10, 0),

    Strength_1("Strength", 1, 0),
    Strength_2("Strength", 2, 0),
    Strength_3("Strength", 3, 0),
    Strength_4("Strength", 4, 0),
    Strength_5("Strength", 5, 0),
    Strength_6("Strength", 6, 0),
    Strength_7("Strength", 7, 0),
    Strength_8("Strength", 8, 0),
    Strength_9("Strength", 9, 0),
    Strength_10("Strength", 10, 0);

    private String traitName;
    private int traitLevel;
    private double dataValue;

    CounselorTraitLevel(String n, int l, double d) {
        traitName = n;
        traitLevel = l;
        dataValue = d;
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
}
