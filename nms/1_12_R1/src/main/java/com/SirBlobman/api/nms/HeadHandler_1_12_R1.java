package com.SirBlobman.api.nms;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

public class HeadHandler_1_12_R1 extends HeadHandler {
    public HeadHandler_1_12_R1(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getPlayerHead(String username) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
        return getPlayerHead(offlinePlayer);
    }

    @Override
    public ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if(item == null) return null;

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
        item.setItemMeta(meta);
        return item;
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
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if(item == null) return null;
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        try {
            Class<? extends SkullMeta> metaClass = meta.getClass();
            Field profileField = metaClass.getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch(Exception ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Failed to create GameProfile head because of an error:", ex);
            return null;
        }

        item.setItemMeta(meta);
        return item;
    }
}