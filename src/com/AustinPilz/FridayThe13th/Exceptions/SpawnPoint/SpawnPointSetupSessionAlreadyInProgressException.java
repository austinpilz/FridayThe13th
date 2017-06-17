package com.AustinPilz.FridayThe13th.Exceptions.SpawnPoint;

public class SpawnPointSetupSessionAlreadyInProgressException extends Exception
{
    public SpawnPointSetupSessionAlreadyInProgressException() {}

    // Constructor that accepts a message
    public SpawnPointSetupSessionAlreadyInProgressException(String message)
    {
        super(message);
    }
}
