package com.github.sirblobman.api.nms;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import com.github.sirblobman.api.shaded.xseries.XMaterial;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class HeadHandler_1_20_R1 extends HeadHandler {
    public HeadHandler_1_20_R1(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull ItemStack getPlayerHead(@NotNull OfflinePlayer player) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if (item == null) {
            return new ItemStack(Material.AIR);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof SkullMeta skullMeta)) {
            return item;
        }

        skullMeta.setOwningPlayer(player);
        item.setItemMeta(skullMeta);
        return item;
    }

    @Override
    public @NotNull ItemStack getBase64Head(@NotNull String base64) {
        byte[] base64Bytes = base64.getBytes(StandardCharsets.UTF_8);
        UUID idFromBytes = UUID.nameUUIDFromBytes(base64Bytes);
        return getBase64Head(base64, idFromBytes);
    }

    @Override
    public @NotNull ItemStack getBase64Head(@NotNull String base64, @Nullable UUID customId) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if (item == null) {
            return new ItemStack(Material.AIR);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof SkullMeta skullMeta)) {
            return item;
        }

        PlayerProfile playerProfile = createPlayerProfile(base64, customId);
        skullMeta.setOwnerProfile(playerProfile);
        item.setItemMeta(skullMeta);
        return item;
    }

    private PlayerProfile createPlayerProfile(String base64, UUID uniqueId) {
        PlayerProfile profile = Bukkit.createPlayerProfile(uniqueId, "custom");
        PlayerTextures textures = profile.getTextures();
        URL skinUrl = decodeSkinUrl(base64);

        textures.setSkin(skinUrl);
        profile.setTextures(textures);
        return profile;
    }

    private @Nullable URL decodeSkinUrl(String base64) {
        Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(base64);

        String decodedString = new String(decode, StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(decodedString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonObject textures = jsonObject.get("textures").getAsJsonObject();
        JsonObject skin = textures.get("SKIN").getAsJsonObject();
        String url = skin.get("url").getAsString();

        try {
            return URI.create(url).toURL();
        } catch (MalformedURLException ignored) {
            return null;
        }
    }
}
