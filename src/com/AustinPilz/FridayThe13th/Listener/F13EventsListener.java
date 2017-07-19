package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Menu.SpawnPreferenceMenu;
import com.AustinPilz.FridayThe13th.Events.F13BlockPlacedEvent;
import com.AustinPilz.FridayThe13th.Events.F13MenuItemClickedEvent;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Utilities.InventoryActions;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
                    SpawnPreferenceMenu.openMenu(event.getPlayer());

                }
                event.setCancelled(true);
            } else if (json.containsKey("SpawnPrefSelect")) {
                //They're changing their spawn preference
                String action = (String) json.get("SpawnPrefSelect");
                if (action.equals("J")) {
                    FridayThe13th.playerController.getPlayer(event.getPlayer()).setSpawnPreferenceJason();
                } else if (action.equals("C")) {
                    FridayThe13th.playerController.getPlayer(event.getPlayer()).setSpawnPreferenceCounselor();
                }
                event.setCancelled(true);
            } else if (json.containsKey("TrapTeleport")) {
                //Jason teleport to trapped player
                String playerName = (String) json.get("TrapTeleport");
                if (Bukkit.getOfflinePlayer(playerName).isOnline()) {
                    event.getPlayer().teleport(Bukkit.getPlayer(playerName).getLocation());
                    InventoryActions.remove(event.getPlayer().getInventory(), event.getItem(), 1, (short) -1);
                }
                event.setCancelled(true);
            } else if (json.containsKey("PlaceItem")) {
                event.setCancelled(false); //All place items are handled by a different event
            } else {
                event.setCancelled(false); //Unknown object
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
                    if (!event.getArena().getObjectManager().isATrap(event.getBlock().getRelative(BlockFace.DOWN)) && !event.getArena().getObjectManager().isATrap(event.getBlock().getRelative(BlockFace.UP))) {
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
