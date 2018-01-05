package com.AustinPilz.FridayThe13th.Session;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Arena.EscapePoint;
import com.AustinPilz.FridayThe13th.Components.Enum.EscapePointType;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EscapePointSetupSession {
    private Arena arena;
    private Player player;
    private String playerUUID;
    private int state;
    private EscapePointType pointType;

    private Location boundary1;

    public EscapePointSetupSession(Arena arena, String playerUUID, EscapePointType pointType) {
        this.arena = arena;
        this.player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        this.playerUUID = playerUUID;
        this.state = 0;
        this.pointType = pointType;
        this.selectionMade();
    }

    public void selectionMade() {
        switch (this.state) {
            case 0:
                lowPointSelect();
                break;
            case 1:
                highPointSelect();
                break;
            case 2:
                selectionComplete();
                break;
        }
    }

    private void lowPointSelect() {
        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arena.getName() + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "To add " + pointType.getFieldDescription().toLowerCase() + " escape point, go to the lowest boundary point and execute " + ChatColor.GREEN + "/f13 here" + ChatColor.WHITE + ".");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
    }

    private void highPointSelect() {
        boundary1 = player.getLocation();

        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arena.getName() + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "Now go to the highest boundary and execute " + ChatColor.GREEN + "/f13 here" + ChatColor.WHITE + ".");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
    }

    private void selectionComplete() {
        EscapePoint escapePoint = new EscapePoint(arena, pointType, boundary1, player.getLocation());

        try {
            FridayThe13th.inputOutput.storeEscapePoint(escapePoint);
            arena.getLocationManager().getEscapePointManager().addEscapePoint(escapePoint);
        } catch (SaveToDatabaseException exception) {
            this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
            this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arena.getName() + ChatColor.WHITE + ":");
            this.player.sendMessage("");
            this.player.sendMessage(ChatColor.WHITE + "Escape point setup " + ChatColor.RED + "failed" + ChatColor.WHITE + ". See console.");
            this.player.sendMessage(ChatColor.RED + "--------------------------------------");
            this.state++;
        } finally {
            FridayThe13th.escapePointSetupManager.removePlayerSetupSession(playerUUID);
        }

        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arena.getName() + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "Escape point setup " + ChatColor.GREEN + "success" + ChatColor.WHITE + ".");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
    }

}
