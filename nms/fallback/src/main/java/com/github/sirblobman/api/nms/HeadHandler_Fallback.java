package com.github.sirblobman.api.nms;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.shaded.xseries.XMaterial;
import com.github.sirblobman.api.utility.VersionUtility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HeadHandler_Fallback extends HeadHandler {
    public HeadHandler_Fallback(JavaPlugin plugin) {
        super(plugin);

        String minecraftVersion = VersionUtility.getMinecraftVersion();
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();

        Logger logger = getLogger();
        logger.warning("Using fallback HeadHandler.");
        logger.warning("Version '" + minecraftVersion + "' and NMS '" + nmsVersion + "' combo is not supported.");
        logger.warning("Please contact SirBlobman if you believe this is a mistake.");
        logger.warning("https://github.com/SirBlobman/BlueSlimeCore/issues/new/choose");
    }

    @Override
    public @NotNull ItemStack getPlayerHead(@NotNull OfflinePlayer player) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if (item == null) {
            return new ItemStack(Material.AIR);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof SkullMeta)) {
            return new ItemStack(Material.AIR);
        }

        SkullMeta skullMeta = (SkullMeta) itemMeta;
        String playerName = player.getName();
        skullMeta.setOwner(playerName);

        item.setItemMeta(skullMeta);
        return item;
    }

    @Override
    public @NotNull ItemStack getBase64Head(@NotNull String base64) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        if (item == null) {
            return new ItemStack(Material.AIR);
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return new ItemStack(Material.AIR);
        }

        itemMeta.setDisplayName("Fallback does not support Base64.");
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public @NotNull ItemStack getBase64Head(@NotNull String base64, @Nullable UUID customId) {
        return getBase64Head(base64);
    }
}
