package com.AustinPilz.FridayThe13th.IO;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaAlreadyExistsException;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaCreationException;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.SpawnPoint.SpawnPointCreationException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

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

    private static Connection createConnection() {

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection ret = DriverManager.getConnection("jdbc:sqlite:" +  new File(FridayThe13th.instance.getDataFolder().getPath(), "db.sqlite").getPath());
            ret.setAutoCommit(false);
            return ret;
        }
        catch (ClassNotFoundException e)
        {
            FridayThe13th.log.log(Level.SEVERE, FridayThe13th.consolePrefix + "ClassNotFound while attempting to create database connection");
            e.printStackTrace();
            return null;
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.SEVERE, FridayThe13th.consolePrefix + "Encountered SQL exception while attempting to create database connection");
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized Connection getConnection() {
        if (connection == null) connection = createConnection();
        try
        {
            if(connection.isClosed()) connection = createConnection();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        return connection;
    }

    public static synchronized void freeConnection() {
        Connection conn = getConnection();
        if(conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prepares the database
     */
    public void prepareDB()
    {
        Connection conn = getConnection();
        Statement st = null;
        try
        {
            st = conn.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_arenas\" (\"Name\" VARCHAR PRIMARY KEY NOT NULL, \"B1X\" DOUBLE, \"B1Y\" DOUBLE, \"B1Z\" DOUBLE, \"B2X\" DOUBLE, \"B2Y\" DOUBLE, \"B2Z\" DOUBLE, \"ArenaWorld\" VARCHAR, \"WaitX\" DOUBLE, \"WaitY\" DOUBLE, \"WaitZ\" DOUBLE, \"WaitWorld\" VARCHAR, \"ReturnX\" DOUBLE, \"ReturnY\" DOUBLE, \"ReturnZ\" DOUBLE, \"ReturnWorld\" VARCHAR, \"JasonX\" DOUBLE, \"JasonY\" DOUBLE, \"JasonZ\" DOUBLE)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_spawn_points\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, \"Arena\" VARCHAR)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_chests\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" DOUBLE, \"Arena\" VARCHAR, \"Type\" VARCHAR)");
            //st.executeUpdate("CREATE TABLE IF NOT EXISTS \"alcatraz_cells\" (\"Prison\" VARCHAR, \"CellNumber\" VARCHAR, \"InmateUUID\" VARCHAR)");

            conn.commit();
            st.close();

        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.SEVERE, FridayThe13th.consolePrefix + "Encountered SQL error while attempting to prepare database: " + e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            FridayThe13th.log.log(Level.SEVERE, FridayThe13th.consolePrefix + "Unknown error encountered while attempting to prepare database.");
        }
    }

    /**
     * Load arenas into arena controller memory
     */
    public void loadArenas()
    {
        try
        {
            Connection conn;
            PreparedStatement ps = null;
            ResultSet result = null;
            conn = getConnection();
            ps = conn.prepareStatement("SELECT `Name`, `B1X`, `B1Y`, `B1Z`, `B2X`, `B2Y`, `B2Z`, `ArenaWorld`, `WaitX`, `WaitY`, `WaitZ`, `WaitWorld`, `ReturnX`, `ReturnY`, `ReturnZ`, `ReturnWorld`, `JasonX`, `JasonY`, `JasonZ` FROM `f13_arenas`");
            result = ps.executeQuery();

            int count = 0;
            while (result.next())
            {

                Location boundary1 = new Location(Bukkit.getWorld(result.getString("ArenaWorld")), result.getDouble("B1X"),result.getDouble("B1Y"),result.getDouble("B1Z"));
                Location boundary2 = new Location(Bukkit.getWorld(result.getString("ArenaWorld")), result.getDouble("B2X"),result.getDouble("B2Y"),result.getDouble("B2Z"));
                Location waitLoc = new Location(Bukkit.getWorld(result.getString("WaitWorld")), result.getDouble("WaitX"),result.getDouble("WaitY"),result.getDouble("WaitZ"));
                Location returnLoc = new Location(Bukkit.getWorld(result.getString("ReturnWorld")), result.getDouble("ReturnX"),result.getDouble("ReturnY"),result.getDouble("ReturnZ"));
                Location jasonLoc = new Location(Bukkit.getWorld(result.getString("ArenaWorld")), result.getDouble("JasonX"),result.getDouble("JasonY"),result.getDouble("JasonZ"));

                Arena arena = new Arena(result.getString("Name"), boundary1, boundary2, waitLoc, returnLoc, jasonLoc);

                try
                {
                    FridayThe13th.arenaController.addArena(arena);
                }
                catch (ArenaAlreadyExistsException exception)
                {
                    FridayThe13th.log.log(Level.SEVERE, FridayThe13th.consolePrefix + "Attempted to load arena ("+arena.getArenaName()+") from database that was already in controller memory");
                }

                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Arena " + arena.getArenaName() + " loaded successfully.");
                count++;
            }

            if (count > 0)
            {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Loaded " + count + " arena(s).");
            }

            conn.commit();
            ps.close();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to load arenas..." + e.getMessage());
        }
    }

    /**
     * Stores arena into database
     * @param arena
     */
    public void storeArena(Arena arena) throws ArenaCreationException
    {
        try
        {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_arenas (`Name`, `B1X`, `B1Y`, `B1Z`, `B2X`, `B2Y`, `B2Z`, `ArenaWorld`, `WaitX`, `WaitY`, `WaitZ`, `WaitWorld`, `ReturnX`, `ReturnY`, `ReturnZ`, `ReturnWorld`, `JasonX`, `JasonY`, `JasonZ`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, arena.getArenaName()+"");
            preparedStatement.setDouble(2, arena.getBoundary1().getX());
            preparedStatement.setDouble(3, arena.getBoundary1().getY());
            preparedStatement.setDouble(4, arena.getBoundary1().getZ());
            preparedStatement.setDouble(5, arena.getBoundary2().getX());
            preparedStatement.setDouble(6, arena.getBoundary2().getY());
            preparedStatement.setDouble(7, arena.getBoundary2().getZ());
            preparedStatement.setString(8, arena.getBoundary1().getWorld().getName()+"");
            preparedStatement.setDouble(9, arena.getWaitingLocation().getX());
            preparedStatement.setDouble(10, arena.getWaitingLocation().getY());
            preparedStatement.setDouble(11, arena.getWaitingLocation().getZ());
            preparedStatement.setString(12, arena.getWaitingLocation().getWorld().getName()+"");
            preparedStatement.setDouble(13, arena.getReturnLocation().getX());
            preparedStatement.setDouble(14, arena.getReturnLocation().getY());
            preparedStatement.setDouble(15, arena.getReturnLocation().getZ());
            preparedStatement.setString(16, arena.getReturnLocation().getWorld().getName()+"");
            preparedStatement.setDouble(17, arena.getJasonStartLocation().getX());
            preparedStatement.setDouble(18, arena.getJasonStartLocation().getY());
            preparedStatement.setDouble(19, arena.getJasonStartLocation().getZ());

            preparedStatement.executeUpdate();
            conn.commit();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to save new arena into database: " + e.getMessage());
            throw new ArenaCreationException();
        }
    }

    public void loadSpawnPoints()
    {
        try
        {
            Connection conn;
            PreparedStatement ps = null;
            ResultSet result = null;
            conn = getConnection();
            ps = conn.prepareStatement("SELECT `Arena`, `X`, `Y`, `Z`, `World` FROM `f13_spawn_points`");
            result = ps.executeQuery();

            int count = 0;
            while (result.next())
            {
                Location spawnLocation = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X"),result.getDouble("Y"),result.getDouble("Z"));

                try
                {
                    Arena arena = FridayThe13th.arenaController.getArena(result.getString("Arena"));
                    arena.getLocationManager().addStartingPoint(spawnLocation);
                }
                catch (ArenaDoesNotExistException exception)
                {
                    FridayThe13th.log.log(Level.SEVERE, FridayThe13th.consolePrefix + "Attempted to load spawn point in arena ("+result.getString("Arena")+"), arena does not exist in memory.");
                }
                count++;
            }

            if (count > 0)
            {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Loaded " + count + " spawn point(s).");
            }

            conn.commit();
            ps.close();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to load arenas..." + e.getMessage());
        }
    }

    /**
     * Stores new spawn point into the database
     * @param arena
     * @param location
     * @throws SpawnPointCreationException
     */
    public void storeSpawnPoint(Arena arena, Location location) throws SpawnPointCreationException
    {
        try
        {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_spawn_points (`Arena`, `X`, `Y`, `Z`, `World`) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, arena.getArenaName()+"");
            preparedStatement.setDouble(2, location.getX());
            preparedStatement.setDouble(3, location.getY());
            preparedStatement.setDouble(4, location.getZ());
            preparedStatement.setString(5, location.getWorld().getName()+"");


            preparedStatement.executeUpdate();
            conn.commit();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to save new spawn point in arena "+arena.getArenaName()+" into database: " + e.getMessage());
            throw new SpawnPointCreationException();
        }
    }


    public void loadChests()
    {
        //
    }

    public void newChest()
    {
        //
    }

    public void deleteChest()
    {
        //
    }




}