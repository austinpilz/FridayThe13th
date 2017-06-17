package com.AustinPilz.FridayThe13th.Exceptions.Game;

public class GameFullException extends Exception
{
    public GameFullException() {}

    // Constructor that accepts a message
    public GameFullException(String message)
    {
        super(message);
    }
}
