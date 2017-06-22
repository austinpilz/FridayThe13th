package com.AustinPilz.FridayThe13th.Manager.Setup;

import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.SpawnPoint.SpawnPointSetupSessionAlreadyInProgressException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Session.SpawnPointSetupSession;

import java.util.HashMap;

public class SpawnPointCreationManager
{
    private HashMap<String, SpawnPointSetupSession> playerSetupSessions;

    public SpawnPointCreationManager()
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
    public SpawnPointSetupSession getPlayerSetupSession(String UUID)
    {
        return playerSetupSessions.get(UUID);
    }

    /**
     * Begins and returns a new arena setup session for the provided user UUID
     * @return New arena setup session
     */
    public SpawnPointSetupSession startSetupSession(String UUID, String arenaName) throws SpawnPointSetupSessionAlreadyInProgressException, ArenaDoesNotExistException
    {
        if (FridayThe13th.arenaController.doesArenaExist(arenaName))
        {
            if (!doesUserHaveActiveSession(UUID))
            {
                SpawnPointSetupSession newSession = new SpawnPointSetupSession(FridayThe13th.arenaController.getArena(arenaName), UUID.toString());
                playerSetupSessions.put(UUID, newSession);
                return newSession;
            }
            else
            {
                //The user already has a setup session in progress
                throw new SpawnPointSetupSessionAlreadyInProgressException();
            }
        }
        else
        {
            //Arena with that name does not exist
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
