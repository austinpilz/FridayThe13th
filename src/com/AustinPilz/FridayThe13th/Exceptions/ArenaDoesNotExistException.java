package com.AustinPilz.FridayThe13th.Exceptions;

public class ArenaDoesNotExistException extends Exception
{
    public ArenaDoesNotExistException() {}

    // Constructor that accepts a message
    public ArenaDoesNotExistException(String message)
    {
        super(message);
    }
}