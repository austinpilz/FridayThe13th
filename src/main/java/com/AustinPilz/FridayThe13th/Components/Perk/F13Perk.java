package com.AustinPilz.FridayThe13th.Components.Perk;

import com.AustinPilz.FridayThe13th.Components.Enum.CharacterType;
import org.bukkit.Material;

import java.text.NumberFormat;

public enum F13Perk {

    Jason_Part2Pickaxe(CharacterType.Jason, "Part II Pickaxe", "J-Part2PickAxe", "Works only with Jason Part II.", 10000, Material.DIAMOND_PICKAXE),
    Jason_AxeThrow(CharacterType.Jason, "Axe Throw", "J-AxeThrow", "Jason throws axe to kill.", 15000, Material.DIAMOND_AXE),

    Counselor_Dramamine(CharacterType.Counselor, "Dramamine", "C-Dramamine", "Random chance to remove nausea effect.", 10000, Material.GLOWSTONE_DUST),
    Counselor_FirstAid(CharacterType.Counselor, "First Aid", "C-FirstAid", "Start every game with an antiseptic spray.", 5000, Material.GLASS_BOTTLE),
    Counselor_Radio(CharacterType.Counselor, "Sat Phone", "C-SatPhone", "Start every game with a radio.", 20000, Material.NETHER_STAR),
    Counselor_AhDarkness(CharacterType.Counselor, "Ah, Darkness", "C-AhDarkness", "Ability to turn off light switches.", 5000, Material.LEVER),
    ;

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
     * @return
     */
    public CharacterType getCharacterType() {
        return characterType;
    }

    /**
     * Returns the perk's name
     * @return
     */
    public String getName()
    {
        return perkName;
    }

    /**
     * Returns the perk's internal identifier
     * @return
     */
    public String getInternalIdentifier()
    {
        return internalIdentifier;
    }

    /**
     * Returns the perk's description
     * @return
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns the cost of the perk in customization points
     * @return
     */
    public int getCost()
    {
        return cost;
    }

    /**
     * Returns a formatted version of the perk cost
     * @return
     */
    public String getFormattedCost()
    {
        return NumberFormat.getInstance().format(cost);
    }

    /**
     * Returns the material that the perk displays as
     * @return
     */
    public Material getDisplayMaterial()
    {
        return displayMaterial;
    }
}
