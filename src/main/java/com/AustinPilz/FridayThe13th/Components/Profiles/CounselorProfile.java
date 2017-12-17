package com.AustinPilz.FridayThe13th.Components.Profiles;

import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.Components.Level.CounselorTraitLevel;
import com.AustinPilz.FridayThe13th.Components.Level.F13PlayerLevel;
import com.AustinPilz.FridayThe13th.Components.Skin.F13Skin;

import java.util.Random;

public enum CounselorProfile {

    Adam("Adam Palomino", "C-Adam", "Adam", F13Skin.Counselor_Adam, F13PlayerLevel.L5, 1, CounselorTraitLevel.Composure_8, CounselorTraitLevel.Luck_3, CounselorTraitLevel.Intelligence_4, CounselorTraitLevel.Speed_5, CounselorTraitLevel.Stamina_4, CounselorTraitLevel.Stealth_3, CounselorTraitLevel.Strength_8, F13SoundEffect.GuyGasp),
    Brandon("Brandon Wilson", "C-Brandon", "Brandon", F13Skin.Counselor_Brandon, F13PlayerLevel.L4, 2, CounselorTraitLevel.Composure_4, CounselorTraitLevel.Luck_2, CounselorTraitLevel.Intelligence_1, CounselorTraitLevel.Speed_8, CounselorTraitLevel.Stamina_8, CounselorTraitLevel.Stealth_2, CounselorTraitLevel.Strength_10, F13SoundEffect.GuyGasp),
    Chad("Chad Kensington", "C-Chad", "Chad", F13Skin.Counselor_Chad, F13PlayerLevel.L1, 3, CounselorTraitLevel.Composure_1, CounselorTraitLevel.Luck_10, CounselorTraitLevel.Intelligence_2, CounselorTraitLevel.Speed_9, CounselorTraitLevel.Stamina_1, CounselorTraitLevel.Stealth_6, CounselorTraitLevel.Strength_3, F13SoundEffect.GuyGasp),
    Deborah("Deborah Kim", "C-Deborah", "Deborah", F13Skin.Counselor_Deborah, F13PlayerLevel.L2, 4, CounselorTraitLevel.Composure_5, CounselorTraitLevel.Luck_3, CounselorTraitLevel.Intelligence_10, CounselorTraitLevel.Speed_4, CounselorTraitLevel.Stamina_3, CounselorTraitLevel.Stealth_9, CounselorTraitLevel.Strength_1, F13SoundEffect.GirlGasp),
    Jenny("Jenny Myers", "C-Jenny", "Jenny", F13Skin.Counselor_Jenny, F13PlayerLevel.L4, 5, CounselorTraitLevel.Composure_10, CounselorTraitLevel.Luck_9, CounselorTraitLevel.Intelligence_2, CounselorTraitLevel.Speed_3, CounselorTraitLevel.Stamina_4, CounselorTraitLevel.Stealth_6, CounselorTraitLevel.Strength_1, F13SoundEffect.GirlGasp),
    AJ("A.J. Mason", "C-AJ", "A.J.", F13Skin.Counselor_AJ, F13PlayerLevel.L3, 6, CounselorTraitLevel.Composure_7, CounselorTraitLevel.Luck_1, CounselorTraitLevel.Intelligence_7, CounselorTraitLevel.Speed_4, CounselorTraitLevel.Stamina_4, CounselorTraitLevel.Stealth_10, CounselorTraitLevel.Strength_2, F13SoundEffect.GirlGasp),
    Vanessa("Vanessa Jones", "C-Vanessa", "Vanessa", F13Skin.Counselor_Vanessa, F13PlayerLevel.L3, 7, CounselorTraitLevel.Composure_3, CounselorTraitLevel.Luck_6, CounselorTraitLevel.Intelligence_2, CounselorTraitLevel.Speed_10, CounselorTraitLevel.Stamina_9, CounselorTraitLevel.Stealth_1, CounselorTraitLevel.Strength_4, F13SoundEffect.GirlGasp),
    Tiffany("Tiffany Cox", "C-Tiffany", "Tiffany", F13Skin.Counselor_Tiffany, F13PlayerLevel.L2, 8, CounselorTraitLevel.Composure_3, CounselorTraitLevel.Luck_4, CounselorTraitLevel.Intelligence_1, CounselorTraitLevel.Speed_6, CounselorTraitLevel.Stamina_9, CounselorTraitLevel.Stealth_10, CounselorTraitLevel.Strength_2, F13SoundEffect.GirlGasp),
    Eric("Eric J.R. Lachappa", "C-Eric", "Eric", F13Skin.Counselor_Eric, F13PlayerLevel.L5, 9, CounselorTraitLevel.Composure_4, CounselorTraitLevel.Luck_5, CounselorTraitLevel.Intelligence_10, CounselorTraitLevel.Speed_3, CounselorTraitLevel.Stamina_2, CounselorTraitLevel.Stealth_8, CounselorTraitLevel.Strength_3, F13SoundEffect.GuyGasp),
    Mitch("Mitch Floyd", "C-Mitch", "Mitch", F13Skin.Counselor_Mitch, F13PlayerLevel.L14, 10, CounselorTraitLevel.Composure_9, CounselorTraitLevel.Luck_2, CounselorTraitLevel.Intelligence_8, CounselorTraitLevel.Speed_3, CounselorTraitLevel.Stamina_4, CounselorTraitLevel.Stealth_6, CounselorTraitLevel.Strength_3, F13SoundEffect.GuyGasp),
    Fox("Fox", "C-Fox", "Fox", F13Skin.Counselor_Fox, F13PlayerLevel.L20, 11, CounselorTraitLevel.Composure_6, CounselorTraitLevel.Luck_3, CounselorTraitLevel.Intelligence_8, CounselorTraitLevel.Speed_4, CounselorTraitLevel.Stamina_5, CounselorTraitLevel.Stamina_4, CounselorTraitLevel.Strength_7, F13SoundEffect.GirlGasp),
    ;

    private String counselorName;
    private String internalIdentifier;
    private String shortName;
    private F13PlayerLevel requiredLevel;
    private int displayOrder;
    private F13Skin skin;
    private F13SoundEffect gaspSoundEffect;

    //Traits
    CounselorTraitLevel composure;
    CounselorTraitLevel luck;
    CounselorTraitLevel intelligence;
    CounselorTraitLevel speed;
    CounselorTraitLevel stamina;
    CounselorTraitLevel stealth;
    CounselorTraitLevel strength;


    CounselorProfile(String name, String internal, String sn, F13Skin sk, F13PlayerLevel level, int order, CounselorTraitLevel c, CounselorTraitLevel l, CounselorTraitLevel i, CounselorTraitLevel sp, CounselorTraitLevel sta, CounselorTraitLevel ste, CounselorTraitLevel str, F13SoundEffect gasp) {
        counselorName = name;
        internalIdentifier = internal;
        shortName = sn;
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
        gaspSoundEffect = gasp;

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
     * Returns the internal identifier
     * @return
     */
    public String getInternalIdentifier()
    {
        return internalIdentifier;
    }

    /**
     * Returns the short name of the profile
     * @return
     */
    public String getShortName() { return shortName; }

    /**
     * Returns the required level for the profile
     *
     * @return
     */
    public F13PlayerLevel getRequiredLevel() {
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

    public F13SoundEffect getGaspSoundEffect() {
        return gaspSoundEffect;
    }

    /**
     * Returns a random counselor profile from the available list
     *
     * @return Counselor profile
     */
    public static CounselorProfile getRandomCounselorProfile() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
