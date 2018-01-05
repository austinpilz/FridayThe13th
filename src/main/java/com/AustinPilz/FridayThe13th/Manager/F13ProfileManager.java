package com.AustinPilz.FridayThe13th.Manager;


import com.AustinPilz.FridayThe13th.Components.Profiles.CounselorProfile;
import com.AustinPilz.FridayThe13th.Components.Profiles.JasonProfile;

public class F13ProfileManager {

    /**
     * Returns Jason profile from internal identifier
     * @param name
     * @return
     */
    public static JasonProfile getJasonProfileByInternalIdentifier(String name)
    {
        for (JasonProfile profile : JasonProfile.values())
        {
            if (profile.getInternalIdentifier().equalsIgnoreCase(name))
            {
                return profile;
            }
        }

        return null;
    }

    /**
     * Returns Jason profile from internal identifier
     * @param name
     * @return
     */
    public static CounselorProfile getCounselorProfileByInternalIdentifier(String name)
    {
        for (CounselorProfile profile : CounselorProfile.values())
        {
            if (profile.getInternalIdentifier().equalsIgnoreCase(name))
            {
                return profile;
            }
        }

        return null;
    }
}
