package com.AustinPilz.FridayThe13th.Components.Arena;

import com.AustinPilz.FridayThe13th.Components.Enum.PhoneType;
import com.AustinPilz.FridayThe13th.Components.Enum.XPAward;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Utilities.InventoryActions;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.*;
import org.bukkit.block.BlockFace;

/**
 * Created by austinpilz on 7/14/17.
 */
public class ArenaPhone
{
    private Arena arena;
    private Location location;
    private Hologram hologram;
    private boolean isVisible;
    private boolean isBroken;
    private boolean isFusePresent;

    private double callAttempts;
    private int callAttemptsRequired;
    private double repairAttempts;
    private int repairAttemptsRequired;

    //Per game settings
    private PhoneType phoneType;

    public ArenaPhone(Arena arena, Location location)
    {
        this.arena = arena;
        this.location = location;
        setPhoneType(PhoneType.None);
        isVisible = false;
        isBroken = false;
        isFusePresent = false;

        //Values
        callAttempts = 0;
        callAttemptsRequired = 75;
        repairAttempts = 0;
        repairAttemptsRequired = 75;
    }

    /**
     * Returns the phone's arena
     * @return Arena
     */
    public Arena getArena()
    {
        return arena;
    }

    /**
     * Returns the phone's location
     * @return Location of phone
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Sets the phone type
     *
     * @param type Phone Type
     */
    public void setPhoneType(PhoneType type) {
        this.phoneType = type;
    }

    /**
     * @return If the phone is the Tommy Jarvis phone
     */
    private boolean isTommyJarvisPhone() {
        return phoneType.equals(PhoneType.TommyJarvis);
    }

    /**
     * @return If the phone is the police phone
     */
    private boolean isPolicePhone() {
        return phoneType.equals(PhoneType.Police);
    }

    /**
     * @return If the phone is broken
     */
    public boolean isBroken() {
        return isBroken;
    }

    /**
     * @return If the fuse is present in the phone
     */
    public boolean isFusePresent() {
        return isFusePresent;
    }

    /**
     * @return If this specific phone can be broken
     */
    private boolean canBeBroken() {
        return isPolicePhone();
    }

    /**
     * Breaks the phone, if applicable
     */
    public void breakPhone() {
        if (canBeBroken() && !isBroken && !hasCallBeenCompleted()) {
            isBroken = true;
            repairAttempts = 0;
            callAttempts = 0;
            updateHologram();
        }
    }

    /**
     * @return If the phone call has been successfully placed
     */
    private boolean hasCallBeenCompleted() {
        return callAttempts >= callAttemptsRequired;
    }

    /**
     * @return If the phone repair has been successful
     */
    private boolean hasRepairBeenCompleted() {
        return repairAttempts >= repairAttemptsRequired;
    }

    /**
     * Shows phone
     */
    public void showPhone() {
        //Reset values
        callAttempts = 0;
        repairAttempts = 0;

        //Set that this is the visible phone
        isVisible = true;

        isBroken = isPolicePhone(); //Police phone always starts broken

        //Hide from Jason
        createHologram();

        if (isTommyJarvisPhone()) {
            hologram.getVisibilityManager().hideTo(arena.getGameManager().getPlayerManager().getJason().getPlayer());
        }
    }

    /**
     * Hides phone
     */
    public void hidePhone() {
        if (hologram != null) {
            hologram.clearLines();
            hologram.delete();
        }

        isVisible = false;
    }

