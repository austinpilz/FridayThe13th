package com.AustinPilz.FridayThe13th.Components;

import com.AustinPilz.FridayThe13th.FridayThe13th;
import com.AustinPilz.FridayThe13th.Structures.GameSkin;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutRespawn;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class SkinChange {

    private UUID uuid;
    private GameSkin gameSkin;

    private String[] originalTextures;

    public SkinChange(Player player) {
        this.uuid = player.getUniqueId();
    }

    public SkinChange setPlayer(Player player) {
        this.uuid = player.getUniqueId();
        return this;
    }

    public SkinChange withSkin(GameSkin gameSkin) {
        this.gameSkin = gameSkin;
        return this;
    }

    public void apply() {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return;
        }
        CraftPlayer craftPlayer = ((CraftPlayer) player);
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        GameProfile gameProfile = craftPlayer.getProfile();

        Collection<Property> profileProperties = gameProfile.getProperties().get("textures");
        this.originalTextures = new String[]{profileProperties.iterator().next().getValue(), profileProperties.iterator().next().getSignature()};

        gameProfile.getProperties().clear();
        gameProfile.getProperties().put("textures", new Property("textures", this.gameSkin.getValue(), this.gameSkin.getSignature()));



        PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        PacketPlayOutPlayerInfo add = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);


        Bukkit.getOnlinePlayers().stream().map(CraftPlayer.class::cast).map(CraftPlayer::getHandle).forEach(onlineEntityPlayer -> {
            onlineEntityPlayer.playerConnection.sendPacket(remove);
            onlineEntityPlayer.playerConnection.sendPacket(add);
            onlineEntityPlayer.getBukkitEntity().hidePlayer(Bukkit.getPlayer(this.uuid));
        });

        Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, () -> {
            player.teleport(player.getLocation().add(0, 0.1, 0));
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showPlayer(Bukkit.getPlayer(this.uuid)));
            entityPlayer.playerConnection.sendPacket(new PacketPlayOutRespawn(entityPlayer.dimension, entityPlayer.getWorld().getDifficulty(), entityPlayer.getWorld().getWorldData().getType(), entityPlayer.playerInteractManager.getGameMode()));
            player.updateInventory();
        }, 1);
    }

    public void revert() {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return;
        }
        GameSkin original = GameSkin.ORIGINAL;
        original.setValue(this.originalTextures[0]);
        original.setSignature(this.originalTextures[1]);
        new SkinChange(player).withSkin(original).apply();
    }
}