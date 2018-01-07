package com.AustinPilz.FridayThe13th.Components.Vehicle;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Utilities.InventoryActions;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class F13Boat extends F13Vehicle {

    private double propellerRepairProgress;
    private double gasRepairProgress;

    private int repairRequiredMax;

    private Hologram hologram;
    private Boat boat;

    public F13Boat(Arena arena, Location spawnLocation) {
        super(arena, spawnLocation, VehicleType.Boat);
        propellerRepairProgress = 0;
        gasRepairProgress = 0;
        repairRequiredMax = 100;
    }

    public void reset() {
        gasRepairProgress = 0;
        propellerRepairProgress = 0;

        if (hologram != null) {
            hologram.delete();
        }

        if (boat != null) {
            getArena().getObjectManager().getVehicleManager().removeSpawnedBoat(this);
            boat.remove();
            boat = null;
        }
    }

    /**
     * @return Bukkit boat
     */
    public Boat getBoat() {
        return boat;
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
        boat = getSpawnLocation().getWorld().spawn(getSpawnLocation(), Boat.class);
        getArena().getObjectManager().getVehicleManager().registerSpawnedBoat(this);
    }

    /**
     * Performs Minecart actions when the Minecart escapes
     */
    public void escaped() {
        boat.remove();
    }

    /**
     * @return If the car is fully repaired and ready to be driven
     */
    public boolean isFullyRepaired() {
        return (hasPropeller() && hasGas());
    }

    private boolean hasPropeller() {
        return propellerRepairProgress >= repairRequiredMax;
    }

    private boolean hasGas() {
        return gasRepairProgress >= repairRequiredMax;
    }

    public void repairAttempt(Player p) {

        F13Player player = FridayThe13th.playerController.getPlayer(p);
        if (getArena().getGameManager().getPlayerManager().isCounselor(player)) {
            if (!isFullyRepaired()) {
                if (!hasPropeller() && player.getBukkitPlayer().getInventory().contains(Material.SHULKER_SHELL)) {
                    propellerRepairProgress += FridayThe13th.playerController.getPlayer(player.getBukkitPlayer()).getCounselorProfile().getIntelligence().getRegenerationRate();

                    if (hasPropeller()) {
                        InventoryActions.remove(player.getBukkitPlayer().getInventory(), Material.SHULKER_SHELL, 1, (short) -1);
                    }
                } else if (!hasGas() && player.getBukkitPlayer().getInventory().contains(Material.BLAZE_POWDER)) {
                    gasRepairProgress += FridayThe13th.playerController.getPlayer(player.getBukkitPlayer()).getCounselorProfile().getIntelligence().getRegenerationRate();

                    if (hasGas()) {
                        InventoryActions.remove(player.getBukkitPlayer().getInventory(), Material.BLAZE_POWDER, 1, (short) -1);
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
            if (hasPropeller()) {
                hologram.appendTextLine(ChatColor.WHITE + "Propeller");
            } else if (!hasPropeller() && propellerRepairProgress > 0) {
                hologram.appendTextLine(ChatColor.RED + "Propeller - " + getDisplayPercentageString(propellerRepairProgress, repairRequiredMax));
            } else {
                hologram.appendTextLine(ChatColor.RED + "Propeller");
            }

            if (hasGas()) {
                hologram.appendTextLine(ChatColor.WHITE + "Gas");
            } else if (!hasGas() && gasRepairProgress > 0) {
                hologram.appendTextLine(ChatColor.RED + "Gas - " + getDisplayPercentageString(gasRepairProgress, repairRequiredMax));
            } else {
                hologram.appendTextLine(ChatColor.RED + "Gas");
            }

            //Display the rotating Minecart
            ItemLine itemLine = hologram.appendItemLine(new ItemStack(Material.BOAT));
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
