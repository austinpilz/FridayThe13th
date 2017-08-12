package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Profiles.CounselorProfile;
import com.AustinPilz.FridayThe13th.Components.Perk.F13Perk;
import com.AustinPilz.FridayThe13th.Components.Profiles.F13ProfileManager;
import com.AustinPilz.FridayThe13th.Components.Profiles.JasonProfile;
import com.AustinPilz.FridayThe13th.Components.Menu.*;
import com.AustinPilz.FridayThe13th.Events.F13BlockPlacedEvent;
import com.AustinPilz.FridayThe13th.Events.F13MenuItemClickedEvent;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Components.Perk.F13PerkManager;
import com.AustinPilz.FridayThe13th.Utilities.InventoryActions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.UUID;

public class F13EventsListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onF13MenuItemClicked(F13MenuItemClickedEvent event) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(event.getHiddenData());

            if (json.containsKey("Menu")) {
                //Opening a menu
                String action = (String) json.get("Menu");

                if (action.equals("SpawnPref")) {
                    //Open the spawn preference menu
                    SpawnPreferenceMenu.openMenu(event.getPlayer());
                } else if (action.equals("Spectate")) {
                    //Open the spectate selection  menu
                    SpectateMenu.openMenu(event.getPlayer(), event.getArena());
                } else if (action.equals("Profiles")) {
                    //Open the profile main menu
                    Profiles_MainMenu.openMenu(event.getPlayer());
                } else if (action.equals("JasonProfiles")) {
                    //Open the spectate selection menu
                    Profiles_JasonMenu.openMenu(event.getPlayer());
                } else if (action.equals("CounselorProfiles")) {
                    //Open the spectate selection menu
                    Profiles_CounselorMenu.openMenu(event.getPlayer());
                } else if (action.equals("Shop_Main")) {
                    //Open main shop
                    Shop_MainMenu.openMenu(event.getPlayer());
                } else if (action.equals("Shop_Jason")) {
                    //Open Jason perks shop
                    Shop_JasonPerksMenu.openMenu(event.getPlayer());
                } else if (action.equals("Shop_Counselor")) {
                    //Open Counselor perks shop
                    Shop_CounselorPerksMenu.openMenu(event.getPlayer());
                } else if (action.equals("Purchased_Perks")) {
                    //Open purchased perks menu
                    PurchasedPerksMenu.openMenu(event.getPlayer());
                }
                event.setCancelled(true);
            } else if (json.containsKey("SpawnPrefSelect")) {
                //They're changing their spawn preference
                String action = (String) json.get("SpawnPrefSelect");
                if (action.equals("J")) {
                    FridayThe13th.playerController.getPlayer(event.getPlayer()).setSpawnPreferenceJason();
                    event.getPlayer().closeInventory();
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.menu.spawnPrefSetJason", "Your spawn preference has been set as {0}Jason{1}.", ChatColor.RED + "" + ChatColor.BOLD, ChatColor.RESET + "" + ChatColor.WHITE));
                } else if (action.equals("C")) {
                    FridayThe13th.playerController.getPlayer(event.getPlayer()).setSpawnPreferenceCounselor();
                    event.getPlayer().closeInventory();
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.menu.spawnPrefSetCounselor", "Your spawn preference has been set as a {0}counselor{1}.", ChatColor.DARK_GREEN, ChatColor.WHITE));
                }
                event.setCancelled(true);
            } else if (json.containsKey("TrapTeleport")) {
                //Jason teleport to trapped player
                String playerName = (String) json.get("TrapTeleport");

                //Ensure that the trapped player we're going to teleport to is still online, still paying & is alive
                if (Bukkit.getOfflinePlayer(playerName).isOnline() && event.getArena().getGameManager().getPlayerManager().isAlive(Bukkit.getPlayer(playerName))) {
                    event.getPlayer().teleport(Bukkit.getPlayer(playerName).getLocation());
                    InventoryActions.remove(event.getPlayer().getInventory(), event.getItem(), 1, (short) -1);
                }
                event.setCancelled(true);
            } else if (json.containsKey("SpectateTeleport")) {
                event.getPlayer().teleport(Bukkit.getPlayer(UUID.fromString((String) json.get("SpectateTeleport"))).getLocation().add(1, 1, 0));
                event.setCancelled(true);
            } else if (json.containsKey("PlaceItem")) {
                event.setCancelled(false); //All place items are handled by a different event but need to be ignored by this one
            } else if (json.containsKey("JasonProfileSelect")) {
                String profileName = (String) json.get("JasonProfileSelect");

                if (!profileName.equalsIgnoreCase("Locked")) {
                    if (F13ProfileManager.getJasonProfileByInternalIdentifier(profileName) != null)
                    {
                        if (FridayThe13th.playerController.getPlayer(event.getPlayer()).setJasonProfile(F13ProfileManager.getJasonProfileByInternalIdentifier(profileName))) {
                            //Profile set successfully
                            Profiles_JasonMenu.openMenu(event.getPlayer());
                        }
                    }
                }
                event.setCancelled(true);
            } else if (json.containsKey("CounselorProfileSelect")) {
                String profileName = (String) json.get("CounselorProfileSelect");

                if (!profileName.equalsIgnoreCase("Locked")) {

                    if (F13ProfileManager.getCounselorProfileByInternalIdentifier(profileName) != null)
                    {
                        if (FridayThe13th.playerController.getPlayer(event.getPlayer()).setCounselorProfile(F13ProfileManager.getCounselorProfileByInternalIdentifier(profileName))) {
                            //Profile set successfully
                            Profiles_CounselorMenu.openMenu(event.getPlayer());
                        }
                    }
                }

                event.setCancelled(true);
            } else if (json.containsKey("PurchasePerk")) {
                String perkName = (String) json.get("PurchasePerk");

                F13Perk perk = F13PerkManager.getPerkByInternalIdentifier(perkName);

                if (perk != null)
                {
                    if (FridayThe13th.playerController.getPlayer(event.getPlayer()).purchasePerk(perk))
                    {
                        //Open their ender chest thing
                        PurchasedPerksMenu.openMenu(event.getPlayer());
                    }
                }
                event.setCancelled(true);
            } else if (json.containsKey("ActivatePerk"))
            {
                event.setCancelled(true);
            }
            else {
                event.setCancelled(false); //Unknown object so we'll ignore it
            }
        } catch (ParseException exception) {
            //Probably a transmission error, just ignore event
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onF13BlockPlaced(F13BlockPlacedEvent event) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(event.getHiddenData());

            if (json.containsKey("PlaceItem")) {
                //Opening a menu
                String action = (String) json.get("PlaceItem");

                if (action.equals("JasonTrap") || action.equals("CounselorTrap")) {
                    //Make sure they're not stacking traps
                    if (!event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.CARPET) && !event.getBlock().getRelative(BlockFace.UP).getType().equals(Material.CARPET)) {
                        if (action.equals("JasonTrap")) {
                            event.getArena().getObjectManager().placeJasonTrap(event.getBlock(), event.getOriginalMaterial());
                        } else if (action.equals("CounselorTrap")) {
                            event.getArena().getObjectManager().placeCounselorTrap(event.getBlock(), event.getOriginalMaterial());
                        }
                        event.setCancelled(false);
                    } else {
                        //There's a trap the block above or below this one
                        event.setCancelled(true);
                    }

                } else {
                    event.setCancelled(true); //No idea what they're trying to place, so deny it
                }
            }
        } catch (ParseException exception) {
            //Probably a transmission error, just ignore event
            event.setCancelled(true);
        }
    }
}
