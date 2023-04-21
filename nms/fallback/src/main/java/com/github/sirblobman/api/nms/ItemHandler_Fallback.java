package com.github.sirblobman.api.nms;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.CustomNbtContainer_Fallback;
import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.shaded.xseries.XMaterial;
import com.github.sirblobman.api.utility.VersionUtility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class ItemHandler_Fallback extends ItemHandler {
    public ItemHandler_Fallback(JavaPlugin plugin) {
        super(plugin);

        String minecraftVersion = VersionUtility.getMinecraftVersion();
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();

        Logger logger = getLogger();
        logger.warning("Using fallback ItemHandler.");
        logger.warning("Version '" + minecraftVersion + "' and NMS '" + nmsVersion + "' combo is not supported.");
        logger.warning("Please contact SirBlobman if you believe this is a mistake.");
        logger.warning("https://github.com/SirBlobman/BlueSlimeCore/issues/new/choose");
    }

    @Override
    public @NotNull String getLocalizedName(@NotNull ItemStack item) {
        XMaterial material = XMaterial.matchXMaterial(item);
        return material.toString();
    }

    @Override
    public @NotNull String getKeyString(@NotNull ItemStack item) {
        // Not Supported, may not be accurate.
        XMaterial material = XMaterial.matchXMaterial(item);
        return ("minecraft:" + material.name());
    }

    @Override
    public @NotNull String toNBT(@NotNull ItemStack item) {
        // Not Supported
        return "{}";
    }

    @Override
    public @NotNull ItemStack fromNBT(@NotNull String string) {
        // Not Supported
        return new ItemStack(Material.AIR);
    }

    @Override
    public @NotNull String toBase64String(@NotNull ItemStack item) {
        // Not Supported
        return "";
    }

    @Override
    public @NotNull ItemStack fromBase64String(@NotNull String string) {
        // Not Supported
        return new ItemStack(Material.AIR);
    }

    @Override
    public @NotNull CustomNbtContainer createNewCustomNbtContainer() {
        JavaPlugin plugin = getPlugin();
        return new CustomNbtContainer_Fallback(plugin);
    }

    @Override
    public @NotNull CustomNbtContainer getCustomNbt(@NotNull ItemStack item) {
        return createNewCustomNbtContainer();
    }

    @Override
    public @NotNull ItemStack setCustomNbt(@NotNull ItemStack item, @NotNull CustomNbtContainer container) {
        // Not Supported
        return item;
    }

    @Override
    public @Nullable Component getDisplayName(@NotNull ItemStack item) {
        // Not Supported
        return null;
    }

    @Override
    public @NotNull ItemStack setDisplayName(@NotNull ItemStack item, @Nullable Component displayName) {
        // Not Supported
        return item;
    }

    @Override
    public @Nullable List<Component> getLore(@NotNull ItemStack item) {
        // Not Supported
        return null;
    }

    @Override
    public @NotNull ItemStack setLore(@NotNull ItemStack item, @Nullable List<Component> lore) {
        // Not Supported
        return item;
    }
}
