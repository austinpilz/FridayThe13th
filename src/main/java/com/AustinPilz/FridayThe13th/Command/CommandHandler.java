package com.AustinPilz.FridayThe13th.Command;


import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Enum.ChestType;
import com.AustinPilz.FridayThe13th.Components.Enum.EscapePointType;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Level.F13PlayerLevel;
import com.AustinPilz.FridayThe13th.Components.Vehicle.VehicleType;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaSetupSessionAlreadyInProgress;
import com.AustinPilz.FridayThe13th.Exceptions.Chest.ChestSetupSessionAlreadyInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.EscapePointSessionAlreadyInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.PhoneSetupSessionAlreadyInProgressException;
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
            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.pluginName + " version " + ChatColor.GREEN + FridayThe13th.pluginVersion + ChatColor.WHITE + " by austinpilz - " + FridayThe13th.pluginURL);
            sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.GREEN + "Type " + ChatColor.AQUA + "/F13 help " + ChatColor.GREEN + "for help.");
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
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaAlreadyExists", "Game {0}{1}{2} already exists. Please choose another name and try again.", ChatColor.RED, arenaName, ChatColor.WHITE));
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
            } else if (args[0].equalsIgnoreCase("test")) {
                //
            } else if (args[0].equalsIgnoreCase("memory")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + "-- F13 Active Memory Storage --");
                    sender.sendMessage("# F13 Players: " + FridayThe13th.playerController.getNumPlayers());
                    sender.sendMessage("# Arenas: " + FridayThe13th.arenaController.getNumberOfArenas());
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

                                    if (!arena.getGameManager().isGameEmpty()) {
                                        arena.getGameManager().gameTimeUp();
                                    }

                                    arena.delete();
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.message.arenaDeleted", "Game {0} has been deleted successfully.", ChatColor.RED + arenaName + ChatColor.WHITE));

                                } catch (ArenaDoesNotExistException exception) {
                                    //
                                }

                            } else {
                                //An arena with that name already exists in the arena controller memory
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
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
            } else if (args[0].equalsIgnoreCase("tutorial")) {
                if (sender.hasPermission("FridayThe13th.User")) {
                    //Setup commands cannot be executed by the console
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("jason")) {
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.jasonHeader", ChatColor.STRIKETHROUGH + "---" + ChatColor.RESET + "F13 Jason Tutorial" + ChatColor.STRIKETHROUGH + "---"));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.jason1", "- You are Jason Voorhees. Your mission is to kill all of the counselors."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.jason2", "- Stalk by crouching to vanish and sneak up on counselors."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.jason3", "- Use sense potion to see players through terrain."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.jason4", "- Fly to use warp."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.jason5", "- Use your axe to break down doors, power switches and windows."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.jason6", "- Set traps to ensnare counselors."));
                        } else if (args[1].equalsIgnoreCase("counselor")) {
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselorHeader", ChatColor.STRIKETHROUGH + "---" + ChatColor.RESET + "F13 Counselor Tutorial" + ChatColor.STRIKETHROUGH + "---"));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselor1", "- You are a counselor. Run, hide, and fight for your life."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselor2", "- Chat is proximity based. Spectators can hear you, but you cannot hear them."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselor3", "- Your fear level is calculated by the light level and distance from Jason. When you're scared, Jason can sense you."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselor4", "- Be mindful of your stamina level."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselor5", "- Loot chests for items and weapons. Hitting Jason with a weapon will temporarily stun him."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselor6", "- Jump through windows by interacting with them and standing still for 2 seconds. Sprint and interact to immediately jump."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselor7", "- Find radios to communicate with other radio wielding counselors regardless of distance."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselor8", "- Find the active phone and call Tommy Jarvis to bring a player back from the dead."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselor9", "- Repair broken items with repair wire."));
                            sender.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(sender, "command.tutorial.counselor10", "- Have fun."));
                        } else {
                            //Incorrect syntax
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.tutorialSyntaxError", "Incorrect tutorial syntax. Usage: {0}", ChatColor.AQUA + "/f13 tutorial [jason/counselor]"));
                        }
                    } else {
                        //Incorrect syntax
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.tutorialSyntaxError", "Incorrect tutorial syntax. Usage: {0}", ChatColor.AQUA + "/f13 tutorial [jason/counselor]"));
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
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                }
                            } else if (args[2].equalsIgnoreCase("phone")) {
                                //All is good, begin the setup process handled by the ArenaCreation manager
                                try {
                                    FridayThe13th.phoneSetupManager.startSetupSession(((Player) sender).getUniqueId().toString(), arenaName);
                                } catch (PhoneSetupSessionAlreadyInProgressException exception) {
                                    //They already have a setup session in progress
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addPhoneSessionExisting", "You already have an phone setup session in progress. You must finish that session before starting a new one."));
                                } catch (ArenaDoesNotExistException exception) {
                                    //An arena with that name does not exist in the arena controller memory
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
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
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                    }
                                } else if (args[2].equalsIgnoreCase("chest:item")) {
                                    try {
                                        FridayThe13th.chestSetupManager.startSetupSession(((Player) sender).getUniqueId().toString(), arenaName, ChestType.Item);
                                    } catch (ChestSetupSessionAlreadyInProgressException exception) {
                                        //They already have a setup session in progress
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addChestItemSessionExisting", "You already have an item chest setup session in progress. You must finish that session before starting a new one."));
                                    } catch (ArenaDoesNotExistException exception) {
                                        //An arena with that name does not exist in the arena controller memory
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                    }
                                } else {
                                    //Unknown type of chest
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.chestTypeError", "Unknown chest type. Available types are {0} and {1}.", ChatColor.AQUA + "chest:weapon" + ChatColor.WHITE, ChatColor.AQUA + "chest:item" + ChatColor.WHITE));
                                }
                            } else if (args[2].contains("vehicle")) {
                                if (args[2].equalsIgnoreCase("vehicle:car")) {
                                    try {
                                        FridayThe13th.vehicleSetupManager.startSetupSession(((Player) sender).getUniqueId().toString(), arenaName, VehicleType.Car);
                                    } catch (ChestSetupSessionAlreadyInProgressException exception) {
                                        //They already have a setup session in progress
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addVehicleSessionExisting", "You already have a vehicle setup session in progress. You must finish that session before starting a new one."));
                                    } catch (ArenaDoesNotExistException exception) {
                                        //An arena with that name does not exist in the arena controller memory
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                    }
                                } else if (args[2].equalsIgnoreCase("vehicle:boat")) {
                                    try {
                                        FridayThe13th.vehicleSetupManager.startSetupSession(((Player) sender).getUniqueId().toString(), arenaName, VehicleType.Boat);
                                    } catch (ChestSetupSessionAlreadyInProgressException exception) {
                                        //They already have a setup session in progress
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addVehicleSessionExisting", "You already have a vehicle setup session in progress. You must finish that session before starting a new one."));
                                    } catch (ArenaDoesNotExistException exception) {
                                        //An arena with that name does not exist in the arena controller memory
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                    }
                                } else {
                                    //Unknown type of chest
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.vehicleTypeError", "Unknown vehicle type. Available types are {0} and {1}.", ChatColor.AQUA + "vehicle:car" + ChatColor.WHITE, ChatColor.AQUA + "vehicle:weapon" + ChatColor.WHITE));
                                }
                            } else if (args[2].contains("escape")) {
                                if (args[2].equalsIgnoreCase("escape:land")) {
                                    try {
                                        FridayThe13th.escapePointSetupManager.startSetupSession(((Player) sender).getUniqueId().toString(), arenaName, EscapePointType.Land);
                                    } catch (EscapePointSessionAlreadyInProgressException exception) {
                                        //They already have a setup session in progress
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addEscapeSessionExisting", "You already have an escape point setup session in progress. You must finish that session before starting a new one."));
                                    } catch (ArenaDoesNotExistException exception) {
                                        //An arena with that name does not exist in the arena controller memory
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                    }
                                } else if (args[2].equalsIgnoreCase("escape:water")) {
                                    try {
                                        FridayThe13th.escapePointSetupManager.startSetupSession(((Player) sender).getUniqueId().toString(), arenaName, EscapePointType.Water);
                                    } catch (EscapePointSessionAlreadyInProgressException exception) {
                                        //They already have a setup session in progress
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addEscapeSessionExisting", "You already have an escape point setup session in progress. You must finish that session before starting a new one."));
                                    } catch (ArenaDoesNotExistException exception) {
                                        //An arena with that name does not exist in the arena controller memory
                                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                    }
                                } else {
                                    //Unknown type of chest
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.chestTypeError", "Unknown chest type. Available types are {0} and {1}.", ChatColor.AQUA + "chest:weapon" + ChatColor.WHITE, ChatColor.AQUA + "chest:item" + ChatColor.WHITE));
                                }
                            } else {
                                //Unknown add command
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.addItemError", "Incorrect add item. Available items are {0}, {1}, and {2}", ChatColor.AQUA + "spawn" + ChatColor.WHITE, ChatColor.AQUA + "chest" + ChatColor.WHITE, ChatColor.AQUA + "phone"));
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
            } else if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    //Correct Syntax: /f13 setup [arenaName] [object]
                    if (args.length == 4) {
                        String arenaName = args[1];
                        try {
                            Arena arena = FridayThe13th.arenaController.getArena(arenaName);

                            if (args[2].equalsIgnoreCase("tpc")) {
                                //Setting time per counselor
                                try {
                                    Double minutes = Double.parseDouble(args[3]);
                                    minutes = Math.max(1.8, minutes);

                                    arena.setMinutesPerCounselor(minutes);
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.success.setField", "Success! Minutes per counselor in {0} have been set to {1}.", ChatColor.AQUA + arena.getName() + ChatColor.WHITE, ChatColor.GREEN + "" + minutes + ChatColor.WHITE));
                                } catch (NumberFormatException e) {
                                    //They didn't provide numbers
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setValueSyntaxError", "Error. The value of the field must be a number."));
                                }
                            } else if (args[2].equalsIgnoreCase("wait")) {
                                //Setting time per counselor
                                try {
                                    int seconds = Integer.parseInt(args[3]);
                                    seconds = Math.max(1, seconds);

                                    arena.setSecondsWaitingRoom(seconds);
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.success.setWaitingRoomTime", "Success! Waiting room time in {0} has been set to {1} seconds.", ChatColor.AQUA + arena.getName() + ChatColor.WHITE, ChatColor.GREEN + "" + seconds + ChatColor.WHITE));
                                } catch (NumberFormatException e) {
                                    //They didn't provide numbers
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setValueSyntaxError", "Error. The value of the field must be a number."));
                                }
                            } else {
                                //Unknown field
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setFieldSyntaxError", "Unknown field to set."));
                            }
                        } catch (ArenaDoesNotExistException exception) {
                            //Game does not exist
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                        }
                    } else {
                        //Incorrect set syntax
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setSyntaxError", "Incorrect set syntax. Usage: {0}", ChatColor.AQUA + "/f13 set [arenaName] [field] [value]"));
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
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                            } catch (GameFullException exception) {
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaGameFull", "The game in {0} is currently full.", ChatColor.RED + arenaName + ChatColor.WHITE));
                            } catch (GameInProgressException exception) {
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaGameInProgress", "The game in {0} is already in progress. You'll need to wait until it's over to join.", ChatColor.RED + arenaName + ChatColor.WHITE));
                            }
                        } else if (args.length == 1) {
                            //Auto-join
                            if (!FridayThe13th.arenaController.playerAutoJoin((Player)sender)) {
                                //There was an issue with auto-join
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.autoJoinError", "There was an error while attempting to auto-join you to an arena. Please try again."));
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
            } else if (args[0].equalsIgnoreCase("spectate")) {
                if (sender.hasPermission("FridayThe13th.User")) {
                    //Setup commands cannot be executed by the console
                    if (sender instanceof Player) {
                        //Correct Syntax: /f13 setup [arenaName] [object]
                        if (args.length == 2) {
                            String arenaName = args[1];

                            //All is good, begin the play process handled by the ArenaCreation manager
                            try {
                                Arena arena = FridayThe13th.arenaController.getArena(arenaName);

                                if (arena.getGameManager().isGameInProgress()) {
                                    arena.getGameManager().getPlayerManager().becomeSpectator((Player) sender);
                                } else {
                                    //You can't spectate if the game isn't in progress
                                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.spectateNotInProgress", "The game in {0} is not in progress. You can't spectate until the game has begun.", ChatColor.RED + arenaName + ChatColor.WHITE));
                                }
                            } catch (ArenaDoesNotExistException exception) {
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                            }
                        } else {
                            //Incorrect play syntax
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.spectateSyntaxError", "Incorrect spectate syntax. Usage: {0}", ChatColor.AQUA + "/f13 spectate [arenaName]"));
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

                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.STRIKETHROUGH + "-----" + ChatColor.RESET + ChatColor.RED + arena.getName() + ChatColor.WHITE + ChatColor.STRIKETHROUGH + " -----");

                            if (arena.getGameManager().isGameEmpty()) {
                                sender.sendMessage("Game Status: " + ChatColor.RED + "Empty");
                            } else if (arena.getGameManager().isGameWaiting()) {
                                sender.sendMessage("Game Status: " + ChatColor.GOLD + "Waiting");
                            } else if (arena.getGameManager().isGameInProgress()) {
                                sender.sendMessage("Game Status: " + ChatColor.GREEN + "In Progress");

                                int rem = arena.getGameManager().getGameTimeLeft() % 3600;
                                int mn = rem / 60;
                                int sec = rem % 60;
                                sender.sendMessage("Time Left: " + mn + "m " + sec + "sec");
                                sender.sendMessage("# Players: " + arena.getGameManager().getPlayerManager().getNumPlayers());

                                if (arena.getGameManager().getPlayerManager().getNumSpectators() > 0) {
                                    sender.sendMessage("# Spectators: " + arena.getGameManager().getPlayerManager().getNumSpectators());
                                }

                                sender.sendMessage(ChatColor.STRIKETHROUGH + "--------------");
                            }

                            //Spawn locations
                            if (arena.getLocationManager().getNumberStartingPoints() > 0) {
                                sender.sendMessage("# Spawn Locations: " + arena.getLocationManager().getNumberStartingPoints());
                            }

                            //Item chests
                            if (arena.getObjectManager().getNumChestsItems() > 0) {
                                sender.sendMessage("# Item Chests: " + arena.getObjectManager().getNumChestsItems());
                            }

                            //Weapon chests
                            if (arena.getObjectManager().getNumChestsWeapon() > 0) {
                                sender.sendMessage("# Weapon Chests: " + arena.getObjectManager().getNumChestsWeapon());
                            }

                            //Phones
                            if (arena.getObjectManager().getPhoneManager().getNumberOfPhones() > 0) {
                                sender.sendMessage("# Phones: " + arena.getObjectManager().getPhoneManager().getNumberOfPhones());
                            }

                            sender.sendMessage("# Minutes per Counselor: " + arena.getMinutesPerCounselor());
                            sender.sendMessage("# Waiting Room Seconds: " + arena.getSecondsWaitingRoom());
                            sender.sendMessage("# Lifetime Games: " + arena.getNumLifetimeGames());

                            //Cars
                            if (arena.getObjectManager().getVehicleManager().getNumCars() > 0) {
                                sender.sendMessage("# Cars: " + arena.getObjectManager().getVehicleManager().getNumCars());
                            }

                            //Boats
                            if (arena.getObjectManager().getVehicleManager().getNumBoats() > 0) {
                                sender.sendMessage("# Boats: " + arena.getObjectManager().getVehicleManager().getNumBoats());
                            }

                            if (arena.getLocationManager().getEscapePointManager().getNumberOfLandEscapePoints() > 0) {
                                sender.sendMessage("# Land Escape Points: " + arena.getLocationManager().getEscapePointManager().getNumberOfLandEscapePoints());
                            }

                            if (arena.getLocationManager().getEscapePointManager().getNumberOfWaterEscapePoints() > 0) {
                                sender.sendMessage("# Water Escape Points: " + arena.getLocationManager().getEscapePointManager().getNumberOfWaterEscapePoints());
                            }


                        } catch (ArenaDoesNotExistException exception) {
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                        }
                    } else {
                        //Incorrect setup syntax
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaSyntaxError", "Incorrect arena syntax. Usage: {0}", ChatColor.AQUA + "/f13 arena [arenaName]"));
                    }
                } else {
                    //No permissions
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("end")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    if (args.length == 2) {
                        String arenaName = args[1];

                        try {
                            Arena arena = FridayThe13th.arenaController.getArena(arenaName);

                            if (arena.getGameManager().isGameInProgress()) {
                                arena.getGameManager().gameTimeUp();
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.gameEnded", "Game in {0} ended.", ChatColor.RED + arenaName + ChatColor.WHITE));
                            } else {
                                //The game is not in progress, thus we can't end it
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noGameToEnd", "There is no in progress game in {0} to end.", ChatColor.RED + arenaName + ChatColor.WHITE));
                            }
                        } catch (ArenaDoesNotExistException exception) {
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.arenaDoesNotExist", "Game {0} does not exist.", ChatColor.RED + arenaName + ChatColor.WHITE));
                        }
                    } else {
                        //Incorrect setup syntax
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.endSyntaxError", "Incorrect end syntax. Usage: {0}", ChatColor.AQUA + "/f13 end [arenaName]"));
                    }
                } else {
                    //No permissions
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    if (args.length == 2) {
                        String playerName = args[1];

                        if (Bukkit.getOfflinePlayer(playerName).isOnline()) {
                            //The player is online
                            try {
                                Player player = (Player) Bukkit.getOfflinePlayer(playerName);

                                FridayThe13th.arenaController.getPlayerArena(Bukkit.getOfflinePlayer(playerName).getUniqueId().toString()).getGameManager().getPlayerManager().onplayerQuit(player);

                                //Let the kicker know we kicked them, let the kickee know they were kicked
                                player.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(player, "command.playerWasKicked", "You were kicked from your game."));
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + playerName + " " + FridayThe13th.language.get(sender, "command.playerKicked", "has been kicked from their game."));
                            } catch (PlayerNotPlayingException exception) {
                                //The player is not playing
                                sender.sendMessage(FridayThe13th.pluginAdminPrefix + playerName + " " + FridayThe13th.language.get(sender, "command.error.kickPlayerNotPlaying", "is not currently playing F13."));
                            }
                        } else {
                            //The player is not online
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + playerName + " " + FridayThe13th.language.get(sender, "command.error.kickPlayerNotOnline", ChatColor.DARK_RED +"is not online."));
                        }

                    } else {
                        //Incorrect kick syntax
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.kickSyntaxError", "Incorrect kick syntax. Usage: {0}", ChatColor.GREEN + "/f13 kick" + ChatColor.AQUA + "[playerName]"));
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
                        } else if (FridayThe13th.phoneSetupManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            FridayThe13th.phoneSetupManager.getPlayerSetupSession(((Player) sender).getUniqueId().toString()).selectionMade();
                        } else if (FridayThe13th.escapePointSetupManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            FridayThe13th.escapePointSetupManager.getPlayerSetupSession(((Player) sender).getUniqueId().toString()).selectionMade();
                        } else if (FridayThe13th.vehicleSetupManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            FridayThe13th.vehicleSetupManager.getPlayerSetupSession(((Player) sender).getUniqueId().toString()).selectionMade();
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
            } else if (args[0].equalsIgnoreCase("cancel")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    if (sender instanceof Player) {
                        if (FridayThe13th.arenaCreationManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            FridayThe13th.arenaCreationManager.removePlayerSetupSession(((Player) sender).getUniqueId().toString());
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setupSessionCanceled", "Setup session canceled"));
                        } else if (FridayThe13th.spawnPointCreationManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            FridayThe13th.spawnPointCreationManager.removePlayerSetupSession(((Player) sender).getUniqueId().toString());
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setupSessionCanceled", "Setup session canceled"));
                        } else if (FridayThe13th.chestSetupManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            FridayThe13th.chestSetupManager.removePlayerSetupSession(((Player) sender).getUniqueId().toString());
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setupSessionCanceled", "Setup session canceled"));
                        } else if (FridayThe13th.phoneSetupManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            FridayThe13th.phoneSetupManager.removePlayerSetupSession(((Player) sender).getUniqueId().toString());
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setupSessionCanceled", "Setup session canceled"));
                        } else if (FridayThe13th.escapePointSetupManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            FridayThe13th.escapePointSetupManager.removePlayerSetupSession(((Player) sender).getUniqueId().toString());
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setupSessionCanceled", "Setup session canceled"));
                        } else if (FridayThe13th.vehicleSetupManager.doesUserHaveActiveSession(((Player) sender).getUniqueId().toString())) {
                            FridayThe13th.vehicleSetupManager.removePlayerSetupSession(((Player) sender).getUniqueId().toString());
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.setupSessionCanceled", "Setup session canceled"));
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
                            Arena arena = FridayThe13th.arenaController.getPlayerArena(((Player) sender).getUniqueId().toString());
                            if (arena.getGameManager().getPlayerManager().isJustSpectator(((Player) sender).getUniqueId().toString())) {
                                //They're only a spectator - not prev a player
                                arena.getGameManager().getPlayerManager().leaveSpectator((Player) sender);
                            } else {
                                arena.getGameManager().getPlayerManager().onplayerQuit(((Player) sender));
                            }
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
            } else if (args[0].equalsIgnoreCase("stats")) {
                if (sender.hasPermission("FridayThe13th.User")) {
                    if (args.length >= 1 && args.length <= 2) {
                        //Retrieve their own stats
                        String statsUsername = "";

                        if (args.length == 1) {
                            //No username provided, so get stats for sender
                            statsUsername = sender.getName();
                        } else if (args.length == 2) {
                            //Get stats for supplied player
                            statsUsername = args[1];
                        }

                        if (FridayThe13th.playerController.hasPlayerPlayed(Bukkit.getOfflinePlayer(statsUsername).getUniqueId().toString())) {
                            F13Player player = FridayThe13th.playerController.getPlayer(Bukkit.getOfflinePlayer(statsUsername).getUniqueId().toString());

                            sender.sendMessage(FridayThe13th.pluginPrefix + ChatColor.STRIKETHROUGH + ChatColor.RED + "---- " + ChatColor.RESET + "Player Stats" + ChatColor.STRIKETHROUGH + ChatColor.RED + "---- ");
                            sender.sendMessage("Player: " + statsUsername);
                            sender.sendMessage("Level: " + player.getLevel().getLevelNumber());
                            sender.sendMessage("XP: " + player.getXP());

                            if (player.getLevel().isLessThan(F13PlayerLevel.L20)) {
                                sender.sendMessage("XP until Next Level: " + (player.getNextLevel().getMinXP() - player.getXP()));
                            }

                            try {
                                Arena arena = FridayThe13th.arenaController.getPlayerArena(Bukkit.getOfflinePlayer(statsUsername).getUniqueId().toString());
                                {
                                    sender.sendMessage("Current arena: " + ChatColor.GREEN + arena.getName());
                                }
                            } catch (PlayerNotPlayingException exception) {
                                //They're not playing, so we don't care
                            }

                            //Customization points
                            if (player.getPlayerUUID().equals(Bukkit.getOfflinePlayer(sender.getName()).getUniqueId().toString())) {
                                sender.sendMessage("Customization Points: " + player.getCP());
                            }
                        } else {
                            //The player has never played F13 before
                            sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.statsPlayerError", "The player {0} has never played F13 before on this server.", ChatColor.AQUA + statsUsername + ChatColor.WHITE));
                        }
                    } else {
                        //Syntax error
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.statsSyntaxError", "Incorrect stats syntax. Usage: {0}", ChatColor.GREEN + "/f13 stats" + ChatColor.AQUA + "[playerName]"));
                    }
                } else {
                    //No permissions
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else if (args[0].equalsIgnoreCase("arenas")) {
                if (sender.hasPermission("FridayThe13th.Admin") || sender.hasPermission("FridayThe13th.*")) {
                    if (FridayThe13th.arenaController.getNumberOfArenas() > 0) {
                        sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.STRIKETHROUGH + "---" + ChatColor.RESET + "Arenas" + ChatColor.STRIKETHROUGH + "---");

                        //Print all arenas
                        Iterator it = FridayThe13th.arenaController.getArenas().entrySet().iterator();
                        int count = 1;
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            Arena arena = (Arena) entry.getValue();

                            sender.sendMessage(count++ + ".) " + arena.getName());
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
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.STRIKETHROUGH + "---" + ChatColor.RESET + "Help Menu" + ChatColor.STRIKETHROUGH + "---");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/setup [arenaName]");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/add [arenaName] [object]");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/set [arenaName] (tpc/wait) [value]");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/arena [arenaName]");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/arenas");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/here");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/join [arenaName]" + ChatColor.GREEN + " <or> " + ChatColor.AQUA + "play [arenaName]");
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.AQUA + "/leave" + ChatColor.GREEN + " <or> " + ChatColor.AQUA + "quit");
                } else if (sender.hasPermission("FridayThe13th.User")) {
                    sender.sendMessage(FridayThe13th.pluginPrefix + ChatColor.STRIKETHROUGH + "---" + ChatColor.RESET + "Help Menu" + ChatColor.STRIKETHROUGH + "---");
                    sender.sendMessage(FridayThe13th.pluginPrefix + ChatColor.AQUA + "/join [arenaName]" + ChatColor.GREEN + " <or> " + ChatColor.AQUA + "play [arenaName]");
                    sender.sendMessage(FridayThe13th.pluginPrefix + ChatColor.AQUA + "/leave" + ChatColor.GREEN + " <or> " + ChatColor.AQUA + "quit");
                } else {
                    sender.sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(sender, "command.error.noPermission", "You don't have permission to access this command."));
                }
            } else {
                //Unknown command
                sender.sendMessage(FridayThe13th.pluginAdminPrefix + ChatColor.DARK_RED + "Unknown command " + ChatColor.AQUA + ChatColor.ITALIC + args[0]);
            }
        }

        return true;
    }
}
