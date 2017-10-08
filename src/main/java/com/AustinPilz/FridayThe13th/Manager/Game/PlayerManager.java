package com.AustinPilz.FridayThe13th.Manager.Game;

import com.AustinPilz.CustomSoundManagerAPI.API.PlayerSoundAPI;
import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
import com.AustinPilz.FridayThe13th.Components.Characters.Jason;
import com.AustinPilz.FridayThe13th.Components.Characters.Spectator;
import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Menu.*;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerAlreadyPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.austinpilz.ResourcePackAPI.Components.ResourcePackRequest;
import com.austinpilz.ResourcePackAPI.ResourcePackAPI;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class PlayerManager
{
    private Arena arena;

    //Game Players
    private HashMap<String, Player> players;
    private HashMap<String, Counselor> counselors;
    private HashMap<String, Spectator> spectators;
    private Jason jason;

    //Game Stat
    private HashMap<String, Player> alivePlayers;
    private HashSet<String> deadPlayers;

    /**
     * @param arena Game
     */
    public PlayerManager(Arena arena)
    {
        this.arena = arena;
        this.players = new HashMap<>();
        this.counselors = new HashMap<>();
        this.alivePlayers = new HashMap<>();
        this.deadPlayers = new HashSet<>();
        this.spectators = new HashMap<>();
    }

    /**
     * Resets lists of alive and dead players
     */
    public void resetPlayerStorage()
    {
        players.clear();
        counselors.clear();
        alivePlayers.clear();
        deadPlayers.clear();
        spectators.clear();
    }

    /**
     * Returns the number of players in the game
     * @return
     */
    public int getNumPlayers()
    {
        return players.size();
    }

    /**
     * Returns hash map of current players
     * @return
     */
    public HashMap<String, Player> getPlayers()
    {
        return players;
    }

    /**
     * Returns hash map of current counselors
     * @return
     */
    public HashMap<String, Counselor> getCounselors() { return counselors; }

    /**
     * Returns living players
     * @return
     */
    public HashMap<String, Player> getAlivePlayers()
    {
        return alivePlayers;
    }

    /**
     * Returns dead players
     * @return
     */
    public HashSet<String> getDeadPlayers()
    {
        return deadPlayers;
    }

    /**
     * Returns spectators
     * @return
     */
    public HashMap<String, Spectator> getSpectators() { return spectators; }

    /**
     * Returns existing spectator object for supplied player UUID
     * @param uuid
     * @return
     */
    public Spectator getSpectator(String uuid)
    {
        return spectators.get(uuid);
    }

    /**
     * Returns existing spectator object for supplied player object
     * @param player
     * @return
     */
    public Spectator getSpectator(Player player) { return spectators.get(player.getUniqueId().toString()); }

    /**
     * Returns the number of spectators
     *
     * @return
     */
    public int getNumSpectators() {
        return spectators.size();
    }

    /**
     * Returns if the player with supplied uuid is a spectator
     * @param uuid
     * @return
     */
    public boolean isSpectator(String uuid)
    {
        return spectators.containsKey(uuid);
    }

    /**
     * Returns if the player object is a spectator
     * @param player
     * @return
     */
    public boolean isSpectator(Player player) { return isSpectator(player.getUniqueId().toString()); }

    /**
     * Returns if the player is just a spectator and not counselor/jason
     * @param uuid
     * @return
     */
    public boolean isJustSpectator(String uuid)
    {
        if (isSpectator(uuid))
        {
            Spectator spectator = getSpectator(uuid);
            return !(isCounselor(spectator.getPlayer()) || isJason(spectator.getPlayer()));
        }
        else
        {
            return false;
        }
    }

    /**
     * Adds a spectator to the HashMap
     * @param spectator
     */
    private void addSpectator(Spectator spectator)
    {
        spectators.put(spectator.getPlayer().getUniqueId().toString(), spectator);
    }

    /**
     * Removes a spectator from the HashMap
     * @param uuid
     */
    private void removeSpectator(String uuid)
    {
        spectators.remove(uuid);
    }

    /**
     * Returns the number of players that are alive
     * @return
     */
    public int getNumPlayersAlive()
    {
        return alivePlayers.size();
    }

    /**
     * Returns the number of players that are dead
     * @return
     */
    public int getNumPlayersDead()
    {
        return deadPlayers.size();
    }

    /**
     * Returns the number of counselors
     * @return
     */
    public int getNumCounselors()
    {
        return counselors.size();
    }
    /**
     * Returns if the supplied player is a counselor
     * @param player
     * @return
     */
    public boolean isCounselor(Player player)
    {
        return counselors.containsKey(player.getUniqueId().toString());
    }

    /**
     * Returns if the supplied payer is jason
     * @param player
     * @return
     */
    public boolean isJason(Player player)
    {
        return jason != null && jason.getPlayer().equals(player);
    }

    /**
     * Returns if the player is in the alive players hash map
     * @param player
     * @return
     */
    public boolean isAlive(Player player)
    {
        return alivePlayers.containsKey(player.getUniqueId().toString());
    }

    /**
     * Returns the counselor object for the player
     * @param player
     * @return
     */
    public Counselor getCounselor (Player player)
    {
        return counselors.get(player.getUniqueId().toString());
    }

    /**
     * Returns the jason object
     * @return
     */

    public Jason getJason () { return jason; }
    /**
     * Returns the counselor object for the player
     * @param playerUUID
     * @return
     */
    public Counselor getCounselor (String playerUUID)
    {
        return counselors.get(playerUUID);
    }

    /**
     * Adds player to the player hash map
     * @param p
     */
    private void addPlayer(Player p)
    {
        players.put(p.getUniqueId().toString(), p);
    }

    /**
     * Removes player from the player hash map
     * @param playerUUID
     */
    private void removePlayer(String playerUUID)
    {
        players.remove(playerUUID);
    }

    /**
     * Calculates if there is room for the player to join the game
     * @return
     */
    public boolean isRoomForPlayerToJoin()
    {
        //Determine if we have enough spawn points for the game's 8 counselors
        if (arena.getLocationManager().getNumberStartingPoints() >= 8)
        {
            //We don't have to worry about spawn points
            //Games capped at 8 counselors + Jason.
            return getNumPlayers() < 9;
        }
        else
        {
            //They're are less than 8 spawn points for counselors
            return arena.getLocationManager().getNumberStartingPoints() - getNumPlayers() + 1 > 0;
        }
    }

    /**
     * Returns the max number of players in the arena
     * @return
     */
    private int getMaxNumberOfPlayers()
    {
        if (arena.getLocationManager().getNumberStartingPoints() >= 8)
        {
            //We don't have to worry about spawn points
            //Games capped at 8 counselors + Jason.
            return 9;
        }
        else
        {
            //They're are less than 8 spawn points for counselors
            return arena.getLocationManager().getNumberStartingPoints();
        }
    }


   /* Display */

    /**
     * Resets the action bars of all players to nothing
     */
    public void resetPlayerActionBars()
    {
        Iterator it = getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player player = (Player) entry.getValue();
            ActionBarAPI.sendActionBar(player, "");
        }
    }

    /**
     * Displays the waiting countdown for all players
     */
    public void displayWaitingCountdown()
    {
        Iterator it = getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player player = (Player) entry.getValue();
            arena.getGameManager().getWaitingCountdownDisplayManager().displayForPlayer(player);
        }
    }

    /**
     * Hides the waiting countdown from all players
     */
    public void hideWaitingCountdown()
    {
        arena.getGameManager().getWaitingCountdownDisplayManager().hideFromAllPlayers();
    }


    /* Player Events */

    /**
     * Adds player to the game, if room is available
     * @param player
     * @throws GameFullException
     */
    public synchronized void playerJoinGame(Player player) throws GameFullException, GameInProgressException
    {
        if (true)
        {
            //All is well with the resource pack, they can join
            if (arena.getGameManager().isGameEmpty() || arena.getGameManager().isGameWaiting()) {
                //Determine if there's room for this user
                if (isRoomForPlayerToJoin()) {
                    try {
                        //Add to lists
                        FridayThe13th.arenaController.addPlayer(player.getUniqueId().toString(), arena);
                        addPlayer(player);
                        alivePlayers.put(player.getUniqueId().toString(), player);

                        //Waiting actions
                        performWaitingActions(player);

                        //Announce arrival
                        int playerNumber = players.size();
                        sendMessageToAllPlayers(ChatColor.GRAY + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerJoinBroadcast", "{0} has joined the game.", player.getName()) + " (" + playerNumber + "/" + getMaxNumberOfPlayers()+")");

                        if (players.size() == 1)
                        {
                            arena.getSignManager().updateJoinSigns(); //If it's just them, update signs
                        }

                    } catch (PlayerAlreadyPlayingException exception) {
                        //They're already in the controller global player list
                        player.sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerJoinFailAR", "Failed to add you to game because you're already registered as playing a game."));
                    }
                } else {
                    throw new GameFullException();
                }
            }
            else
            {
                throw new GameInProgressException();
            }
        }
        else
        {
            //They don't have the resource pack, so let's request it
            ResourcePackRequest request = new ResourcePackRequest("f13", player, "https://s3.amazonaws.com/fridaythe13th/F13.zip", FridayThe13th.pluginPrefix + "Resource pack success! Please join the game again", FridayThe13th.pluginPrefix + "Error! You MUST have the resource pack in order to play. You'll need to re-enable server resource packs on this server by returning to your server selection screen, hitting edit and enabling them. Then completely quit and restart your Minecraft client.", false);
            ResourcePackAPI.playerController.getPlayer(player).newResourcePackRequest(request);
            request.sendRequest();
        }
    }

    /**
     * Performs actions when a player logs off of server
     * @param playerUUID
     */
    public void onPlayerLogout(String playerUUID)
    {
        //Hurry and see if we can teleport them out and clear inventory
        Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));
        player.teleport(arena.getReturnLocation());
        player.getInventory().clear(); 

        if (!isJustSpectator(playerUUID)) {
            //Cleanup
            performPlayerCleanupActions(playerUUID);

            if (arena.getGameManager().isGameInProgress()) {

                if (isJason(player)) {
                    //Jason logged off, so end the game
                    sendMessageToAllPlayers(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerLogoutJasonBroadcast", "GAME OVER! {0} (Jason) has left the game.", player.getName()));
                    arena.getGameManager().endGame();
                } else {
                    //They're a counselor
                    if (getNumPlayersAlive() <= 1) {
                        //They were the last one
                        arena.getGameManager().endGame();
                    }
                }
            } else {
                if (players.size() == 0) {
                    arena.getSignManager().updateJoinSigns(); //If it's just them, update signs
                }
            }


            //Message everyone in game
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
            sendMessageToAllPlayers(ChatColor.GRAY + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerLogoutBroadcast", "{0} has logged out and left the game.", player.getName()));
        } else {
            //Just a spectator who logged out
            leaveSpectator(player);
        }
    }

    /**
     * Performs actions when a player quits the game via command
     * @param player
     */
    public void onplayerQuit(Player player)
    {
        if (!isJustSpectator(player.getUniqueId().toString()))
        {
            //Clean up
            performPlayerCleanupActions(player.getUniqueId().toString());

            if (arena.getGameManager().isGameInProgress())
            {

                if (isJason(player)) {
                    //Jason quit off, so end the game
                    sendMessageToAllPlayers(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerQuitJasonBroadcast", "GAME OVER! {0} (Jason) has left the game.", player.getName()));
                    arena.getGameManager().endGame();
                } else {
                    //They're a counselor
                    if (getNumPlayersAlive() <= 1) {
                        //They were the last one
                        arena.getGameManager().endGame();
                    }
                }
            }
            else
            {
                if (players.size() == 0)
                {
                    arena.getSignManager().updateJoinSigns(); //If it's just them, update signs
                }
            }

            //Message everyone in game
            sendMessageToAllPlayers(ChatColor.GRAY + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerQuitBroadcast", "{0} has left the game.", player.getName()));
        }
        else
        {
            //They're just a spectator leaving spectate mode
            leaveSpectator(player);
        }
    }

    /**
     * Performs actions when a player dies in game
     * @param player
     */
    public void onPlayerDeath(Player player)
    {
        if (arena.getGameManager().isGameInProgress()) {
            //Transition from alive to dead hash set
            alivePlayers.remove(player.getUniqueId().toString());
            deadPlayers.add(player.getUniqueId().toString());

            //Check to see if they're jason, which would end the game
            if (isJason(player)) {
                //Counselors win
                counselorsWin();
                arena.getGameManager().endGame(); //Game over kiddos
            } else {
                //Firework
                arena.getGameManager().getPlayerManager().fireFirework(player, Color.RED);

                //They're a normal player, see if there are still others alive
                if (getNumPlayersAlive() > 1) //since jason is still presumably alive
                {
                    //Spawn their corpse
                    arena.getObjectManager().spawnCorpse(player);

                    //They're are others still alive, enter spectating mode
                    getCounselor(player).transitionToSpectatingMode();
                    becomeSpectator(player);
                } else {
                    //They were the last to die, so end the game
                    arena.getGameManager().endGame();
                }
            }

            //Let everyone know
            sendMessageToAllPlayers(ChatColor.GRAY + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerKilledBroadcast", "{0} was killed.", player.getName()));
        }
    }

    /* Spectator Actions */

    /**
     * Adds the player as a spectator and performs all actions to put them into the game
     * @param player
     */
    public void becomeSpectator(Player player)
    {
        addSpectator(new Spectator(player, arena));
        getSpectator(player).enterSpectatingMode();

        try
        {
            FridayThe13th.arenaController.addPlayer(player.getUniqueId().toString(), arena);
            addPlayer(player);
        }
        catch (PlayerAlreadyPlayingException exception)
        {
            //Don't need to do anything
        }
    }

    /**
     * Removes the player as a spectator and performs all actions to remove them from the game
     * @param player
     */
    public void leaveSpectator(Player player)
    {
        //Need to remove from player lists if they're just a spectator
        if (isJustSpectator(player.getUniqueId().toString()))
        {
            FridayThe13th.arenaController.removePlayer(player.getUniqueId().toString());
            removePlayer(player.getUniqueId().toString());
        }

        getSpectator(player).leaveSpectatingMode();
        removeSpectator(player.getUniqueId().toString());

        //Teleport them out if they're not a counselor
        if (!isCounselor(player))
        {
            if (Bukkit.getOfflinePlayer(player.getUniqueId()).isOnline())
            {
                player.teleport(arena.getReturnLocation());
            }
        }

    }

    public void leaveSpectator(String uuid)
    {
        leaveSpectator(getSpectator(uuid).getPlayer());
    }


    /* Player Preparation Actions */

    /**
     * Performs waiting actions for specific player
     * @param player
     */
    private void performWaitingActions(Player player)
    {
        //Teleport player to waiting location
        teleportPlayerToWaitingPoint(player);

        //Show the countdown timer
        FridayThe13th.playerController.getPlayer(player).getWaitingPlayerStatsDisplayManager().displayStatsScoreboard();
        arena.getGameManager().getWaitingCountdownDisplayManager().displayForPlayer(player);

        //Change game mode & clear inventory
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(10);
        player.getInventory().clear();

        //Give them waiting room items
        SpawnPreferenceMenu.addMenuOpenItem(player);
        Profiles_MainMenu.addMenuOpenItem(player);
        Shop_MainMenu.addMenuOpenItem(player);
        PurchasedPerksMenu.addMenuOpenItem(player);

        //Begin waiting room music
        PlayerSoundAPI.getPlayerSoundManager(player).playCustomSound(player.getLocation(), F13SoundEffect.LobbyMusic.getResourcePackValue(), F13SoundEffect.LobbyMusic.getLengthInSeconds(), 10, true, true);
    }

    /**
     * Transitions and prepares all players to play the game
     */
    protected void performInProgressActions()
    {
        FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Game in " + arena.getArenaName() + " beginning...");

        //Assign roles and teleport players there
        assignGameRoles();


        assignSpawnLocations();


        //Hide waiting scoreboard from everyone
        for (Player player : players.values()) {
            FridayThe13th.playerController.getPlayer(player).getWaitingPlayerStatsDisplayManager().removeStatsScoreboard();
        }



        //Display player bars
        Iterator it = getCounselors().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Counselor counselor = (Counselor) entry.getValue();

            counselor.prepareForGameplay();
        }



        //Jason stuff
        jason.prepareforGameplay();

        //Tell everyone who Jason is
        sendMessageToAllPlayers(ChatColor.AQUA + jason.getPlayer().getName() + ChatColor.WHITE + " is Jason.");

        //Play game start music
        SoundManager.playSoundForAllPlayers(F13SoundEffect.Music_GameStart, arena, true, true, 0);
    }

    /**
     * Assigns all players a role (counselor or jason)
     */
    private void assignGameRoles()
    {

        //Populate role preference data
        List<Player> preferJason = new ArrayList<>();
        List<Player> preferCounselor = new ArrayList<>();
        List<Player> noPreference = new ArrayList<>();

        for (Player player : players.values())
        {
            F13Player f13p = FridayThe13th.playerController.getPlayer(player);
            if (f13p.isSpawnPreferenceJason()) {
                preferJason.add(player);
            } else if (f13p.isSpawnPreferenceCounselor()) {
                preferCounselor.add(player);
            } else {
                //No preference set
                noPreference.add(player);
            }
        }

        //Determine who Jason is going to be
        if (preferJason.size() > 0) {
            //There is at least one person who prefers to be jason
            if (preferJason.size() > 1) {
                //More than one person who prefer to be jason
                assignJason(pickRandomPlayer(preferJason));
            } else {
                //There's just one person who prefers to be Jason, so they become Jason
                assignJason(preferJason.get(0));
            }
        } else {
            //There's nobody who has it set that they want to be Jason
            if (noPreference.size() > 0) {
                //There's at least one person with no preference set, so they'll be Jason
                if (noPreference.size() > 1) {
                    //There's more than one person with no preference, choose someone random
                    assignJason(pickRandomPlayer(noPreference));
                } else {
                    //There's just one person with no preference, so they get Jason
                    assignJason(noPreference.get(0));
                }
            } else {
                //Everyone prefers counselor, but tough shit at this point, choose a random counselor
                assignJason(pickRandomPlayer(preferCounselor));
            }
        }

        //Set everyone who is not Jason as a counselor
        for (Player player : players.values()) {
            if (!isJason(player)) {
                assignCounselor(player);
            }
        }
    }

    /**
     * Assigns the provided player as Jason
     *
     * @param player
     */
    private void assignJason(Player player) {
        this.jason = new Jason(player, arena);
    }

    /**
     * Assigns the provided player as a counselor
     *
     * @param player
     */
    private void assignCounselor(Player player) {
        this.counselors.put(player.getUniqueId().toString(), new Counselor(player, arena));
    }

    /**
     * Picks a random player from supplied player list
     *
     * @param players
     * @return
     */
    public Player pickRandomPlayer(List<Player> players) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, players.size() - 1);
        return players.get(randomNum);
    }


    /**
     * Assigns and teleports players and jason to their spawn locations
     */
    private void assignSpawnLocations()
    {
        //Teleport jason to jason start point
        jason.getPlayer().teleport(arena.getJasonStartLocation());

        //Teleport counselors to starting points
        Location[] counselorLocations = arena.getLocationManager().getAvailableStartingPoints().toArray(new Location[arena.getLocationManager().getAvailableStartingPoints().size()]);

        //Randomize starting points
        Random rnd = ThreadLocalRandom.current();
        for (int i = counselorLocations.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);

            // Simple swap
            Location a = counselorLocations[index];
            counselorLocations[index] = counselorLocations[i];
            counselorLocations[i] = a;
        }

        //Assign the spots
        Iterator it = getCounselors().entrySet().iterator();
        int i = 0;
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Counselor counselor = (Counselor) entry.getValue();
            counselor.getPlayer().teleport(counselorLocations[i++]);
        }
    }

    /**
     * Transtions players from the game once it ends
     */
    protected void performEndGameActions()
    {
        //Game ended
        sendMessageToAllPlayers(ChatColor.RED + "Game over! " + ChatColor.WHITE + getNumPlayersDead() + "/" + getNumCounselors() + " counselors killed." + " Thanks for playing Friday the 13th.");

        //Award XP to Counselors and Jason
        Iterator counselorIterator = getCounselors().entrySet().iterator();
        while (counselorIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) counselorIterator.next();
            Counselor counselor = (Counselor) entry.getValue();
            counselor.awardXP();
            counselor.awardCP();
        }

        //If Jason killed all of the players, he gets a time bonus
        if (getNumPlayersAlive() == 0) {
            getJason().getXPManager().setTimeLeftMinutes(arena.getGameManager().getGameTimeLeft() / 60);
        }

        //Award Jason XP if there was a current game
        if (getJason() != null) {
            getJason().awardXP();
            getJason().awardCP();
        }

        //Clean everyone up
        Iterator it = getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player player = (Player) entry.getValue();
            it.remove();
            performPlayerCleanupActions(player.getUniqueId().toString());
        }
    }

    /**
     * Cleans up a player and restores them to pre-game status
     * @param playerUUID
     */
    private void performPlayerCleanupActions(String playerUUID)
    {
        //Get offline player to be able to account for both online and offline players
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));

        //Remove player from hash maps (have to remove these first since listeners check to see if player has an arena)
        FridayThe13th.arenaController.removePlayer(playerUUID);
        removePlayer(playerUUID);
        alivePlayers.remove(playerUUID);
        deadPlayers.remove(playerUUID);

        if (arena.getGameManager().isGameWaiting() || arena.getGameManager().isGameEmpty())
        {
            //Waiting mode, so just teleport them out
            if (offlinePlayer.isOnline())
            {
                //Teleport them to the return point
                Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));

                FridayThe13th.playerController.getPlayer(player).getWaitingPlayerStatsDisplayManager().removeStatsScoreboard();
                teleportPlayerToReturnPoint(player);
                arena.getGameManager().getWaitingCountdownDisplayManager().hideForPlayer(player);
                player.getInventory().clear();

                //Stop all music
                PlayerSoundAPI.getPlayerSoundManager(player).stopAllSounds();
            }
        }
        else
        {
            if (isSpectator(playerUUID))
            {
                leaveSpectator(playerUUID);
            }

            if (isCounselor((Player)offlinePlayer))
            {
                Counselor counselor = getCounselor(playerUUID);

                //Cancel scheduled tasks
                counselor.cancelTasks();

                //Remove any potions
                counselor.removePotionEffects();
                counselor.restoreOriginalSpeeds();

                //Hide the stats bars
                counselor.getStatsDisplayManager().hideStats();

                //Remove skin
                counselor.removeSkin();

                //Remove from counselors
                counselors.remove(playerUUID);
            }
            else if (isJason((Player)offlinePlayer))
            {
                //Stop tasks
                jason.cancelTasks();

                //Remove skin
                jason.removeSkin();

                //Stop Potions
                jason.removePotionEffects();
                jason.restoreOriginalSpeeds();

                //Remove his ability display
                jason.getAbilityDisplayManager().hideAbilities();
            }

            //Actions done only if they're online
            if (offlinePlayer.isOnline())
            {
                Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));

                //Hide game-wide scoreboard
                arena.getGameManager().getGameScoreboardManager().hideFromPlayer(player);

                //Teleport them to the return point
                teleportPlayerToReturnPoint(player);

                //Restore health and hunger
                player.setHealth(20);
                player.setFoodLevel(20);

                //Clear inventory
                player.getInventory().clear();

                //Remove all potion effects
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }

                //Stop all music
                PlayerSoundAPI.getPlayerSoundManager(player).stopAllSounds();
            }
        }
    }


    /* Teleports */

    /**
     * Teleports the player to the arena's return point
     * @param player
     */
    private void teleportPlayerToReturnPoint(Player player)
    {
        player.teleport(arena.getReturnLocation());
    }

    /**
     * Teleports the player to the arena's waiting point
     * @param player
     */
    private void teleportPlayerToWaitingPoint(Player player)
    {
        player.teleport(arena.getWaitingLocation());
    }


    /* JASONS ABILITIES */

    /**
     * Performs jason sense ability actions to applicable counselors
     * @param value
     */
    public void jasonSensing(boolean value)
    {
        Iterator it = getCounselors().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Counselor counselor = (Counselor) entry.getValue();
            counselor.setSenseMode(value);
        }
    }

    /**
     * Selects a random spectating counselor to be Tommy Jarvis
     */
    public void spawnTommyJarvis()
    {
        //set players stamina higher and max fear higher
        if (arena.getGameManager().hasTommyBeenCalled() && !arena.getGameManager().hasTommyBeenSpawned())
        {
            if (getNumPlayersDead() > 0)
            {
                //Select a random dead player
                String[] pl = getDeadPlayers().toArray(new String[getDeadPlayers().size()]);

                //Randomize starting points
                Random rnd = ThreadLocalRandom.current();
                for (int i = pl.length - 1; i > 0; i--) {
                    int index = rnd.nextInt(i + 1);

                    // Simple swap
                    String a = pl[index];
                    pl[index] = pl[i];
                    pl[i] = a;
                }

                //Randomize starting points
                Location[] counselorLocations = arena.getLocationManager().getAvailableStartingPoints().toArray(new Location[arena.getLocationManager().getAvailableStartingPoints().size()]);
                Random rnd2 = ThreadLocalRandom.current();
                for (int i = counselorLocations.length - 1; i > 0; i--)
                {
                    int index = rnd.nextInt(i + 1);

                    // Simple swap
                    Location a = counselorLocations[index];
                    counselorLocations[index] = counselorLocations[i];
                    counselorLocations[i] = a;
                }

                //Do the things
                Counselor counselor = getCounselor(pl[0]);

                if (isSpectator(counselor.getPlayer().getUniqueId().toString()))
                {
                    //Leave spectating mode
                    getSpectator(getCounselor(pl[0]).getPlayer().getUniqueId().toString()).leaveSpectatingMode();
                    removeSpectator(getCounselor(pl[0]).getPlayer().getUniqueId().toString());
                }

                counselor.prepareForGameplay();
                counselor.getPlayer().teleport(counselorLocations[0]);
                counselor.setTommyJarvis();
                arena.getGameManager().setTommySpawned();

                //Move from dead->alive hashmap
                alivePlayers.put(counselor.getPlayer().getUniqueId().toString(), getCounselor(pl[0]).getPlayer());
                deadPlayers.remove(counselor.getPlayer().getUniqueId().toString());

                //Message everyone
                sendMessageToAllPlayers(ChatColor.AQUA + counselor.getPlayer().getName() + ChatColor.WHITE + " " + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.chat.TommyRisen", "has risen from the dead as Tommy Jarvis."));
            }
        }
    }


    /* SPECIALS */

    public void fireFirework(Player player, Color color)
    {
        Firework f = player.getWorld().spawn(player.getWorld().getHighestBlockAt(player.getLocation()).getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.BALL)
                .withColor(color)
                .build());
        fm.setPower(1);
        f.setFireworkMeta(fm);
    }

    /* Messaging */

    /**
     * Sends in game message to all players
     * @param message
     */
    public void sendMessageToAllPlayers(String message)
    {
        Iterator it = getPlayers().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            Player player = (Player) entry.getValue();
            player.sendMessage(FridayThe13th.pluginPrefix + message);
        }
    }

    /**
     * Performs actions when counselors win
     */
    private void counselorsWin()
    {
        sendMessageToAllPlayers(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.counselorsWin", "Counselors win! Jason was slain."));
    }

    /**
     * Updates waiting scoreboards
     */
    public void updateWaitingStatsScoreboards() {
        if (arena.getGameManager().isGameEmpty() || arena.getGameManager().isGameWaiting()) {
            for (Player player : players.values()) {
                FridayThe13th.playerController.getPlayer(player).getWaitingPlayerStatsDisplayManager().updateStatsScoreboard();
            }
        }
    }
}
