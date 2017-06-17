package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    public PlayerListener()
    {
        //
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        //
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        try
        {
            FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString()).getPlayerManager().playerLeaveGame(event.getPlayer().getUniqueId().toString());
        }
        catch (PlayerNotPlayingException exception)
        {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamage(EntityDamageEvent e)
    {
        //
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        try
        {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            //TODO: This will have to be changed to allow for game teleports and such.
            if (arena.getGameManager().isGameWaiting())
            {
                //In waiting, only teleporting can be to the waiting location
                if (!event.getTo().equals(arena.getWaitingLocation()))
                {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You cannot teleport while playing.");
                }
            }
            else if (arena.getGameManager().isGameInProgress())
            {
                if (!arena.isLocationWithinArenaBoundaries(event.getTo()))
                {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You cannot teleport while playing.");
                }
            }
        }
        catch (PlayerNotPlayingException exception)
        {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        //
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        try
        {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            if (arena.getGameManager().isGameInProgress())
            {
                if (arena.getPlayerManager().isCounselor(event.getPlayer()))
                {
                    //Counselor [has stamina]
                    if (event.getPlayer().isSprinting())
                    {
                        //Sprinting
                        arena.getPlayerManager().getCounselor(event.getPlayer()).setSprinting(true);
                    }
                    else if (event.getPlayer().isSneaking())
                    {
                        //Sneaking
                        arena.getPlayerManager().getCounselor(event.getPlayer()).setSneaking(true);
                    }
                    else if (event.getPlayer().isFlying())
                    {
                        //TODO if spectating mode, allow it?
                        event.getPlayer().setFlying(false);
                    }
                    else
                    {
                        //Must just be walking
                        arena.getPlayerManager().getCounselor(event.getPlayer()).setWalking(true);
                    }
                }
                else if (arena.getPlayerManager().isJason(event.getPlayer()))
                {
                    //TODO: Jason
                }
            }
        }
        catch (PlayerNotPlayingException exception)
        {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler
    public void onHungerDeplete(FoodLevelChangeEvent event)
    {
        try
        {
            if (event.getEntity() instanceof Player)
            {
                Player player = (Player) event.getEntity();
                Arena arena = FridayThe13th.arenaController.getPlayerArena(player.getUniqueId().toString()); //See if they're playing
                event.setCancelled(true);
            }
        }
        catch (PlayerNotPlayingException exception)
        {
            //Do nothing since in this case, we couldn't care
        }
    }
}
