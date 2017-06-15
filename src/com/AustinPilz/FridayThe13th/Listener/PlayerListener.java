package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {


        //TEMPORARY FOR TESTING

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        //
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
        //
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        //
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();

        if (player.isSprinting())
        {
            player.sendMessage("You're running");
        }

        if (player.isSneaking())
        {
            player.sendMessage("You're sneaking");
        }


        //player.sendMessage("Walk Speed: " + player.getWalkSpeed());

        Block block = player.getLocation().getBlock().getRelative(0, 1, 0);
        int sunlight_level = block.getLightFromSky();
        //player.sendMessage("Light Level" + sunlight_level);
    }
}
