package com.AustinPilz.FridayThe13th;

import com.AustinPilz.FridayThe13th.Command.CommandHandler;
import com.AustinPilz.FridayThe13th.Controller.ArenaController;
import com.AustinPilz.FridayThe13th.IO.InputOutput;
import com.AustinPilz.FridayThe13th.IO.MetricsLite;
import com.AustinPilz.FridayThe13th.IO.SpigotUpdateChecker;
import com.AustinPilz.FridayThe13th.Listener.BlockListener;
import com.AustinPilz.FridayThe13th.Listener.PlayerListener;
import com.AustinPilz.FridayThe13th.Manager.Setup.ArenaCreationManager;
import com.AustinPilz.FridayThe13th.Manager.Setup.ChestSetupManager;
import com.AustinPilz.FridayThe13th.Manager.Setup.SpawnPointCreationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
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
    public static SpigotUpdateChecker updateChecker;

    //Global Managers
    public static ArenaCreationManager arenaCreationManager;
    public static SpawnPointCreationManager spawnPointCreationManager;
    public static ChestSetupManager chestSetupManager;
    public static InputOutput inputOutput;

    //3rd Party Plugins

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
        chestSetupManager = new ChestSetupManager();

        //InputOutput
        inputOutput = new InputOutput();
        inputOutput.LoadSettings();
        inputOutput.prepareDB();
        inputOutput.loadArenas();
        inputOutput.loadSpawnPoints();
        inputOutput.loadChests();

        //Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);

        //Register Command Handlers
        getCommand("f13").setExecutor(new CommandHandler());

        //3rd Party Plugins
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
        {
            log.log(Level.SEVERE, consolePrefix + "Holographic displays not found - required for gameplay.");
            this.setEnabled(false);
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("ActionBarAPI"))
        {
            log.log(Level.SEVERE, consolePrefix + "ActionBarAPI not found - required for gameplay.");
            this.setEnabled(false);
            return;
        }

        //Check for Update
        try
        {
            this.updateChecker = new SpigotUpdateChecker();
            this.updateChecker.checkUpdate(this.pluginVersion);

            if (updateChecker.isUpdateNeeded())
            {
                log.log(Level.INFO, consolePrefix + "Update available! New Version - v" + updateChecker.getLatestVersion() + " & Your Version - v" + FridayThe13th.pluginVersion);
            }
        } catch (Exception e)
        {
            log.log(Level.WARNING, consolePrefix + "Encountered an error while attempting to check Spigot for update.");
            e.printStackTrace();
        }

        //Submit Metrics
        try
        {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        }
        catch (IOException e)
        {
            log.log(Level.WARNING, consolePrefix + "Encountered an error while attempting to submit plugin metrics.");
        }


        log.log(Level.INFO, consolePrefix + "Boot up complete - took " + (System.currentTimeMillis() - startMili) + " ms");
    }

    @Override
    public void onDisable()
    {
        //End every game, restore players, etc.

        //When implemented, store any values in DB that would need updates.

        //Close database connection
        inputOutput.freeConnection();
    }
}
