package com.AustinPilz.FridayThe13th.Manager.Game;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Arena.ArenaPhone;
import com.AustinPilz.FridayThe13th.Components.Enum.PhoneType;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PhoneManager {

    private Arena arena;
    private HashMap<Block, ArenaPhone> phones;

    public PhoneManager(Arena arena) {
        this.arena = arena;
        this.phones = new HashMap<>();
    }

    /**
     * Adds phone to the arena
     *
     * @param phone
     */
    public void addPhone(ArenaPhone phone) {
        phones.put(phone.getLocation().getBlock(), phone);
    }

    /**
     * Removes phone from the arena
     *
     * @param phone
     */
    public void removePhone(ArenaPhone phone) {
        phones.remove(phone.getLocation().getBlock());
    }

    /**
     * @param block
     * @return ArenaPhone object
     */
    public ArenaPhone getPhone(Block block) {
        return phones.get(block);
    }

    /**
     * @return Number of phones in the arena
     */
    public int getNumberOfPhones() {
        return phones.size();
    }

    /**
     * @param block Block in question
     * @return If the supplied block is a registered phone within the arena
     */
    public boolean isBlockARegisteredPhone(Block block) {
        return phones.containsKey(block);
    }

    /**
     * Hides all phones from view
     */
    protected void hideAllPhones() {
        Iterator it = phones.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            ArenaPhone phone = (ArenaPhone) entry.getValue();
            phone.hidePhone();
        }
    }

    /**
     * Determines phone types and displays them accordingly for players
     */
    public void displayGamePhones() {
        ArenaPhone[] randomizedPhones = getRandomizedPhoneArray();

        //Tommy Jarvis phone
        randomizedPhones[0].setPhoneType(PhoneType.TommyJarvis);
        randomizedPhones[0].showPhone();

        //Police phone
        randomizedPhones[1].setPhoneType(PhoneType.Police);
        randomizedPhones[1].showPhone();
    }

    /**
     * @return Randomly sorted array of arena phones
     */
    private ArenaPhone[] getRandomizedPhoneArray() {
        if (phones.size() > 0) {
            ArenaPhone[] pl = phones.values().toArray(new ArenaPhone[phones.size()]);

            //Randomize possible phones
            Random rnd = ThreadLocalRandom.current();
            for (int i = pl.length - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);

                // Simple swap
                ArenaPhone a = pl[index];
                pl[index] = pl[i];
                pl[i] = a;
            }

            return pl;
        } else {
            return null;
        }
    }
}
