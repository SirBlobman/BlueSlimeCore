package com.github.sirblobman.api.bungeecord.bungeeperms;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import com.github.sirblobman.api.bungeecord.hook.permission.IPermissionHook;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.BungeePermsAPI;
import net.alpenblock.bungeeperms.Group;
import net.alpenblock.bungeeperms.PermissionsManager;

public record BungeePermsHook(Plugin plugin) implements IPermissionHook {
    @Override
    public boolean isDisabled() {
        Plugin plugin = plugin();
        ProxyServer proxy = plugin.getProxy();
        PluginManager pluginManager = proxy.getPluginManager();

        Plugin bungeePerms = pluginManager.getPlugin("BungeePerms");
        return (bungeePerms == null);
    }

    @Override
    public @Nullable String getPrefix(@NotNull UUID playerId) {
        if (isDisabled()) {
            return null;
        }

        String playerIdString = playerId.toString();
        return BungeePermsAPI.userPrefix(playerIdString, null, null);
    }

    @Override
    public @Nullable String getSuffix(@NotNull UUID playerId) {
        if (isDisabled()) {
            return null;
        }

        String playerIdString = playerId.toString();
        return BungeePermsAPI.userSuffix(playerIdString, null, null);
    }

    @Override
    public @Nullable String getPrimaryGroupName(@NotNull UUID playerId) {
        if (isDisabled()) {
            return null;
        }

        String playerIdString = playerId.toString();
        return BungeePermsAPI.userMainGroup(playerIdString);
    }

    @Override
    public int getPrimaryGroupWeight(@NotNull UUID playerId, int defaultWeight) {
        if (isDisabled()) {
            return defaultWeight;
        }

        String primaryGroupName = getPrimaryGroupName(playerId);
        if (primaryGroupName == null) {
            return defaultWeight;
        }

        BungeePerms instance = BungeePerms.getInstance();
        PermissionsManager permissionsManager = instance.getPermissionsManager();
        Group group = permissionsManager.getGroup(primaryGroupName);
        if (group == null) {
            return defaultWeight;
        }

        return group.getWeight();
    }
}
