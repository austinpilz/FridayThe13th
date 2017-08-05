package com.AustinPilz.FridayThe13th.Runnable;


import com.AustinPilz.FridayThe13th.Components.Characters.Jason;

public class JasonAbilitiesDisplayUpdate implements Runnable
{
    Jason jason;

    public JasonAbilitiesDisplayUpdate (Jason jason)
    {
        this.jason = jason;
    }

    @Override
    public void run()
    {
        jason.getAbilityDisplayManager().updateLevels();
    }
}
