package com.AustinPilz.FridayThe13th.IO;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Arena.ArenaChest;
import com.AustinPilz.FridayThe13th.Components.Arena.ArenaPhone;
import com.AustinPilz.FridayThe13th.Components.Arena.EscapePoint;
import com.AustinPilz.FridayThe13th.Components.Enum.ChestType;
import com.AustinPilz.FridayThe13th.Components.Enum.EscapePointType;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.Components.Perk.F13Perk;
import com.AustinPilz.FridayThe13th.Components.Perk.F13PerkManager;
import com.AustinPilz.FridayThe13th.Components.Vehicle.F13Boat;
import com.AustinPilz.FridayThe13th.Components.Vehicle.F13Car;
import com.AustinPilz.FridayThe13th.Components.Vehicle.F13Vehicle;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaAlreadyExistsException;
import com.AustinPilz.FridayThe13th.Exceptions.Arena.ArenaDoesNotExistException;
import com.AustinPilz.FridayThe13th.Exceptions.SaveToDatabaseException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.F13ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
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
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_arenas\" (\"Name\" VARCHAR PRIMARY KEY NOT NULL, \"B1X\" DOUBLE, \"B1Y\" DOUBLE, \"B1Z\" DOUBLE, \"B2X\" DOUBLE, \"B2Y\" DOUBLE, \"B2Z\" DOUBLE, \"ArenaWorld\" VARCHAR, \"WaitX\" DOUBLE, \"WaitY\" DOUBLE, \"WaitZ\" DOUBLE, \"WaitWorld\" VARCHAR, \"ReturnX\" DOUBLE, \"ReturnY\" DOUBLE, \"ReturnZ\" DOUBLE, \"ReturnWorld\" VARCHAR, \"JasonX\" DOUBLE, \"JasonY\" DOUBLE, \"JasonZ\" DOUBLE,  \"MinutesPerCounselor\" DOUBLE DEFAULT 2, \"SecondsWaitingRoom\" DOUBLE DEFAULT 60, \"LifeTimeGames\" INTEGER DEFAULT 0)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_spawn_points\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, \"Arena\" VARCHAR)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_chests\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, \"Arena\" VARCHAR, \"Type\" VARCHAR)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_vehicles\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, \"Arena\" VARCHAR, \"Type\" VARCHAR)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_escape_points\" (\"X1\" DOUBLE, \"Y1\" DOUBLE, \"Z1\" DOUBLE, \"X2\" DOUBLE, \"Y2\" DOUBLE, \"Z2\" DOUBLE, \"World\" VARCHAR, \"Arena\" VARCHAR, \"Type\" VARCHAR)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_signs\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, \"Arena\" VARCHAR, \"Type\" VARCHAR)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_phones\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, \"Arena\" VARCHAR)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_players\" (\"UUID\" VARCHAR PRIMARY KEY NOT NULL, \"SpawnPreference\" VARCHAR, \"XP\" INTEGER DEFAULT 0, \"JasonProfile\" VARCHAR, \"CounselorProfile\" VARCHAR, \"CP\" INTEGER DEFAULT 0)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"f13_players_purchase\" (\"UUID\" VARCHAR NOT NULL, \"Item\" VARCHAR NOT NULL, \"Name\" VARCHAR NOT NULL)");

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
     * Updates DB to latest version
     */
    public void updateDB() {
        Update("SELECT SecondsWaitingRoom FROM f13_arenas", "ALTER TABLE f13_arenas ADD SecondsWaitingRoom DOUBLE DEFAULT 60");
        Update("SELECT MinutesPerCounselor FROM f13_arenas", "ALTER TABLE f13_arenas ADD MinutesPerCounselor DOUBLE DEFAULT 2");
        Update("SELECT XP FROM f13_players", "ALTER TABLE f13_players ADD XP INTEGER DEFAULT 0");
        Update("SELECT CP FROM f13_players", "ALTER TABLE f13_players ADD CP INTEGER DEFAULT 0");
        Update("SELECT JasonProfile FROM f13_players", "ALTER TABLE f13_players ADD JasonProfile VARCHAR");
        Update("SELECT CounselorProfile FROM f13_players", "ALTER TABLE f13_players ADD CounselorProfile VARCHAR");
        Update("SELECT LifeTimeGames FROM f13_arenas", "ALTER TABLE f13_arenas ADD LifeTimeGames INTEGER DEFAULT 0");
    }

    /**
     * Performs update to database if check query fails
     *
     * @param check
     * @param sqlite
     */
    private void Update(String check, String sqlite) {
        try {
            Statement statement = getConnection().createStatement();
            statement.executeQuery(check);
            statement.close();
        } catch (SQLException ex) {
            try {
                String[] query;

                query = sqlite.split(";");
                Connection conn = getConnection();
                Statement st = conn.createStatement();
                for (String q : query)
                    st.executeUpdate(q);
                conn.commit();
                st.close();
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Database updated to new version!");
            } catch (SQLException e) {
                FridayThe13th.log.log(Level.SEVERE, FridayThe13th.consolePrefix + "Error while attempting to update database to new version!");
                e.printStackTrace();
            }
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
            ps = conn.prepareStatement("SELECT `Name`, `B1X`, `B1Y`, `B1Z`, `B2X`, `B2Y`, `B2Z`, `ArenaWorld`, `WaitX`, `WaitY`, `WaitZ`, `WaitWorld`, `ReturnX`, `ReturnY`, `ReturnZ`, `ReturnWorld`, `JasonX`, `JasonY`, `JasonZ`, `MinutesPerCounselor`, `SecondsWaitingRoom`, `LifeTimeGames` FROM `f13_arenas`");
            result = ps.executeQuery();

            int count = 0;
            while (result.next())
            {

                Location boundary1 = new Location(Bukkit.getWorld(result.getString("ArenaWorld")), result.getDouble("B1X"),result.getDouble("B1Y"),result.getDouble("B1Z"));
                Location boundary2 = new Location(Bukkit.getWorld(result.getString("ArenaWorld")), result.getDouble("B2X"),result.getDouble("B2Y"),result.getDouble("B2Z"));
                Location waitLoc = new Location(Bukkit.getWorld(result.getString("WaitWorld")), result.getDouble("WaitX"),result.getDouble("WaitY"),result.getDouble("WaitZ"));
                Location returnLoc = new Location(Bukkit.getWorld(result.getString("ReturnWorld")), result.getDouble("ReturnX"),result.getDouble("ReturnY"),result.getDouble("ReturnZ"));
                Location jasonLoc = new Location(Bukkit.getWorld(result.getString("ArenaWorld")), result.getDouble("JasonX"),result.getDouble("JasonY"),result.getDouble("JasonZ"));

                Arena arena = new Arena(result.getString("Name"), boundary1, boundary2, waitLoc, returnLoc, jasonLoc, result.getDouble("MinutesPerCounselor"), (int) result.getDouble("SecondsWaitingRoom"), result.getInt("LifeTimeGames"));

                try
                {
                    FridayThe13th.arenaController.addArena(arena);
                }
                catch (ArenaAlreadyExistsException exception)
                {
                    FridayThe13th.log.log(Level.SEVERE, FridayThe13th.consolePrefix + "Attempted to load arena (" + arena.getName() + ") from database that was already in controller memory");
                }

                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Arena " + arena.getName() + " loaded successfully.");
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
    public void storeArena(Arena arena) throws SaveToDatabaseException {
        try {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_arenas (`Name`, `B1X`, `B1Y`, `B1Z`, `B2X`, `B2Y`, `B2Z`, `ArenaWorld`, `WaitX`, `WaitY`, `WaitZ`, `WaitWorld`, `ReturnX`, `ReturnY`, `ReturnZ`, `ReturnWorld`, `JasonX`, `JasonY`, `JasonZ`, `LifeTimeGames`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, arena.getName() + "");
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
            preparedStatement.setInt(20, arena.getNumLifetimeGames());

            preparedStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to save new arena into database: " + e.getMessage());
            throw new SaveToDatabaseException();
        }
    }

    /**
     * Updates arena in the database
     *
     * @param arena
     */
    public void updateArena(Arena arena) {
        try {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "UPDATE `f13_arenas` SET `MinutesPerCounselor` = ?,  `SecondsWaitingRoom` = ?, `LifeTimeGames` = ? WHERE `Name` = ?";
            //updateInDatabase
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDouble(1, arena.getMinutesPerCounselor());
            preparedStatement.setDouble(2, arena.getSecondsWaitingRoom());
            preparedStatement.setInt(3, arena.getNumLifetimeGames());
            preparedStatement.setString(4, arena.getName());
            preparedStatement.executeUpdate();
            connection.commit();
            conn.commit();

        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to update arena in DB: " + e.getMessage());
        }
    }

    /**
     * Removes an arena from the database
     * @param arenaName
     */
    public void deleteArena(String arenaName) {
        try {
            Connection conn = InputOutput.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM f13_arenas WHERE Name = ?");
            ps.setString(1, arenaName);
            ps.executeUpdate();
            conn.commit();
            ps.close();

        } catch (SQLException e) {
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
                if (Bukkit.getWorld(result.getString("World")) != null) {
                    Location spawnLocation = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"));

                    try {
                        Arena arena = FridayThe13th.arenaController.getArena(result.getString("Arena"));
                        arena.getLocationManager().addStartingPoint(spawnLocation);
                        count++;
                    } catch (ArenaDoesNotExistException exception) {
                        deleteSpawnPoint(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"), result.getString("Arena"));
                        removed++;
                    }
                } else {
                    //The world no longer exists
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

            preparedStatement.setString(1, arena.getName() + "");
            preparedStatement.setDouble(2, location.getX());
            preparedStatement.setDouble(3, location.getY());
            preparedStatement.setDouble(4, location.getZ());
            preparedStatement.setString(5, location.getWorld().getName()+"");


            preparedStatement.executeUpdate();
            conn.commit();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to save new spawn point in arena " + arena.getName() + " into database: " + e.getMessage());
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
                if (Bukkit.getWorld(result.getString("World")) != null) {
                    Location chestLocation = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"));

                    if (chestLocation.getBlock().getType().equals(Material.CHEST)) {
                        try {
                            //Determine chest type
                            ChestType type = ChestType.Item; //default

                            if (result.getString("Type").equals("Item")) {
                                type = ChestType.Item;
                            } else if (result.getString("Type").equals("Weapon")) {
                                type = ChestType.Weapon;
                            }


                            Arena arena = FridayThe13th.arenaController.getArena(result.getString("Arena"));
                            arena.getObjectManager().addChest(new ArenaChest(arena, chestLocation, type));
                            count++;
                        } catch (ArenaDoesNotExistException exception) {
                            deleteChest(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"));
                            removed++;
                        }
                    } else {
                        //This location is no longer a chest, so remove it
                        deleteChest(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"));
                        removed++;
                    }
                } else {
                    //The world was deleted
                    deleteChest(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"));
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
    public void storeChest(ArenaChest chest) throws SaveToDatabaseException
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
            preparedStatement.setString(5, chest.getArena().getName());
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
    public void storeSign(Sign sign, Arena arena) throws SaveToDatabaseException
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
            preparedStatement.setString(5, arena.getName());
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
                if (Bukkit.getWorld(result.getString("World")) != null) {
                    Location signLocation = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"));

                    if (signLocation.getBlock().getType().equals(Material.SIGN_POST) || signLocation.getBlock().getType().equals(Material.WALL_SIGN)) {
                        try {
                            Arena arena = FridayThe13th.arenaController.getArena(result.getString("Arena"));
                            arena.getSignManager().addJoinSign((Sign) signLocation.getBlock().getState());
                            count++;
                        } catch (ArenaDoesNotExistException exception) {
                            FridayThe13th.log.log(Level.SEVERE, FridayThe13th.consolePrefix + "Attempted to load sign in arena (" + result.getString("Arena") + "), arena does not exist in memory.");
                            deleteSign(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"));
                            removed++;
                        }
                    } else {
                        //This location is no longer a chest, so remove it
                        deleteSign(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"));
                        removed++;
                    }
                } else {
                    //The world was deleted
                    deleteSign(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"));
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

    /**
     * Stores phone to the database
     * @param phone
     * @throws SaveToDatabaseException
     */
    public void storePhone(ArenaPhone phone) throws SaveToDatabaseException
    {
        try
        {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_phones (`X`, `Y`, `Z`, `World`, `Arena`) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);


            preparedStatement.setDouble(1, phone.getLocation().getX());
            preparedStatement.setDouble(2, phone.getLocation().getY());
            preparedStatement.setDouble(3, phone.getLocation().getZ());
            preparedStatement.setString(4, phone.getLocation().getWorld().getName());
            preparedStatement.setString(5, phone.getArena().getName());

            preparedStatement.executeUpdate();
            conn.commit();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to store a phone to the database: " + e.getMessage());
            throw new SaveToDatabaseException();
        }
    }

    /**
     * Removes a phone from the database
     * @param X
     * @param Y
     * @param Z
     * @param world
     */
    public void deletePhone(double X, double Y, double Z, String world)
    {
        try
        {
            Connection conn = InputOutput.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM f13_phones WHERE World = ? AND X = ? AND Y = ? AND Z = ?");
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
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to remove a phone from the database: " + e.getMessage());
        }
    }

    /**
     * Loads phones from database into arena memory
     */
    public void loadPhones()
    {
        try
        {
            Connection conn;
            PreparedStatement ps = null;
            ResultSet result = null;
            conn = getConnection();
            ps = conn.prepareStatement("SELECT `World`, `X`, `Y`, `Z`, `Arena` FROM `f13_phones`");
            result = ps.executeQuery();

            int count = 0;
            int removed = 0;
            while (result.next())
            {
                if (Bukkit.getWorld(result.getString("World")) != null) {
                    Location location = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"));

                    if (location != null && location.getBlock() != null && location.getBlock().getType().equals(Material.TRIPWIRE_HOOK)) {
                        try {
                            Arena arena = FridayThe13th.arenaController.getArena(result.getString("Arena"));
                            arena.getObjectManager().getPhoneManager().addPhone(new ArenaPhone(arena, location));
                            count++;
                        } catch (ArenaDoesNotExistException exception) {
                            deletePhone(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"));
                            removed++;
                        }
                    } else {
                        //This location is no longer a chest, so remove it
                        deletePhone(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"));
                        removed++;
                    }
                } else {
                    //The world no longer exists, so remove the phone
                    deletePhone(result.getDouble("X"),result.getDouble("Y"),result.getDouble("Z"), result.getString("World"));
                    removed++;
                }
            }

            if (count > 0)
            {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Loaded " + count + " phone(s).");
            }

            if (removed > 0)
            {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Removed " + removed + " phone(s).");
            }

            conn.commit();
            ps.close();
        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to load signs from database: " + e.getMessage());
        }
    }

    /**
     * Loads player from database into controller memory
     */
    public void loadPlayer(String UUID) {
        try {
            Connection conn;
            PreparedStatement ps = null;
            ResultSet result = null;
            conn = getConnection();
            ps = conn.prepareStatement("SELECT `SpawnPreference`, `XP`, `JasonProfile`, `CounselorProfile`, `CP` FROM `f13_players` WHERE `UUID` = ?");
            ps.setString(1, UUID);
            result = ps.executeQuery();

            while (result.next()) {
                F13Player player = new F13Player(UUID, result.getString("JasonProfile"), result.getString("CounselorProfile"), result.getInt("XP"), result.getInt("CP"));
                FridayThe13th.playerController.addPlayer(player);

                if (result.getString("SpawnPreference").equals("J")) {
                    player.setSpawnPreferenceJason();
                } else if (result.getString("SpawnPreference").equals("C")) {
                    player.setSpawnPreferenceCounselor();
                }
            }

            conn.commit();
            ps.close();
        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to load F13 player from database: " + e.getMessage());
        }
    }

    /**
     * Stores phone to the database
     *
     * @param player
     * @throws SaveToDatabaseException
     */
    public void storePlayer(F13Player player) throws SaveToDatabaseException {
        try {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_players (`UUID`, `SpawnPreference`) VALUES (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);


            preparedStatement.setString(1, player.getPlayerUUID());

            //Spawn Preference
            if (player.isSpawnPreferenceJason()) {
                preparedStatement.setString(2, "J");
            } else if (player.isSpawnPreferenceCounselor()) {
                preparedStatement.setString(2, "C");
            } else {
                preparedStatement.setString(2, "None");
            }

            preparedStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, e.getMessage());
            throw new SaveToDatabaseException();
        }
    }

    /**
     * Updates player in the database
     *
     * @param player
     */
    public void updatePlayer(F13Player player) {
        try {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "UPDATE `f13_players` SET `SpawnPreference` = ?, `XP` = ?, `JasonProfile` = ?, `CounselorProfile` = ?, `CP` = ? WHERE `UUID` = ?";
            //updateInDatabase
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            if (player.isSpawnPreferenceJason()) {
                preparedStatement.setString(1, "J");
            } else if (player.isSpawnPreferenceCounselor()) {
                preparedStatement.setString(1, "C");
            } else {
                preparedStatement.setString(1, "None");
            }

            preparedStatement.setInt(2, player.getXP());
            preparedStatement.setString(3, player.getJasonProfile().getInternalIdentifier());
            preparedStatement.setString(4, player.getCounselorProfile().getInternalIdentifier());
            preparedStatement.setInt(5, player.getCP());
            preparedStatement.setString(6, player.getPlayerUUID());
            preparedStatement.executeUpdate();
            connection.commit();

            conn.commit();


        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to update player in DB: " + e.getMessage());
        }
    }

    public void loadPlayerPurchases(F13Player player)
    {
        try {
            Connection conn;
            PreparedStatement ps = null;
            ResultSet result = null;
            conn = getConnection();
            ps = conn.prepareStatement("SELECT `Item`, `Name` FROM `f13_players_purchase` WHERE `UUID` = ?");
            ps.setString(1, player.getPlayerUUID());
            result = ps.executeQuery();

            while (result.next())
            {
                if (result.getString("Item").equalsIgnoreCase("Perk"))
                {
                    if (F13PerkManager.getPerkByInternalIdentifier(result.getString("Name")) != null)
                    {
                        player.addPurchasedPerk(F13PerkManager.getPerkByInternalIdentifier(result.getString("Name")), false);
                    }
                }
                else if (result.getString("Item").equalsIgnoreCase("Profile"))
                {
                    if (F13ProfileManager.getCounselorProfileByInternalIdentifier(result.getString("Name")) != null)
                    {
                        player.addPurchasedCounselorProfile(F13ProfileManager.getCounselorProfileByInternalIdentifier(result.getString("Name")), false);
                    }
                    else if (F13ProfileManager.getJasonProfileByInternalIdentifier(result.getString("Name")) != null)
                    {
                        player.addPurchasedJasonProfile(F13ProfileManager.getJasonProfileByInternalIdentifier(result.getString("Name")), false);
                    }
                }
            }

            conn.commit();
            ps.close();
        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to load F13 player purchases from database.");
        }
    }

    /**
     * Stores player perk into the database
     * @param player
     * @param perk
     * @throws SaveToDatabaseException
     */
    public void storePurchasedPlayerPerk(F13Player player, F13Perk perk) throws SaveToDatabaseException
    {
        try {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_players_purchase (`UUID`, `Item`, `Name`) VALUES (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);


            preparedStatement.setString(1, player.getPlayerUUID());
            preparedStatement.setString(2, "Perk");
            preparedStatement.setString(3, perk.getInternalIdentifier());

            preparedStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new SaveToDatabaseException();
        }
    }

    /**
     * Removes player perk from the database
     * @param player
     * @param perk
     */
    public void removePurchasedPlayerPerk(F13Player player, F13Perk perk)
    {
        try
        {
            Connection conn = InputOutput.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM f13_players_purchase WHERE UUID = ? AND Item = ? AND Name = ?");
            ps.setString(1, player.getPlayerUUID());
            ps.setString(2, "Perk");
            ps.setString(3, perk.getInternalIdentifier());
            ps.executeUpdate();
            conn.commit();
            ps.close();

        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to remove a player perk from the database for player " + player.getPlayerUUID());
        }
    }

    /**
     * Stores player perk into the database
     * @param player
     * @param internalIdentifier
     * @throws SaveToDatabaseException
     */
    public void storePurchasedPlayerProfile(F13Player player, String internalIdentifier) throws SaveToDatabaseException
    {
        try {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_players_purchase (`UUID`, `Item`, `Name`) VALUES (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);


            preparedStatement.setString(1, player.getPlayerUUID());
            preparedStatement.setString(2, "Profile");
            preparedStatement.setString(3, internalIdentifier);

            preparedStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new SaveToDatabaseException();
        }
    }

    /**
     * Removes purchased profile from the database
     * @param player
     * @param internalIdentifier
     */
    public void removePurchasedPlayerProfile(F13Player player, String internalIdentifier)
    {
        try
        {
            Connection conn = InputOutput.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM f13_players_purchase WHERE UUID = ? AND Item = ? AND Name = ?");
            ps.setString(1, player.getPlayerUUID());
            ps.setString(2, "Profile");
            ps.setString(3, internalIdentifier);
            ps.executeUpdate();
            conn.commit();
            ps.close();

        }
        catch (SQLException e)
        {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to remove a purchased player profile from the database for player " + player.getPlayerUUID());
        }
    }

    /**
     * Loads escape points from database into arena memory
     */
    public void loadEscapePoints() {
        try {
            Connection conn;
            PreparedStatement ps = null;
            ResultSet result = null;
            conn = getConnection();
            ps = conn.prepareStatement("SELECT `World`, `X1`, `Y1`, `Z1`,`X2`, `Y2`, `Z2`, `Arena`, `Type` FROM `f13_escape_points`");
            result = ps.executeQuery();

            int count = 0;
            int removed = 0;
            while (result.next()) {
                if (Bukkit.getWorld(result.getString("World")) != null) {
                    Location boundary1 = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X1"), result.getDouble("Y1"), result.getDouble("Z1"));
                    Location boundary2 = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X2"), result.getDouble("Y2"), result.getDouble("Z2"));

                    try {
                        //Determine chest type
                        EscapePointType type = EscapePointType.Land; //default

                        if (result.getString("Type").equals("Land")) {
                            type = EscapePointType.Land;
                        } else if (result.getString("Type").equals("Water")) {
                            type = EscapePointType.Water;
                        }

                        Arena arena = FridayThe13th.arenaController.getArena(result.getString("Arena"));
                        arena.getLocationManager().getEscapePointManager().addEscapePoint(new EscapePoint(arena, type, boundary1, boundary2));
                        count++;
                    } catch (ArenaDoesNotExistException exception) {
                        deleteEscapePoint(result.getDouble("X1"), result.getDouble("Y1"), result.getDouble("Z1"), result.getDouble("X2"), result.getDouble("Y2"), result.getDouble("Z2"), result.getString("Type"), result.getString("World"));
                        removed++;
                    }
                } else {
                    //The world was deleted
                    deleteEscapePoint(result.getDouble("X1"), result.getDouble("Y1"), result.getDouble("Z1"), result.getDouble("X2"), result.getDouble("Y2"), result.getDouble("Z2"), result.getString("Type"), result.getString("World"));
                    removed++;
                }
            }

            if (count > 0) {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Loaded " + count + " escape point(s).");
            }

            if (removed > 0) {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Removed " + removed + " escape point(s) for arenas that no longer exist.");
            }

            conn.commit();
            ps.close();
        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to load chests from database: " + e.getMessage());
        }
    }

    /**
     * Stores escape point to the database
     *
     * @throws SaveToDatabaseException
     */
    public void storeEscapePoint(EscapePoint escapePoint) throws SaveToDatabaseException {
        try {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_escape_points (`X1`, `Y1`, `Z1`, `X2`, `Y2`, `Z2`, `World`, `Arena`, `Type`) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);


            preparedStatement.setDouble(1, escapePoint.getBoundary1().getX());
            preparedStatement.setDouble(2, escapePoint.getBoundary1().getY());
            preparedStatement.setDouble(3, escapePoint.getBoundary1().getZ());
            preparedStatement.setDouble(4, escapePoint.getBoundary2().getX());
            preparedStatement.setDouble(5, escapePoint.getBoundary2().getY());
            preparedStatement.setDouble(6, escapePoint.getBoundary2().getZ());
            preparedStatement.setString(7, escapePoint.getBoundary2().getWorld().getName());
            preparedStatement.setString(8, escapePoint.getArena().getName());
            preparedStatement.setString(9, escapePoint.getType().getFieldDescription());

            preparedStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to store an escape point to the database: " + e.getMessage());
            throw new SaveToDatabaseException();
        }
    }

    /**
     * Removes an escape point from the database
     *
     * @param X1
     * @param Y1
     * @param Z1
     * @param X2
     * @param Y2
     * @param Z2
     * @param type
     * @param world
     */
    public void deleteEscapePoint(double X1, double Y1, double Z1, double X2, double Y2, double Z2, String type, String world) {
        try {
            Connection conn = InputOutput.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM f13_escape_points WHERE World = ? AND X1 = ? AND Y1 = ? AND Z1 = ? AND X2 = ? AND Y2 = ? AND Z2 = ? AND Type = ?");
            ps.setString(1, world);
            ps.setDouble(2, X1);
            ps.setDouble(3, Y1);
            ps.setDouble(4, Z1);
            ps.setDouble(5, X2);
            ps.setDouble(6, Y2);
            ps.setDouble(7, Z2);
            ps.setString(8, type);
            ps.executeUpdate();
            conn.commit();
            ps.close();

        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to remove an escape point from the database: " + e.getMessage());
        }
    }

    /**
     * Loads vehicles from database into arena memory
     */
    public void loadVehicles() {
        try {
            Connection conn;
            PreparedStatement ps = null;
            ResultSet result = null;
            conn = getConnection();
            ps = conn.prepareStatement("SELECT `World`, `X`, `Y`, `Z`, `Arena`, `Type` FROM `f13_vehicles`");
            result = ps.executeQuery();

            int carCount = 0;
            int boatCount = 0;
            int removed = 0;
            while (result.next()) {
                if (Bukkit.getWorld(result.getString("World")) != null) {
                    Location spawnLocation = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"));


                    try {
                        Arena arena = FridayThe13th.arenaController.getArena(result.getString("Arena"));

                        if (result.getString("Type").equals("Boat")) {
                            arena.getObjectManager().getVehicleManager().addBoat(new F13Boat(arena, spawnLocation));
                            boatCount++;
                        } else if (result.getString("Type").equals("Car")) {
                            arena.getObjectManager().getVehicleManager().addCar(new F13Car(arena, spawnLocation));
                            carCount++;
                        }
                    } catch (ArenaDoesNotExistException exception) {
                        deleteVehicle(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"), result.getString("Arena"));
                        removed++;
                    }

                } else {
                    //The world was deleted
                    deleteVehicle(result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"), result.getString("World"), result.getString("Arena"));
                    removed++;
                }
            }

            if (carCount > 0) {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Loaded " + carCount + " car(s).");
            }

            if (boatCount > 0) {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Loaded " + boatCount + " boat(s).");
            }

            if (removed > 0) {
                FridayThe13th.log.log(Level.INFO, FridayThe13th.consolePrefix + "Removed " + removed + " vehicles(s) for arenas that no longer exist.");
            }

            conn.commit();
            ps.close();
        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered a SQL exception while attempting to load vehicles from database: " + e.getMessage());
        }
    }

    /**
     * Stores vehicle to the database
     *
     * @param vehicle
     * @throws SaveToDatabaseException
     */
    public void storeVehicle(F13Vehicle vehicle) throws SaveToDatabaseException {
        try {
            String sql;
            Connection conn = InputOutput.getConnection();

            sql = "INSERT INTO f13_vehicles (`X`, `Y`, `Z`, `World`, `Arena`, `Type`) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);


            preparedStatement.setDouble(1, vehicle.getSpawnLocation().getX());
            preparedStatement.setDouble(2, vehicle.getSpawnLocation().getY());
            preparedStatement.setDouble(3, vehicle.getSpawnLocation().getZ());
            preparedStatement.setString(4, vehicle.getSpawnLocation().getWorld().getName());
            preparedStatement.setString(5, vehicle.getArena().getName());
            preparedStatement.setString(6, vehicle.getType().name());

            preparedStatement.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to store a vehicle to the database: " + e.getMessage());
            throw new SaveToDatabaseException();
        }
    }

    /**
     * Removes a vehicle from the database
     *
     * @param X
     * @param Y
     * @param Z
     * @param world
     * @param arena
     */
    public void deleteVehicle(double X, double Y, double Z, String world, String arena) {
        try {
            Connection conn = InputOutput.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM f13_vehicles WHERE World = ? AND X = ? AND Y = ? AND Z = ? AND Arena = ?");
            ps.setString(1, world);
            ps.setDouble(2, X);
            ps.setDouble(3, Y);
            ps.setDouble(4, Z);
            ps.setString(5, arena);

            ps.executeUpdate();
            conn.commit();
            ps.close();

        } catch (SQLException e) {
            FridayThe13th.log.log(Level.WARNING, FridayThe13th.consolePrefix + "Encountered an error while attempting to remove a vehicle from the database: " + e.getMessage());
        }
    }

}