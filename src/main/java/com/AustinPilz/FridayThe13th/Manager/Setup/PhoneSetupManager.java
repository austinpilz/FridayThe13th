package com.AustinPilz.FridayThe13th.Manager.Setup;

import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.PhoneSetupSessionAlreadyInProgressException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Session.PhoneSetupSession;

import java.util.HashMap;

public class PhoneSetupManager
{
    private HashMap<String, PhoneSetupSession> playerSetupSessions;

    public PhoneSetupManager()
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
    public PhoneSetupSession getPlayerSetupSession(String UUID)
    {
        return playerSetupSessions.get(UUID);
    }

    /**
     * Begins and returns a new arena setup session for the provided user UUID
     * @return New arena setup session
     */
    public PhoneSetupSession startSetupSession(String UUID, String arenaName) throws PhoneSetupSessionAlreadyInProgressException, ArenaDoesNotExistException
    {
        if (FridayThe13th.arenaController.doesArenaExist(arenaName))
        {
            if (!doesUserHaveActiveSession(UUID))
            {
                PhoneSetupSession newSession = new PhoneSetupSession(FridayThe13th.arenaController.getArena(arenaName), UUID.toString());
                playerSetupSessions.put(UUID, newSession);
                return newSession;
            }
            else
            {
                //The user already has a setup session in progress
                throw new PhoneSetupSessionAlreadyInProgressException();
            }
        }
        else
        {
            //Game with that name does not exist
            throw new ArenaDoesNotExistException();
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
