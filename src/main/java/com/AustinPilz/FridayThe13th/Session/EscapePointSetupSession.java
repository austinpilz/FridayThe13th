package com.AustinPilz.FridayThe13th.Session;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Enum.EscapePointType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EscapePointSetupSession {
    private Arena arena;
    private Player player;
    private String playerUUID;
    private int state;
    private EscapePointType pointType;

    public EscapePointSetupSession(Arena arena, String playerUUID, EscapePointType pointType) {
        this.arena = arena;
        this.player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        this.playerUUID = playerUUID;
        this.state = 0;
        this.pointType = pointType;
        //this.selectionMade();
    }

}
