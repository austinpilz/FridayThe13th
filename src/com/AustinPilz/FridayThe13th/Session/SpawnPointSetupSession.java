package com.AustinPilz.FridayThe13th.Session;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpawnPointSetupSession
{
    private Arena arena;
    private Player player;
    private Location spawnLocation;
    private int state;

    public SpawnPointSetupSession(Arena arena, String playerUUID)
    {
        this.arena = arena;
        this.player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        this.state = 0;
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
                pointSelected();
                break;
        }
    }

    private void beginSelection()
    {
        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Arena " + ChatColor.RED + this.arena.getArenaName() + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "To add spawn point, go to desired point and execute " + ChatColor.GREEN + "/f13 here" + ChatColor.WHITE + " to use your current location.");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
    }

    private void pointSelected()
    {
        if (arena.isLocationWithinArenaBoundaries(player.getLocation())) {
            this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
            this.player.sendMessage(ChatColor.WHITE + "Arena " + ChatColor.RED + this.arena.getArenaName() + ChatColor.WHITE + ":");
            this.player.sendMessage("");
            this.player.sendMessage(ChatColor.WHITE + "Spawn point selected " + ChatColor.GREEN + "successfully" + ChatColor.WHITE + ".");
            this.player.sendMessage(ChatColor.RED + "--------------------------------------");
            this.state++;
            spawnLocation = player.getLocation();

            //Add to database
            try {
                //Add to DB
                FridayThe13th.inputOutput.storeSpawnPoint(this.arena, spawnLocation);

                //Add to arena's location manager
                this.arena.getLocationManager().addStartingPoint(spawnLocation);
            } catch (SaveToDatabaseException exception) {
                player.sendMessage(FridayThe13th.pluginAdminPrefix + "Spawn point setup FAILED due to a database issue.");
            } finally {
                //Terminate session
                FridayThe13th.spawnPointCreationManager.removePlayerSetupSession(player.getUniqueId().toString());
            }
        }
        else
        {
            //The spawn point is not within the arena
            player.sendMessage(FridayThe13th.pluginAdminPrefix + "Spawn point setup " + ChatColor.RED + " FAILED" + ChatColor.WHITE + ". The spawn location must be within the arena boundaries.");

            //Terminate session
            FridayThe13th.spawnPointCreationManager.removePlayerSetupSession(player.getUniqueId().toString());
        }
    }


}
