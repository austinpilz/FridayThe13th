package com.AustinPilz.FridayThe13th.Exceptions.Player;

public class PlayerAlreadyPlayingException extends Exception
{
    public PlayerAlreadyPlayingException() {}

    // Constructor that accepts a message
    public PlayerAlreadyPlayingException(String message)
    {
        super(message);
    }
}
