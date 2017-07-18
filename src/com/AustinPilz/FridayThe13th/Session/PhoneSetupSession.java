package com.AustinPilz.FridayThe13th.Session;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Arena.ArenaPhone;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class PhoneSetupSession
{
    private Arena arena;
    private Player player;
    private String playerUUID;
    private int state;

    public PhoneSetupSession(Arena arena, String playerUUID)
    {
        this.arena = arena;
        this.player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        this.playerUUID = playerUUID;
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
                phoneSelected();
                break;
        }
    }

    private void beginSelection()
    {
        this.player.sendMessage(ChatColor.RED + "----------Friday The 13th----------");
        this.player.sendMessage(ChatColor.WHITE + "Arena " + ChatColor.RED + this.arena.getArenaName() + ChatColor.WHITE + ":");
        this.player.sendMessage("");
        this.player.sendMessage(ChatColor.WHITE + "To add phone, put it in your crosshairs and execute " + ChatColor.GREEN + "/f13 here" + ChatColor.WHITE + ".");
        this.player.sendMessage(ChatColor.RED + "--------------------------------------");
        this.state++;
    }

    private void phoneSelected()
    {
        Location phoneLocation = player.getTargetBlock((HashSet<Material>)null, 10).getLocation();

        if (phoneLocation.getBlock().getType().equals(Material.TRIPWIRE_HOOK))
        {
            try
            {
                ArenaPhone phone = new ArenaPhone(arena, phoneLocation);
                arena.getObjectManager().addPhone(phone);
                FridayThe13th.inputOutput.storePhone(phone);
                FridayThe13th.phoneSetupManager.removePlayerSetupSession(playerUUID);

                player.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.GREEN + "Success!" + ChatColor.WHITE + " You've added the phone to " + arena.getArenaName() + ".");
            }
            catch (SaveToDatabaseException exception)
            {
                player.sendMessage(FridayThe13th.pluginAdminPrefix + "Error! There was an issue while attempting to save phone to the database.");
            }
            finally
            {
                FridayThe13th.chestSetupManager.removePlayerSetupSession(playerUUID);
            }
        }
        else
        {
            //It's not a chest, so cancel
            player.sendMessage(FridayThe13th.pluginAdminPrefix + "Error! You can only add a phone (Tripwire Hook) and you're looking at " + ChatColor.AQUA + phoneLocation.getBlock().getType().toString() + ChatColor.WHITE + ".");
            FridayThe13th.phoneSetupManager.removePlayerSetupSession(playerUUID);
        }
    }


}
