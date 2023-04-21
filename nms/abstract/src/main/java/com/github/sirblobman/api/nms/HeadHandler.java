package com.github.sirblobman.api.nms;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonObject;

/**
 * Abstract NMS Head Handler Class
 * @author SirBlobman
 */
public abstract class HeadHandler extends Handler {
    /**
     * Create a new instance of this handler.
     * @param plugin The plugin that owns this instance.
     */
    public HeadHandler(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Encodes the given {@link String} using the Base64 encoding scheme.
     * @param original the string to be encoded.
     * @return the string encoded as a Base64 string.
     * @see Base64
     */
    protected final @NotNull String encodeBase64(@NotNull String original) {
        Encoder encoder = Base64.getEncoder();
        byte[] originalByteArray = original.getBytes(StandardCharsets.UTF_8);
        byte[] base64ByteArray = encoder.encode(originalByteArray);
        return new String(base64ByteArray);
    }

    /**
     * Encodes the given string URL as a Base64 texture JSON for Minecraft player heads.
     * @param url The URL to encode. Must be allowed by the client. Usually contains 'textures.minecraft.net'.
     * @return a Base64 encoded version of the json for Minecraft player heads.
     */
    protected final @NotNull String encodeTextureURL(@NotNull String url) {
        JsonObject skin = new JsonObject();
        skin.addProperty("url", url);

        JsonObject textures = new JsonObject();
        textures.add("SKIN", skin);

        JsonObject parent = new JsonObject();
        parent.add("textures", textures);

        String jsonString = parent.toString();
        return encodeBase64(jsonString);
    }

    /**
     * Creates an ItemStack that is a player head with a custom texture.
     * @param url The URL for the custom skin texture. Must be allowed by the client.
     *            Usually contains 'textures.minecraft.net'.
     * @return a new ItemStack that is a player head with the custom texture URL.
     */
    public final @NotNull ItemStack getTextureHead(@NotNull String url) {
        String base64 = encodeTextureURL(url);
        return getBase64Head(base64);
    }

    /**
     * Returns an {@link ItemStack} representing a player head with the specified texture URL and custom ID.
     * @param url The URL for the custom skin texture. Must be allowed by the client.
     *            Usually contains 'textures.minecraft.net'.
     * @param customId the UUID to use as the custom ID for the head, can be null.
     * @return an {@link ItemStack} representing the player head with the specified texture URL and custom id.
     */
    public final @NotNull ItemStack getTextureHead(@NotNull String url, @Nullable UUID customId) {
        String base64 = encodeTextureURL(url);
        return getBase64Head(base64, customId);
    }

    /**
     * Create a player head from a player's username.
     * @deprecated Use {@link #getPlayerHead(OfflinePlayer)}
     * @param username The username of the player.
     * @return an {@link ItemStack} representing the player head with the specified username.
     */
    @Deprecated
    public @NotNull ItemStack getPlayerHead(@NotNull String username) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
        return getPlayerHead(offlinePlayer);
    }

    /**
     * Returns an {@link ItemStack} representing the head of the specified player.
     * @param player the {@link OfflinePlayer} whose head should be returned.
     * @return an {@link ItemStack} representing the head of the specified player.
     */
    public abstract @NotNull ItemStack getPlayerHead(@NotNull OfflinePlayer player);

    /**
     * Returns an {@link ItemStack} representing a player head with the specified base64 texture.
     * @param base64 the base64 encoded texture to use for the head
     * @return an {@link ItemStack} representing the player head with the specified base64 texture.
     */
    public abstract @NotNull ItemStack getBase64Head(@NotNull String base64);

    /**
     * Returns an {@link ItemStack} representing a player head with the specified base64 texture and custom ID.
     * @param base64 the base64 encoded texture to use for the head
     * @param customId the UUID to use as the custom ID for the head, can be null
     * @return an {@link ItemStack} representing the player head with the specified base64 texture and custom ID
     */
    public abstract @NotNull ItemStack getBase64Head(@NotNull String base64, @Nullable UUID customId);
}
