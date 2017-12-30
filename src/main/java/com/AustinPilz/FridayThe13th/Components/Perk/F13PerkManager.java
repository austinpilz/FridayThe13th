package com.AustinPilz.FridayThe13th.Components.Perk;

public class F13PerkManager {

    /**
     * Returns F13 perk object from name
     * @param name Perk Name
     * @return F13Perk enum
     */
    public static F13Perk getPerkByInternalIdentifier(String name)
    {
        for (F13Perk perk : F13Perk.values())
        {
            if (perk.getInternalIdentifier().equalsIgnoreCase(name))
            {
                return perk;
            }
        }

        return null;
    }
}
