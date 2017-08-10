package com.AustinPilz.FridayThe13th.Components.Perk;

import com.AustinPilz.FridayThe13th.Components.Perk.F13Perk;

public class F13PerkManager {

    /**
     * Returns F13 perk object from name
     * @param name
     * @return
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
