package com.AustinPilz.FridayThe13th.Manager.Arena;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.Vehicle.F13Boat;
import com.AustinPilz.FridayThe13th.Components.Vehicle.F13Car;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Minecart;

import java.util.HashMap;
import java.util.HashSet;

public class VehicleManager {

    private Arena arena;
    private HashSet<F13Car> cars;
    private HashSet<F13Boat> boats;
    private HashMap<Minecart, F13Car> spawnedCars;
    private HashMap<Boat, F13Boat> spawnedBoats;


    VehicleManager(Arena arena) {
        this.arena = arena;
        this.cars = new HashSet<>();
        this.spawnedCars = new HashMap<>();
        this.boats = new HashSet<>();
        this.spawnedBoats = new HashMap<>();
    }

    /**
     * Adds car to arena
     *
     * @param car
     */
    public void addCar(F13Car car) {
        cars.add(car);
    }

    /**
     * Register a spawned car
     *
     * @param car
     */
    public void registerSpawnedCar(F13Car car) {
        spawnedCars.put(car.getMinecart(), car);
    }

    /**
     * Deregisters spawned car
     * @param car
     */
    public void removeSpawnedCar(F13Car car) {
        spawnedCars.remove(car.getMinecart());
    }

    /**
     * @param cart
     * @return If the Minecart is a spawned car
     */
    public boolean isRegisteredCar(Minecart cart) {
        return spawnedCars.containsKey(cart);
    }

    /**
     * Returns registered car from Minecart
     *
     * @param cart
     * @return F13Car
     */
    public F13Car getRegisteredCar(Minecart cart) {
        return spawnedCars.get(cart);
    }

    /**
     * @return Number of cars
     */
    public int getNumCars() {
        return cars.size();
    }

    /**
     * Adds new boat to arena
     *
     * @param boat
     */
    public void addBoat(F13Boat boat) {
        boats.add(boat);
    }

    /**
     * Register a spawned car
     *
     * @param boat F13Boat
     */
    public void registerSpawnedBoat(F13Boat boat) {
        spawnedBoats.put(boat.getBoat(), boat);
    }

    /**
     * Deregisters spawned boat
     *
     * @param boat
     */
    public void removeSpawnedBoat(F13Boat boat) {
        spawnedBoats.remove(boat.getBoat());
    }

    /**
     * Returns registered car from Minecart
     *
     * @param boat
     * @return F13Boat
     */
    public F13Boat getRegisteredBoat(Boat boat) {
        return spawnedBoats.get(boat);
    }

    /**
     * @return Number of boats
     */
    public int getNumBoats() {
        return boats.size();
    }

    /**
     * Prepares vehicles for a game
     */
    public void prepareVehicles() {
        if (arena.getLocationManager().getEscapePointManager().getNumberOfLandEscapePoints() >= getNumCars()) {
            if (arena.getObjectManager().getNumChestsItems() >= getMinRequiredChests()) {
                for (F13Car car : cars) {
                    car.prepare();
                }
            }
        }

        if (arena.getLocationManager().getEscapePointManager().getNumberOfWaterEscapePoints() >= getNumBoats()) {
            if (arena.getObjectManager().getNumChestsItems() >= getMinRequiredChests()) {
                for (F13Boat boat : boats) {
                    boat.prepare();
                }
            }
        }
    }

    /**
     * Resets vehicles after a game
     */
    public void resetVehicles() {
        for (F13Car car : cars) {
            car.reset();
        }

        for (F13Boat boat : boats) {
            boat.reset();
        }
    }

    /**
     * Deletes all vehicles from memory and the database
     */
    public void deleteVehicles() {
        //TODO - Remove them from the DB
    }

    /**
     * Calculates the minimum number of chests required for the vehicle portion
     *
     * @return Required number of chests
     */
    public int getMinRequiredChests() {
        int total = 0;
        total += boats.size() * 2; //Per boat - 1 gas, 1 propeller
        total += cars.size() * 3; //Per car - 1 gas, 1 battery, 1 key
        return total;
    }
}
