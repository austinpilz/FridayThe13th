package com.AustinPilz.FridayThe13th.Manager.Game;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
import com.AustinPilz.FridayThe13th.Components.Characters.Jason;
import com.AustinPilz.FridayThe13th.Components.Characters.Spectator;
import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.Components.Enum.XPAward;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameFullException;
import com.AustinPilz.FridayThe13th.Exceptions.Game.GameInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerAlreadyPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerManager {
    private Arena arena;

    //Game Players
    private HashSet<F13Player> players;
    private HashMap<F13Player, Counselor> counselors;

    private Jason jason;

    //Game Stat
    private HashSet<F13Player> alivePlayers;
    private HashSet<F13Player> deadPlayers;
    private HashSet<F13Player> escapedPlayers;
    private HashMap<F13Player, Spectator> spectators;

    /**
     * @param arena Game
     */
    PlayerManager(Arena arena) {
        this.arena = arena;
        this.players = new HashSet<>();
        this.counselors = new HashMap<>();
        this.alivePlayers = new HashSet<>();
        this.deadPlayers = new HashSet<>();
        this.escapedPlayers = new HashSet<>();
        this.spectators = new HashMap<>();
    }

    /**
     * Resets lists of alive and dead players
     */
    public void resetPlayerStorage() {
        players.clear();
        counselors.clear();
        alivePlayers.clear();
        deadPlayers.clear();
        spectators.clear();
        escapedPlayers.clear();
    }

    /**
     * @return Current players
     */
    public HashSet<F13Player> getPlayers() {
        return players;
    }

    /**
     * Adds player to the current game's player list
     * @param player F13Player
     */
    private void addPlayer(F13Player player) {
        players.add(player);

    }

    /**
     * Removes player from current game's player list
     * @param player F13Player
     */
    private void removePlayer(F13Player player) {
        players.remove(player);
        FridayThe13th.arenaController.removePlayer(player);
    }

    /**
     * @return Number of players in the current game
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * @return Alive players
     */
    public HashSet<F13Player> getAlivePlayers() {
        return alivePlayers;
    }

    /**
     * Adds player to the alive player list
     * @param player F13Player
     */
    private void addAlivePlayer(F13Player player) {
        alivePlayers.add(player);
    }

    /**
     * Removes player from the alive player list
     *
     * @param player F13Player
     */
    private void removeAlivePlayer(F13Player player) {
        alivePlayers.remove(player);
    }

    /**
     * @return Number of players alive
     */
    public int getNumberOfPlayersAlive() {
        return alivePlayers.size();
    }

    /**
     * @param player F13Player
     * @return If the player is alive
     */
    public boolean isAlive(F13Player player) {
        return alivePlayers.contains(player);
    }

    /**
     * @return Dead players
     */
    private HashSet<F13Player> getDeadPlayers() {
        return deadPlayers;
    }

    /**
     * Adds player to the dead player list
     * @param player F13Player
     */
    public void addDeadPlayer(F13Player player) {
        deadPlayers.add(player);
    }

    /**
     * Removes player from the dead player list
     * @param player F13Player
     */
    public void removeDeadPlayer(F13Player player) {
        deadPlayers.remove(player); }

    /**
     * @return Number of players dead
     */
    public int getNumberOfPlayersDead() {
        return deadPlayers.size();
    }

    /**
     * @return Current game's counselor HashMap
     */
    public HashMap<F13Player, Counselor> getCounselors() {
        return counselors;
    }

    /**
     * Adds new counselor
     *
     * @param counselor Counselor object
     */
    private void addCounselor(Counselor counselor) {
        counselors.put(counselor.getF13Player(), counselor);
    }

    /**
     * Assigns the provided player as a counselor
     *
     * @param player F13Player
     */
    private void assignCounselor(F13Player player) {
        addCounselor(new Counselor(player, arena));
    }

    /**
     * Removes the counselor
     *
     * @param player F13Player
     */
    private void removeCounselor(F13Player player) {
        counselors.remove(player);
    }

    /**
     * @param player F13Player
     * @return Counselor
     */
    public Counselor getCounselor(F13Player player) {
        return counselors.get(player);
    }

    /**
     * @return Number of counselors
     */
    public int getNumberOfCounselors() {
        return counselors.size();
    }

    /**
     * @param player F13Player
     * @return If the player is a counselor
     */
    public boolean isCounselor(F13Player player) {
        return counselors.containsKey(player);
    }

    /**
     * @return Jason
     */
    public Jason getJason() {
        return jason;
    }

    /**
     * @param player F13Player
     * @return If the player is Jason
     */
    public boolean isJason(F13Player player) {
        return jason != null && jason.getF13Player().equals(player);
    }

    /**
     * Assigns the provided player as Jason
     *
     * @param player F13Player
     */
    private void assignJason(F13Player player) {
        this.jason = new Jason(player, arena);
        removeCounselor(player);
    }

    /**
     * @return Number of counselors that escaped
     */
    private int getNumberOfPlayersEscaped() {
        return escapedPlayers.size();
    }

    /**
     * Adds player to escaped player list
     *
     * @param player F13Player
     */
    public void addEscapedPlayer(F13Player player) {
        escapedPlayers.add(player);
    }

    /**
     * @param player F13Player
     * @return If the player escaped
     */
    public boolean didPlayerEscape(F13Player player) {
        return escapedPlayers.contains(player);
    }

    /**
     * @return Spectators
     */
    public HashMap<F13Player, Spectator> getSpectators() {
        return spectators;
    }

    /**
     * Adds a spectator to the HashMap
     *
     * @param spectator Spectator
     */
    private void addSpectator(Spectator spectator) {
        spectators.put(spectator.getF13Player(), spectator);
    }

    /**
     * Removes a spectator from the HashMap
     *
     * @param player F13Player
     */
    private void removeSpectator(F13Player player) {
        spectators.remove(player);
    }

    /**
     * @param player F13Player
     * @return Spectator
     */
    private Spectator getSpectator(F13Player player) {
        return spectators.get(player);
    }

    /**
     * @return Number of spectators
     */
    public int getNumberOfSpectators() {
        return spectators.size();
    }

    /**
     * @param player F13Player
     * @return If the player is a spectator
     */
    public boolean isSpectator(F13Player player) {
        return spectators.containsKey(player);
    }

    /**
     * @param player F13Player
     * @return If the player if just a spectator, not an active or previous player
     */
    public boolean isJustSpectator(F13Player player) {
        return (isSpectator(player) && !isCounselor(player) && !isJason(player));
    }

    /**
     * Adds the player as a spectator and performs all actions to put them into the game
     * @param player F13Player
     */
    public void becomeSpectator(F13Player player) {
        addSpectator(new Spectator(player, arena));
        getSpectator(player).enterSpectatingMode();

        try {
            FridayThe13th.arenaController.addPlayer(player, arena);
            addPlayer(player);
        } catch (PlayerAlreadyPlayingException exception) {
            //Don't need to do anything
        }
    }

    /**
     * Removes the player as a spectator and performs all actions to remove them from the game
     *
     * @param player F13Player
     */
    public void leaveSpectator(F13Player player) {
        //Need to remove from player lists if they're just a spectator
        if (isJustSpectator(player)) {
            FridayThe13th.arenaController.removePlayer(player);
            removePlayer(player);
        }

        removeSpectator(player);
    }

    /**
     * @return If there is enough room for the player to join
     */
    public synchronized boolean isRoomForPlayerToJoin() {
        //Determine if we have enough spawn points for the game's 8 counselors
        if (arena.getLocationManager().getNumberStartingPoints() >= 8) {
            //We don't have to worry about spawn points - Games capped at 8 counselors + Jason.
            return getNumberOfPlayers() < 9;
        } else {
            //They're are less than 8 spawn points for counselors
            return arena.getLocationManager().getNumberStartingPoints() - getNumberOfPlayers() + 1 > 0;
        }
    }

    /**
     * Adds player to the game, if room is available
     *
     * @param player F13Player
     * @throws GameFullException The game is full and cannot accept any more players
     */
    public synchronized void playerJoinGame(F13Player player) throws GameFullException, GameInProgressException {
        if (arena.getGameManager().isGameEmpty() || arena.getGameManager().isGameWaiting()) {
            //Determine if there's room for this user
            if (isRoomForPlayerToJoin()) {
                try {
                    //Add to lists
                    FridayThe13th.arenaController.addPlayer(player, arena);
                    addPlayer(player);

                    //Prepare them for the game
                    assignCounselor(player);
                    getCounselor(player).enterWaitingRoom();

                    //Announce arrival
                    int playerNumber = players.size();
                    sendMessageToAllPlayers(ChatColor.GRAY + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerJoinBroadcast", "{0} has joined the game.", player.getBukkitPlayer().getName()) + " (" + playerNumber + "/" + arena.getMaxNumberOfPlayers() + ")");

                    if (players.size() == 1) {
                        arena.getSignManager().updateJoinSigns(); //If it's just them, update signs
                    }

                } catch (PlayerAlreadyPlayingException exception) {
                    //They're already in the controller global player list
                    player.getBukkitPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerJoinFailAR", "Failed to add you to game because you're already registered as playing a game."));
                }
            } else {
                throw new GameFullException();
            }
        } else {
            throw new GameInProgressException();
        }
    }

    /**
     * Performs actions for players when the game begins
     */
    protected void beginGame() {
        assignGameRoles();
        assignSpawnLocations();

        //Prepare everyone for gameplay
        getJason().prepareforGameplay();

        for (Counselor counselor : counselors.values()) {
            counselor.prepareForGameplay();
            addAlivePlayer(counselor.getF13Player());
        }

        //Tell everyone who Jason is
        sendMessageToAllPlayers(ChatColor.AQUA + jason.getF13Player().getBukkitPlayer().getName() + ChatColor.WHITE + " is Jason.");

        //Play game start music
        SoundManager.playSoundForAllPlayers(F13SoundEffect.Music_GameStart, arena, true, true, 0);
    }

    /**
     * Performs actions for players when the game ends
     */
    protected void endGame() {
        sendEndOfGameStatisticMessage();

        //Jason - Award XP
        if (getNumberOfPlayersAlive() == 0) {
            getJason().getXpManager().registerXPAward(XPAward.Jason_NoEscapes);
        }

        getJason().getXpManager().awardXPToPlayer(); //TODO - We have to make sure Jason doesn't get XP if he quits
        getJason().leaveGame();
        removePlayer(getJason().getF13Player());

        //Counselors - Award XP & Leave Game
        for (Counselor counselor : counselors.values()) {
            counselor.getXpManager().awardXPToPlayer();
            counselor.leaveGame();
            removePlayer(counselor.getF13Player());
        }

        for (Spectator spectator : spectators.values()) {
            spectator.leaveGame();
            removePlayer(spectator.getF13Player());
        }

        resetPlayerStorage();
    }

    /**
     * Assigns all players a role (counselor or jason)
     */
    private void assignGameRoles() {
        //Populate role preference data
        List<F13Player> preferJason = new ArrayList<>();
        List<F13Player> preferCounselor = new ArrayList<>();
        List<F13Player> noPreference = new ArrayList<>();

        for (F13Player player : players) {
            if (player.isSpawnPreferenceJason()) {
                preferJason.add(player);
            } else if (player.isSpawnPreferenceCounselor()) {
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
    }

    /**
     * Assigns and teleports players and jason to their spawn locations
     */
    private void assignSpawnLocations() {
        //Teleport counselors to starting points
        Location[] counselorLocations = arena.getLocationManager().getRandomSpawnLocations();

        int i = 0;
        for (Counselor counselor : counselors.values()) {
            counselor.teleport(counselorLocations[i++]);
        }
    }

    /**
     * Picks a random player from supplied player list
     *
     * @param players F13 Player list
     * @return Random F13Player
     */
    private F13Player pickRandomPlayer(List<F13Player> players) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, players.size() - 1);
        return players.get(randomNum);
    }

    /**
     * Sends in game message to all players
     *
     * @param message Message to be sent in chat
     */
    public void sendMessageToAllPlayers(String message) {
        for (F13Player player : players) {
            player.getBukkitPlayer().sendMessage(FridayThe13th.pluginPrefix + message);
        }
    }

    /**
     * Sends in game message to all players
     * @param message Message to be sent in the Action Bar
     */
    public void sendActionBarMessageToAllPlayers(String message, int ticksDuration) {
        for (F13Player player : players) {
            ActionBarAPI.sendActionBar(player.getBukkitPlayer(), message, ticksDuration);
        }
    }

    /**
     * Resets the action bars of all players to nothing
     */
    public void resetPlayerActionBars() {
        for (F13Player player : players) {
            ActionBarAPI.sendActionBar(player.getBukkitPlayer(), "");
        }
    }

    /**
     * Sends end of game message to all players
     */
    private void sendEndOfGameStatisticMessage()
    {
        sendMessageToAllPlayers(ChatColor.RED + "" + ChatColor.BOLD + "Game over!");
        sendMessageToAllPlayers(getNumberOfPlayersDead() + "/" + getNumberOfCounselors() + " counselors killed.");

        if (getNumberOfPlayersEscaped() > 0) {
            sendMessageToAllPlayers(getNumberOfPlayersEscaped() + "/" + getNumberOfCounselors() + " counselors escaped.");
        }
    }

    /**
     * Performs actions when a player quits the game via command
     *
     * @param player F13Player
     */
    public void onplayerQuit(F13Player player) {
        //Message everyone in game
        sendMessageToAllPlayers(ChatColor.GRAY + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerQuitBroadcast", "{0} has left the game.", player.getBukkitPlayer().getName()));

        if (arena.getGameManager().isGameInProgress()) {
            if (isJason(player)) {
                //Jason quit off, so end the game
                sendMessageToAllPlayers(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerQuitJasonBroadcast", "GAME OVER! {0} (Jason) has left the game.", player.getBukkitPlayer().getName()));
                arena.getGameManager().endGame();
            } else if (isCounselor(player)) {
                if (isAlive(player) && getNumberOfPlayersAlive() <= 1) {
                    //They were the last one
                    arena.getGameManager().endGame();
                } else {
                    if (!isAlive(player) || didPlayerEscape(player)) {
                        arena.getGameManager().getPlayerManager().getCounselor(player).getXpManager().awardXPToPlayer();
                    }

                    arena.getGameManager().getPlayerManager().getCounselor(player).leaveGame();
                }
            } else if (isSpectator(player)) {
                arena.getGameManager().getPlayerManager().getSpectator(player).leaveGame();
            }
        } else {
            //Before the game, everyone is considered a counselor
            getCounselor(player).leaveGame();
        }

        removePlayerFromDataStructures(player);
    }

    /**
     * Performs actions when a player logs off of server
     * @param player F13Player
     */
    public void onPlayerLogout(F13Player player) {
        //Hurry and see if we can teleport them out and clear inventory
        player.getBukkitPlayer().teleport(arena.getReturnLocation());
        player.getBukkitPlayer().getInventory().clear();

        //Message everyone in game
        sendMessageToAllPlayers(ChatColor.GRAY + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerLogoutBroadcast", "{0} has logged out and left the game.", Bukkit.getOfflinePlayer(UUID.fromString(player.getPlayerUUID())).getName()));

        if (!isJustSpectator(player)) {
            if (arena.getGameManager().isGameInProgress()) {
                if (isJason(player)) {
                    //Jason logged off, so end the game
                    sendMessageToAllPlayers(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerLogoutJasonBroadcast", "GAME OVER! Jason left the game."));
                    arena.getGameManager().endGame();
                } else if (isCounselor(player)) {
                    //They're a counselor
                    if (isAlive(player) && getNumberOfPlayersAlive() <= 1) {
                        //They were the last one
                        arena.getGameManager().endGame();
                    }

                    getCounselor(player).leaveGame();
                }
            } else {
                //Before the game, everyone is considered a counselor
                getCounselor(player).leaveGame();
            }
        } else {
            //Just a spectator who logged out
            getSpectator(player).leaveGame();
        }

        removePlayerFromDataStructures(player);
    }

    /**
     * Performs actions when a player escapes
     *
     * @param player F13Player
     */
    public void onPlayerEscape(F13Player player) {
        if (arena.getGameManager().isGameInProgress()) {
            //Check to see if they're jason, which would end the game
            if (isCounselor(player)) {
                removeAlivePlayer(player);
                addEscapedPlayer(player);

                //Award them XP
                getCounselor(player).getXpManager().registerXPAward(XPAward.Counselor_Escaped);

                //Let everyone know
                sendMessageToAllPlayers(ChatColor.GRAY + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerEscapeBroadcast", "{0} escaped.", player.getBukkitPlayer().getName()));

                if (getNumberOfPlayersAlive() >= 1) {
                    //They're are others still alive, enter spectating mode
                    getCounselor(player).transitionToSpectatingMode();
                    becomeSpectator(player);
                } else {
                    //They were the last to die, so end the game
                    arena.getGameManager().endGame();
                }
            }
        }
    }

    /**
     * Performs actions when a player dies in game
     *
     * @param player F13Player
     */
    public void onPlayerDeath(F13Player player) {
        if (arena.getGameManager().isGameInProgress()) {
            //Transition from alive to dead hash set
            removeAlivePlayer(player);
            addDeadPlayer(player);

            //Let everyone know
            sendMessageToAllPlayers(ChatColor.GRAY + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.playerKilledBroadcast", "{0} was killed.", player.getBukkitPlayer().getName()));

            //Check to see if they're jason, which would end the game
            if (isJason(player)) {
                //Counselors win
                counselorsWin();
                arena.getGameManager().endGame(); //Game over kiddos
            } else {
                //Firework
                arena.getGameManager().getPlayerManager().fireFirework(player, Color.RED);

                //They're a normal player, see if there are still others alive
                if (getNumberOfPlayersAlive() >= 1) //since jason is still presumably alive
                {
                    //Spawn their corpse
                    arena.getObjectManager().spawnCorpse(player);

                    //Enter spectating mode
                    getCounselor(player).transitionToSpectatingMode();
                    becomeSpectator(player);
                } else {
                    //They were the last to die, so end the game
                    arena.getGameManager().endGame();
                }
            }
        }
    }

    /**
     * Removes player from all game data structures
     *
     * @param player F13Player
     */
    private void removePlayerFromDataStructures(F13Player player) {
        removePlayer(player);
        removeAlivePlayer(player);
        removeDeadPlayer(player);
        removeCounselor(player);
        removeSpectator(player);
    }

    /**
     * Performs actions when counselors win
     */
    private void counselorsWin() {
        sendMessageToAllPlayers(FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.counselorsWin", "Counselors win! Jason was slain."));
    }

    /**
     * Fires a firework
     * @param player F13Player whos location to fire the firework at
     * @param color Firework color
     */
    public void fireFirework(F13Player player, Color color) {
        Firework f = player.getBukkitPlayer().getWorld().spawn(player.getBukkitPlayer().getLocation().getWorld().getHighestBlockAt(player.getBukkitPlayer().getLocation()).getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(color)
                .build());
        fm.setPower(1);
        f.setFireworkMeta(fm);
    }

    /**
     * Updates waiting scoreboards
     */
    public void updateWaitingStatsScoreboards() {
        if (arena.getGameManager().isGameEmpty() || arena.getGameManager().isGameWaiting()) {
            for (F13Player player : players) {
                player.getWaitingPlayerStatsDisplayManager().updateStatsScoreboard();
            }
        }
    }

    /**
     * Displays the waiting countdown for all players
     */
    public void displayWaitingCountdown() {
        for (F13Player player : players) {
            arena.getGameManager().getWaitingCountdownDisplayManager().displayForPlayer(player.getBukkitPlayer());
        }
    }

    /**
     * Hides the waiting countdown from all players
     */
    public void hideWaitingCountdown() {
        arena.getGameManager().getWaitingCountdownDisplayManager().hideFromAllPlayers();
    }

    /**
     * Selects a random spectating counselor to be Tommy Jarvis
     */
    public void spawnTommyJarvis() {
        //set players stamina higher and max fear higher
        if (arena.getGameManager().hasTommyBeenCalled() && !arena.getGameManager().hasTommyBeenSpawned() && (escapedPlayers.size() + deadPlayers.size() > 0)) {
            HashSet<F13Player> playersToSelectFrom = new HashSet<>();
            playersToSelectFrom.addAll(escapedPlayers);
            playersToSelectFrom.addAll(deadPlayers);

            if (players.size() > 0) {
                //Select a random player
                F13Player[] tommyOptions = playersToSelectFrom.toArray(new F13Player[playersToSelectFrom.size()]);

                Random rnd = ThreadLocalRandom.current();
                for (int i = tommyOptions.length - 1; i > 0; i--) {
                    int index = rnd.nextInt(i + 1);

                    // Simple swap
                    F13Player a = tommyOptions[index];
                    tommyOptions[index] = tommyOptions[i];
                    tommyOptions[i] = a;
                }

                Location tommySpawnLocation = arena.getLocationManager().getRandomSpawnLocations()[0];
                Counselor counselorToBeTommy = getCounselor(tommyOptions[0]);

                removeSpectator(counselorToBeTommy.getF13Player());

                counselorToBeTommy.prepareForGameplay();
                counselorToBeTommy.setTommyJarvis();
                counselorToBeTommy.teleport(tommySpawnLocation);
                arena.getGameManager().setTommySpawned();

                //Move from dead->alive hashmap
                addAlivePlayer(counselorToBeTommy.getF13Player());
                removeDeadPlayer(counselorToBeTommy.getF13Player());

                //Message everyone
                sendMessageToAllPlayers(ChatColor.AQUA + counselorToBeTommy.getF13Player().getBukkitPlayer().getName() + ChatColor.WHITE + " " + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.chat.TommyRisen", "has risen from the dead as Tommy Jarvis."));
            }
        }
    }

    /**
     * Performs jason sense ability actions to applicable counselors
     * @param value If Jason is currently sensing
     */
    public void jasonSensing(boolean value) {
        Iterator it = getCounselors().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Counselor counselor = (Counselor) entry.getValue();
            counselor.setSenseMode(value);
        }
    }


}
