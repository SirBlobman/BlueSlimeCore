package com.github.sirblobman.api.bungeecord.luckperms;

import java.util.OptionalInt;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

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

public final class LuckPermsHook {
    private final Plugin plugin;

    public LuckPermsHook(Plugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Check if LuckPerms is disabled or does not exist.
     * @return {@code true} if LuckPerms doesn't exist on the proxy, otherwise {@code false}.
     */
    public boolean isDisabled() {
        Plugin plugin = getPlugin();
        ProxyServer proxy = plugin.getProxy();
        PluginManager pluginManager = proxy.getPluginManager();

        Plugin luckPerms = pluginManager.getPlugin("LuckPerms");
        return (luckPerms == null);
    }

    /**
     * Get the LuckPerms prefix for a specific player.
     * @param playerId The {@link UUID} of the player.
     * @return The prefix for the player, or an empty string if they do not have one.
     */
    @NotNull
    public String getPrefix(UUID playerId) {
        if(isDisabled()) {
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

    /**
     * Get the LuckPerms suffix for a specific player.
     * @param playerId The {@link UUID} of the player.
     * @return The suffix for the player, or an empty string if they do not have one.
     */
    @NotNull
    public String getSuffix(UUID playerId) {
        if(isDisabled()) {
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


    /**
     * Get the current LuckPerms primary group weight for a player.
     * @param playerId The {@link UUID} of the player.
     * @param defaultWeight The default weight to use if the player doesn't have a group or weight.
     * @return The suffix for the player, or an empty string if they do not have one.
     */
    public int getWeight(UUID playerId, int defaultWeight) {
        if(isDisabled()) {
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
