package com.AustinPilz.FridayThe13th.Exceptions.Arena;

public class ArenaAlreadyExistsException extends Exception
{
    public ArenaAlreadyExistsException() {}

    // Constructor that accepts a message
    public ArenaAlreadyExistsException(String message)
    {
        super(message);
    }
}