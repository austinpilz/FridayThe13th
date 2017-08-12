package com.AustinPilz.FridayThe13th.Components;

import com.AustinPilz.FridayThe13th.Components.Profiles.CounselorProfile;
import com.AustinPilz.FridayThe13th.Components.Level.F13Level;
import com.AustinPilz.FridayThe13th.Components.Perk.F13Perk;
import com.AustinPilz.FridayThe13th.Components.Profiles.F13ProfileManager;
import com.AustinPilz.FridayThe13th.Components.Profiles.JasonProfile;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Display.WaitingPlayerStatsDisplayManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

public class F13Player {
    private String playerUUID;

    //Spawn Preference
    private boolean spawnPreferenceJason;
    private boolean spawnPreferenceCounselor;

    //Profiles
    private JasonProfile jasonProfile;
    private CounselorProfile counselorProfile;

    //Purchases
    private HashSet<CounselorProfile> purchasedCounselorProfiles;
    private HashSet<JasonProfile> purchasedJasonProfiles;
    private HashSet<F13Perk> purchasedPerks;


    //Statistics
    private F13Level level;
    private int experiencePoints;
    private int customizationPoints;

    //Waiting Scoreboard
    private WaitingPlayerStatsDisplayManager waitingPlayerStatsDisplayManager;

    public F13Player(String uuid, String jasonProfile, String counselorProfile) {
        this(uuid, jasonProfile, counselorProfile, 0, 0);
    }

    public F13Player(String uuid, String jasonProfile, String counselorProfile, int xp, int cp) {
        this.playerUUID = uuid;

        //Data Structures
        purchasedCounselorProfiles = new HashSet<>();
        purchasedJasonProfiles = new HashSet<>();
        purchasedPerks = new HashSet<>();


        //Spawn Preferences
        spawnPreferenceJason = false;
        spawnPreferenceCounselor = false;

        //Values
        experiencePoints = xp;
        customizationPoints = cp;
        determineLevel();
        determineJasonProfile(jasonProfile);
        determineCounselorProfile(counselorProfile);
        FridayThe13th.inputOutput.loadPlayerPurchases(this);

        //Display
        waitingPlayerStatsDisplayManager = new WaitingPlayerStatsDisplayManager(this);
    }

    /**
     * Returns the player's UUID
     *
     * @return
     */
    public String getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Returns the manager for the waiting player stats scoreboard
     *
     * @return
     */
    public WaitingPlayerStatsDisplayManager getWaitingPlayerStatsDisplayManager() {
        return waitingPlayerStatsDisplayManager;
    }

    /**
     * Returns the players saved Jason profile
     *
     * @return
     */
    public JasonProfile getJasonProfile() {
        return jasonProfile;
    }

    /**
     * Returns the players saved Jason profile
     *
     * @return
     */
    public CounselorProfile getCounselorProfile() {
        return counselorProfile;
    }

    /**
     * Sets the players spawn preference as Jason
     */
    public void setSpawnPreferenceJason() {
        spawnPreferenceCounselor = false;
        spawnPreferenceJason = true;
        updateDB();
    }

    /**
     * Sets the players spawn preference as Counselor
     */
    public void setSpawnPreferenceCounselor() {
        spawnPreferenceCounselor = true;
        spawnPreferenceJason = false;
        updateDB();
    }

    /**
     * Returns if the player prefers to play as Jason
     */
    public boolean isSpawnPreferenceJason() {
        return spawnPreferenceJason;
    }

    /**
     * Returns if the player prefers to play as a counselor
     */
    public boolean isSpawnPreferenceCounselor() {
        return spawnPreferenceCounselor;
    }

    /**
     * Stores the player in the database
     */
    public void storeToDB() {
        try {
            FridayThe13th.inputOutput.storePlayer(this);
        } catch (SaveToDatabaseException exception) {
            //Ruh-roh raggy. Couldn't save them, probably means they already exist
            FridayThe13th.log.log(Level.WARNING, "Encountered an unexpected error while attempting to save F13 player to database.");
        }
    }

