package com.github.sirblobman.api.nms;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonObject;

public abstract class HeadHandler extends Handler {
    public HeadHandler(JavaPlugin plugin) {
        super(plugin);
    }

    protected final String encodeBase64(String original) {
        Encoder encoder = Base64.getEncoder();
        byte[] originalByteArray = original.getBytes(StandardCharsets.UTF_8);
        byte[] base64ByteArray = encoder.encode(originalByteArray);
        return new String(base64ByteArray);
    }

    protected final String encodeTextureURL(String url) {
        JsonObject skin = new JsonObject();
        skin.addProperty("url", url);

        JsonObject textures = new JsonObject();
        textures.add("SKIN", skin);

        JsonObject parent = new JsonObject();
        parent.add("textures", textures);

        String jsonString = parent.toString();
        return encodeBase64(jsonString);
    }

    public final ItemStack getTextureHead(String url) {
        String base64 = encodeTextureURL(url);
        return getBase64Head(base64);
    }

    public final ItemStack getTextureHead(String url, UUID customId) {
        String base64 = encodeTextureURL(url);
        return getBase64Head(base64, customId);
    }

    @SuppressWarnings("deprecation")
    public ItemStack getPlayerHead(String username) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
        return getPlayerHead(offlinePlayer);
    }

    public abstract ItemStack getPlayerHead(OfflinePlayer player);

    public abstract ItemStack getBase64Head(String base64);

    public abstract ItemStack getBase64Head(String base64, UUID customId);
}
