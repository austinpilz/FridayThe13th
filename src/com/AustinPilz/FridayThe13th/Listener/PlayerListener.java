package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Characters.Jason;
import com.AustinPilz.FridayThe13th.Components.Menu.SpawnPreferenceMenu;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Runnable.CounselorWindowJump;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Chest;
import org.bukkit.material.Door;
import org.bukkit.material.Lever;

import java.util.Iterator;
import java.util.Map;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (FridayThe13th.updateChecker.isUpdateNeeded() && event.getPlayer().hasPermission("FridayThe13th.admin"))
        {
            event.getPlayer().sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.updateAvailable", "There is a newer version of Friday the 13th available for update!"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        try
        {
            FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString()).getGameManager().getPlayerManager().onPlayerLogout(event.getPlayer().getUniqueId().toString());
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        try {
            if (event.getEntity() instanceof Player)
            {

                //This should NEVER happen, it should always look for damage

                Player player = event.getEntity();
                FridayThe13th.arenaController.getPlayerArena(player.getUniqueId().toString()).getGameManager().getPlayerManager().onPlayerDeath(player); //See if they're playing
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            if (arena.getGameManager().isGameWaiting()) {
                //In waiting, only teleporting can be to the waiting location
                if (!event.getTo().equals(arena.getWaitingLocation())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.teleportWaiting", "You cannot be teleported while waiting."));
                }
            } else if (arena.getGameManager().isGameInProgress()) {
                if (!arena.isLocationWithinArenaBoundaries(event.getTo())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.teleportPlaying", "You cannot be teleported while playing."));
                }
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {

        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            if (arena.getGameManager().isGameInProgress())
            {
                if (arena.getGameManager().getPlayerManager().isSpectator(event.getPlayer().getUniqueId().toString()))
                {
                    if (event.hasItem() && event.getItem().getType().equals(Material.EMERALD))
                    {
                        //Open spectate menu
                        event.getPlayer().openInventory(arena.getGameManager().getPlayerManager().getSpectator(event.getPlayer()).getSpectateMenuInventory());
                        event.setCancelled(true);
                    }
                    else
                    {
                        //Let them know they can't interact in spectating mode
                        event.setCancelled(true);
                    }
                } else if (arena.getGameManager().getPlayerManager().isCounselor(event.getPlayer()))
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
                            event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.doorBroken", "That door has been broken by Jason."));
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
                                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.needRepairWire", "You need repair wire to fix broken switches."));
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
                            event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.phoneNotManaged", "This phone has not been added to the arena. Ask your admin to add it."));
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
                            event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.gameFull", "The game in {0} is currently full.", ChatColor.RED + arena.getArenaName() + ChatColor.WHITE));
                        } catch (GameInProgressException e) {
                            //Enter as a spectator
                            arena.getGameManager().getPlayerManager().becomeSpectator(event.getPlayer());
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            if (arena.getGameManager().isGameInProgress())
            {
                //Counselor in spectate mode
                if (!arena.isLocationWithinArenaBoundaries(event.getTo())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.arenaBoundary", "You can't leave the arena boundary while playing."));
                }

                if (arena.getGameManager().getPlayerManager().isCounselor(event.getPlayer())) {
                    if (!arena.isLocationWithinArenaBoundaries(event.getTo())) {
                        event.setCancelled(true);
                    } else {
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
                                if (!arena.getGameManager().getPlayerManager().isSpectator(event.getPlayer())) {
                                    event.getPlayer().setFlying(false); //Prevent cheating
                                }
                            } else {
                                //Must just be walking
                                arena.getGameManager().getPlayerManager().getCounselor(event.getPlayer()).setWalking(true);
                            }
                        }
                    }
                }
                else if (arena.getGameManager().getPlayerManager().isJason(event.getPlayer()))
                {
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
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (FridayThe13th.arenaController.isPlayerPlaying(event.getPlayer().getUniqueId().toString())) {
            if (!event.getMessage().toLowerCase().startsWith("/f13")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.commandsDisabled", "Only Friday the 13th commands are available during gameplay. If you'd like to leave, execute {0} /f13 leave", ChatColor.AQUA));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player)
        {
            try
            {
                Player temp = (Player) event.getEntity();
                Player playerDamaged = Bukkit.getPlayer(temp.getUniqueId());

                Arena arena = FridayThe13th.arenaController.getPlayerArena(playerDamaged.getUniqueId().toString()); //See if they're playing

                if (arena.getGameManager().isGameInProgress())
                {
                    if (arena.getGameManager().getPlayerManager().isSpectator(playerDamaged))
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
                                    if (arena.getGameManager().getPlayerManager().isSpectator(playerDamager))
                                    {
                                        //The damage is a counselor in spectate mode
                                        event.setCancelled(true);
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
                                            else if (playerDamager.getItemInHand().getType().equals(Material.POTION) || playerDamager.getItemInHand().getType().equals(Material.REDSTONE) || playerDamager.getItemInHand().getType().equals(Material.NETHER_STAR))
                                            {
                                                event.setCancelled(true); //Counselors can't hurt Jason with items not meant for combat
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    //The person doing the damage isn't even playing
                                    event.setCancelled(true);
                                    playerDamager.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(playerDamager, "game.error.hitWhileNotPlaying", "You can't hit F13 players while you're not playing."));
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

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHealthRegen(EntityRegainHealthEvent event) {
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

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        try
        {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString()); //See if they're playing

            if (arena.getGameManager().isGameInProgress() && arena.getGameManager().getPlayerManager().isCounselor(event.getPlayer()) && !arena.getGameManager().getPlayerManager().isSpectator(event.getPlayer()))
            {
                event.setCancelled(false); //Regular counselors can drop items
            } else {
                event.setCancelled(true);
            }

        }
        catch (PlayerNotPlayingException exception)
        {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        try
        {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getWhoClicked().getUniqueId().toString()); //See if they're playing
            Player player = (Player)event.getWhoClicked();

            if (arena.getGameManager().isGameInProgress() && arena.getGameManager().getPlayerManager().isSpectator(player))
            {
                event.setCancelled(true);

                if (event.getCurrentItem() != null)
                {
                    if (event.getCurrentItem().getType().equals(Material.EMERALD)) //They're picking item to open spectate menu
                    {
                        //Open spectate menu
                        player.openInventory(arena.getGameManager().getPlayerManager().getSpectator(player).getSpectateMenuInventory());
                    } else if (event.getCurrentItem().getType().equals(Material.SKULL_ITEM)) //They're picking a player head to spectate
                    {
                        SkullMeta playerMetaData = (SkullMeta) event.getCurrentItem().getItemMeta();
                        player.teleport(Bukkit.getPlayer(playerMetaData.getDisplayName()));
                        player.closeInventory();
                    }
                }
            } else {
                //The game is in the waiting room phase
                if (event.getCurrentItem() != null) {
                    if (event.getCurrentItem().getType().equals(Material.EMPTY_MAP)) //They're trying to open the spawn preference menu
                    {
                        //Open spectate menu
                        SpawnPreferenceMenu.openMenu(player);
                    }
                }
            }

        }
        catch (PlayerNotPlayingException exception)
        {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMessage(AsyncPlayerChatEvent event) {
        //First check to see if the person playing is a F13 player or not
        if (FridayThe13th.arenaController.isPlayerPlaying(event.getPlayer()))
        {
            //They're playing F13 - cancel the event because we'll send it ourselves
            event.setCancelled(true);

            try
            {
                Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString()); //See if they're playing
                FridayThe13th.chatController.routeInternalMesssage(event.getPlayer(), event.getMessage(), arena);
            }
            catch (PlayerNotPlayingException exception)
            {
                //Panic and cancel
                event.setCancelled(true);
            }
        }
        else
        {
            //They're not playing F13 - so remove all F13 players as recipients
            event.getRecipients().removeAll(FridayThe13th.chatController.getF13Players());
        }
    }

}
