package com.AustinPilz.FridayThe13th.Components.Enum;

public enum CounselorProfile {

    Adam("Adam Palomino", F13Skin.Counselor_Adam, F13Level.L5, 1, CounselorTraitLevel.Composure_8, CounselorTraitLevel.Luck_3, CounselorTraitLevel.Intelligence_4, CounselorTraitLevel.Speed_5, CounselorTraitLevel.Stamina_4, CounselorTraitLevel.Stealth_3, CounselorTraitLevel.Strength_8),
    Brandon("Brandon Wilson", F13Skin.Counselor_Brandon, F13Level.L4, 2, CounselorTraitLevel.Composure_4, CounselorTraitLevel.Luck_2, CounselorTraitLevel.Intelligence_1, CounselorTraitLevel.Speed_8, CounselorTraitLevel.Stamina_8, CounselorTraitLevel.Stealth_2, CounselorTraitLevel.Strength_10),
    Chad("Chad Kensington", F13Skin.Counselor_Chad, F13Level.L1, 3, CounselorTraitLevel.Composure_1, CounselorTraitLevel.Luck_10, CounselorTraitLevel.Intelligence_2, CounselorTraitLevel.Speed_9, CounselorTraitLevel.Stamina_4, CounselorTraitLevel.Stealth_6, CounselorTraitLevel.Strength_3),
    Deborah("Deborah Kim", F13Skin.Counselor_Deborah, F13Level.L2, 4, CounselorTraitLevel.Composure_5, CounselorTraitLevel.Luck_3, CounselorTraitLevel.Intelligence_10, CounselorTraitLevel.Speed_4, CounselorTraitLevel.Stamina_3, CounselorTraitLevel.Stealth_9, CounselorTraitLevel.Strength_1),
    Jenny("Jenny Myers", F13Skin.Counselor_Jenny, F13Level.L1, 5, CounselorTraitLevel.Composure_10, CounselorTraitLevel.Luck_9, CounselorTraitLevel.Intelligence_2, CounselorTraitLevel.Speed_3, CounselorTraitLevel.Stamina_4, CounselorTraitLevel.Stealth_6, CounselorTraitLevel.Strength_1),
    AJ("A.J. Mason", F13Skin.Counselor_AJ, F13Level.L3, 6, CounselorTraitLevel.Composure_7, CounselorTraitLevel.Luck_1, CounselorTraitLevel.Intelligence_7, CounselorTraitLevel.Speed_4, CounselorTraitLevel.Stamina_4, CounselorTraitLevel.Stealth_10, CounselorTraitLevel.Strength_2),
    Vanessa("Vanessa Jones", F13Skin.Counselor_Vanessa, F13Level.L2, 7, CounselorTraitLevel.Composure_3, CounselorTraitLevel.Luck_6, CounselorTraitLevel.Intelligence_2, CounselorTraitLevel.Speed_10, CounselorTraitLevel.Stamina_9, CounselorTraitLevel.Stealth_1, CounselorTraitLevel.Strength_4),
    Tiffany("Tiffany Cox", F13Skin.Counselor_Tiffany, F13Level.L3, 8, CounselorTraitLevel.Composure_3, CounselorTraitLevel.Luck_4, CounselorTraitLevel.Intelligence_1, CounselorTraitLevel.Speed_6, CounselorTraitLevel.Stamina_9, CounselorTraitLevel.Stealth_10, CounselorTraitLevel.Strength_2),
    Eric("Eric J.R. Lachappa", F13Skin.Counselor_Eric, F13Level.L10, 9, CounselorTraitLevel.Composure_4, CounselorTraitLevel.Luck_5, CounselorTraitLevel.Intelligence_10, CounselorTraitLevel.Speed_3, CounselorTraitLevel.Stamina_2, CounselorTraitLevel.Stealth_8, CounselorTraitLevel.Strength_3);

    private String counselorName;
    private F13Level requiredLevel;
    private int displayOrder;
    private F13Skin skin;

    //Traits
    CounselorTraitLevel composure;
    CounselorTraitLevel luck;
    CounselorTraitLevel intelligence;
    CounselorTraitLevel speed;
    CounselorTraitLevel stamina;
    CounselorTraitLevel stealth;
    CounselorTraitLevel strength;


    CounselorProfile(String name, F13Skin sk, F13Level level, int order, CounselorTraitLevel c, CounselorTraitLevel l, CounselorTraitLevel i, CounselorTraitLevel sp, CounselorTraitLevel sta, CounselorTraitLevel ste, CounselorTraitLevel str) {
        counselorName = name;
        requiredLevel = level;
        displayOrder = order;
        skin = sk;

        //Traits
        composure = c;
        luck = l;
        intelligence = i;
        speed = sp;
        stamina = sta;
        stealth = ste;
        strength = str;
    }

    /**
     * Returns the display name of the profile
     *
     * @return
     */
    public String getDisplayName() {
        return counselorName;
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
     * Returns the numerical representation of the profile order
     *
     * @return
     */
    public int getOrder() {
        return displayOrder;
    }

    public F13Skin getSkin() {
        return skin;
    }

    public CounselorTraitLevel getComposure() {
        return composure;
    }

    public CounselorTraitLevel getLuck() {
        return luck;
    }

    public CounselorTraitLevel getIntelligence() {
        return intelligence;
    }

    public CounselorTraitLevel getSpeed() {
        return speed;
    }

    public CounselorTraitLevel getStamina() {
        return stamina;
    }

    public CounselorTraitLevel getStealth() {
        return stealth;
    }

    public CounselorTraitLevel getStrength() {
        return strength;
    }
}
