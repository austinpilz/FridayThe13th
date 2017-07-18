package com.AustinPilz.FridayThe13th.Components.Characters;

public class F13Player {
    private String playerUUID;

    //Spawn Preference
    private boolean spawnPreferenceJason;
    private boolean spawnPreferenceCounselor;

    public F13Player(String playerUUID) {
        //Spawn Preferences
        spawnPreferenceJason = false;
        spawnPreferenceCounselor = false;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Sets the players spawn preference as Jason
     */
    public void setSpawnPreferenceJason() {
        spawnPreferenceCounselor = false;
        spawnPreferenceJason = true;
    }

    /**
     * Sets the players spawn preference as Counselor
     */
    public void setSpawnPreferenceCounselor() {
        spawnPreferenceCounselor = true;
        spawnPreferenceJason = false;
    }

    /**
     * Returns if the player prefers
     */
    public boolean isSpawnPreferenceJason() {
        return spawnPreferenceJason;
    }

    public boolean isSpawnPreferenceCounselor() {
        return spawnPreferenceCounselor;
    }
}
