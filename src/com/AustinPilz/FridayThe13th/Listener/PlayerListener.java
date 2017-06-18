package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    public PlayerListener() {
        //
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString()).getGameManager().getPlayerManager().onPlayerLogout(event.getPlayer().getUniqueId().toString());
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        try {
            if (event.getEntity() instanceof Player)
            {

                //This should NEVER happen, it should always look for damage

                Player player = (Player) event.getEntity();
                FridayThe13th.arenaController.getPlayerArena(player.getUniqueId().toString()).getGameManager().getPlayerManager().onPlayerDeath(player); //See if they're playing
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            //TODO: This will have to be changed to allow for game teleports and such.
            if (arena.getGameManager().isGameWaiting()) {
                //In waiting, only teleporting can be to the waiting location
                if (!event.getTo().equals(arena.getWaitingLocation())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You cannot teleport while playing.");
                }
            } else if (arena.getGameManager().isGameInProgress()) {
                if (!arena.isLocationWithinArenaBoundaries(event.getTo())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You cannot teleport while playing.");
                }
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            if (arena.getGameManager().isGameInProgress())
            {
                if (arena.getGameManager().getPlayerManager().isCounselor(event.getPlayer()))
                {
                    if (arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).isInSpectatingMode())
                    {
                        event.setCancelled(true);

                        //Let them know they can't interact in spectating mode
                        event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You cannot interact while you're in spectating mode.");
                    }
                }

            }
            else if (arena.getGameManager().isGameWaiting())
            {
                event.setCancelled(true); //Disable interaction while in the waiting room
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            if (arena.getGameManager().isGameInProgress())
            {
                if (arena.getGameManager().getPlayerManager().isCounselor(event.getPlayer()))
                {
                    //Counselor [has stamina]
                    if (event.getFrom().distance(event.getTo()) > 0)
                    {
                        if (event.getPlayer().isSprinting())
                        {
                            //Sprinting
                            arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).setSprinting(true);
                        }
                        else if (event.getPlayer().isSneaking())
                        {
                            //Sneaking
                            arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).setSneaking(true);
                        }
                        else if (event.getPlayer().isFlying())
                        {
                            if (!arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).isInSpectatingMode())
                            {
                                event.getPlayer().setFlying(false); //Prevent cheating
                            }
                        }
                        else
                        {
                            //Must just be walking
                            arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).setWalking(true);
                        }
                    }
                }
                else if (arena.getGameManager().getPlayerManager().isJason(event.getPlayer()))
                {
                    if (event.getPlayer().isSneaking())
                    {
                        if (arena.getGameManager().getPlayerManager().getJason().canStalk())
                        {
                            //He's stalking
                            arena.getGameManager().getPlayerManager().getJason().setStalking(true);
                        }
                        else
                        {
                            //Can't stalk, so cancel event
                            event.setCancelled(true);
                        }
                    }
                }
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onHungerDeplete(FoodLevelChangeEvent event) {
        try {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                Arena arena = FridayThe13th.arenaController.getPlayerArena(player.getUniqueId().toString()); //See if they're playing
                event.setCancelled(true);
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (FridayThe13th.arenaController.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
            if (!event.getMessage().toLowerCase().startsWith("/f13")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "Only Friday the 13th commands are available during gameplay. If you'd like to leave, execute " + ChatColor.AQUA + "/f13 leave");
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent event)
    {
        if(event.getEntity() instanceof Player)
        {
            try
            {
                Player player = (Player) event.getEntity();

                Arena arena = FridayThe13th.arenaController.getPlayerArena(player.getUniqueId().toString()); //See if they're playing

                if (arena.getGameManager().isGameInProgress()) {
                    if (player.getHealth() <= event.getDamage()) {
                        //This blow would kill them
                        event.setCancelled(true);
                        arena.getGameManager().getPlayerManager().onPlayerDeath(player);
                    }
                }
                else
                {
                    //You can't get damaged while waiting
                    event.setCancelled(true);
                }

            }
            catch (PlayerNotPlayingException exception)
            {
                //Do nothing since in this case, we couldn't care
            }
        }
    }

    @EventHandler
    public void onItemDrop (PlayerDropItemEvent event)
    {
        if (FridayThe13th.arenaController.isPlayerPlaying(event.getPlayer()))
        {
            if (event.getItemDrop().getItemStack().hasItemMeta() && !event.getItemDrop().getItemStack().getItemMeta().getDisplayName().isEmpty() && event.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains("Jason's"))
            {
                event.setCancelled(true);
            }
        }
    }

}
