package com.AustinPilz.FridayThe13th.Exceptions.Game;


public class GameInProgressException extends Exception
{
    public GameInProgressException() {}

    // Constructor that accepts a message
    public GameInProgressException(String message)
    {
        super(message);
    }
}
