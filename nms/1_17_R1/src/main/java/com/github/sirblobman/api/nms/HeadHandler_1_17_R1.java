package com.github.sirblobman.api.nms;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import com.github.sirblobman.api.shaded.xseries.XMaterial;
import com.github.sirblobman.api.utility.ItemUtility;


public class HeadHandler_1_17_R1 extends HeadHandler {
    public HeadHandler_1_17_R1(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack itemStack = XMaterial.PLAYER_HEAD.parseItem();
        if (ItemUtility.isAir(itemStack)) {
            throw new IllegalStateException("Failed to create PLAYER_HEAD ItemStack!");
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof SkullMeta skullMeta)) {
            throw new IllegalStateException("PLAYER_HEAD ItemStack doesn't have SkullMeta.");
        }

        skullMeta.setOwningPlayer(player);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    @Override
    public ItemStack getBase64Head(String base64) {
        byte[] base64ByteArray = base64.getBytes();
        UUID uuid = UUID.nameUUIDFromBytes(base64ByteArray);
        return getBase64Head(base64, uuid);
    }

    @Override
    public ItemStack getBase64Head(String base64, UUID customId) {
        GameProfile gameProfile = new GameProfile(customId, "custom");
        Property property = new Property("textures", base64);

        PropertyMap properties = gameProfile.getProperties();
        properties.put("textures", property);
        return createGameProfileHead(gameProfile);
    }

    private ItemStack createGameProfileHead(GameProfile gameProfile) {
        try {
            ItemStack itemStack = XMaterial.PLAYER_HEAD.parseItem();
            if (ItemUtility.isAir(itemStack)) {
                throw new IllegalStateException("Failed to create PLAYER_HEAD ItemStack!");
            }

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (!(itemMeta instanceof SkullMeta skullMeta)) {
                throw new IllegalStateException("PLAYER_HEAD ItemStack doesn't have SkullMeta.");
            }

            Class<? extends SkullMeta> class_SkullMeta = skullMeta.getClass();
            Method method_setProfile = class_SkullMeta.getDeclaredMethod("setProfile", GameProfile.class);
            method_setProfile.setAccessible(true);

            method_setProfile.invoke(skullMeta, gameProfile);
            method_setProfile.setAccessible(false);

            itemStack.setItemMeta(skullMeta);
            return itemStack;
        } catch (Exception ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to create a GameProfile head because an error occurred:", ex);
            return null;
        }
    }
}
