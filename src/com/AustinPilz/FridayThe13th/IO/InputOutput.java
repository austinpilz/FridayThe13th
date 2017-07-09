package com.AustinPilz.FridayThe13th.IO;

import com.AustinPilz.FridayThe13th.Components.Arena;
import com.AustinPilz.FridayThe13th.Components.ArenaChest;
import com.AustinPilz.FridayThe13th.Components.ChestType;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaAlreadyExistsException;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
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
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_chests\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, \"Arena\" VARCHAR, \"Type\" VARCHAR)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_signs\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, \"Arena\" VARCHAR, \"Type\" VARCHAR)");

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
    public void storeArena(Arena arena) throws SaveToDatabaseException
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
            throw new SaveToDatabaseException();
        }
    }

    public void deleteArena(String arenaName)
    {
        try
        {
            Connection conn = InputOutput.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM f13_arenas WHERE Name = ?");
            ps.setString(1, arenaName);
            ps.executeUpdate();
            conn.commit();
            ps.close();

        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to remove an arena from the database: " + e.getMessage());
        }
    }

    /**
     * Loads all spawn points into arena objects
     */
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
            int removed = 0;
            while (result.next())
            {
                Location spawnLocation = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X"),result.getDouble("Y"),result.getDouble("Z"));

                try
                {
                    Arena arena = FridayThe13th.arenaController.getArena(result.getString("Arena"));
                    arena.getLocationManager().addStartingPoint(spawnLocation);
                    count++;
                }
                catch (ArenaDoesNotExistException exception)
                {
                    deleteSpawnPoint(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"), result.getString("Arena"));
                    removed++;
                }
            }

            if (count > 0)
            {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Loaded " + count + " spawn point(s).");
            }

            if (removed > 0)
            {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Removed " + removed + " spawn point(s) for arenas that no longer exist.");
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
     * @throws SaveToDatabaseException
     */
    public void storeSpawnPoint(Arena arena, Location location) throws SaveToDatabaseException
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
            throw new SaveToDatabaseException();
        }
    }

    public void deleteSpawnPoint(double X, double Y, double Z, String world, String arena)
    {
        try
        {
            Connection conn = InputOutput.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM f13_spawn_points WHERE Arena = ? AND World = ? AND X = ? AND Y = ? AND Z = ?");
            ps.setString(1, arena);
            ps.setString(2, world);
            ps.setDouble(3, X);
            ps.setDouble(4, Y);
            ps.setDouble(5, Z);
            ps.executeUpdate();
            conn.commit();
            ps.close();

        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to remove a chest from the database: " + e.getMessage());
        }
    }

    /**
     * Loads chests from database into arena memory
     */
    public void loadChests()
    {
        try
        {
            Connection conn;
            PreparedStatement ps = null;
            ResultSet result = null;
            conn = getConnection();
            ps = conn.prepareStatement("SELECT `World`, `X`, `Y`, `Z`, `Arena`, `Type` FROM `f13_chests`");
            result = ps.executeQuery();

            int count = 0;
            int removed = 0;
            while (result.next())
            {
                Location chestLocation = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X"),result.getDouble("Y"),result.getDouble("Z"));

                if (chestLocation.getBlock().getType().equals(Material.CHEST))
                {
                    try
                    {
                        //Determine chest type
                        ChestType type = ChestType.Item; //default

                        if (result.getString("Type").equals("Item"))
                        {
                            type = ChestType.Item;
                        }
                        else if (result.getString("Type").equals("Weapon"))
                        {
                            type = ChestType.Weapon;
                        }


                        Arena arena = FridayThe13th.arenaController.getArena(result.getString("Arena"));
                        arena.getObjectManager().addChest(new ArenaChest(arena, chestLocation, type));
                        count++;
                    }
                    catch (ArenaDoesNotExistException exception)
                    {
                        deleteChest(result.getDouble("X"),result.getDouble("Y"),result.getDouble("Z"), result.getString("World"));
                        removed++;
                    }
                }
                else
                {
                    //This location is no longer a chest, so remove it
                    deleteChest(result.getDouble("X"),result.getDouble("Y"),result.getDouble("Z"), result.getString("World"));
                    removed++;
                }
            }

            if (count > 0)
            {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Loaded " + count + " chest(s).");
            }

            if (removed > 0)
            {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Removed " + removed + " chest(s) for arenas that no longer exist.");
            }

            conn.commit();
            ps.close();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to load chests from database: " + e.getMessage());
        }
    }

    /**
     * Stores chest to the database
     * @param chest
     * @throws SaveToDatabaseException
     */
    public void newChest(ArenaChest chest) throws SaveToDatabaseException
    {
        try
        {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_chests (`X`, `Y`, `Z`, `World`, `Arena`, `Type`) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);


            preparedStatement.setDouble(1, chest.getLocation().getX());
            preparedStatement.setDouble(2, chest.getLocation().getY());
            preparedStatement.setDouble(3, chest.getLocation().getZ());
            preparedStatement.setString(4, chest.getLocation().getWorld().getName());
            preparedStatement.setString(5, chest.getArena().getArenaName());
            preparedStatement.setString(6, chest.getChestType().getFieldDescription());

            preparedStatement.executeUpdate();
            conn.commit();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to store a chest to the database: " + e.getMessage());
            throw new SaveToDatabaseException();
        }
    }

    /**
     * Removes a chest from the database
     * @param X
     * @param Y
     * @param Z
     * @param world
     */
    public void deleteChest(double X, double Y, double Z, String world)
    {
        try
        {
            Connection conn = InputOutput.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM f13_chests WHERE World = ? AND X = ? AND Y = ? AND Z = ?");
            ps.setString(1, world);
            ps.setDouble(2, X);
            ps.setDouble(3, Y);
            ps.setDouble(4, Z);
            ps.executeUpdate();
            conn.commit();
            ps.close();

        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to remove a chest from the database: " + e.getMessage());
        }
    }

    /**
     * Stores sign to the database
     * @param sign
     * @throws SaveToDatabaseException
     */
    public void newSign(Sign sign, Arena arena) throws SaveToDatabaseException
    {
        try
        {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_signs (`X`, `Y`, `Z`, `World`, `Arena`, `Type`) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);


            preparedStatement.setDouble(1, sign.getLocation().getX());
            preparedStatement.setDouble(2, sign.getLocation().getY());
            preparedStatement.setDouble(3, sign.getLocation().getZ());
            preparedStatement.setString(4, sign.getLocation().getWorld().getName());
            preparedStatement.setString(5, arena.getArenaName());
            preparedStatement.setString(6, "Join");

            preparedStatement.executeUpdate();
            conn.commit();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to store a sign to the database: " + e.getMessage());
            throw new SaveToDatabaseException();
        }
    }

    /**
     * Removes a sign from the database
     * @param X
     * @param Y
     * @param Z
     * @param world
     */
    public void deleteSign(double X, double Y, double Z, String world)
    {
        try
        {
            Connection conn = InputOutput.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM f13_signs WHERE World = ? AND X = ? AND Y = ? AND Z = ?");
            ps.setString(1, world);
            ps.setDouble(2, X);
            ps.setDouble(3, Y);
            ps.setDouble(4, Z);
            ps.executeUpdate();
            conn.commit();
            ps.close();

        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to remove a sign from the database: " + e.getMessage());
        }
    }


    /**
     * Loads chests from database into arena memory
     */
    public void loadSigns()
    {
        try
        {
            Connection conn;
            PreparedStatement ps = null;
            ResultSet result = null;
            conn = getConnection();
            ps = conn.prepareStatement("SELECT `World`, `X`, `Y`, `Z`, `Arena`, `Type` FROM `f13_signs`");
            result = ps.executeQuery();

            int count = 0;
            int removed = 0;
            while (result.next())
            {
                Location signLocation = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X"),result.getDouble("Y"),result.getDouble("Z"));

                if (signLocation.getBlock().getType().equals(Material.SIGN_POST) || signLocation.getBlock().getType().equals(Material.WALL_SIGN))
                {
                    try
                    {
                        Arena arena = FridayThe13th.arenaController.getArena(result.getString("Arena"));
                        arena.getSignManager().addJoinSign((Sign)signLocation.getBlock().getState());
                        count++;
                    }
                    catch (ArenaDoesNotExistException exception)
                    {
                        FridayThe13th.log.log(Level.SEVERE, FridayThe13th.consolePrefix + "Attempted to load sign in arena ("+result.getString("Arena")+"), arena does not exist in memory.");
                        deleteSign(result.getDouble("X"),result.getDouble("Y"),result.getDouble("Z"), result.getString("World"));
                        removed++;
                    }
                }
                else
                {
                    //This location is no longer a chest, so remove it
                    deleteSign(result.getDouble("X"),result.getDouble("Y"),result.getDouble("Z"), result.getString("World"));
                    removed++;
                }
            }

            if (count > 0)
            {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Loaded " + count + " signs(s).");
            }

            if (removed > 0)
            {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Removed " + removed + " signs(s).");
            }

            conn.commit();
            ps.close();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to load signs from database: " + e.getMessage());
        }
    }




}