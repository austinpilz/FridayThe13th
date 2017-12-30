package com.AustinPilz.FridayThe13th.Components.Arena;

import com.AustinPilz.FridayThe13th.Components.Enum.EscapePointType;
import org.bukkit.Location;

public class EscapePoint {

    private Location boundary1;
    private Location boundary2;
    private EscapePointType type;

    /*
    Also need to have car starting location array, automatically places minecart that has hologram with what it needs and can't be ridden until then
     */

    public EscapePoint() {
        //
    }

    public boolean crossBoundaryAttempt() {
        //
        return false;
    }

}
