package com.github.sirblobman.api.bungeecord.bungeeperms;

import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import com.github.sirblobman.api.bungeecord.hook.permission.IPermissionHook;
import com.github.sirblobman.api.utility.Validate;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.BungeePermsAPI;
import net.alpenblock.bungeeperms.Group;
import net.alpenblock.bungeeperms.PermissionsManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BungeePermsHook implements IPermissionHook {
    private final Plugin plugin;

    public BungeePermsHook(Plugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean isDisabled() {
        Plugin plugin = getPlugin();
        ProxyServer proxy = plugin.getProxy();
        PluginManager pluginManager = proxy.getPluginManager();

        Plugin bungeePerms = pluginManager.getPlugin("BungeePerms");
        return (bungeePerms == null);
    }

    @NotNull
    @Override
    public String getPrefix(UUID playerId) {
        if (isDisabled()) {
            return "";
        }

        String playerIdString = playerId.toString();
        String prefix = BungeePermsAPI.userPrefix(playerIdString, null, null);
        return (prefix == null || prefix.isBlank() ? "" : prefix);
    }

    @NotNull
    @Override
    public String getSuffix(UUID playerId) {
        if (isDisabled()) {
            return "";
        }

        String playerIdString = playerId.toString();
        String suffix = BungeePermsAPI.userSuffix(playerIdString, null, null);
        return (suffix == null || suffix.isBlank() ? "" : suffix);
    }

    @Nullable
    @Override
    public String getPrimaryGroupName(UUID playerId) {
        if (isDisabled()) {
            return null;
        }

        String playerIdString = playerId.toString();
        return BungeePermsAPI.userMainGroup(playerIdString);
    }

    @Override
    public int getPrimaryGroupWeight(UUID playerId, int defaultWeight) {
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
