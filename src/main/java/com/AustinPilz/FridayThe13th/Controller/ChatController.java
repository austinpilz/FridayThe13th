package com.AustinPilz.FridayThe13th.Controller;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
import com.AustinPilz.FridayThe13th.Components.Characters.Spectator;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatController
{

    /**
     * Routes chat from a player currently playing F13
     * @param sender Sender of the message
     * @param message The message
     * @param arena Arena of the sending player
     */
    public void routeInternalMessage(Player sender, String message, Arena arena)
    {
        Set<Player> recipients = new HashSet<>();
        String prefix = "";
        boolean areAnyPlayersRecipients = false;

        if (arena.getGameManager().isGameInProgress())
        {
            if (!arena.getGameManager().getPlayerManager().isSpectator(sender))
            {
                //They're not spectating
                if (sender.getInventory().contains(Material.NETHER_STAR))
                {
                    //Sender has a radio - so they can communicate across the map with anyone else that has a radio.
                    for (Counselor counselor : arena.getGameManager().getPlayerManager().getCounselors().values())
                    {
                        if (counselor.getPlayer().getInventory().contains(Material.NETHER_STAR))
                        {
                            //This counselor has a radio, add them to recipients
                            if (!counselor.getPlayer().equals(sender)) //Don't add themselves
                            {
                                recipients.add(counselor.getPlayer());
                            }
                        }
                    }
                }

                //Also need to add all of the players that are physically close to the sender
                for (Player player : arena.getGameManager().getPlayerManager().getAlivePlayers().values())
                {
                    if (sender.getLocation().distance(player.getLocation()) <= 20)
                    {
                        //They're close enough for the other play to hear them
                        recipients.add(player);
                    }
                }
            }

            //Determine if any of the recipients are players before we add all of the spectators
            if (recipients.size() > 1) //You are always included, so more than just you
            {
                areAnyPlayersRecipients = true;
            }

            //Always add all of the spectators - but don't consider them being a player hearing the message
            for (Spectator spectator : arena.getGameManager().getPlayerManager().getSpectators().values())
            {
                recipients.add(spectator.getPlayer());
            }
        }
        else
        {
            //Game is empty/waiting - everyone else in the waiting room is a recipient
            recipients.addAll(arena.getGameManager().getPlayerManager().getPlayers().values());
        }

        //Special character if there are no players to hear their message
        String wasHeard = "";

        //Determine their prefix - no prefix if it's in the waiting room
        if (arena.getGameManager().isGameInProgress())
        {
            if (arena.getGameManager().getPlayerManager().isSpectator(sender))
            {
                prefix = ChatColor.GREEN + "[" + FridayThe13th.language.get(sender, "game.chatPrefix.Spectator", "Spectator") + "]";
            }
            else if (arena.getGameManager().getPlayerManager().isCounselor(sender))
            {
                prefix = ChatColor.GOLD + "[" + FridayThe13th.language.get(sender, "game.chatPrefix.Counselor", "Counselor") + "]";
            }
            else if (arena.getGameManager().getPlayerManager().isJason(sender))
            {
                prefix = ChatColor.RED + "[" + FridayThe13th.language.get(sender, "game.chatPrefix.Jason", "Jason") + "]";
            }

            if (!areAnyPlayersRecipients && arena.getGameManager().getPlayerManager().getNumSpectators() < 2)
            {
                wasHeard = ChatColor.WHITE + "[" + ChatColor.RED + "!" + ChatColor.WHITE + "] ";
            }
        }
        else
        {
            //Waiting room
            prefix = ChatColor.GRAY + "[" + FridayThe13th.language.get(sender, "game.chatPrefix.Waiting", "Waiting") + "]" + ChatColor.WHITE;
        }

        //Final message
        String finalMessage = wasHeard + FridayThe13th.playerController.getPlayer(sender).getLevel().getChatPrefix() + prefix + " " + sender.getName() + ChatColor.WHITE + ": " + message;

        //Send it
        for (Player player : recipients)
        {
            player.sendMessage(finalMessage);
        }
    }

    /**
     * Returns a list of all F13 players
     * @return List with all F13 players
     */
    public List<Player> getF13Players()
    {
        List<String> rawUUIDs = new ArrayList<>(FridayThe13th.arenaController.getPlayers().keySet());
        List<Player> players = new ArrayList<>();

        for (String uuid : rawUUIDs)
        {
            players.add(Bukkit.getPlayer(UUID.fromString(uuid)));
        }

        return players;
    }
}
