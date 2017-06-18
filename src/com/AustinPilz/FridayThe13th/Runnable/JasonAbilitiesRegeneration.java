package com.AustinPilz.FridayThe13th.Runnable;

import com.AustinPilz.FridayThe13th.Components.Jason;

public class JasonAbilitiesRegeneration implements Runnable
{
    Jason jason;

    public JasonAbilitiesRegeneration (Jason jason)
    {
        this.jason = jason;
    }

    @Override
    public void run()
    {
        //Check to see if initial generations reached
        if (!jason.hasInitialStalkGenerationCompleted())
        {
            if (jason.getStalkLevelPercentage() == 1)
            {
                jason.setInitialStalkGenerationCompleted(true);
            }
        }

        //Regenerate Values
        jason.regenerateStalking();
    }
}
