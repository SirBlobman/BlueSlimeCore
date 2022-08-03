package com.github.sirblobman.api.nms;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class HeadHandler_Fallback extends HeadHandler {
    public HeadHandler_Fallback(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public ItemStack getPlayerHead(String username) {
        return null;
    }

    @Override
    public ItemStack getPlayerHead(OfflinePlayer player) {
        return null;
    }

    @Override
    public ItemStack getBase64Head(String base64) {
        return null;
    }

    @Override
    public ItemStack getBase64Head(String base64, UUID customId) {
        return null;
    }
}
