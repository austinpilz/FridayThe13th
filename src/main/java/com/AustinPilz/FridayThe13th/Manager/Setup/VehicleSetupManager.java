package com.AustinPilz.FridayThe13th.Manager.Setup;

import com.AustinPilz.FridayThe13th.Components.Vehicle.VehicleType;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.Chest.ChestSetupSessionAlreadyInProgressException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Session.VehicleSetupSession;

import java.util.HashMap;

public class VehicleSetupManager {
    private HashMap<String, VehicleSetupSession> playerSetupSessions;

    public VehicleSetupManager() {
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
    public VehicleSetupSession getPlayerSetupSession(String UUID) {
        return playerSetupSessions.get(UUID);
    }

    /**
     * Begins and returns a new setup session for the provided user UUID
     *
     * @return New arena setup session
     */
    public VehicleSetupSession startSetupSession(String UUID, String arenaName, VehicleType vehicleType) throws ChestSetupSessionAlreadyInProgressException, ArenaDoesNotExistException {
        if (FridayThe13th.arenaController.doesArenaExist(arenaName)) {
            if (!doesUserHaveActiveSession(UUID)) {
                VehicleSetupSession newSession = new VehicleSetupSession(FridayThe13th.arenaController.getArena(arenaName), UUID.toString(), vehicleType);
                playerSetupSessions.put(UUID, newSession);
                return newSession;
            } else {
                //The user already has a setup session in progress
                throw new ChestSetupSessionAlreadyInProgressException();
            }
        } else {
            //Game with that name does not exist
            throw new ArenaDoesNotExistException();
        }
    }

    /**
     * Removes the setup session of the supplied UUID
     *
     * @param UUID
     */
    public void removePlayerSetupSession(String UUID) {
        playerSetupSessions.remove(UUID);
    }
}
