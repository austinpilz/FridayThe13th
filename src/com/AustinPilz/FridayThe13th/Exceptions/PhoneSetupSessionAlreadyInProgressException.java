package com.AustinPilz.FridayThe13th.Exceptions;

/**
 * Created by austinpilz on 7/14/17.
 */
public class PhoneSetupSessionAlreadyInProgressException extends Exception
{
    public PhoneSetupSessionAlreadyInProgressException() {}

    // Constructor that accepts a message
    public PhoneSetupSessionAlreadyInProgressException(String message)
    {
        super(message);
    }
}
