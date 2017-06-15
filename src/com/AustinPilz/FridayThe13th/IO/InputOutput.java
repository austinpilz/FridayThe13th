package com.AustinPilz.FridayThe13th.IO;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;

public class InputOutput
{
    public static YamlConfiguration global;
    private static Connection connection;

    public InputOutput() {
        if (!FridayThe13th.instance.getDataFolder().exists()) {
            try {
                (FridayThe13th.instance.getDataFolder()).mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        global = new YamlConfiguration();
    }

    /**
     * Loads settings from YML file
     */
    public void LoadSettings() {
        try {
            if (!new File(FridayThe13th.instance.getDataFolder(), "config.yml").exists())
                global.save(new File(FridayThe13th.instance.getDataFolder(), "config.yml"));

            global.load(new File(FridayThe13th.instance.getDataFolder(), "config.yml"));
            for (Setting s : Setting.values()) {
                if (global.get(s.getString()) == null) global.set(s.getString(), s.getDefault());
            }


            global.save(new File(FridayThe13th.instance.getDataFolder(), "config.yml"));


        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}