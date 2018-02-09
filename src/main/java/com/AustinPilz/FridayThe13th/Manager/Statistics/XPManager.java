package com.AustinPilz.FridayThe13th.Manager.Statistics;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Enum.XPAward;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class XPManager {

    private Arena arena;
    private F13Player player;
    private ArrayList<XPAward> xpAwards;

    public XPManager(F13Player player, Arena arena) {
        this.arena = arena;
        this.player = player;
        this.xpAwards = new ArrayList<>();
    }

    /**
     * Adds XP Award
     *
     * @param toAward XP Award
     */
    public void registerXPAward(XPAward toAward) {
        Map<XPAward, Long> counts = xpAwards.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        if ((!counts.containsKey(toAward) || (counts.containsKey(toAward) && counts.get(toAward) < toAward.getMaxUses())) && doesAwardXPMatchPlayersType(toAward)) {
            xpAwards.add(toAward);

            //Send the message
            if (!toAward.getMessageOnAward().isEmpty() && player.isOnline()) {
                ActionBarAPI.sendActionBar(player.getBukkitPlayer(), toAward.getMessageOnAward(), 60);
            }
        }
    }

    /**
     * @param toAward
     * @return If the award's character type matches the role of the player
     */
    private boolean doesAwardXPMatchPlayersType(XPAward toAward) {
        return ((arena.getGameManager().getPlayerManager().isCounselor(player) && toAward.isCounselorXPAward()) || (arena.getGameManager().getPlayerManager().isJason(player) && toAward.isJasonXPAward()));
    }

    /**
     * Calculates the total XP for the game for the player
     *
     * @return Total XP for player's current game
     */
    private int calculateTotalXP() {
        Map<XPAward, Long> counts = xpAwards.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        int xp = 0;

        Iterator it = counts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            XPAward award = (XPAward) entry.getKey();
            Long count = (Long) entry.getValue();

            xp += award.getXPAward() * count.intValue(); //Max # of uses checking is done before they're added to the ArrayList, so we can just use the count value here
        }

        //Double XP when it's actually a Friday the 13th
        if (FridayThe13th.isItFridayThe13th() && xp > 0) {
            xp *= 2;
        }

        return Math.max(0, xp);
    }

    /**
     * Awards XP from the game to the player
     */
    public void awardXPToPlayer() {
        if (arena.getGameManager().getPlayerManager().isCounselor(player)) {
            registerXPAward(XPAward.Counselor_MatchCompleted);
        }

        int xpToAward = calculateTotalXP();
        player.addXP(xpToAward);

        //Send confirmation message to the player
        if (player.isOnline()) {
            player.getBukkitPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(player.getBukkitPlayer(), "message.gameEarnedXP", "You earned {0} xp from this round and now have a total of {1} xp.", ChatColor.GREEN + "" + xpToAward + ChatColor.WHITE, ChatColor.GREEN + "" + ChatColor.BOLD + "" + player.getXP() + ChatColor.RESET));
        }

        //Award successful match completion CP as well
        awardCPToPlayer();
    }

    /**
     * Awards XP from the game to the player
     */
    public void awardCPToPlayer() {
        player.addCP(500);

        if (player.isOnline()) {
            player.getBukkitPlayer().sendMessage(FridayThe13th.pluginPrefix + FridayThe13th.language.get(player.getBukkitPlayer(), "message.gameEarnedCP", "You earned {0} cp from this round and now have a total of {1} cp.", ChatColor.GREEN + "" + "500" + ChatColor.WHITE, ChatColor.GREEN + "" + ChatColor.BOLD + "" + player.getCP() + ChatColor.RESET));
        }
    }
}
