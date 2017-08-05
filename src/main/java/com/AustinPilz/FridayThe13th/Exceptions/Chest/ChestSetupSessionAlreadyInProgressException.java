package com.AustinPilz.FridayThe13th.Exceptions.Chest;

/**
 * Created by austinpilz on 6/17/17.
 */
public class ChestSetupSessionAlreadyInProgressException extends Exception
{
    public ChestSetupSessionAlreadyInProgressException() {}

    // Constructor that accepts a message
    public ChestSetupSessionAlreadyInProgressException(String message)
    {
        super(message);
    }
}
