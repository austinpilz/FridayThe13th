package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Components.Counselor;
import com.AustinPilz.FridayThe13th.Components.Jason;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.CounselorWindowJump;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Chest;
import org.bukkit.material.Door;
import org.bukkit.material.Lever;

import java.util.Iterator;
import java.util.Map;

public class PlayerListener implements Listener {

    public PlayerListener() {
        //
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (FridayThe13th.updateChecker.isUpdateNeeded() && event.getPlayer().hasPermission("FridayThe13th.admin"))
        {
            event.getPlayer().sendMessage(FridayThe13th.pluginAdminPrefix + "There is a new version of Friday the 13th available for update!");
        }
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            //TODO: This will have to be changed to allow for game teleports and such.
            if (arena.getGameManager().isGameWaiting()) {
                //In waiting, only teleporting can be to the waiting location
                if (!event.getTo().equals(arena.getWaitingLocation())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You cannot teleport while waiting.");
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
                        if (event.hasItem() && event.getItem().getType().equals(Material.EMERALD))
                        {
                            //Open spectate menu
                            event.getPlayer().openInventory(arena.getGameManager().getPlayerManager().getSpectateMenuInventory());
                            event.setCancelled(true);
                        }
                        else
                        {
                            //Let them know they can't interact in spectating mode
                            event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You cannot interact while you're in spectating mode.");
                            event.setCancelled(true);
                        }
                    }
                    else
                    {
                        //They're in regular play mode
                        if (event.hasBlock() && event.getClickedBlock().getState().getData() instanceof Door)
                        {
                            BlockState state = event.getClickedBlock().getState();
                            Door door = (Door) state.getData();

                            Block testBlock = event.getClickedBlock();
                            if (door.isTopHalf())
                            {
                                //Top 1/2
                                testBlock = event.getClickedBlock().getRelative(BlockFace.DOWN);
                            }

                            if (arena.getObjectManager().getBrokenDoors().contains(testBlock))
                            {
                                //They're trying to interact with a broken door
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "That door has been broken by Jason.");
                            }
                        }
                        else if (event.hasBlock() && event.getClickedBlock().getState().getData() instanceof Lever)
                        {
                            if (arena.getObjectManager().getBrokenSwitches().containsKey(event.getClickedBlock()))
                            {
                                event.setCancelled(true);

                                if (event.hasItem() && event.getItem().getType().equals(Material.REDSTONE))
                                {
                                    //Register the repair attempt
                                    arena.getObjectManager().getBrokenSwitches().get(event.getClickedBlock()).repairSwitchAttempt(event.getPlayer());
                                }
                                else
                                {
                                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You need repair wire to fix broken switches.");
                                }
                            }
                            else
                            {
                                //Players can only turn off switches on if they're not broken
                                BlockState state = event.getClickedBlock().getState();
                                Lever lever = (Lever)state.getData();

                                if (lever.isPowered())
                                {
                                    event.setCancelled(true);
                                }
                            }

                        }
                        else if (event.hasBlock() && event.getClickedBlock().getType().equals(Material.BED_BLOCK))
                        {
                            event.setCancelled(true);
                        }
                        else if (event.hasBlock() && event.getClickedBlock().getType().equals(Material.TRIPWIRE_HOOK))
                        {
                            if (arena.getObjectManager().getPhones().containsKey(event.getClickedBlock()))
                            {
                                arena.getObjectManager().getPhones().get(event.getClickedBlock()).callAttempt(event.getPlayer());
                            }
                            else
                            {
                                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "This phone is not managed by the arena. Ask your admin to add this tripwire hook to the arena.");
                            }
                        }
                        else if (event.hasBlock() && event.getClickedBlock().getType().equals(Material.THIN_GLASS))
                        {
                            //Window jumping
                            if (event.getPlayer().isSprinting())
                            {
                                //Fast jump - breaks window
                                arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).setAwaitingWindowJump(true); //So that they don't spam window jumps
                                arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).teleportThroughWindow(event.getClickedBlock(), true);
                                arena.getObjectManager().breakWindow(event.getClickedBlock());
                                arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).setAwaitingWindowJump(false);
                            }
                            else
                            {
                                if (!arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).isAwaitingWindowJump())
                                {
                                    arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).setAwaitingWindowJump(true); //So that they don't spam window jumps
                                    ActionBarAPI.sendActionBar(event.getPlayer(), FridayThe13th.language.get(event.getPlayer(), "actionBar.counselor.windowJumpWait", "Don't move! You'll jump in {0} seconds...", "2"), 40);
                                    Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new CounselorWindowJump(arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()), event.getPlayer().getLocation(), event.getClickedBlock()), 40);
                                }
                            }
                        }
                    }
                }
                else if (arena.getGameManager().getPlayerManager().isJason(event.getPlayer()))
                {
                    Jason jason = arena.getGameManager().getPlayerManager().getJason();

                    if (event.hasItem() && event.getItem().hasItemMeta() && event.getItem().getItemMeta().getDisplayName().contains("Sense"))
                    {
                        //Sense Potion
                        jason.senseActivationRequest(true);
                        event.setCancelled(true);
                    }
                    else if (event.hasBlock())
                    {
                        //Physical object interactions
                        if (event.getClickedBlock().getState().getData() instanceof Door || event.getClickedBlock().getState().getData() instanceof Lever)
                        {
                            //Door or lever clicked
                            if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) && event.hasItem())
                            {
                                if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName() && event.getItem().getItemMeta().getDisplayName().contains("Jason's"))
                                {
                                    event.setCancelled(false);
                                }
                                else
                                {
                                    event.setCancelled(true);
                                }
                            }
                            else
                            {
                                event.setCancelled(true);
                            }
                        }
                        else if (event.getClickedBlock().getState().getData() instanceof Chest)
                        {
                            event.setCancelled(true);
                        }
                        else if (event.hasBlock() && event.getClickedBlock().getType().equals(Material.BED_BLOCK))
                        {
                            event.setCancelled(true);
                        }
                    }
                }

            }
            else if (arena.getGameManager().isGameWaiting())
            {
                event.setCancelled(true); //Disable interaction while in the waiting room
            }
        } catch (PlayerNotPlayingException exception)
        {
            //Do nothing since in this case, we couldn't care
            if (event.hasBlock() && (event.getClickedBlock().getType().equals(Material.WALL_SIGN) || event.getClickedBlock().getType().equals(Material.SIGN_POST)))
            {
                Sign sign = (Sign)event.getClickedBlock().getState();

                Iterator it = FridayThe13th.arenaController.getArenas().entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    Arena arena = (Arena) entry.getValue();
                    if (arena.getSignManager().isJoinSign(sign))
                    {
                        try {
                            arena.getGameManager().getPlayerManager().playerJoinGame(event.getPlayer());
                        } catch (GameFullException e) {
                            event.getPlayer().sendMessage(FridayThe13th.pluginAdminPrefix + "The game in " + ChatColor.RED + arena.getArenaName() + ChatColor.WHITE + " is currently full.");
                        } catch (GameInProgressException e) {
                            event.getPlayer().sendMessage(FridayThe13th.pluginAdminPrefix + "The game in " + ChatColor.RED + arena.getArenaName() + ChatColor.WHITE + " is currently in progress. You cannot join during a game.");
                        }
                    }
                }
            }
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
                    if (arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).isInSpectatingMode())
                    {
                        //Counselor in spectate mode
                        if (!arena.isLocationWithinArenaBoundaries(event.getTo()))
                        {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + "You can't leave the arena boundary while in spectate mode.");
                        }
                    }
                    else
                    {
                        //Counselor in regular mod
                        if (!arena.isLocationWithinArenaBoundaries(event.getTo())) {
                            event.setCancelled(true);
                        }
                        else {
                            if (event.getFrom().distance(event.getTo()) > 0) {
                                if (event.getPlayer().isSprinting()) {
                                    if (arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).getStaminaPercentage() == 0) {
                                        event.setCancelled(true); //cant run when they have no energy
                                    } else {
                                        //Sprinting
                                        arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).setSprinting(true);
                                    }
                                } else if (event.getPlayer().isSneaking()) {
                                    //Sneaking
                                    arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).setSneaking(true);
                                } else if (event.getPlayer().isFlying()) {
                                    if (!arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).isInSpectatingMode()) {
                                        event.getPlayer().setFlying(false); //Prevent cheating
                                    }
                                } else {
                                    //Must just be walking
                                    arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).setWalking(true);
                                }
                            }
                        }
                    }
                }
                else if (arena.getGameManager().getPlayerManager().isJason(event.getPlayer()))
                {
                    if (!arena.isLocationWithinArenaBoundaries(event.getTo())) {
                        event.setCancelled(true);
                    }
                    else {
                        if (event.getPlayer().isSneaking()) {
                            if (arena.getGameManager().getPlayerManager().getJason().canStalk()) {
                                //He's stalking
                                arena.getGameManager().getPlayerManager().getJason().setStalking(true);
                            } else {
                                //Can't stalk, so cancel event
                                event.setCancelled(true);
                            }
                        } else if (event.getPlayer().isSprinting()) {
                            arena.getGameManager().getPlayerManager().getJason().setSprinting(true);
                        } else if (event.getPlayer().isFlying()) {
                            if (arena.getGameManager().getPlayerManager().getJason().canWarp()) {
                                arena.getGameManager().getPlayerManager().getJason().setFlying(true);
                            } else {
                                event.setCancelled(true);
                                event.getPlayer().setFlying(false);
                            }
                        } else {
                            //Must just be walking
                            arena.getGameManager().getPlayerManager().getJason().setWalking(true);
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
                Player temp = (Player) event.getEntity();
                Player playerDamaged = Bukkit.getPlayer(temp.getUniqueId());

                Arena arena = FridayThe13th.arenaController.getPlayerArena(playerDamaged.getUniqueId().toString()); //See if they're playing

                if (arena.getGameManager().isGameInProgress())
                {
                    if (arena.getGameManager().getPlayerManager().isCounselor(playerDamaged) && arena.getGameManager().getPlayerManager().getCounselor(playerDamaged).isInSpectatingMode())
                    {
                        event.setCancelled(true); //You can't get hurt in spectate mode
                    }
                    else
                    {
                        if(event instanceof EntityDamageByEntityEvent)
                        {
                            EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent) event;
                            if(edbeEvent.getDamager() instanceof Player)
                            {
                                //The person doing the damage is a player, too.
                                Player playerDamager = (Player)edbeEvent.getDamager();

                                if (FridayThe13th.arenaController.isPlayerPlaying(playerDamager))
                                {
                                    //The person doing the damage is playing
                                    if (arena.getGameManager().getPlayerManager().isCounselor(playerDamager) && arena.getGameManager().getPlayerManager().getCounselor(playerDamager).isInSpectatingMode())
                                    {
                                        //The damage is a counselor in spectate mode
                                        event.setCancelled(true);
                                        playerDamager.sendMessage(FridayThe13th.pluginPrefix + "You can't damage players while in spectating mode.");
                                    }
                                    else if (arena.getGameManager().getPlayerManager().isCounselor(playerDamager) && arena.getGameManager().getPlayerManager().isJason(playerDamaged))
                                    {
                                        //Counselor is damaging Jason
                                        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
                                        {
                                            if (playerDamager.getItemInHand().getType().equals(Material.IRON_SWORD) || playerDamager.getItemInHand().getType().equals(Material.IRON_AXE) || playerDamager.getItemInHand().getType().equals(Material.WOOD_AXE))
                                            {
                                                arena.getGameManager().getPlayerManager().getJason().stun();
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    //The person doing the damage isn't even playing
                                    event.setCancelled(true);
                                    playerDamager.sendMessage(FridayThe13th.pluginPrefix + "You can't hit players when you're not playing.");
                                }
                            }
                        }
                    }

                    if (!event.isCancelled())
                    {
                        if (playerDamaged.getHealth() <= event.getDamage())
                        {
                            //This blow would kill them
                            event.setCancelled(true);
                            arena.getGameManager().getPlayerManager().onPlayerDeath(playerDamaged);
                        }
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onHealthRegen (EntityRegainHealthEvent event)
    {
        if(event.getEntity() instanceof Player && event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.REGEN))
        {
            try
            {
                Player player = (Player) event.getEntity();
                Arena arena = FridayThe13th.arenaController.getPlayerArena(player.getUniqueId().toString()); //See if they're playing
                event.setCancelled(true);

            } catch (PlayerNotPlayingException exception) {
                //Do nothing since in this case, we couldn't care
            }
        }
    }

    @EventHandler
    public void onItemDrop (PlayerDropItemEvent event)
    {
        try
        {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString()); //See if they're playing

            if (arena.getGameManager().isGameInProgress() && (arena.getGameManager().getPlayerManager().isJason(event.getPlayer()) || (arena.getGameManager().getPlayerManager().isCounselor(event.getPlayer()) && arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).isInSpectatingMode())))
            {
                event.setCancelled(true); //Jason and counselors in spectate mode can't drop anything
            }

        }
        catch (PlayerNotPlayingException exception)
        {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event)
    {
        try
        {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getWhoClicked().getUniqueId().toString()); //See if they're playing
            Player player = (Player)event.getWhoClicked();

            if (arena.getGameManager().isGameInProgress() && (arena.getGameManager().getPlayerManager().isCounselor(player) && arena.getGameManager().getPlayerManager().getCounselor(player).isInSpectatingMode()))
            {
                event.setCancelled(true);

                if (event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.SKULL_ITEM))
                {
                    SkullMeta playerMetaData = (SkullMeta)event.getCurrentItem().getItemMeta();
                    player.teleport(Bukkit.getPlayer(playerMetaData.getDisplayName()));
                    player.closeInventory();
                }
            }

        }
        catch (PlayerNotPlayingException exception)
        {
            //Do nothing since in this case, we couldn't care
        }
    }

}
