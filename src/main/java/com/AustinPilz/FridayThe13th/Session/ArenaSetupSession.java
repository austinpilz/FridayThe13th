package com.AustinPilz.FridayThe13th.Session;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaAlreadyExistsException;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArenaSetupSession
{
    private int state;
    private String arenaName;
    private String playerUUID;
    private Player player;
    private Location arenaBoundary1;
    private Location arenaBoundary2;
    private Location waitingLocation;
    private Location returnLocation;

    public ArenaSetupSession(String playerUUID, String arenaName)
    {
        this.state = 0;
        this.playerUUID = playerUUID;
        this.arenaName = arenaName;
        this.player = Bukkit.getPlayer(UUID.fromString(this.playerUUID));
        this.selectionMade();

    }

    public void selectionMade()
    {
        switch (this.state)
        {
            case 0:
                beginArenaCreation();
                break;
            case 1:
                lowestBoundarySelected();
                break;
            case 2:
                highestBoundarySelected();
                break;
            case 3:
                waitingLocationSelected();
                break;
            case 4:
                returnLocationSelected();
                break;
            case 5:
                jasonLocationSelected();
                break;
        }
    }

    private void beginArenaCreation()
    {
        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + FridayThe13th.language.get(player, "setup.arena.begin", "To begin the setup process for arena") + " " + ChatColor.RED + this.arenaName + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + FridayThe13th.language.get(player, "setup.arena.begin2", "Go to the lowest point of the arena boundary and execute {0} to use your current location.", ChatColor.AQUA + "/f13 here" + ChatColor.WHITE));
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;

    }

    private void lowestBoundarySelected()
    {
        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arenaName + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "Lowest point selected. Go to the highest point of the arena boundary and execute " + ChatColor.GREEN + "/f13 here" + ChatColor.WHITE + " to use your current location.");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
        this.arenaBoundary1 = player.getLocation();
    }

    private void highestBoundarySelected()
    {
        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arenaName + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "Highest point selected. Go to where players will wait for other players before the game begins and execute " + ChatColor.GREEN + "/f13 here" + ChatColor.WHITE + " to use your current location.");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
        this.arenaBoundary2 = player.getLocation();
    }

    private void waitingLocationSelected()
    {
        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arenaName + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "Waiting location selected. Go to where players will be sent after the game ends and execute " + ChatColor.GREEN + "/f13 here" + ChatColor.WHITE + " to use your current location.");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
        this.waitingLocation = player.getLocation();
    }

    private void returnLocationSelected()
    {
        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arenaName + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "Return location selected. Go to where JASON will start the game and execute " + ChatColor.GREEN + "/f13 here" + ChatColor.WHITE + " to use your current location.");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
        this.returnLocation = player.getLocation();
    }

    private void jasonLocationSelected()
    {
        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Game " + ChatColor.RED + this.arenaName + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "Jason start location selected. Game setup " + ChatColor.GREEN + "success " + ChatColor.WHITE + ". You'll need to use commands to now add player starting location(s) and other game objects.");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
        Location jasonStartLocation = player.getLocation();

        //Create Game
        Arena arena = new Arena(arenaName, arenaBoundary1, arenaBoundary2, waitingLocation, returnLocation, jasonStartLocation, 1.8, 60, 0);

        //Attempt to store arena in database
        try
        {
            FridayThe13th.inputOutput.storeArena(arena);
            FridayThe13th.arenaController.addArena(arena);
        }
        catch (SaveToDatabaseException exception)
        {
            player.sendMessage(FridayThe13th.pluginAdminPrefix + "Game setup FAILED due to a database error.");
        }

        catch (ArenaAlreadyExistsException exception)
        {
            player.sendMessage(FridayThe13th.pluginAdminPrefix + "Game setup FAILED due to there already being an arena with that name in the controller memory.");
        }
        finally
        {
            //Terminate setup session
            FridayThe13th.arenaCreationManager.removePlayerSetupSession(this.playerUUID);
        }

    }
}
