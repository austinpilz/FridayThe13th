package com.AustinPilz.FridayThe13th.Components.Skin;

import com.AustinPilz.FridayThe13th.Components.Enum.F13Skin;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class SkullPreview extends ItemStack {

    public SkullPreview(F13Skin skin, String displayName, List<String> lore) {
        super(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta skullMeta = (SkullMeta) this.getItemMeta();

        skullMeta.setDisplayName(displayName);
        skullMeta.setLore(lore);

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));

        Field profile = null;
        try {
            profile = skullMeta.getClass().getDeclaredField("profile");
            profile.setAccessible(true);
            profile.set(skullMeta, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        this.setItemMeta(skullMeta);

    }
}