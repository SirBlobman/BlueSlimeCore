package com.github.sirblobman.api.bungeecord.luckperms;

import java.util.OptionalInt;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import com.github.sirblobman.api.bungeecord.hook.permission.IPermissionHook;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;

public record LuckPermsHook(Plugin plugin) implements IPermissionHook {
    @Override
    public boolean isDisabled() {
        Plugin plugin = plugin();
        ProxyServer proxy = plugin.getProxy();
        PluginManager pluginManager = proxy.getPluginManager();

        Plugin luckPerms = pluginManager.getPlugin("LuckPerms");
        return (luckPerms == null);
    }

    @Override
    public @Nullable String getPrefix(@NotNull UUID playerId) {
        if (isDisabled()) {
            return null;
        }

        LuckPerms luckPerms = LuckPermsProvider.get();
        UserManager userManager = luckPerms.getUserManager();
        User user = userManager.getUser(playerId);
        if (user == null) {
            return null;
        }

        CachedDataManager dataManager = user.getCachedData();
        CachedMetaData metaData = dataManager.getMetaData();
        String prefix = metaData.getPrefix();
        return (prefix == null || prefix.isBlank() ? "" : prefix);
    }

    @Override
    public @NotNull String getSuffix(@NotNull UUID playerId) {
        if (isDisabled()) {
            return "";
        }

        LuckPerms luckPerms = LuckPermsProvider.get();
        UserManager userManager = luckPerms.getUserManager();
        User user = userManager.getUser(playerId);
        if (user == null) {
            return "";
        }

        CachedDataManager dataManager = user.getCachedData();
        CachedMetaData metaData = dataManager.getMetaData();
        String suffix = metaData.getSuffix();
        return (suffix == null || suffix.isBlank() ? "" : suffix);
    }

    @Override
    public @Nullable String getPrimaryGroupName(@NotNull UUID playerId) {
        if (isDisabled()) {
            return null;
        }

        LuckPerms luckPerms = LuckPermsProvider.get();
        UserManager userManager = luckPerms.getUserManager();
        User user = userManager.getUser(playerId);
        if (user == null) {
            return null;
        }

        return user.getPrimaryGroup();
    }

    @Override
    public int getPrimaryGroupWeight(@NotNull UUID playerId, int defaultWeight) {
        if (isDisabled()) {
            return defaultWeight;
        }

        LuckPerms luckPerms = LuckPermsProvider.get();
        UserManager userManager = luckPerms.getUserManager();
        GroupManager groupManager = luckPerms.getGroupManager();
        User user = userManager.getUser(playerId);
        if (user == null) {
            return defaultWeight;
        }

        String groupName = user.getPrimaryGroup();
        Group group = groupManager.getGroup(groupName);
        if (group == null) {
            return defaultWeight;
        }

        OptionalInt optionalWeight = group.getWeight();
        return optionalWeight.orElse(defaultWeight);
    }
}
