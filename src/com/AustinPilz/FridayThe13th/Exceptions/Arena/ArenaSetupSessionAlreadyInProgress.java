package com.AustinPilz.FridayThe13th.Exceptions.Arena;

public class ArenaSetupSessionAlreadyInProgress extends Exception
{
    public ArenaSetupSessionAlreadyInProgress() {}

    // Constructor that accepts a message
    public ArenaSetupSessionAlreadyInProgress(String message)
    {
        super(message);
    }
}
