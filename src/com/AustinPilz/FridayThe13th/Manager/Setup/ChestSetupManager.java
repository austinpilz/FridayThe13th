package com.AustinPilz.FridayThe13th.Manager.Setup;

import com.AustinPilz.FridayThe13th.Components.ChestType;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.Chest.ChestSetupSessionAlreadyInProgressException;
import com.AustinPilz.FridayThe13th.Exceptions.SpawnPoint.SpawnPointSetupSessionAlreadyInProgressException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Session.ChestSetupSession;
import com.AustinPilz.FridayThe13th.Session.SpawnPointSetupSession;

import java.util.HashMap;

public class ChestSetupManager
{
    private HashMap<String, ChestSetupSession> playerSetupSessions;

    public ChestSetupManager()
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
    public ChestSetupSession getPlayerSetupSession(String UUID)
    {
        return playerSetupSessions.get(UUID);
    }

    /**
     * Begins and returns a new arena setup session for the provided user UUID
     * @return New arena setup session
     */
    public ChestSetupSession startSetupSession(String UUID, String arenaName, ChestType chestType) throws ChestSetupSessionAlreadyInProgressException, ArenaDoesNotExistException
    {
        if (FridayThe13th.arenaController.doesArenaExist(arenaName))
        {
            if (!doesUserHaveActiveSession(UUID))
            {
                ChestSetupSession newSession = new ChestSetupSession(FridayThe13th.arenaController.getArena(arenaName), UUID.toString(), chestType);
                playerSetupSessions.put(UUID, newSession);
                return newSession;
            }
            else
            {
                //The user already has a setup session in progress
                throw new ChestSetupSessionAlreadyInProgressException();
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
