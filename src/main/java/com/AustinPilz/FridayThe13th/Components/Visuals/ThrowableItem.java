package com.AustinPilz.FridayThe13th.Components.Visuals;

import com.AustinPilz.FridayThe13th.Exceptions.Player.PlayerNotPlayingException;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.UUID;

public class ThrowableItem {

    private static final double DAMAGE = 2;
    private static final double ROTATION_ANGLE_AMOUNT = 0.6;
    private static final double SPEED = 1.3;
    private static final double DIRECTIONAL_VECTOR_AMOUNT = 1.3;
    private static final double HIT_RADIUS = 1.2;
    private static final double DOWN_FALL = .1; //.03

    private final UUID player;
    private Player p;
    private ItemStack hand;

    private ArmorStand armorStand;
    private Vector direction;
    private BukkitTask task;

    public ThrowableItem(Player player) {
        this.player = player.getUniqueId();
        this.p = player;
    }

    public void display() {
        Player thrower = Bukkit.getPlayer(this.player);
        this.hand = thrower.getInventory().getItemInMainHand();
        if (this.hand.getType() == Material.AIR) {
            return;
        }
        Location location = thrower.getLocation();

        this.armorStand = (ArmorStand) thrower.getWorld().spawnEntity(thrower.getLocation().clone().add(0, 1.3, 0), EntityType.ARMOR_STAND);
        this.armorStand.setVisible(false);
        this.armorStand.setSmall(true);
        this.armorStand.setItemInHand(hand);
        thrower.getInventory().setItemInMainHand(this.hand.getAmount() == 1 ? null : this.recalculateAmount(this.hand));

        direction = location.add(location.getDirection().multiply(ThrowableItem.SPEED)).toVector().subtract(location.toVector()).normalize().multiply(ThrowableItem.DIRECTIONAL_VECTOR_AMOUNT);
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                armorStand.getNearbyEntities(ThrowableItem.HIT_RADIUS, ThrowableItem.HIT_RADIUS, ThrowableItem.HIT_RADIUS).stream().filter(Damageable.class::isInstance).filter(entity -> !entity.equals(thrower)).filter(entity -> !entity.equals(armorStand)).map(Damageable.class::cast).forEach(damageable -> {
                    damageable.damage(ThrowableItem.DAMAGE);
                    armorStand.remove();
                    cancel();
                    regive();
                });
                double armPosition = armorStand.getRightArmPose().getX();
                EulerAngle eulerAngle = new EulerAngle(armPosition + ThrowableItem.ROTATION_ANGLE_AMOUNT, 0.0D, 0.0D);
                armorStand.setRightArmPose(eulerAngle);

                armorStand.setVelocity(thrower.getLocation().getDirection().multiply(ThrowableItem.SPEED).subtract(new Vector(0, ThrowableItem.DOWN_FALL, 0))); //define this outside of the runnable for a locked on direction. currently will follow where the player looks.
                if (!armorStand.isOnGround()) {
                    return;
                }
                armorStand.remove();
                cancel();
                regive();
            }
        }.runTaskTimer(FridayThe13th.instance, 0, 3);
    }

    private void regive() {
        Player thrower = Bukkit.getPlayer(this.player);

        try
        {
            if (FridayThe13th.arenaController.getPlayerArena(player.toString()).getGameManager().isGameInProgress())
            {
                thrower.getInventory().addItem(this.hand);
            }
        }
        catch (PlayerNotPlayingException exception)
        {
            //
        }
    }

    private ItemStack recalculateAmount(ItemStack itemStack) {
        ItemStack item = itemStack.clone();
        item.setAmount(item.getAmount() - 1);
        return item;
    }
}
