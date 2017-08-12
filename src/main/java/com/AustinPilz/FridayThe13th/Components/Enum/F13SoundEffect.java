package com.AustinPilz.FridayThe13th.Components.Enum;

import org.bukkit.Bukkit;

public enum F13SoundEffect {

    ChiChiChi("f13.chi", 4, 10),
    Stab("f13.stab", 2, 1),
    LobbyMusic("f13.music.lobby", 180, 10),
    GlassBreak("f13.glassbreak", 2, 1),
    DoorHit("f13.doorhit", 1, 1),
    DoorBreak("f13.doorbreak", 2, 1),
    Music_GameStart("f13.gamestart", 69, 10),
    Music_2Minute("f13.twominute", 120, 10),
    GuyGasp("f13.guygasp", 1, 10),
    GirlGasp("f13.girlgasp", 1, 10),
    RetroChase("f13.retrochase", 82, 10),
    RegularChase("f13.chase", 81, 10),
    TrapCaught("f13.trapcaught", 1, 1),
    JumpScare("f13.jumpscare", 10, 10);

    private String value;
    private int lengthInSeconds;
    private int volume;

    F13SoundEffect(String v, int s, int vol)
    {
        value = v;
        lengthInSeconds = s;
        volume = vol;
    }

    /**
     * Returns the custom sound effect name to be played
     * @return
     */
    public String getResourcePackValue()
    {
        return value;
    }

    /**
     * Returns the length of the sound effect in seconds
     * @return
     */
    public int getLengthInSeconds()
    {
        return lengthInSeconds;
    }

    /**
     * Returns the volume to play the sound
     * @return
     */
    public int getVolume()
    {
        return volume;
    }
}
