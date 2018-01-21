package com.AustinPilz.FridayThe13th.Listener;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Arena.ArenaPhone;
import com.AustinPilz.FridayThe13th.Components.Arena.EscapePoint;
import com.AustinPilz.FridayThe13th.Components.Characters.Jason;
import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.Components.Enum.TrapType;
import com.AustinPilz.FridayThe13th.Components.Enum.XPAward;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Perk.F13Perk;
import com.AustinPilz.FridayThe13th.Components.Visuals.ThrowableItem;
import com.AustinPilz.FridayThe13th.Events.F13MenuItemClickedEvent;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Game.SoundManager;
import com.AustinPilz.FridayThe13th.Runnable.CounselorWindowJump;
import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import com.AustinPilz.FridayThe13th.Utilities.InventoryActions;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Chest;
import org.bukkit.material.Door;
import org.bukkit.material.Lever;
import org.bukkit.material.TripwireHook;
import org.golde.bukkit.corpsereborn.CorpseAPI.events.CorpseClickEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (FridayThe13th.updateChecker != null && FridayThe13th.updateChecker.isUpdateNeeded() && event.getPlayer().hasPermission("FridayThe13th.admin"))
        {
            event.getPlayer().sendMessage(FridayThe13th.pluginAdminPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.updateAvailable", "There is a newer version of Friday the 13th available for update!"));
        }

        if (event.getPlayer().getName().equals("austinpilz"))
        {
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (!FridayThe13th.arenaController.isPlayerPlaying(player))
                {
                    player.sendMessage(FridayThe13th.pluginPrefix + "Plugin creator austinpilz has just joined the server!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        try
        {
            FridayThe13th.arenaController.getPlayerArena(event.getPlayer()).getGameManager().getPlayerManager().onPlayerLogout(FridayThe13th.playerController.getPlayer(event.getPlayer()));
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
                FridayThe13th.arenaController.getPlayerArena(player).getGameManager().getPlayerManager().onPlayerDeath(FridayThe13th.playerController.getPlayer(player)); //See if they're playing
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer());

            if (!arena.getGameManager().isGameInProgress()) {
                //In waiting, only teleporting can be to the waiting or return location
                if (!event.getTo().equals(arena.getWaitingLocation()) && !event.getTo().equals(arena.getReturnLocation())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.teleportWaiting", "You cannot be teleported while waiting."));
                }
            } else if (arena.getGameManager().isGameInProgress()) {
                if (!arena.isLocationWithinArenaBoundaries(event.getTo()) && !arena.getReturnLocation().equals(event.getTo())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.teleportPlaying", "You cannot be teleported outside of the arena boundary while playing."));
                }
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer());
            F13Player player = FridayThe13th.playerController.getPlayer(event.getPlayer());

            if (event.hasItem() && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasLore() && event.getItem().getItemMeta().getLore().size() > 0 && HiddenStringsUtil.hasHiddenString(event.getItem().getItemMeta().getLore().get(0))) {
                F13MenuItemClickedEvent newEvent = new F13MenuItemClickedEvent(event.getPlayer(), arena, HiddenStringsUtil.extractHiddenString(event.getItem().getItemMeta().getLore().get(0)), event.getMaterial());
                Bukkit.getServer().getPluginManager().callEvent(newEvent);
                event.setCancelled(newEvent.isCancelled());
            } else if (arena.getGameManager().isGameInProgress()) {
                //Check to see if they're trampling anything
                if (event.getAction() == Action.PHYSICAL && event.getClickedBlock() != null && (event.getClickedBlock().getType() == Material.SOIL || event.getClickedBlock().getType() == Material.FIRE)) {
                    event.setCancelled(true);
                } else if (arena.getGameManager().getPlayerManager().isCounselor(player) && !arena.getGameManager().getPlayerManager().isSpectator(player)) {
                    //They're in regular play mode

                    //Insta-health regen
                    if (event.hasItem() && event.getItem().getType().equals(Material.POTION) && player.getBukkitPlayer().getHealth() < 20) {
                        event.getPlayer().setHealth(20);
                        InventoryActions.remove(event.getPlayer().getInventory(), Material.POTION, 1, (short) 0);
                    }

                    if (event.getClickedBlock() != null && event.getClickedBlock().getState().getData() instanceof Door)
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
                        } else {
                            //Door isn't broken
                            if (door.isOpen()) {
                                //They're closing a door - register for XP
                                arena.getGameManager().getPlayerManager().getCounselor(player).getXpManager().registerXPAward(XPAward.Counselor_DoorClosed);
                            } else {
                                //They're opening a door, random chance for jump scare noise
                                double scareChance = Math.random() * 100;
                                if ((scareChance -= 10) < 0)
                                {
                                    if (!SoundManager.areAnySoundsPlayingForPlayer(event.getPlayer()))
                                    {
                                        SoundManager.playSoundForPlayer(event.getPlayer(), F13SoundEffect.JumpScare, false, true, 0);
                                    }
                                }
                            }
                        }
                    } else if (event.getClickedBlock() != null && event.getClickedBlock().getState().getData() instanceof Lever) {
                        if (arena.getObjectManager().getBrokenSwitches().containsKey(event.getClickedBlock())) {
                            event.setCancelled(true);

                            if (event.hasItem() && event.getItem().getType().equals(Material.REDSTONE)) {
                                //Register the repair attempt
                                arena.getObjectManager().getBrokenSwitches().get(event.getClickedBlock()).repairSwitchAttempt(player);
                            } else {
                                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.needRepairWire", "You need repair wire to fix broken switches."));
                            }
                        } else {
                            //Players can only turn off switches on if they're not broken
                            BlockState state = event.getClickedBlock().getState();
                            Lever lever = (Lever)state.getData();

                            if (lever.isPowered()) {
                                if (!arena.getGameManager().getPlayerManager().getCounselor(player).getF13Player().hasPerk(F13Perk.Counselor_AhDarkness)) {
                                    //They don't have the "ah, darkness" perk, so they can't turn off levers that are powered on.
                                    event.setCancelled(true);
                                }
                            }
                        }

                    } else if (event.getClickedBlock() != null && event.getClickedBlock().getState().getData() instanceof TripwireHook) {
                        if (arena.getObjectManager().getPhoneManager().isBlockARegisteredPhone(event.getClickedBlock())) {
                            if (arena.getObjectManager().getPhoneManager().getPhone(event.getClickedBlock()).isBroken()) {
                                ArenaPhone phone = arena.getObjectManager().getPhoneManager().getPhone(event.getClickedBlock());
                                if ((!phone.isFusePresent() && event.getPlayer().getInventory().contains(Material.END_ROD)) || (phone.isFusePresent() && event.getPlayer().getInventory().contains(Material.REDSTONE))) {
                                    //Register the repair attempt
                                    arena.getObjectManager().getPhoneManager().getPhone(event.getClickedBlock()).callAttempt(player);
                                } else {
                                    if (!phone.isFusePresent()) {
                                        event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.needPhoneFuse", "You need a phone fuse to fix broken phones."));
                                    } else {
                                        event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.needPhoneRepairWire", "You need a repair wire to fix broken phones."));
                                    }
                                }
                            } else {
                                arena.getObjectManager().getPhoneManager().getPhone(event.getClickedBlock()).callAttempt(player);
                            }
                        }
                    } else if (event.getClickedBlock() != null && event.getClickedBlock().getState().getData() instanceof Chest) {
                        //They're interacting with a chest
                        if (arena.getObjectManager().isLocationAChest(event.getClickedBlock().getLocation())) {
                            //They're clicking one of the arena's chests - generate it
                            arena.getObjectManager().getChest(event.getClickedBlock().getLocation()).randomlyFill();
                        } else {
                            event.setCancelled(true); //They can't open non-arena chests
                        }
                    } else if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.BED_BLOCK)) {
                        //Players are not allowed to sleep during the game
                        event.setCancelled(true);
                    } else if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.TRIPWIRE_HOOK)) {
                        //Touching a phone
                        if (arena.getObjectManager().getPhoneManager().isBlockARegisteredPhone(event.getClickedBlock())) {
                            arena.getObjectManager().getPhoneManager().getPhone(event.getClickedBlock()).callAttempt(player);
                        } else {
                            event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.phoneNotManaged", "This phone has not been added to the arena. Ask your admin to add it."));
                        }
                    } else if (event.hasBlock() && event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.THIN_GLASS)) {
                        //Window jumping
                        if (event.getPlayer().isSprinting()) {
                            if (arena.getObjectManager().getWindowManager().canPlayerJumpThroughWindow(event.getClickedBlock(), event.getPlayer())) {
                                arena.getGameManager().getPlayerManager().getCounselor(player).teleportThroughWindow(event.getClickedBlock(), true, true);
                                arena.getGameManager().getPlayerManager().getCounselor(player).getXpManager().registerXPAward(XPAward.Counselor_WindowSprint); //Register event for XP
                            } else {
                                ActionBarAPI.sendActionBar(event.getPlayer(), FridayThe13th.language.get(event.getPlayer(), "actionBar.counselor.windowJumpFail2", "Can't jump! No free space on the other side of the window."), 40);
                            }

                        } else {
                            if (!arena.getGameManager().getPlayerManager().getCounselor(player).isAwaitingWindowJump()) {
                                arena.getGameManager().getPlayerManager().getCounselor(player).setAwaitingWindowJump(true); //So that they don't spam window jumps
                                ActionBarAPI.sendActionBar(event.getPlayer(), FridayThe13th.language.get(event.getPlayer(), "actionBar.counselor.windowJumpWait", "Don't move! You'll jump in {0} seconds...", "2"), 40);
                                Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new CounselorWindowJump(arena.getGameManager().getPlayerManager().getCounselor(player), event.getPlayer().getLocation(), event.getClickedBlock()), 40);
                            }
                        }
                    } else if (event.hasBlock() && event.getClickedBlock().getType().equals(Material.CARPET)) {
                        if (arena.getObjectManager().isATrap(event.getClickedBlock()) && arena.getObjectManager().getTrap(event.getClickedBlock()).getTrapType().equals(TrapType.Counselor) && !arena.getObjectManager().getTrap(event.getClickedBlock()).isActivated()) {
                            //They're clicking a counselor trap
                            arena.getObjectManager().getTrap(event.getClickedBlock()).activationAttempt(arena.getGameManager().getPlayerManager().getCounselor(player));
                        }
                    }
                } else if (arena.getGameManager().getPlayerManager().isJason(player))
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
                } else {
                    //They're a spectator and trying to interact without using one of their special objects
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true); //Disable interaction in the waiting room
            }
        } catch (PlayerNotPlayingException exception)
        {
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
                            arena.getGameManager().getPlayerManager().playerJoinGame(FridayThe13th.playerController.getPlayer(event.getPlayer()));
                        } catch (GameFullException e) {
                            event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.gameFull", "The game in {0} is currently full.", ChatColor.RED + arena.getName() + ChatColor.WHITE));
                        } catch (GameInProgressException e) {
                            //Enter as a spectator
                            arena.getGameManager().getPlayerManager().becomeSpectator(FridayThe13th.playerController.getPlayer(event.getPlayer()));
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer());
            F13Player player = FridayThe13th.playerController.getPlayer(event.getPlayer());

            if (arena.getGameManager().isGameInProgress())
            {
                if (arena.getLocationManager().getEscapePointManager().isLocationWithinEscapePoint(event.getTo())) {
                    if (arena.getGameManager().getPlayerManager().isCounselor(player) && !arena.getGameManager().getPlayerManager().isSpectator(player)) {
                        EscapePoint escapePoint = arena.getLocationManager().getEscapePointManager().getEscapePointFromLocation(event.getTo());
                        //Counselor is trying to move into
                        if ((escapePoint.isWaterPoint() && event.getPlayer().getVehicle() instanceof Boat) || (escapePoint.isLandPoint() && event.getPlayer().getVehicle() instanceof Minecart)) {
                            //Counselor escaping via vehicle
                            //TODO
                        } else {
                            //Counselor escaping via foot - can only do when police are there
                            if (escapePoint.isLandPoint() && escapePoint.isPoliceLocation()) {
                                //They can escape
                                arena.getGameManager().getPlayerManager().onPlayerEscape(player);
                            } else {
                                //Cannot escape on foot if the police aren't there.
                                event.setCancelled(true);
                            }
                        }
                    } else {
                        event.setCancelled(true); //Non-counselors cannot enter escape point areas
                    }
                } else if (!arena.isLocationWithinArenaBoundaries(event.getTo())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.arenaBoundary", "You can't leave the arena boundary while playing."));
                }

                if (arena.getGameManager().getPlayerManager().isSpectator(player)) {
                    //
                } else if (arena.getGameManager().getPlayerManager().isCounselor(player)) {

                    //Check their movement for their stats
                    if (event.getFrom().distance(event.getTo()) > 0) {
                        if (event.getPlayer().isSprinting()) {
                            if (arena.getGameManager().getPlayerManager().getCounselor(player).getStaminaPercentage() == 0) {
                                event.getPlayer().setSprinting(false);
                                event.setCancelled(true);
                            } else {
                                //Sprinting
                                arena.getGameManager().getPlayerManager().getCounselor(player).setSprinting(true);
                            }
                        } else if (event.getPlayer().isSneaking()) {
                            //Sneaking
                            arena.getGameManager().getPlayerManager().getCounselor(player).setSneaking(true);
                        } else if (event.getPlayer().isFlying()) {
                            if (!arena.getGameManager().getPlayerManager().isSpectator(player)) {
                                event.getPlayer().setFlying(false); //Prevent cheating
                            }
                        } else {
                            //Must just be walking
                            arena.getGameManager().getPlayerManager().getCounselor(player).setWalking(true);
                        }
                    }

                    //Check to see if they're on a trap
                    if (arena.getObjectManager().isATrap(event.getPlayer().getLocation().getBlock()) && arena.getObjectManager().getTrap(event.getPlayer().getLocation().getBlock()).getTrapType().equals(TrapType.Jason)) {
                        arena.getObjectManager().getTrap(event.getPlayer().getLocation().getBlock()).steppedOn(player);
                    }

                } else if (arena.getGameManager().getPlayerManager().isJason(player))
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

                    //Check to see if they're on a trap
                    if (arena.getObjectManager().isATrap(event.getPlayer().getLocation().getBlock()) && arena.getObjectManager().getTrap(event.getPlayer().getLocation().getBlock()).getTrapType().equals(TrapType.Counselor)) {
                        arena.getObjectManager().getTrap(event.getPlayer().getLocation().getBlock()).steppedOn(player);
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
                Arena arena = FridayThe13th.arenaController.getPlayerArena(player); //See if they're playing
                event.setCancelled(true);
            }
        } catch (PlayerNotPlayingException exception) {
            //Do nothing since in this case, we couldn't care
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (FridayThe13th.arenaController.isPlayerPlaying(event.getPlayer())) {
            if (!event.getMessage().toLowerCase().startsWith("/f13")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(event.getPlayer(), "game.error.commandsDisabled", "Only Friday the 13th commands are available during gameplay. If you'd like to leave, execute {0} /f13 leave", ChatColor.AQUA));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player)
        {
            try
            {
                Player temp = (Player) event.getEntity();

                //Make sure they're an actual player and not an NPC
                if (Bukkit.getPlayer(temp.getUniqueId()) != null) {
                    F13Player playerDamaged = FridayThe13th.playerController.getPlayer((Player) event.getEntity());
                    Arena arena = FridayThe13th.arenaController.getPlayerArena(playerDamaged); //See if they're playing

                    if (arena.getGameManager().isGameInProgress()) {
                        if (arena.getGameManager().getPlayerManager().isSpectator(playerDamaged)) {
                            event.setCancelled(true); //You can't get hurt in spectate mode
                        } else {
                            if (event instanceof EntityDamageByEntityEvent) {
                                EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent) event;
                                if (edbeEvent.getDamager() instanceof Player) {
                                    //The person doing the damage is a player, too.
                                    F13Player playerDamager = FridayThe13th.playerController.getPlayer((Player) edbeEvent.getDamager());

                                    if (FridayThe13th.arenaController.isPlayerPlaying(playerDamager)) {
                                        //The person doing the damage is playing
                                        if (arena.getGameManager().getPlayerManager().isSpectator(playerDamager)) {
                                            //The damage is a counselor in spectate mode
                                            event.setCancelled(true);
                                        } else if (arena.getGameManager().getPlayerManager().isCounselor(playerDamager)) {
                                            //Counselor is damaging
                                            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                                                if (playerDamager.getBukkitPlayer().getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD) || playerDamager.getBukkitPlayer().getInventory().getItemInMainHand().getType().equals(Material.IRON_AXE) || playerDamager.getBukkitPlayer().getInventory().getItemInMainHand().getType().equals(Material.WOOD_AXE)) {
                                                    //They're using a weapon
                                                    if (arena.getGameManager().getPlayerManager().isCounselor(playerDamaged) && playerDamaged.getBukkitPlayer().getHealth() <= event.getDamage()) {
                                                        //Friendly kill
                                                        arena.getGameManager().getPlayerManager().getCounselor(playerDamager).getXpManager().registerXPAward(XPAward.Counselor_FriendlyHit);
                                                    } else if (arena.getGameManager().getPlayerManager().isJason(playerDamaged)) {
                                                        //Jason Hit
                                                        arena.getGameManager().getPlayerManager().getJason().stun();

                                                        //Register counselor XP
                                                        arena.getGameManager().getPlayerManager().getCounselor(playerDamager).getXpManager().registerXPAward(XPAward.Counselor_JasonStuns);

                                                        //Alter damage based on their strength level
                                                        event.setDamage(event.getDamage() * playerDamager.getCounselorProfile().getStrength().getDepletionRate());

                                                        if (playerDamaged.getBukkitPlayer().getHealth() <= event.getDamage()) {
                                                            //Counselor killed jason
                                                            arena.getGameManager().getPlayerManager().getCounselor(playerDamager).getXpManager().registerXPAward(XPAward.Counselor_JasonKilled);
                                                        }
                                                    }
                                                } else {
                                                    event.setCancelled(true); //Counselors can't hurt unless they have a special item
                                                }
                                            }
                                        } else if (arena.getGameManager().getPlayerManager().isJason(playerDamager) && arena.getGameManager().getPlayerManager().isCounselor(playerDamaged)) {
                                            //Jason hitting a counselor

                                            //Play sound effect
                                            SoundManager.playSoundForNearbyPlayers(F13SoundEffect.Stab, arena, playerDamaged.getBukkitPlayer().getLocation(), 4, false, true);

                                            if (playerDamaged.getBukkitPlayer().getHealth() <= event.getDamage())
                                            {
                                                //Jason kills a counselor
                                                arena.getGameManager().getPlayerManager().getJason().getXpManager().registerXPAward(XPAward.Jason_CounselorKill);

                                                //
                                                if (arena.getGameManager().getPlayerManager().getJason().getF13Player().hasPerk(F13Perk.Jason_AxeThrow))
                                                {
                                                    //Has axe throw perk
                                                    ThrowableItem item = new ThrowableItem(playerDamager.getBukkitPlayer());
                                                    item.display();
                                                }
                                            }
                                        }
                                    } else {
                                        //The person doing the damage isn't even playing
                                        event.setCancelled(true);
                                        playerDamager.getBukkitPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(playerDamager.getBukkitPlayer(), "game.error.hitWhileNotPlaying", "You can't hit F13 players while you're not playing."));
                                    }
                                }
                            }
                        }

                        if (!event.isCancelled()) {
                            if (playerDamaged.getBukkitPlayer().getHealth() <= event.getDamage()) {
                                //This blow would kill them
                                event.setCancelled(true);
                                playerDamaged.getBukkitPlayer().setHealth(20);
                                arena.getGameManager().getPlayerManager().onPlayerDeath(playerDamaged);
                            }
                        }
                    } else {
                        //You can't get damaged while waiting
                        event.setCancelled(true);
                    }
                } else {
                    //They're not an actual player, most likely an NPC
                }
            }
            catch (PlayerNotPlayingException exception)
            {
                //Do nothing since in this case, we couldn't care
            }
        } else {
            //The entity being damaged is not a player, check to see if player is damaging this unknown entity
            if (event instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent) event;
                if (edbeEvent.getDamager() instanceof Player) {
                    if (FridayThe13th.arenaController.isPlayerPlaying((Player) edbeEvent.getDamager())) {
                        //F13 player is damaging a non-player entity.
                        event.setCancelled(true);
                    }
                }
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
                Arena arena = FridayThe13th.arenaController.getPlayerArena(player); //See if they're playing
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
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer()); //See if they're playing
            F13Player player = FridayThe13th.playerController.getPlayer(event.getPlayer());

            if (arena.getGameManager().isGameInProgress() && arena.getGameManager().getPlayerManager().isCounselor(player) && !arena.getGameManager().getPlayerManager().isSpectator(player))
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
            Arena arena = FridayThe13th.arenaController.getPlayerArena((Player) event.getWhoClicked()); //See if they're playing
            Player player = (Player)event.getWhoClicked();

            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().hasItemMeta()) {
                    //It has meta, so it must be one of the F13 selections
                    ItemMeta meta = event.getCurrentItem().getItemMeta();
                    List<String> lore = meta.getLore();

                    if (lore != null && lore.size() > 0 && HiddenStringsUtil.hasHiddenString(lore.get(0))) {
                        //It's a F13 menu item - call custom event
                        F13MenuItemClickedEvent newEvent = new F13MenuItemClickedEvent(player, arena, HiddenStringsUtil.extractHiddenString(lore.get(0)), event.getCurrentItem().getType());
                        Bukkit.getServer().getPluginManager().callEvent(newEvent);
                        event.setCancelled(newEvent.isCancelled());
                    }
                }
            }
        }
        catch (PlayerNotPlayingException exception)
        {
            //They're not playing, but we have non-player GUI's
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().hasItemMeta()) {
                    //It has meta, so it must be one of the F13 selections
                    ItemMeta meta = event.getCurrentItem().getItemMeta();
                    List<String> lore = meta.getLore();

                    if (lore != null && lore.size() > 0 && HiddenStringsUtil.hasHiddenString(lore.get(0))) {
                        //It's a F13 menu item - call custom event
                        Player player = (Player)event.getWhoClicked();
                        F13MenuItemClickedEvent newEvent = new F13MenuItemClickedEvent(player, FridayThe13th.arenaController.getRandomArena(), HiddenStringsUtil.extractHiddenString(lore.get(0)), event.getCurrentItem().getType());
                        Bukkit.getServer().getPluginManager().callEvent(newEvent);
                        event.setCancelled(newEvent.isCancelled());
                    }
                }
            }
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
                Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer()); //See if they're playing
                FridayThe13th.chatController.routeInternalMessage(FridayThe13th.playerController.getPlayer(event.getPlayer()), event.getMessage(), arena);
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
            event.getRecipients().removeAll(FridayThe13th.chatController.getAllF13Players());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent event) {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getPlayer());

            if (arena.getGameManager().isGameInProgress()) {
                if (arena.getGameManager().getPlayerManager().isSpectator(FridayThe13th.playerController.getPlayer(event.getPlayer()))) {
                    event.setCancelled(true); //Spectators can't pick things up
                } else if (arena.getGameManager().getPlayerManager().isJason(FridayThe13th.playerController.getPlayer(event.getPlayer()))) {
                    if (!event.getItem().getType().equals(Material.ARROW)) {
                        //Jason can only pickup arrows
                        event.setCancelled(true);
                    }
                }

            }
        } catch (PlayerNotPlayingException exception) {
            //
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {

        if (FridayThe13th.arenaController.isPlayerPlaying(event.getPlayer())) {
            event.setCancelled(true); //Cannot manipulate armor stands while playing
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCorpseClick(CorpseClickEvent event)
    {
        try {
            Arena arena = FridayThe13th.arenaController.getPlayerArena(event.getClicker());

            if (arena.getGameManager().isGameInProgress())
            {
                if (arena.getGameManager().getPlayerManager().isSpectator(FridayThe13th.playerController.getPlayer(event.getClicker())))
                {
                    event.setCancelled(true); //Spectators can't interact with corpses
                } else if (arena.getGameManager().getPlayerManager().isJason(FridayThe13th.playerController.getPlayer(event.getClicker())))
                {
                    event.setCancelled(true); //Jason can't interact with corpses
                }

            }
        } catch (PlayerNotPlayingException exception) {
            //
        }
    }
}