    /**
     * Attempt to repair the switch
     */
    public void callAttempt(F13Player player) {
        if (isVisible) {
            if (isTommyJarvisPhone()) {
                if (!arena.getGameManager().hasTommyBeenCalled()) {
                    callAttempts += player.getCounselorProfile().getIntelligence().getRegenerationRate();

                    if (hasCallBeenCompleted()) {
                        //The Tommy Jarvis phone call has been placed
                        arena.getGameManager().getPlayerManager().fireFirework(player, Color.GREEN);
                        arena.getGameManager().getPlayerManager().sendMessageToAllPlayers(ChatColor.AQUA + player.getBukkitPlayer().getName() + ChatColor.WHITE + " has called Tommy Jarvis.");
                        arena.getGameManager().setTommyCalled();
                        callAttempts++;

                        //Register Tommy called for XP
                        arena.getGameManager().getPlayerManager().getCounselor(player).getXpManager().registerXPAward(XPAward.Counselor_TommyCalled);
                    }
                }
            } else if (isPolicePhone()) {
                if (isBroken()) {
                    //Repair Attempt
                    repairAttempts += player.getCounselorProfile().getIntelligence().getRegenerationRate();

                    if (hasRepairBeenCompleted()) {
                        isBroken = false;

                        if (!isFusePresent) {
                            isFusePresent = true;
                            InventoryActions.remove(player.getBukkitPlayer().getInventory(), Material.END_ROD, 1, (short) -1);
                        } else {
                            InventoryActions.remove(player.getBukkitPlayer().getInventory(), Material.REDSTONE, 1, (short) -1);
                        }

                        arena.getGameManager().getPlayerManager().getCounselor(player).getXpManager().registerXPAward(XPAward.Counselor_PhoneRepaired);
                    }
                } else {
                    //Regular call
                    callAttempts += player.getCounselorProfile().getIntelligence().getRegenerationRate();

                    if (hasCallBeenCompleted()) {
                        //The Tommy Jarvis phone call has been placed
                        arena.getGameManager().getPlayerManager().fireFirework(player, Color.GREEN);
                        arena.getGameManager().getPlayerManager().sendMessageToAllPlayers(ChatColor.AQUA + player.getBukkitPlayer().getName() + ChatColor.WHITE + " has called the police.");
                        arena.getGameManager().setPoliceCalled(true);
                        callAttempts++;

                        //Register xp for police called
                        arena.getGameManager().getPlayerManager().getCounselor(player).getXpManager().registerXPAward(XPAward.Counselor_PoliceCalled);
                    }
                }
            }

            updateHologram();
        }
    }

    /**
     * Create's the phone hologram
     */
    private void createHologram() {
        hologram = HologramsAPI.createHologram(FridayThe13th.instance, location.getBlock().getRelative(BlockFace.UP).getLocation());
        updateHologram();
    }

    /**
     * Updates the display of the phone's hologram
     */
    private void updateHologram() {
        hologram.clearLines();

        if (!hasCallBeenCompleted()) {
            //Hologram headers
            if (isTommyJarvisPhone()) {
                hologram.insertTextLine(0, ChatColor.WHITE + FridayThe13th.language.get(Bukkit.getConsoleSender(), "hologram.TommyJarvisTitle", "Call {0}Tommy Jarvis", ChatColor.DARK_AQUA));
            } else if (isPolicePhone()) {
                hologram.insertTextLine(0, ChatColor.RED + FridayThe13th.language.get(Bukkit.getConsoleSender(), "hologram.911", "Call {0}911", ChatColor.RED));
            }

            if (isBroken()) {
                hologram.insertTextLine(1, ChatColor.RED + "" + ChatColor.BOLD + "BROKEN");

                if (repairAttempts > 0) {
                    //Line 2 is repair progress %
                    hologram.insertTextLine(2, ChatColor.WHITE + getDisplayPercentageString(repairAttempts, repairAttemptsRequired));
                }
            } else {
                //Display call progress
                hologram.insertTextLine(1, ChatColor.WHITE + getDisplayPercentageString(callAttempts, callAttemptsRequired));
            }
        } else {
            hologram.delete();
            isVisible = false;
        }
    }

    /**
     * Returns the display string for the hologram values
     * @param actual Actual value
     * @param required Required value
     * @return Display string
     */
    private String getDisplayPercentageString(double actual, int required) {
        float percentage = ((float) actual) / required * 100;
        percentage = Math.round(percentage);
        String strPercent = String.format("%2.0f", percentage);
        String string = String.valueOf(strPercent) + "%";
        return string;
    }
}
