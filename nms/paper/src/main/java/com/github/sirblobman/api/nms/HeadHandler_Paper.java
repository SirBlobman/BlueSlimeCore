package com.github.sirblobman.api.nms;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;

import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class HeadHandler_Paper extends HeadHandler {
    public HeadHandler_Paper(@NotNull JavaPlugin plugin) {
        super(plugin);
        Logger logger = getLogger();
        logger.info("Using non-NMS Paper HeadHandler");
    }

    @Override
    public @NotNull ItemStack getPlayerHead(@NotNull OfflinePlayer player) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if (item == null) {
            return new ItemStack(Material.AIR);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof SkullMeta)) {
            return item;
        }

        SkullMeta skullMeta = (SkullMeta) itemMeta;
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
        PlayerProfile profile = Bukkit.createProfile(customId, "custom");
        ProfileProperty property = new ProfileProperty("textures", base64);
        profile.setProperty(property);
        return createPlayerProfileHead(profile);
    }

    private @NotNull ItemStack createPlayerProfileHead(@NotNull PlayerProfile profile) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if (item == null) {
            return new ItemStack(Material.AIR);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof SkullMeta)) {
            return item;
        }

        SkullMeta skullMeta = (SkullMeta) itemMeta;
        skullMeta.setPlayerProfile(profile);

        item.setItemMeta(itemMeta);
        return item;
    }
}