    /**
     * Updates player in the database
     */
    public void updateDB() {
        FridayThe13th.inputOutput.updatePlayer(this);
    }

    /**
     * Returns the bukkit player object
     *
     * @return
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(getPlayerUUID()));
    }

    /**
     * Returns the player's experience points
     *
     * @return
     */
    public int getXP() {
        return experiencePoints;
    }

    /**
     * Adds XP to the player's XP balance
     *
     * @param value
     */
    public void addXP(int value) {
        experiencePoints += value;
        updateDB();
        determineLevel();
    }

    /**
     * Sets the player's XP
     *
     * @param value
     */
    private void setXP(int value) {
        experiencePoints = value;
        determineLevel();
    }

    /**
     * Returns the players level
     *
     * @return
     */
    public F13Level getLevel() {
        return level;
    }

    /**
     * Determines the players level
     */
    private void determineLevel() {
        for (F13Level l : F13Level.values()) {
            if (getXP() >= l.getMinXP() && getXP() < l.getMaxXP()) {
                this.level = l;
            }
        }
    }

    /**
     * Determines the players saved profile
     *
     * @param profileName
     */
    private void determineJasonProfile(String profileName) {
        if (F13ProfileManager.getJasonProfileByInternalIdentifier(profileName) != null)
        {
            jasonProfile = F13ProfileManager.getJasonProfileByInternalIdentifier(profileName);
        }

        //Check to see if they still have no skin, set them to the default one
        if (jasonProfile == null) {
            jasonProfile = JasonProfile.PartOne;
        }
    }

    /**
     * Determines the players saved profile
     *
     * @param profileName
     */
    private void determineCounselorProfile(String profileName) {
        if (F13ProfileManager.getCounselorProfileByInternalIdentifier(profileName) != null)
        {
            counselorProfile = F13ProfileManager.getCounselorProfileByInternalIdentifier(profileName);
        }

        //Check to see if they still have no skin, set them to the default one
        if (counselorProfile == null) {
            counselorProfile = CounselorProfile.Chad;
        }
    }

    /**
     * Returns the next level for the player
     *
     * @return
     */
    public F13Level getNextLevel() {
        F13Level level = getLevel();

        for (F13Level l : F13Level.values()) {
            if (l.getLevelNumber() == (getLevel().getLevelNumber() + 1)) {
                level = l;
                break;
            }
        }
        return level;
    }

