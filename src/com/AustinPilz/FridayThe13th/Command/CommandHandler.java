package com.AustinPilz.FridayThe13th.Command;


import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Components.ChestType;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaSetupSessionAlreadyInProgress;
import com.AustinPilz.FridayThe13th.Exceptions.Chest.ChestSetupSessionAlreadyInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.Exceptions.SpawnPoint.SpawnPointSetupSessionAlreadyInProgressException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 1) {
            //f13
            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.pluginName + " v" + ChatColor.GREEN + FridayThe13th.pluginVersion);
        } else {
            if (args[0].equalsIgnoreCase("setup")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    //Setup commands cannot be executed by the console
                    if (sender instanceof Player) {
                        //Correct Syntax: /f13 setup [arenaName]
                        if (args.length == 2) {
                            String arenaName = args[1];

                            //Check to see if the arena with that name already exists
                            if (!FridayThe13th.arenaController.doesArenaExist(arenaName)) {
                                //All is good, begin the setup process handled by the ArenaCreation manager
                                try {
                                    FridayThe13th.arenaCreationManager.startSetupSession(((Player) sender).getUniqueId().toString(), arenaName);
                                } catch (ArenaSetupSessionAlreadyInProgress exception) {
                                    //They already have a setup session in progress
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaSetupSessionInProgress", "You already have an arena setup session in progress. You must finish that session before starting a new one."));
                                }
                            } else {
                                //An arena with that name already exists in the arena controller memory
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaAlreadyExists", "Arena {0}{1}{2} already exists. Please choose another name and try again.", ChatColor.RED, arenaName, ChatColor.WHITE));
                            }
                        } else {
                            //Incorrect setup syntax
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setupSyntaxError", "Incorrect setup syntax. Usage: {0}", ChatColor.AQUA + "/f13 setup [arenaName]"));
                        }
                    } else {
                        //The command was sent by something other than an in-game player
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.consoleSender", "This command must be executed by an in-game player, not the console."));
                    }
                } else {
                    //No permissions
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    //Setup commands cannot be executed by the console
                    if (sender instanceof Player) {
                        //Correct Syntax: /f13 setup [arenaName]
                        if (args.length == 2) {
                            String arenaName = args[1];

                            //Check to see if the arena with that name already exists
                            if (FridayThe13th.arenaController.doesArenaExist(arenaName)) {
                                //End the game and remove the arena
                                try {
                                    Arena arena = FridayThe13th.arenaController.getArena(arenaName);
                                    arena.getGameManager().gameTimeUp();
                                    arena.getSignManager().markDeleted();
                                    FridayThe13th.arenaController.removeArena(arena);
                                    FridayThe13th.inputOutput.deleteArena(arenaName);
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.message.arenaDeleted", "Arena {0} has been deleted successfully.", ChatColor.RED + arenaName + ChatColor.WHITE));

                                } catch (ArenaDoesNotExistException exception) {
                                    //
                                }

                            } else {
                                //An arena with that name already exists in the arena controller memory
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Arena {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                            }
                        } else {
                            //Incorrect setup syntax
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.deleteSyntaxError", "Incorrect delete syntax. Usage: {0}", ChatColor.AQUA + "/f13 delete [arenaName]"));
                        }
                    } else {
                        //The command was sent by something other than an in-game player
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.consoleSender", "This command must be executed by an in-game player, not the console."));
                    }
                } else {
                    //No permissions
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("add")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    //Setup commands cannot be executed by the console
                    if (sender instanceof Player) {
                        //Correct Syntax: /f13 setup [arenaName] [object]
                        if (args.length == 3) {
                            String arenaName = args[1];

                            if (args[2].equalsIgnoreCase("spawn")) {
                                //All is good, begin the setup process handled by the ArenaCreation manager
                                try {
                                    FridayThe13th.spawnPointCreationManager.startSetupSession(((Player) sender).getUniqueId().toString(), arenaName);
                                } catch (SpawnPointSetupSessionAlreadyInProgressException exception) {
                                    //They already have a setup session in progress
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addArenaSessionExisting", "You already have an arena setup session in progress. You must finish that session before starting a new one."));
                                } catch (ArenaDoesNotExistException exception) {
                                    //An arena with that name does not exist in the arena controller memory
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Arena {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                }
                            } else if (args[2].contains("chest")) {
                                if (args[2].equalsIgnoreCase("chest:weapon")) {
                                    try {
                                        FridayThe13th.chestSetupManager.startSetupSession(((Player) sender).getUniqueId().toString(), arenaName, ChestType.Weapon);
                                    } catch (ChestSetupSessionAlreadyInProgressException exception) {
                                        //They already have a setup session in progress
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addChestWeaponSessionExisting", "You already have a weapon chest setup session in progress. You must finish that session before starting a new one."));
                                    } catch (ArenaDoesNotExistException exception) {
                                        //An arena with that name does not exist in the arena controller memory
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Arena {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                    }
                                } else if (args[2].equalsIgnoreCase("chest:item")) {
                                    try {
                                        FridayThe13th.chestSetupManager.startSetupSession(((Player) sender).getUniqueId().toString(), arenaName, ChestType.Item);
                                    } catch (ChestSetupSessionAlreadyInProgressException exception) {
                                        //They already have a setup session in progress
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addChestItemSessionExisting", "You already have an item chest setup session in progress. You must finish that session before starting a new one."));
                                    } catch (ArenaDoesNotExistException exception) {
                                        //An arena with that name does not exist in the arena controller memory
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Arena {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                    }
                                } else {
                                    //Unknown type of chest
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.chestTypeError", "Unknown chest type. Available types are {0} and {1}.", ChatColor.AQUA + "chest:weapon" + ChatColor.WHITE, ChatColor.AQUA + "chest:item" + ChatColor.WHITE));
                                }
                            } else {
                                //Unknown add command
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addItemError", "Incorrect add item. Available items are {0} and {1}.", ChatColor.AQUA + "spawn" + ChatColor.WHITE, ChatColor.AQUA + "chest" + ChatColor.WHITE));
                            }
                        } else {
                            //Incorrect setup syntax
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addSyntaxError", "Incorrect add syntax. Usage: {0}", ChatColor.AQUA + "/f13 add [arenaName] [object]"));
                        }
                    } else {
                        //The command was sent by something other than an in-game player
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.consoleSender", "This command must be executed by an in-game player, not the console."));
                    }
                } else {
                    //No permissions
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("join")) {
                if (sender.hasPermission("FridayThe13th.User")) {
                    //Setup commands cannot be executed by the console
                    if (sender instanceof Player) {
                        //Correct Syntax: /f13 setup [arenaName] [object]
                        if (args.length == 2) {
                            String arenaName = args[1];

                            //All is good, begin the play process handled by the ArenaCreation manager
                            try {
                                FridayThe13th.arenaController.getArena(arenaName).getGameManager().getPlayerManager().playerJoinGame(((Player) sender));
                            } catch (ArenaDoesNotExistException exception) {
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Arena {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                            } catch (GameFullException exception) {
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaGameFull", "The game in {0} is currently full.", ChatColor.RED + arenaName + ChatColor.WHITE));
                            } catch (GameInProgressException exception) {
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaGameInProgress", "The game in {0} is already in progress. You'll need to wait until it's over to join.", ChatColor.RED + arenaName + ChatColor.WHITE));
                            }
                        } else {
                            //Incorrect play syntax
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.playSyntaxError", "Incorrect play syntax. Usage: {0}", ChatColor.AQUA + "/f13 play [arenaName]"));
                        }
                    } else {
                        //The command was sent by something other than an in-game player
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.consoleSender", "This command must be executed by an in-game player, not the console."));
                    }
                } else {
                    //No permissions
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("arena")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    if (args.length == 2) {
                        String arenaName = args[1];

                        try {
                            Arena arena = FridayThe13th.arenaController.getArena(arenaName);

                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + "-----" + ChatColor.RED + arena.getArenaName() + ChatColor.WHITE + " -----");
                            sender.sendMessage("# Spawn Locations: " + arena.getLocationManager().getNumberStartingPoints());

                            if (arena.getGameManager().isGameEmpty()) {
                                sender.sendMessage("Game Status: " + ChatColor.RED + "Empty");
                            } else if (arena.getGameManager().isGameWaiting()) {
                                sender.sendMessage("Game Status: " + ChatColor.GOLD + "Waiting");
                            } else if (arena.getGameManager().isGameInProgress()) {
                                sender.sendMessage("Game Status: " + ChatColor.GREEN + "In Progress");
                            }

                        } catch (ArenaDoesNotExistException exception) {
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Arena {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                        }
                    } else {
                        //Incorrect setup syntax
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaSyntaxError", "Incorrect arena syntax. Usage: {0}", ChatColor.AQUA + "/f13 arena [arenaName]"));
                    }
                } else {
                    //No permissions
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("here")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    if (sender instanceof Player) {
                        if (FridayThe13th.arenaCreationManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            //Make the selection
                            FridayThe13th.arenaCreationManager.getPlayerSetupSession(((Player) sender).getUniqueId().toString()).selectionMade();
                        } else if (FridayThe13th.spawnPointCreationManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            //Make the selection
                            FridayThe13th.spawnPointCreationManager.getPlayerSetupSession(((Player) sender).getUniqueId().toString()).selectionMade();
                        } else if (FridayThe13th.chestSetupManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            FridayThe13th.chestSetupManager.getPlayerSetupSession(((Player) sender).getUniqueId().toString()).selectionMade();
                        } else {
                            //There is no active setup session
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noSetupSession", "You currently have no setup session in progress."));
                        }
                    } else {
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.consoleSender", "This command must be executed by an in-game player, not the console."));
                    }
                } else {
                    //No permissions
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("quit") || args[0].equalsIgnoreCase("leave")) {
                if (sender.hasPermission("FridayThe13th.User")) {
                    //Setup commands cannot be executed by the console
                    if (sender instanceof Player) {
                        try {
                            FridayThe13th.arenaController.getPlayerArena(((Player) sender).getUniqueId().toString()).getGameManager().getPlayerManager().onplayerQuit(((Player) sender));
                        } catch (PlayerNotPlayingException exception) {
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.senderNotPlaying", "You are not currently playing."));
                        }
                    } else {
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.consoleSender", "This command must be executed by an in-game player, not the console."));
                    }
                } else {
                    //No permissions
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("arenas")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    if (FridayThe13th.arenaController.getNumberOfArenas() > 0) {
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + "--- Arenas ---");

                        //Print all arenas
                        Iterator it = FridayThe13th.arenaController.getArenas().entrySet().iterator();
                        int count = 1;
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            Arena arena = (Arena) entry.getValue();

                            sender.sendMessage(count++ + ".) " + arena.getArenaName());
                        }
                    } else {
                        //There are no arenas
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noArenasToDisplay", "There are no arenas to display."));
                    }
                } else {
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + "--- Help Menu ---");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/setup");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/add");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/arena");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/arenas");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/here");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/join" + ChatColor.GREEN + " <or> " + ChatColor.AQUA + "play");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/leave" + ChatColor.GREEN + " <or> " + ChatColor.AQUA + "quit");
                } else if (sender.hasPermission("FridayThe13th.User")) {
                    sender.sendMessage(FridayThe13th.pluginPrefix + "--- Help Menu ---");
                    sender.sendMessage(FridayThe13th.pluginPrefix + ChatColor.AQUA + "/join" + ChatColor.GREEN + " <or> " + ChatColor.AQUA + "play");
                    sender.sendMessage(FridayThe13th.pluginPrefix + ChatColor.AQUA + "/leave" + ChatColor.GREEN + " <or> " + ChatColor.AQUA + "quit");
                } else {
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else {
                //Unknown command
                sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.DARK_RED + "Unknown command.");
            }
        }

        return true;
    }
}
