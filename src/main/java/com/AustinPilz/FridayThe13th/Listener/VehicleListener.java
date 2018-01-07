package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public class VehicleListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVehicleMount(EntityMountEvent event) {
        if (event.getMount() instanceof Minecart && event.getEntity() instanceof Player) {
            if (FridayThe13th.arenaController.isLocationWithinAnArena(event.getMount().getLocation())) {
                Player player = (Player) event.getEntity();

                try {
                    Arena arena = FridayThe13th.arenaController.getPlayerArena(player);
                    if (arena.getGameManager().isGameInProgress() && arena.getGameManager().getPlayerManager().isCounselor(FridayThe13th.playerController.getPlayer(player))) {
                        ((Minecart) event.getMount()).setMaxSpeed(.9D);
                        ((Minecart) event.getMount()).setSlowWhenEmpty(false);
                    } else {
                        event.setCancelled(true);
                        event.getMount().eject();
                    }
                } catch (PlayerNotPlayingException exception) {
                    //Ignore
                }

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVehicleDismount(EntityDismountEvent event) {
        if (event.getDismounted() instanceof Minecart && event.getEntity() instanceof Player) {
            if (FridayThe13th.arenaController.isLocationWithinAnArena(event.getDismounted().getLocation())) {
                ((Minecart) event.getDismounted()).setMaxSpeed(0D);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVehicleCollision(VehicleEntityCollisionEvent event) {

        if (event.getEntity() instanceof Player && event.getVehicle() instanceof Minecart && event.getVehicle().getPassengers().size() > 0) {
            if (FridayThe13th.arenaController.isLocationWithinAnArena(event.getEntity().getLocation())) {
                Player player = (Player) event.getEntity();

                try {
                    Arena arena = FridayThe13th.arenaController.getPlayerArena(player);
                    if (arena.getGameManager().isGameInProgress() && arena.getGameManager().getPlayerManager().isJason(FridayThe13th.playerController.getPlayer(player))) {
                        //Jason collided with the vehicle
                        event.getVehicle().eject();
                    } else if (arena.getGameManager().getPlayerManager().isCounselor(FridayThe13th.playerController.getPlayer(player))) {
                        //player.damage(5);
                    }
                } catch (PlayerNotPlayingException exception) {
                    //Ignore
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVehicleDamage(VehicleDamageEvent event) {
        if (event.getVehicle() instanceof Minecart && event.getAttacker() instanceof Player) {
            if (FridayThe13th.arenaController.isLocationWithinAnArena(event.getVehicle().getLocation())) {
                Arena arena = FridayThe13th.arenaController.getArenaFromLocation(event.getVehicle().getLocation());
                if (arena.getGameManager().isGameWaiting() || arena.getGameManager().isGameInProgress()) {
                    event.setCancelled(true); //Cannot damage cars while in game
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVehicleMove(VehicleMoveEvent event) {
        if (event.getVehicle() instanceof Minecart) {
            Minecart minecart = (Minecart) event.getVehicle();

            if (event.getVehicle().getPassengers().size() > 0) {
                Player player;

                for (Entity entity : event.getVehicle().getPassengers()) {
                    if (entity instanceof Player) {
                        player = (Player) entity;

                        try {
                            Arena arena = FridayThe13th.arenaController.getPlayerArena(player);

                            if (arena.getGameManager().isGameInProgress() && arena.getLocationManager().getEscapePointManager().isLocationWithinEscapePoint(player.getLocation())) {
                                if (arena.getLocationManager().getEscapePointManager().getEscapePointFromLocation(player.getLocation()).isLandPoint() && event.getVehicle() instanceof Minecart) {
                                    //Escaping via Minecart via land
                                    arena.getObjectManager().getVehicleManager().getRegisteredCar(minecart).escaped();
                                    arena.getGameManager().getPlayerManager().onPlayerEscape(FridayThe13th.playerController.getPlayer(player));
                                } else {
                                    event.getVehicle().teleport(event.getFrom());
                                }
                            } else {
                                ((Minecart) event.getVehicle()).setMaxSpeed(0.4D);
                                event.getVehicle().setVelocity(event.getVehicle().getVelocity().multiply(1.3));
                            }
                        } catch (PlayerNotPlayingException exception) {
                            //Ignore
                        }
                    }
                }
            } else {
                if (FridayThe13th.arenaController.isLocationWithinAnArena(event.getTo())) {
                    //There are no passengers in this arena Minecart - prevent it from running away
                    ((Minecart) event.getVehicle()).setMaxSpeed(0D);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        if (FridayThe13th.arenaController.isPlayerPlaying(event.getPlayer())) {
            if (event.getRightClicked() instanceof Minecart) {
                try {
                    Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer());
                    if (!arena.getGameManager().getPlayerManager().isCounselor(FridayThe13th.playerController.getPlayer(event.getPlayer())) || !arena.getGameManager().isGameInProgress()) {
                        event.setCancelled(true); //Only counselors can interact with the minecarts while game is in progress
                    }
                } catch (PlayerNotPlayingException exception) {
                    event.setCancelled(false);
                }
            } else if (!(event.getRightClicked() instanceof Player)) {
                event.setCancelled(true); //Cannot interact with entities that are not Minecarts
            }
        }
    }
}
