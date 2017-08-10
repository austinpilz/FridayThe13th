package com.AustinPilz.FridayThe13th.Command;

import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Profiles.F13ProfileManager;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Components.Perk.F13PerkManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class APICommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        //f13api [add/remove] [UUID] [item] [name]

        if (sender.hasPermission("FridayThe13th.API"))
        {
            if (args.length > 0)
            {
                if (args[0] != null && args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))
                {
                    //we were given the action
                    String action = args[0];

                    if (args[1] != null && Bukkit.getOfflinePlayer(UUID.fromString(args[1])) != null)
                    {
                        //We were provided with the player
                        String playerUUID = args[1];

                        if (args[2] != null && args[2].equalsIgnoreCase("profile") || args[2].equalsIgnoreCase("perk"))
                        {
                            //We were provided with the item type and it matches one of the available options
                            String itemType = args[2];

                            if (args[3] != null)
                            {
                                //We were provided with the item name
                                String itemName = args[3];

                                if (itemType.equalsIgnoreCase("profile"))
                                {
                                    //We're dealing with profiles
                                    if (F13ProfileManager.getCounselorProfileByInternalIdentifier(itemName) != null || F13ProfileManager.getJasonProfileByInternalIdentifier(itemName) != null)
                                    {
                                        boolean counselor = false;
                                        boolean jason = false;

                                        if (F13ProfileManager.getCounselorProfileByInternalIdentifier(itemName) != null)
                                        {
                                            counselor = true;
                                        }
                                        else if (F13ProfileManager.getJasonProfileByInternalIdentifier(itemName) != null)
                                        {
                                            jason = true;
                                        }

                                        if (action.equalsIgnoreCase("add"))
                                        {
                                            if (counselor)
                                            {
                                                FridayThe13th.playerController.getPlayer(playerUUID).addPurchasedCounselorProfile(F13ProfileManager.getCounselorProfileByInternalIdentifier(itemName), true);
                                            }
                                            else if (jason)
                                            {
                                                FridayThe13th.playerController.getPlayer(playerUUID).addPurchasedJasonProfile(F13ProfileManager.getJasonProfileByInternalIdentifier(itemName), true);
                                            }
                                        }
                                        else if (action.equalsIgnoreCase("remove"))
                                        {
                                            if (counselor)
                                            {
                                                FridayThe13th.playerController.getPlayer(playerUUID).removePurchasedCounselorProfile(F13ProfileManager.getCounselorProfileByInternalIdentifier(itemName), true);
                                            }
                                            else if (jason)
                                            {
                                                FridayThe13th.playerController.getPlayer(playerUUID).removePurchasedJasonProfile(F13ProfileManager.getJasonProfileByInternalIdentifier(itemName), true);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        //Unknown profile name
                                    }
                                }
                                else if (itemType.equalsIgnoreCase("perk"))
                                {
                                    //We're dealing with perks
                                    if (F13PerkManager.getPerkByInternalIdentifier(itemName) != null)
                                    {
                                        //We found the perk
                                        if (action.equalsIgnoreCase("add"))
                                        {
                                            FridayThe13th.playerController.getPlayer(playerUUID).addPurchasedPerk(F13PerkManager.getPerkByInternalIdentifier(itemName), true);
                                        }
                                        else if (action.equalsIgnoreCase("remove"))
                                        {
                                            FridayThe13th.playerController.getPlayer(playerUUID).removePurchasedPerk(F13PerkManager.getPerkByInternalIdentifier(itemName), true);
                                        }
                                    }
                                    else
                                    {
                                        //Unknown perk name
                                    }
                                }
                            }
                            else
                            {
                                //No item name provided or unknown item name
                            }
                        }
                        else
                        {
                            //No item type provided or unknown item type
                        }
                    }
                    else
                    {
                        //No player provided
                    }
                }
                else
                {
                    //Unknown command
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.DARK_RED + "Unknown command " + ChatColor.AQUA + ChatColor.ITALIC + args[0]);
                }
            }
            else
            {
                //No arguments provided
            }
        }
        else
        {
            //They don't have permission to the API commands
            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
        }

        return true;
    }
}