    /**
     * Sets Jason profile
     *
     * @param p
     * @return
     */
    public boolean setJasonProfile(JasonProfile p) {
        //Check to make sure they have the right level
        if (getLevel().equals(p.getRequiredLevel()) || getLevel().isGreaterThan(p.getRequiredLevel()) || hasPurchasedJasonProfile(p)) {
            jasonProfile = p;
            updateDB();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets Counselor profile
     *
     * @param p
     * @return
     */
    public boolean setCounselorProfile(CounselorProfile p) {
        //Check to make sure they have the right level
        if (getLevel().equals(p.getRequiredLevel()) || getLevel().isGreaterThan(p.getRequiredLevel()) || hasPurchasedCounselorProfile(p)) {
            counselorProfile = p;
            updateDB();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns customization points
     * @return
     */
    public int getCP()
    {
        return customizationPoints;
    }

    /**
     * Returns the formatted string of the player's customization points
     * @return
     */
    public String getFormattedCP()
    {
        return NumberFormat.getInstance().format(customizationPoints);
    }

    /**
     * Adds CP
     * @param value
     */
    public void addCP(int value)
    {
        customizationPoints += value;
        updateDB();
    }

    /**
     * Subtracts CP if balance allows
     * @param value
     * @return
     */
    private boolean subtractCP(int value)
    {
        if (customizationPoints >= value)
        {
            customizationPoints -= value;
            updateDB();
            return true;
        }
        else
        {
            //Insufficient balance
            return false;
        }
    }

    /**
     * Returns the players purchased perks
     * @return
     */
    public HashSet<F13Perk> getPurchasedPerks()
    {
        return purchasedPerks;
    }

    /**
     * Returns if the player has access to the supplied perk
     * @param perk
     * @return
     */
    public boolean hasPerk(F13Perk perk)
    {
        return purchasedPerks.contains(perk);
    }

    /**
     * Purchases a perk for a player, if sufficient funds
     * @param perk
     * @return
     */
    public boolean purchasePerk(F13Perk perk)
    {
        if (customizationPoints >= perk.getCost() && !hasPerk(perk))
        {
            subtractCP(perk.getCost());
            addPurchasedPerk(perk, true);
            return true;
        }
        else
        {
            //Insufficient funds
            return false;
        }
    }

    /**
     * Adds a purchased perk to the profile and database
     * @param perk
     */
    public void addPurchasedPerk(F13Perk perk, boolean storeToDB)
    {
        purchasedPerks.add(perk);

        if (storeToDB)
        {
            //Save to DB
            try
            {
                FridayThe13th.inputOutput.storePurchasedPlayerPerk(this, perk);
            }
            catch (SaveToDatabaseException e)
            {
                FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Unable to store perk to database for player " + getPlayerUUID());
            }
        }
    }

    /**
     * Removes a purchased perk from the profile and database
     * @param perk
     */
    public void removePurchasedPerk(F13Perk perk, boolean removeFromDB)
    {
        purchasedPerks.remove(perk);

        if (removeFromDB)
        {
            //Remove from DB
            FridayThe13th.inputOutput.removePurchasedPlayerPerk(this, perk);
        }
    }

    /**
     * Returns if the player has purchased the provided Jason profile
     * @param profile
     * @return
     */
    public boolean hasPurchasedJasonProfile(JasonProfile profile)
    {
        return purchasedJasonProfiles.contains(profile);
    }

    /**
     * Adds a purchased Jason profile
     * @param profile
     */
    public void addPurchasedJasonProfile(JasonProfile profile, boolean storeToDB)
    {
        purchasedJasonProfiles.add(profile);

        if (storeToDB)
        {
            //Save to DB
            try
            {
                FridayThe13th.inputOutput.storePurchasedPlayerProfile(this, profile.getInternalIdentifier());
            }
            catch (SaveToDatabaseException e)
            {
                FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Unable to store purchased play profile to database for player " + getPlayerUUID());
            }
        }
    }

    /**
     * Removes a purchased Jason profile
     * @param profile
     */
    public void removePurchasedJasonProfile(JasonProfile profile, boolean removeFromDB)
    {
        purchasedJasonProfiles.remove(profile);

        if (removeFromDB)
        {
            //Remove from DB
            FridayThe13th.inputOutput.removePurchasedPlayerProfile(this, profile.getInternalIdentifier());
        }
    }

    /**
     * Returns if the player has purchased the provided Counselor profile
     * @param profile
     * @return
     */
    public boolean hasPurchasedCounselorProfile(CounselorProfile profile)
    {
        return purchasedCounselorProfiles.contains(profile);
    }

    /**
     * Adds a purchased Counselor profile
     * @param profile
     */
    public void addPurchasedCounselorProfile(CounselorProfile profile, boolean storeToDB)
    {
        purchasedCounselorProfiles.add(profile);

        if (storeToDB)
        {
            //Save to DB
            try
            {
                FridayThe13th.inputOutput.storePurchasedPlayerProfile(this, profile.getInternalIdentifier());
            }
            catch (SaveToDatabaseException e)
            {
                FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Unable to store purchased play profile to database for player " + getPlayerUUID());
            }
        }
    }

    /**
     * Removes a purchased Jason profile
     * @param profile
     */
    public void removePurchasedCounselorProfile(CounselorProfile profile, boolean removeFromDB)
    {
        purchasedCounselorProfiles.remove(profile);

        if (removeFromDB)
        {
            //Remove from DB
            FridayThe13th.inputOutput.removePurchasedPlayerProfile(this, profile.getInternalIdentifier());
        }
    }
}
