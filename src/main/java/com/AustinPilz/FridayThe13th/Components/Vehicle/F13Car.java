package com.AustinPilz.FridayThe13th.Components.Vehicle;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Utilities.InventoryActions;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class F13Car extends F13Vehicle {

    //Per game
    private double gasRepairProgress;
    private double batteryRepairProgress;
    private double keyRepairProgress;

    private int repairRequiredMax;

    private Hologram hologram;
    private Minecart minecart;

    public F13Car(Arena arena, Location spawnLocation) {
        super(arena, spawnLocation, VehicleType.Car);

        repairRequiredMax = 100;
        gasRepairProgress = 0;
        batteryRepairProgress = 0;
        keyRepairProgress = 0;
    }

    /**
     * @return Minecart
     */
    public Minecart getMinecart() {
        return minecart;
    }

    public void reset() {
        gasRepairProgress = 0;
        batteryRepairProgress = 0;
        keyRepairProgress = 0;

        if (hologram != null) {
            hologram.delete();
        }

        if (minecart != null) {
            getArena().getObjectManager().getVehicleManager().removeSpawnedCar(this);
            minecart.setDamage(100);
            minecart.remove();
            minecart = null;
        }
    }

    /**
     * Prepares the car at the beginning of the match
     */
    public void prepare() {
        //Spawn the thing
        reset();
        createHologram();
    }

    /**
     * Spawns the Minecart "Car"
     */
    private void spawn() {
        minecart = getSpawnLocation().getWorld().spawn(getSpawnLocation(), Minecart.class);
        minecart.setSlowWhenEmpty(true);
        minecart.setMaxSpeed(0D); //Can't move to begin with, changes when player mounts it
        getArena().getObjectManager().getVehicleManager().registerSpawnedCar(this);
    }

    /**
     * Performs Minecart actions when the Minecart escapes
     */
    public void escaped() {
        minecart.remove();
    }

    /**
     * @return If the car is fully repaired and ready to be driven
     */
    public boolean isFullyRepaired() {
        return (hasBattery() && hasGas() && hasKeys());
    }

    private boolean hasBattery() {
        return batteryRepairProgress >= repairRequiredMax;
    }

    private boolean hasGas() {
        return gasRepairProgress >= repairRequiredMax;
    }

    private boolean hasKeys() {
        return keyRepairProgress >= repairRequiredMax;
    }

    public void repairAttempt(Player player) {
        if (!isFullyRepaired()) {
            if (!hasBattery() && player.getInventory().contains(Material.REDSTONE_BLOCK)) {
                batteryRepairProgress += FridayThe13th.playerController.getPlayer(player).getCounselorProfile().getIntelligence().getRegenerationRate();

                if (hasBattery()) {
                    InventoryActions.remove(player.getInventory(), Material.REDSTONE_BLOCK, 1, (short) -1);
                }
            } else if (!hasGas() && player.getInventory().contains(Material.BLAZE_POWDER)) {
                gasRepairProgress += FridayThe13th.playerController.getPlayer(player).getCounselorProfile().getIntelligence().getRegenerationRate();

                if (hasGas()) {
                    InventoryActions.remove(player.getInventory(), Material.BLAZE_POWDER, 1, (short) -1);
                }
            } else if (!hasKeys() && player.getInventory().contains(Material.STONE_BUTTON)) {
                keyRepairProgress += FridayThe13th.playerController.getPlayer(player).getCounselorProfile().getIntelligence().getRegenerationRate();

                if (hasKeys()) {
                    InventoryActions.remove(player.getInventory(), Material.STONE_BUTTON, 1, (short) -1);
                }
            }

            //Check to see if fully repaired
            if (isFullyRepaired()) {
                Firework f = getSpawnLocation().getWorld().spawn(getSpawnLocation().getWorld().getHighestBlockAt(getSpawnLocation()).getLocation(), Firework.class);
                FireworkMeta fm = f.getFireworkMeta();
                fm.addEffect(FireworkEffect.builder()
                        .flicker(true)
                        .trail(true)
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withColor(Color.ORANGE)
                        .build());
                fm.setPower(1);
                f.setFireworkMeta(fm);

                spawn(); //Spawn the minecart
            }
        }

        updateHologram();
    }

    /**
     * Create's the phone hologram
     */
    private void createHologram() {

        hologram = HologramsAPI.createHologram(FridayThe13th.instance, getSpawnLocation().getBlock().getRelative(BlockFace.UP).getLocation().add(0, 1, 0));
        updateHologram();
    }

    /**
     * Updates the display of the phone's hologram
     */
    private void updateHologram() {
        hologram.clearLines();

        if (!isFullyRepaired()) {
            if (hasBattery()) {
                hologram.appendTextLine(ChatColor.WHITE + "Battery");
            } else if (!hasBattery() && batteryRepairProgress > 0) {
                hologram.appendTextLine(ChatColor.RED + "Battery - " + getDisplayPercentageString(batteryRepairProgress, repairRequiredMax));
            } else {
                hologram.appendTextLine(ChatColor.RED + "Battery");
            }

            if (hasGas()) {
                hologram.appendTextLine(ChatColor.WHITE + "Gas");
            } else if (!hasGas() && gasRepairProgress > 0) {
                hologram.appendTextLine(ChatColor.RED + "Gas - " + getDisplayPercentageString(gasRepairProgress, repairRequiredMax));
            } else {
                hologram.appendTextLine(ChatColor.RED + "Gas");
            }

            if (hasKeys()) {
                hologram.appendTextLine(ChatColor.WHITE + "Keys");
            } else if (!hasKeys() && keyRepairProgress > 0) {
                hologram.appendTextLine(ChatColor.RED + "Keys - " + getDisplayPercentageString(keyRepairProgress, repairRequiredMax));
            } else {
                hologram.appendTextLine(ChatColor.RED + "Keys");
            }

            //Display the rotating Minecart
            ItemLine itemLine = hologram.appendItemLine(new ItemStack(Material.MINECART));
            itemLine.setTouchHandler(new TouchHandler() {

                @Override
                public void onTouch(Player player) {

                    repairAttempt(player);

                }
            });
        } else {
            hologram.clearLines();
        }
    }
}
