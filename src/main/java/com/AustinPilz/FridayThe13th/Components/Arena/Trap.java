package com.AustinPilz.FridayThe13th.Components.Arena;


import com.AustinPilz.FridayThe13th.Components.Characters.Counselor;
import com.AustinPilz.FridayThe13th.Components.Enum.F13SoundEffect;
import com.AustinPilz.FridayThe13th.Components.Enum.TrapType;
import com.AustinPilz.FridayThe13th.Components.Enum.XPAward;
import com.AustinPilz.FridayThe13th.Components.F13Player;
import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Manager.Game.SoundManager;
import com.AustinPilz.FridayThe13th.Utilities.HiddenStringsUtil;
import com.AustinPilz.FridayThe13th.Utilities.InventoryActions;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Trap {
    private Arena arena;
    private Block block;
    private Material originalMaterial;
    private TrapType type;
    private Hologram hologram;

    //Activation Progress
    private boolean isActivated;
    private double activationAttempts;
    private int activationAttemptsRequired;

    public Trap(Arena a, Block b, Material o, TrapType t) {
        this.arena = a;
        this.block = b;
        this.originalMaterial = o;
        this.type = t;

        //Activation
        isActivated = false;
        activationAttempts = 0;
        activationAttemptsRequired = 40;

        //Create the hologram
        hologram = HologramsAPI.createHologram(FridayThe13th.instance, block.getRelative(BlockFace.UP).getLocation());

        //Jason traps are activated on placement
        if (getTrapType().equals(TrapType.Jason)) {
            activate();

            //Hide from all counselors
            for (Counselor counselor : arena.getGameManager().getPlayerManager().getCounselors().values()) {
                hologram.getVisibilityManager().hideTo(counselor.getF13Player().getBukkitPlayer());
            }
        } else if (getTrapType().equals(TrapType.Counselor)) {
            //Hide from Jason
            hologram.getVisibilityManager().hideTo(arena.getGameManager().getPlayerManager().getJason().getPlayer());

            //Add liens
            hologram.appendTextLine(ChatColor.GREEN + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.hologram.trapUnarmed", "Unarmed"));
            hologram.appendTextLine(ChatColor.WHITE + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.hologram.trapArmPrompt", "Interact to Activate"));
        }
    }

    /**
     * @return Trap type
     */
    public TrapType getTrapType() {
        return type;
    }

    /**
     * Interact event with trap in order to activate it
     * @param counselor Counselor attempting activation
     */
    public void activationAttempt(Counselor counselor) {
        activationAttempts += counselor.getF13Player().getCounselorProfile().getIntelligence().getRegenerationRate();

        if (getActivationProgressPercent() >= 1 && !isActivated) {
            activate();

            //Register trap activated for XP
            counselor.getXpManager().registerXPAward(XPAward.Counselor_TrapActivated);
        } else {
            hologram.clearLines();

            float percentage = ((float) activationAttempts) / activationAttemptsRequired * 100;
            percentage = Math.round(percentage);
            String strPercent = String.format("%2.0f", percentage);

            String string = "" + String.valueOf(strPercent) + "%";
            hologram.appendTextLine(ChatColor.GREEN + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.hologram.trapUnarmed", "Unarmed"));
            hologram.appendTextLine(ChatColor.WHITE + string);
        }
    }

    /**
     * @return Is the trap activated
     */
    public boolean isActivated() {
        return isActivated;
    }

    /**
     * @return Activation percentage
     */
    private double getActivationProgressPercent() {
        return activationAttempts / activationAttemptsRequired;
    }

    /**
     * Activates the trap
     */
    private void activate() {
        setActive(true);
        hologram.clearLines();
        hologram.appendTextLine(ChatColor.RED + FridayThe13th.language.get(Bukkit.getConsoleSender(), "game.hologram.trapArmed", "Armed"));
    }

    /**
     * @param value Trap activation status
     */
    private void setActive(boolean value) {
        isActivated = value;
    }

    /**
     * Removes the trap and restores the original block
     */
    public void remove() {
        hologram.delete();
        block.setType(originalMaterial);
    }


    /**
     * Takes action when the trap is stepped on
     * @param player Player that stepped on the trap
     */
    public void steppedOn(F13Player player) {
        if (isActivated()) {
            //Deactivate the trap
            setActive(false);

            //Apply effects to player
            player.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 20));
            player.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 20));
            player.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 20));

            //Play the sound for nearby players
            SoundManager.playSoundForNearbyPlayers(F13SoundEffect.TrapCaught, arena, player.getBukkitPlayer().getLocation(), 6, false, true);

            if (getTrapType().equals(TrapType.Jason)) {
                //Only counselors get damaged
                player.getBukkitPlayer().damage(6);
            }


            //Send player action bar message
            ActionBarAPI.sendActionBar(player.getBukkitPlayer(), FridayThe13th.language.get(player.getBukkitPlayer(), "game.message.caughtInTrap", "You are caught in a trap."), 100);

            //Remove the trap if it's a Jason rap
            if (getTrapType().equals(TrapType.Jason)) {
                //Give him the XP
                arena.getGameManager().getPlayerManager().getJason().getXpManager().registerXPAward(XPAward.Jason_TrapEnsnare);

                if (arena.getGameManager().getPlayerManager().isAlive(player)) {
                    //Notify Jason
                    arena.getGameManager().getPlayerManager().getJason().getF13Player().getBukkitPlayer().sendMessage(FridayThe13th.pluginPrefix + "A counselor stepped in your trap. Click the inventory item to teleport there.");

                    //Give him item to teleport to
                    ItemStack jasonItem = new ItemStack(Material.EYE_OF_ENDER, 1);
                    ItemMeta jasonItemMeta = jasonItem.getItemMeta();
                    jasonItemMeta.setDisplayName(ChatColor.RED + FridayThe13th.language.get(player.getBukkitPlayer(), "game.item.jasonTeleportPlayerTrap", "Teleport to trapped player..."));

                    List<String> jasonItemLore = new ArrayList<>();
                    jasonItemLore.add(HiddenStringsUtil.encodeString("{\"TrapTeleport\": \"" + player.getBukkitPlayer().getName() + "\"}"));
                    jasonItemMeta.setLore(jasonItemLore);

                    jasonItem.setItemMeta(jasonItemMeta);
                    arena.getGameManager().getPlayerManager().getJason().getF13Player().getBukkitPlayer().getInventory().addItem(jasonItem);

                } else {
                    arena.getGameManager().getPlayerManager().getJason().getF13Player().getBukkitPlayer().sendMessage(FridayThe13th.pluginPrefix + ChatColor.AQUA + player.getBukkitPlayer().getName() + ChatColor.WHITE + FridayThe13th.language.get(arena.getGameManager().getPlayerManager().getJason().getF13Player().getBukkitPlayer(), "game.message.counselorKilledInTrap", " stepped on one of your traps and was killed."));
                }
            }

            //Remove the trap
            remove();

            //Schedule to remove the transport item
            Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, new Runnable() {
                @Override
                public void run() {
                    InventoryActions.remove(arena.getGameManager().getPlayerManager().getJason().getF13Player().getBukkitPlayer().getInventory(), Material.EYE_OF_ENDER, 1, (short) -1);
                }
            }, 100);
        }
    }
}
