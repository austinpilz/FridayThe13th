package com.AustinPilz.FridayThe13th.Exceptions.SpawnPoint;

public class SpawnPointCreationException extends Exception
{
    public SpawnPointCreationException() {}

    // Constructor that accepts a message
    public SpawnPointCreationException(String message)
    {
        super(message);
    }
}
