package com.AustinPilz.FridayThe13th.Components.Skin;

import com.AustinPilz.FridayThe13th.FridayThe13th;
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

public class SkinChange_1_12 implements SkinChange {

    private UUID uuid;
    private String[] originalTextures;
    private boolean reverted;

    public SkinChange_1_12(Player player) {
        this.uuid = player.getUniqueId();
        reverted = false;
    }

    /**
     * Applies the skin
     */
    public void apply(F13Skin skin) {
        Player player = Bukkit.getPlayer(this.uuid);

        if (player == null) {
            return;
        }


        CraftPlayer craftPlayer = ((CraftPlayer) player);
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        GameProfile gameProfile = craftPlayer.getProfile();

        Collection<Property> profileProperties = gameProfile.getProperties().get("textures");
        String playerCurrentSkinValue = profileProperties.iterator().next().getValue();
        String playerCurrentSkinSignature = profileProperties.iterator().next().getSignature();

        if (!skin.getValue().equals(playerCurrentSkinValue) && !reverted) //Don't set skin if it's the same one as before
        {
            if (originalTextures == null) {
                this.originalTextures = new String[]{playerCurrentSkinValue, playerCurrentSkinSignature};
            }

            gameProfile.getProperties().clear();
            gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
            PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
            PacketPlayOutPlayerInfo add = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);


            Bukkit.getOnlinePlayers().stream().map(CraftPlayer.class::cast).map(CraftPlayer::getHandle).forEach(onlineEntityPlayer -> {
                onlineEntityPlayer.playerConnection.sendPacket(remove);
                onlineEntityPlayer.playerConnection.sendPacket(add);
                onlineEntityPlayer.getBukkitEntity().hidePlayer(Bukkit.getPlayer(this.uuid));
            });

            Bukkit.getScheduler().runTaskLater(FridayThe13th.instance, () -> {
                player.teleport(player.getLocation().add(0, 0.1, 0));
                if (Bukkit.getOfflinePlayer(this.uuid).isOnline()) {
                    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.showPlayer(Bukkit.getPlayer(this.uuid)));
                }
                entityPlayer.playerConnection.sendPacket(new PacketPlayOutRespawn(entityPlayer.dimension, entityPlayer.getWorld().getDifficulty(), entityPlayer.getWorld().getWorldData().getType(), entityPlayer.playerInteractManager.getGameMode()));
                player.updateInventory();
            }, 1);

        }
    }

    /**
     * Returns player to their original skin
     */
    public void revert() {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return;
        }
        F13Skin original = F13Skin.ORIGINAL;
        original.setValue(this.originalTextures[0]);
        original.setSignature(this.originalTextures[1]);
        apply(original);
        reverted = true;
    }
}