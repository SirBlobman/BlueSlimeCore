package com.github.sirblobman.api.nms;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class HeadHandler_1_8_R3 extends HeadHandler {
    public HeadHandler_1_8_R3(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull ItemStack getPlayerHead(@NotNull String username) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if (item == null) {
            return new ItemStack(Material.AIR);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof SkullMeta)) {
            return item;
        }

        SkullMeta skullMeta = (SkullMeta) itemMeta;
        skullMeta.setOwner(username);

        item.setItemMeta(skullMeta);
        return item;
    }

    @Override
    public @NotNull ItemStack getPlayerHead(@NotNull OfflinePlayer player) {
        String username = player.getName();
        return getPlayerHead(username);
    }

    @Override
    public @NotNull ItemStack getBase64Head(@NotNull String base64) {
        byte[] base64Bytes = base64.getBytes(StandardCharsets.UTF_8);
        UUID idFromBytes = UUID.nameUUIDFromBytes(base64Bytes);
        return getBase64Head(base64, idFromBytes);
    }

    @Override
    public @NotNull ItemStack getBase64Head(@NotNull String base64, @Nullable UUID customId) {
        GameProfile gameProfile = new GameProfile(customId, "custom");
        Property property = new Property("textures", base64);

        PropertyMap propertyMap = gameProfile.getProperties();
        propertyMap.put("textures", property);
        return createGameProfileHead(gameProfile);
    }

    private @NotNull ItemStack createGameProfileHead(@NotNull GameProfile profile) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if (item == null) {
            return new ItemStack(Material.AIR);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof SkullMeta)) {
            return item;
        }

        SkullMeta skullMeta = (SkullMeta) itemMeta;
        setGameProfile(skullMeta, profile);

        item.setItemMeta(skullMeta);
        return item;
    }

    private void setGameProfile(@NotNull SkullMeta meta, @NotNull GameProfile profile) {
        try {
            Class<?> metaClass = Class.forName("org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaSkull");
            Field field_profile = metaClass.getDeclaredField("profile");
            field_profile.setAccessible(true);
            field_profile.set(meta, profile);
        } catch (ReflectiveOperationException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "Failed to set GameProfile on a SkullMeta:", ex);
        }
    }
}
