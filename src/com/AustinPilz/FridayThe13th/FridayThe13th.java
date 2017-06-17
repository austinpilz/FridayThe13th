package com.AustinPilz.FridayThe13th;

import com.AustinPilz.FridayThe13th.Command.AdminCommand;
import com.AustinPilz.FridayThe13th.Controller.ArenaController;
import com.AustinPilz.FridayThe13th.IO.InputOutput;
import com.AustinPilz.FridayThe13th.Listener.PlayerListener;
import com.AustinPilz.FridayThe13th.Manager.Setup.ArenaCreationManager;
import com.AustinPilz.FridayThe13th.Manager.Setup.SpawnPointCreationManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FridayThe13th extends JavaPlugin implements Listener
{
    public static final String pluginName = "Friday The 13th";
    public static final String pluginVersion = "0.0";
    public static final String pluginPrefix = ChatColor.RED + "[F13] " + ChatColor.WHITE;
    public static final String pluginAdminPrefix = ChatColor.RED + "[F13:A] " + ChatColor.WHITE;
    public static final String signPrefix = "[Friday13]";
    public static final String consolePrefix = "[FridayThe13th] ";
    public static final String pluginURL = "";
    public static FridayThe13th instance;

    public static final Logger log = Logger.getLogger("Minecraft");

    //Game Components
    public static ArenaController arenaController;

    //Global Managers
    public static ArenaCreationManager arenaCreationManager;
    public static SpawnPointCreationManager spawnPointCreationManager;
    public static InputOutput inputOutput;

    @Override
    public void onLoad()
    {

    }

    @Override
    public void onEnable()
    {
        //Instance
        instance = this;
        long startMili = System.currentTimeMillis();

        //Initialize Game Components
        arenaController = new ArenaController();
        arenaCreationManager = new ArenaCreationManager();
        spawnPointCreationManager = new SpawnPointCreationManager();

        //InputOutput
        inputOutput = new InputOutput();
        inputOutput.LoadSettings();
        inputOutput.prepareDB();
        inputOutput.loadArenas();
        inputOutput.loadSpawnPoints();

        //Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        //Register Command Handlers
        getCommand("f13").setExecutor(new AdminCommand());

        //MIN 2 MAX 9

        log.log(Level.INFO, consolePrefix + "Boot up complete - took " + (System.currentTimeMillis() - startMili) + " ms");
    }

    @Override
    public void onDisable()
    {
        //
    }
}
