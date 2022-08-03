package com.github.sirblobman.api.bungeecord.luckperms;

import java.util.OptionalInt;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import com.github.sirblobman.api.bungeecord.hook.permission.IPermissionHook;
import com.github.sirblobman.api.utility.Validate;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LuckPermsHook implements IPermissionHook {
    private final Plugin plugin;

    public LuckPermsHook(Plugin plugin) {
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

        Plugin luckPerms = pluginManager.getPlugin("LuckPerms");
        return (luckPerms == null);
    }

    @NotNull
    @Override
    public String getPrefix(UUID playerId) {
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
        String prefix = metaData.getPrefix();
        return (prefix == null || prefix.isBlank() ? "" : prefix);
    }

    @NotNull
    @Override
    public String getSuffix(UUID playerId) {
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

    @Nullable
    @Override
    public String getPrimaryGroupName(UUID playerId) {
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
    public int getPrimaryGroupWeight(UUID playerId, int defaultWeight) {
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
