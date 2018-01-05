package com.AustinPilz.FridayThe13th.Components.Perk;

import com.AustinPilz.FridayThe13th.Components.Characters.CharacterType;
import org.bukkit.Material;

import java.text.NumberFormat;

public enum F13Perk {

    Jason_Part2Pickaxe(CharacterType.Jason, "Part II Pickaxe", "J-Part2PickAxe", "Works only with Jason Part II.", 10000, Material.DIAMOND_PICKAXE),
    Jason_AxeThrow(CharacterType.Jason, "Axe Throw", "J-AxeThrow", "Jason throws axe to kill.", 15000, Material.DIAMOND_AXE),

    Counselor_Dramamine(CharacterType.Counselor, "Dramamine", "C-Dramamine", "No more nausea effect.", 10000, Material.GLOWSTONE_DUST),
    Counselor_FirstAid(CharacterType.Counselor, "First Aid", "C-FirstAid", "Start every game with an antiseptic spray.", 5000, Material.GLASS_BOTTLE),
    Counselor_Radio(CharacterType.Counselor, "Sat Phone", "C-SatPhone", "Start every game with a radio.", 20000, Material.NETHER_STAR),
    Counselor_AhDarkness(CharacterType.Counselor, "Ah, Darkness", "C-AhDarkness", "Ability to turn off light switches.", 5000, Material.LEVER),
    ;

    //Counselor - Low Profile - Less likely to be sensed
    //Counselor - Medic - Get 2 uses out of every potion

    private CharacterType characterType;
    private String perkName;
    private String internalIdentifier;
    private String description;
    private int cost;
    private Material displayMaterial;

    F13Perk(CharacterType ct, String name, String internal, String d, int c, Material m)
    {
        characterType = ct;
        perkName = name;
        internalIdentifier = internal;
        cost = c;
        displayMaterial = m;
        description = d;
    }

    /**
     * Returns the perk's character type
     * @return Perk's character type
     */
    public CharacterType getCharacterType() {
        return characterType;
    }

    /**
     * Returns the perk's name
     * @return Perk's display name
     */
    public String getName()
    {
        return perkName;
    }

    /**
     * Returns the perk's internal identifier
     * @return Perk's internal identifier
     */
    public String getInternalIdentifier()
    {
        return internalIdentifier;
    }

    /**
     * Returns the perk's description
     * @return Perk's display description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns the cost of the perk in customization points
     * @return Perk's cost in CP
     */
    public int getCost()
    {
        return cost;
    }

    /**
     * Returns a formatted version of the perk cost
     * @return Perk's cost in CP (formatted)
     */
    public String getFormattedCost()
    {
        return NumberFormat.getInstance().format(cost);
    }

    /**
     * Returns the material that the perk displays as
     * @return Perk's display material
     */
    public Material getDisplayMaterial()
    {
        return displayMaterial;
    }
}
