package com.AustinPilz.FridayThe13th.Session;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Vehicle.F13Boat;
import com.AustinPilz.FridayThe13th.Components.Vehicle.F13Car;
import com.AustinPilz.FridayThe13th.Components.Vehicle.VehicleType;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class VehicleSetupSession {
    private Arena arena;
    private Player player;
    private String playerUUID;
    private int state;
    private VehicleType vehicleType;

    public VehicleSetupSession(Arena arena, String playerUUID, VehicleType vehicleType) {
        this.arena = arena;
        this.player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        this.playerUUID = playerUUID;
        this.state = 0;
        this.vehicleType = vehicleType;
        this.selectionMade();
    }

    public void selectionMade() {
        switch (this.state) {
            case 0:
                beginSelection();
                break;
            case 1:
                locationSelected();
                break;
        }
    }

    private void beginSelection() {
        this.player.sendMessage(ChatColor.RED + "------------Friday The 13th-----------");
        this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arena.getName() + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "To add " + vehicleType.name().toLowerCase() + ", hover above the location and execute " + ChatColor.GREEN + "/f13 here" + ChatColor.WHITE + ".");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
    }

    private void locationSelected() {
        try {
            if (vehicleType.equals(VehicleType.Car)) {
                F13Car car = new F13Car(arena, player.getLocation());
                FridayThe13th.inputOutput.storeVehicle(car);
                arena.getObjectManager().getVehicleManager().addCar(car);
            } else if (vehicleType.equals(VehicleType.Boat)) {
                F13Boat boat = new F13Boat(arena, player.getLocation());
                FridayThe13th.inputOutput.storeVehicle(boat);
                arena.getObjectManager().getVehicleManager().addBoat(boat);
            }

            player.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.GREEN + "Success!" + ChatColor.WHITE + " You've added the " + vehicleType.name().toLowerCase() + " to " + arena.getName() + ".");
        } catch (SaveToDatabaseException exception) {
            player.sendMessage(FridayThe13th.pluginAdminPrefix + "Error! There was an issue while attempting to save the vehicle to the database.");
        } finally {
            FridayThe13th.vehicleSetupManager.removePlayerSetupSession(playerUUID);
        }
    }
}

