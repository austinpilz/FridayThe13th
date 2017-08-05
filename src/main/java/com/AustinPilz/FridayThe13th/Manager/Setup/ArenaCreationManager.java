package com.AustinPilz.FridayThe13th.Manager.Setup;


import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaSetupSessionAlreadyInProgress;
import com.AustinPilz.FridayThe13th.Session.ArenaSetupSession;

import java.util.HashMap;

public class ArenaCreationManager
{
    private HashMap<String, ArenaSetupSession> playerSetupSessions;

    public ArenaCreationManager()
    {
        playerSetupSessions = new HashMap<>();
    }

    /**
     * Returns bool if the supplied UUID has an active setup session
     * @param UUID
     * @return
     */
    public boolean doesUserHaveActiveSession(String UUID)
    {
        return playerSetupSessions.containsKey(UUID);
    }

    /**
     * Returns the setup session for the supplied UUID
     * @param UUID
     * @return
     */
    public ArenaSetupSession getPlayerSetupSession(String UUID)
    {
        return playerSetupSessions.get(UUID);
    }

    /**
     * Begins and returns a new arena setup session for the provided user UUID
     * @return New arena setup session
     */
    public ArenaSetupSession startSetupSession(String UUID, String arenaName) throws ArenaSetupSessionAlreadyInProgress
    {
        if (!doesUserHaveActiveSession(UUID))
        {
            ArenaSetupSession newSession = new ArenaSetupSession(UUID, arenaName);
            playerSetupSessions.put(UUID, newSession);
            return newSession;
        }
        else
        {
            //The user already has a setup session in progress
            throw new ArenaSetupSessionAlreadyInProgress("Player already has active setup session");
        }
    }

    /**
     * Removes the arena setup session of the supplied UUID
     * @param UUID
     */
    public void removePlayerSetupSession(String UUID)
    {
        playerSetupSessions.remove(UUID);
    }
}
