package com.AustinPilz.FridayThe13th.Exceptions.Player;

public class PlayerNotPlayingException extends Exception
{
    public PlayerNotPlayingException() {}

    // Constructor that accepts a message
    public PlayerNotPlayingException(String message)
    {
        super(message);
    }
}
