package com.AustinPilz.FridayThe13th;

import com.AustinPilz.FridayThe13th.Command.APICommandHandler;
import com.AustinPilz.FridayThe13th.Command.CommandHandler;
import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Controller.ArenaController;
import com.AustinPilz.FridayThe13th.Controller.ChatController;
import com.AustinPilz.FridayThe13th.Controller.PlayerController;
import com.AustinPilz.FridayThe13th.IO.InputOutput;
import com.AustinPilz.FridayThe13th.IO.LanguageWrapper;
import com.AustinPilz.FridayThe13th.IO.MetricsLite;
import com.AustinPilz.FridayThe13th.IO.SpigotUpdateChecker;
import com.AustinPilz.FridayThe13th.Listener.BlockListener;
import com.AustinPilz.FridayThe13th.Listener.F13EventsListener;
import com.AustinPilz.FridayThe13th.Listener.PlayerListener;
import com.AustinPilz.FridayThe13th.Listener.VehicleListener;
import com.AustinPilz.FridayThe13th.Manager.Setup.*;
import com.AustinPilz.FridayThe13th.Runnable.PlayerMemoryClean;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FridayThe13th extends JavaPlugin implements Listener
{
    public static final String pluginName = "Friday The 13th";
    public static final String pluginVersion = "1.9";
    public static final String pluginPrefix = ChatColor.RED + "[F13] " + ChatColor.WHITE;
    public static final String pluginAdminPrefix = ChatColor.RED + "[F13:A] " + ChatColor.WHITE;
    public static final String signPrefix = ChatColor.RED + "[F13]";
    public static final String consolePrefix = "[FridayThe13th] ";
    public static final String pluginURL = "f13mc.com";
    public static String serverVersion = "";
    public static FridayThe13th instance;

    public static final Logger log = Logger.getLogger("Minecraft");

    //Game Components
    public static ArenaController arenaController;
    public static ChatController chatController;
    public static PlayerController playerController;
    public static SpigotUpdateChecker updateChecker;

    //Global Managers
    public static ArenaCreationManager arenaCreationManager;
    public static SpawnPointCreationManager spawnPointCreationManager;
    public static ChestSetupManager chestSetupManager;
    public static PhoneSetupManager phoneSetupManager;
    public static EscapePointSetupManager escapePointSetupManager;
    public static VehicleSetupManager vehicleSetupManager;
    public static InputOutput inputOutput;

    //3rd Party Plugins
    public static LanguageWrapper language;

    @Override
    public void onEnable()
    {
        //Instance
        instance = this;
        long startMili = System.currentTimeMillis();

        //Language Wrapper
        language = new LanguageWrapper(this, "eng");

        //Determine server version
        try {
            serverVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            log.log(Level.SEVERE, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.error.unknownServerVersion", "Unknown Bukkit/Spigot version - disabling NMS."));
        }

        //Initialize Game Components
        arenaController = new ArenaController();
        chatController = new ChatController();
        playerController = new PlayerController();
        arenaCreationManager = new ArenaCreationManager();
        spawnPointCreationManager = new SpawnPointCreationManager();
        chestSetupManager = new ChestSetupManager();
        phoneSetupManager = new PhoneSetupManager();
        escapePointSetupManager = new EscapePointSetupManager();
        vehicleSetupManager = new VehicleSetupManager();

        //InputOutput
        inputOutput = new InputOutput();
        inputOutput.prepareDB();
        inputOutput.updateDB();
        inputOutput.loadArenas();
        inputOutput.loadSpawnPoints();
        inputOutput.loadChests();
        inputOutput.loadSigns();
        inputOutput.loadPhones();
        inputOutput.loadEscapePoints();
        inputOutput.loadVehicles();

        //Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new VehicleListener(), this);
        getServer().getPluginManager().registerEvents(new F13EventsListener(), this);

        //Schedule tasks
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PlayerMemoryClean(), 36000, 36000);

        //Register Command Handlers
        getCommand("f13").setExecutor(new CommandHandler());
        getCommand("f13api").setExecutor(new APICommandHandler());

        //3rd Party Plugins
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
        {
            log.log(Level.SEVERE, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.error.holographic", "Holographic displays not found - required for gameplay."));
            this.setEnabled(false);
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("ActionBarAPI"))
        {
            log.log(Level.SEVERE, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.error.actionBarAPI", "ActionBar API not found - required for gameplay."));
            this.setEnabled(false);
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("SidebarAPI"))
        {
            log.log(Level.SEVERE, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.error.sideBarAPI", "Sidebar API not found - required for gameplay."));
            this.setEnabled(false);
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            log.log(Level.SEVERE, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.error.protocolLib", "ProtocolLib not found - required for gameplay."));
            this.setEnabled(false);
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("CorpseReborn")) {
            log.log(Level.SEVERE, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.error.corpseReborn", "CorpseReborn not found - required for gameplay."));
            this.setEnabled(false);
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("CustomSoundManagerAPI")) {
            log.log(Level.SEVERE, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.error.customSoundManagerAPI", "CustomSoundManagerAPI not found - required for gameplay."));
            this.setEnabled(false);
            return;
        }

        //Check Spigot API for plugin update
        try
        {
            updateChecker = new SpigotUpdateChecker();
            updateChecker.checkUpdate(pluginVersion);

            if (updateChecker.isUpdateNeeded())
            {
                log.log(Level.INFO, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.message.updateFound", "Update available! New version - v{0} & Current version - v{1}", updateChecker.getLatestVersion(), FridayThe13th.pluginVersion));
            }
        }
        catch (Exception e)
        {
            log.log(Level.WARNING, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.error.spigotUpdate", "Encountered an unexpected error while attempting to check Spigot for update."));
        }

        //Submit Metrics
        try
        {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        }
        catch (IOException e)
        {
            log.log(Level.WARNING, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.error.metrics", "Encountered an unexpected error while attempting to submit stats."));
        }

        //Startup complete
        log.log(Level.INFO, consolePrefix + language.get(Bukkit.getConsoleSender(), "console.message.bootupTime", "Startup complete - took {0} ms", (System.currentTimeMillis() - startMili)));
    }

    @Override
    public void onDisable()
    {
        //End every game, restore players, etc.
        for (Arena arena : arenaController.getArenas().values())
        {
            arena.getGameManager().clearArena();
        }

        //Close database connection
        InputOutput.freeConnection();
    }

    /**
     * Returns if today is Friday the 13th
     * @return
     */
    public static boolean isItFridayThe13th()
    {
        Calendar today = Calendar.getInstance();
        return (today.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && today.get(Calendar.DAY_OF_MONTH) == 13);
    }

    /**
     * Returns if it is currently December
     * @return
     */
    public static boolean isItDecember()
    {
        Calendar today = Calendar.getInstance();
        return (today.get(Calendar.MONTH) == 12);
    }
}
