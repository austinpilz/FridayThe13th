package com.AustinPilz.FridayThe13th.Session;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Arena.ArenaChest;
import com.AustinPilz.FridayThe13th.Components.Enum.ChestType;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChestSetupSession
{
    private Arena arena;
    private Player player;
    private String playerUUID;
    private int state;
    private ChestType chestType;

    public ChestSetupSession(Arena arena, String playerUUID, ChestType chestType)
    {
        this.arena = arena;
        this.player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        this.playerUUID = playerUUID;
        this.state = 0;
        this.chestType = chestType;
        this.selectionMade();
    }

    public void selectionMade()
    {
        switch (this.state)
        {
            case 0:
                beginSelection();
                break;
            case 1:
                chestSelected();
                break;
        }
    }

    private void beginSelection()
    {
        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arena.getName() + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "To add " + chestType.getFieldDescription().toLowerCase() + " chest, put chest in your crosshairs and execute " + ChatColor.GREEN + "/f13 here" + ChatColor.WHITE + ".");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
    }

    private void chestSelected()
    {
        Location chestLocation = player.getTargetBlock(null, 10).getLocation();

        if (chestLocation.getBlock().getType().equals(Material.CHEST))
        {
            if (!arena.getObjectManager().isLocationAChest(chestLocation)) {
                try {
                    ArenaChest newChest = new ArenaChest(arena, chestLocation, chestType);
                    FridayThe13th.inputOutput.storeChest(newChest);
                    arena.getObjectManager().addChest(newChest);

                    player.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.GREEN + "Success!" + ChatColor.WHITE + " You've added the " + chestType.getFieldDescription().toLowerCase() + " chest to " + arena.getName() + ".");
                } catch (SaveToDatabaseException exception) {
                    player.sendMessage(FridayThe13th.pluginAdminPrefix + "Error! There was an issue while attempting to save chest to the database.");
                } finally {
                    FridayThe13th.chestSetupManager.removePlayerSetupSession(playerUUID);
                }
            } else {
                player.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.RED + "Error!" + ChatColor.WHITE + " That chest has already been added to the arena.");
                FridayThe13th.chestSetupManager.removePlayerSetupSession(playerUUID);
            }
        }
        else
        {
            //It's not a chest, so cancel
            player.sendMessage(FridayThe13th.pluginAdminPrefix + "Error! You can only add a chest and you're looking at " + ChatColor.AQUA + chestLocation.getBlock().getType().toString() + ChatColor.WHITE + ".");
            FridayThe13th.chestSetupManager.removePlayerSetupSession(playerUUID);
        }
    }
}
