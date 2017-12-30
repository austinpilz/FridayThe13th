package com.AustinPilz.FridayThe13th.Manager.Setup;

import com.AustinPilz.FridayThe13th.Components.Enum.EscapePointType;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.EscapePointSessionAlreadyInProgressException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Session.EscapePointSetupSession;

import java.util.HashMap;

public class EscapePointSetupManager {
    private HashMap<String, EscapePointSetupSession> playerSetupSessions;

    public EscapePointSetupManager() {
        playerSetupSessions = new HashMap<>();
    }

    /**
     * Returns bool if the supplied UUID has an active setup session
     *
     * @param UUID
     * @return
     */
    public boolean doesUserHaveActiveSession(String UUID) {
        return playerSetupSessions.containsKey(UUID);
    }

    /**
     * Returns the setup session for the supplied UUID
     *
     * @param UUID
     * @return
     */
    public EscapePointSetupSession getPlayerSetupSession(String UUID) {
        return playerSetupSessions.get(UUID);
    }

    /**
     * Begins and returns a new arena setup session for the provided user UUID
     *
     * @return New arena setup session
     */
    public EscapePointSetupSession startSetupSession(String UUID, String arenaName, EscapePointType pointType) throws EscapePointSessionAlreadyInProgressException, ArenaDoesNotExistException {
        if (FridayThe13th.arenaController.doesArenaExist(arenaName)) {
            if (!doesUserHaveActiveSession(UUID)) {
                EscapePointSetupSession newSession = new EscapePointSetupSession(FridayThe13th.arenaController.getArena(arenaName), UUID.toString(), pointType);
                playerSetupSessions.put(UUID, newSession);
                return newSession;
            } else {
                //The user already has a setup session in progress
                throw new EscapePointSessionAlreadyInProgressException();
            }
        } else {
            //Game with that name does not exist
            throw new ArenaDoesNotExistException();
        }
    }

    /**
     * Removes the arena setup session of the supplied UUID
     *
     * @param UUID
     */
    public void removePlayerSetupSession(String UUID) {
        playerSetupSessions.remove(UUID);
    }
}
