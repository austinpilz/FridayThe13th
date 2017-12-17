package com.AustinPilz.FridayThe13th.Components.Characters;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Skin.SkinChange;
import com.AustinPilz.FridayThe13th.Components.Skin.SkinChange_0_0;
import com.AustinPilz.FridayThe13th.Components.Skin.SkinChange_1_12;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.entity.Player;

public class F13Character {
    protected Player player;
    protected F13Player f13Player;
    protected Arena arena;
    protected SkinChange skin;

    //Restore values
    protected float originalWalkSpeed;
    protected float originalFlySpeed;
    protected boolean originalAllowFly;

    public F13Character(Player player, Arena arena) {
        //
        this.f13Player = FridayThe13th.playerController.getPlayer(player);

        //Restore Values
        originalWalkSpeed = player.getWalkSpeed();
        originalFlySpeed = player.getFlySpeed();
        originalAllowFly = player.getAllowFlight();

        //Skin Change
        if (FridayThe13th.serverVersion.equalsIgnoreCase("v1_12_R1")) {
            skin = new SkinChange_1_12(getPlayer());
        } else {
            skin = new SkinChange_0_0(getPlayer());
        }
    }

    public Player getPlayer() {
        return player;
    }

    public F13Player getF13Player() {
        return f13Player;
    }

    public Arena getArena() {
        return arena;
    }

    public float getOriginalWalkSpeed() {
        return originalWalkSpeed;
    }

    public float getOriginalFlySpeed() {
        return originalFlySpeed;
    }

    public boolean isOriginalAllowFly() {
        return originalAllowFly;
    }

    /**
     * Restores the players original speed values
     */
    public void restoreOriginalSpeeds() {
        if (player.isOnline()) {
            player.setFlySpeed(originalFlySpeed);
            player.setWalkSpeed(originalWalkSpeed);
            player.setAllowFlight(originalAllowFly);
        }
    }

}
