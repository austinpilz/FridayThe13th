package com.AustinPilz.FridayThe13th.Components.Characters;

import com.AustinPilz.CustomSoundManagerAPI.API.PlayerSoundAPI;
import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Menu.Profiles_MainMenu;
import com.AustinPilz.FridayThe13th.Components.Menu.Shop_MainMenu;
import com.AustinPilz.FridayThe13th.Components.Menu.Shop_PurchasedPerksMenu;
import com.AustinPilz.FridayThe13th.Components.Menu.SpawnPreferenceMenu;
import com.AustinPilz.FridayThe13th.Components.Skin.SkinChange;
import com.AustinPilz.FridayThe13th.Components.Skin.SkinChange_0_0;
import com.AustinPilz.FridayThe13th.Components.Skin.SkinChange_1_12;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Statistics.XPManager;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class F13Character {
    private F13Player f13Player;
    protected Arena arena;
    protected SkinChange skin;
    private XPManager xpManager;

    //Restore values
    private float originalWalkSpeed;
    private float originalFlySpeed;
    private boolean originalAllowFly;

    F13Character(F13Player player, Arena arena) {
        this.f13Player = player;
        this.arena = arena;
        this.xpManager = new XPManager(f13Player, arena);

        //Restore Values
        originalWalkSpeed = getF13Player().getBukkitPlayer().getWalkSpeed();
        originalFlySpeed = getF13Player().getBukkitPlayer().getFlySpeed();
        originalAllowFly = getF13Player().getBukkitPlayer().getAllowFlight();

        //Skin Change
        if (FridayThe13th.serverVersion.equalsIgnoreCase("v1_12_R1")) {
            skin = new SkinChange_1_12(getF13Player().getBukkitPlayer());
        } else {
            skin = new SkinChange_0_0(getF13Player().getBukkitPlayer());
        }
    }

    /**
     * @return F13Player object
     */
    public F13Player getF13Player() {
        return f13Player;
    }

    /**
     * @return Bukkit player
     */
    public Player getPlayer() {
        return getF13Player().getBukkitPlayer();
    }

    /**
     * @return The character's arena
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * @return The character's XP manager
     */
    public XPManager getXpManager() {
        return xpManager;
    }

    /**
     * @return The player's original walking speed
     */
    private float getOriginalWalkSpeed() {
        return originalWalkSpeed;
    }

    /**
     * @return The player's original fly speed
     */
    private float getOriginalFlySpeed() {
        return originalFlySpeed;
    }

    /**
     * @return The player's original permission to fly
     */
    private boolean isOriginallyAllowedFly() {
        return originalAllowFly;
    }

    /**
     * Restores the players original speed values
     */
    protected void restoreOriginalSpeeds() {
        if (getF13Player().isOnline()) {
            getF13Player().getBukkitPlayer().setFlySpeed(getOriginalFlySpeed());
            getF13Player().getBukkitPlayer().setWalkSpeed(getOriginalWalkSpeed());
            getF13Player().getBukkitPlayer().setAllowFlight(isOriginallyAllowedFly());
        }
    }

    /**
     * Prepares the player for the waiting room and teleports them in
     */
    public void enterWaitingRoom() {
        teleportToWaitingRoom();
        setDefaultSurvivalTraits();
        giveWaitingRoomItems();

        //Begin waiting room music
        PlayerSoundAPI.getPlayerSoundManager(getF13Player().getBukkitPlayer()).playCustomSound(getF13Player().getBukkitPlayer().getLocation(), F13SoundEffect.LobbyMusic.getResourcePackValue(), F13SoundEffect.LobbyMusic.getLengthInSeconds(), 10, true, true);
    }

    /**
     * Removes the character from the game and restores them to pre-game status
     */
    public void leaveGame() {
        teleportToReturnPoint();

        if (arena.getGameManager().isGameWaiting() || arena.getGameManager().isGameEmpty()) {
            if (getF13Player().isOnline()) {
                getF13Player().getWaitingPlayerStatsDisplayManager().removeStatsScoreboard();
                arena.getGameManager().getWaitingCountdownDisplayManager().hideForPlayer(getF13Player().getBukkitPlayer());
                getF13Player().getBukkitPlayer().getInventory().clear();

                //Stop all music
                PlayerSoundAPI.getPlayerSoundManager(getF13Player().getBukkitPlayer()).stopAllSounds();
            }
        } else if (arena.getGameManager().isGameInProgress()) {
            if (arena.getGameManager().getPlayerManager().isSpectator(getF13Player())) {
                arena.getGameManager().getPlayerManager().leaveSpectator(getF13Player());

                //Clear the action bar and hide spectator displays
                ActionBarAPI.sendActionBar(getF13Player().getBukkitPlayer(), "");
                arena.getGameManager().getGameCountdownManager().hideFromPlayer(getF13Player().getBukkitPlayer());
                arena.getGameManager().getGameScoreboardManager().hideFromPlayer(getF13Player().getBukkitPlayer());

                //Make them visible to everyone again
                if (getF13Player().getBukkitPlayer().isOnline()) {
                    //Make visible to all players
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.showPlayer(getF13Player().getBukkitPlayer());
                    }
                }
            }

            if (arena.getGameManager().getPlayerManager().isCounselor(getF13Player())) {
                Counselor counselor = arena.getGameManager().getPlayerManager().getCounselor(getF13Player());

                //Cancel scheduled tasks
                counselor.cancelTasks();

                //Remove any potions
                counselor.removePotionEffects();

                //Hide the stats bars
                counselor.getCounselorStatsDisplayManager().hideStats();

                //Remove skin
                counselor.removeSkin();
            } else if (arena.getGameManager().getPlayerManager().isJason(getF13Player())) {
                Jason jason = arena.getGameManager().getPlayerManager().getJason();

                //Cancel tasks
                jason.cancelTasks();

                //Remove skin
                jason.removeSkin();

                //Stop Potions
                jason.removePotionEffects();

                //Remove his ability display
                jason.getAbilityDisplayManager().hideAbilities();
            }

            if (getF13Player().isOnline()) {
                removeAllPotionEffects();
                arena.getGameManager().getGameScoreboardManager().hideFromPlayer(getF13Player().getBukkitPlayer());

                makePlayerVisibleToEveryone(true);

                //Player stats
                getF13Player().getBukkitPlayer().getInventory().clear();
                getF13Player().getBukkitPlayer().setHealth(20);
                getF13Player().getBukkitPlayer().setFoodLevel(20);
                restoreOriginalSpeeds();

                //Sounds
                PlayerSoundAPI.getPlayerSoundManager(getF13Player().getBukkitPlayer()).stopAllSounds();
            }
        }
    }

    /**
     * Puts the player into classic survival mode for the game
     */
    private void setDefaultSurvivalTraits() {
        //Change game mode & clear inventory
        getF13Player().getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
        getF13Player().getBukkitPlayer().setHealth(20);
        getF13Player().getBukkitPlayer().setFoodLevel(10);
        getF13Player().getBukkitPlayer().getInventory().clear();
    }

    /**
     * Gives the player the waiting room usable items
     */
    private void giveWaitingRoomItems() {
        //Give them waiting room items
        SpawnPreferenceMenu.addMenuOpenItem(getF13Player().getBukkitPlayer());
        Profiles_MainMenu.addMenuOpenItem(getF13Player().getBukkitPlayer());
        Shop_MainMenu.addMenuOpenItem(getF13Player().getBukkitPlayer());
        Shop_PurchasedPerksMenu.addMenuOpenItem(getF13Player().getBukkitPlayer());
    }

    /**
     * Teleports the player to the arena's waiting room
     */
    private void teleportToWaitingRoom() {
        teleport(arena.getWaitingLocation());
    }

    /**
     * Teleports the player to the arena's return point
     */
    private void teleportToReturnPoint() {
        teleport(arena.getReturnLocation());
    }

    /**
     * Teleports the player to the Jason starting point
     */
    public void teleportToJasonStartingPoint() {
        teleport(getArena().getJasonStartLocation());
    }

    /**
     * Teleports player to location
     *
     * @param location Teleport to location
     */
    public void teleport(Location location) {
        if (getF13Player().isOnline()) {
            getF13Player().getBukkitPlayer().teleport(location);
        }
    }

    /**
     * Removes all active potion effects for the player
     */
    private void removeAllPotionEffects() {
        if (getF13Player().isOnline()) {
            //Remove all potion effects
            for (PotionEffect effect : getF13Player().getBukkitPlayer().getActivePotionEffects()) {
                getF13Player().getBukkitPlayer().removePotionEffect(effect.getType());
            }
        }
    }

    /**
     * Makes the player visible to all other players on the server
     */
    public void makePlayerVisibleToEveryone(boolean show) {
        //Make them visible to everyone again
        if (getF13Player().getBukkitPlayer().isOnline()) {
            //Make visible to all players
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (show) {
                    player.showPlayer(getF13Player().getBukkitPlayer());
                } else {
                    player.hidePlayer(getF13Player().getBukkitPlayer());
                }

            }
        }
    }
}
