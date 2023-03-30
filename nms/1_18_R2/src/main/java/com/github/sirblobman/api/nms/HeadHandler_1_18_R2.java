package com.github.sirblobman.api.nms;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import com.github.sirblobman.api.shaded.xseries.XMaterial;
import com.github.sirblobman.api.utility.ItemUtility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HeadHandler_1_18_R2 extends HeadHandler {
    public HeadHandler_1_18_R2(JavaPlugin plugin) {
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
    @SuppressWarnings("deprecation")
    public ItemStack getBase64Head(String base64, UUID customId) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if (item == null) {
            return null;
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

    @SuppressWarnings("deprecation")
    private PlayerProfile createPlayerProfile(String base64, UUID uniqueId) {
        PlayerProfile profile = Bukkit.createPlayerProfile(uniqueId, "custom");
        PlayerTextures textures = profile.getTextures();
        URL skinUrl = decodeSkinUrl(base64);

        textures.setSkin(skinUrl);
        profile.setTextures(textures);
        return profile;
    }

    private URL decodeSkinUrl(String base64) {
        Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(base64);

        String decodedString = new String(decode, StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(decodedString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonObject textures = jsonObject.get("textures").getAsJsonObject();
        JsonObject skin = textures.get("SKIN").getAsJsonObject();
        String url = skin.get("url").getAsString();

        try {
            return new URL(url);
        } catch (MalformedURLException ignored) {
            return null;
        }
    }
}
