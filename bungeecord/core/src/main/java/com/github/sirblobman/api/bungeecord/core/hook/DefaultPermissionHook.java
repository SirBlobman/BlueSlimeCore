package com.github.sirblobman.api.bungeecord.core.hook;

import java.util.Collection;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.bungeecord.configuration.ConfigurationManager;
import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.bungeecord.hook.permission.IPermissionHook;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DefaultPermissionHook implements IPermissionHook {
    private final CorePlugin plugin;

    public DefaultPermissionHook(CorePlugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public CorePlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @NotNull
    @Override
    public String getPrefix(UUID playerId) {
        String primaryGroupName = getPrimaryGroupName(playerId);
        if (primaryGroupName == null) {
            return "";
        }

        Configuration rankSection = getRankSection(primaryGroupName);
        return rankSection.getString("prefix", "");
    }

    @NotNull
    @Override
    public String getSuffix(UUID playerId) {
        String primaryGroupName = getPrimaryGroupName(playerId);
        if (primaryGroupName == null) {
            return "";
        }

        Configuration rankSection = getRankSection(primaryGroupName);
        return rankSection.getString("suffix", "");
    }

    @Nullable
    @Override
    public String getPrimaryGroupName(UUID playerId) {
        ProxiedPlayer player = getPlayer(playerId);
        if (player == null) {
            return "";
        }

        CorePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        Configuration configuration = configurationManager.get("config.yml");

        Configuration sectionRanks = configuration.getSection("ranks");
        Collection<String> rankIdSet = sectionRanks.getKeys();

        String highestRankId = null;
        int highestWeight = Integer.MIN_VALUE;
        for (String rankId : rankIdSet) {
            String permissionName = ("sbcore.ranks." + rankId);
            if (!player.hasPermission(permissionName)) {
                continue;
            }

            Configuration section = sectionRanks.getSection(rankId);
            int weight = section.getInt("weight");
            if (weight > highestWeight) {
                highestRankId = rankId;
                highestWeight = weight;
            }
        }

        return highestRankId;
    }

    @Override
    public int getPrimaryGroupWeight(UUID playerId, int defaultWeight) {
        String primaryGroupName = getPrimaryGroupName(playerId);
        if (primaryGroupName == null) {
            return defaultWeight;
        }

        Configuration rankSection = getRankSection(primaryGroupName);
        return rankSection.getInt("weight", defaultWeight);
    }

    @NotNull
    private ProxyServer getProxy() {
        CorePlugin plugin = getPlugin();
        return plugin.getProxy();
    }

    @Nullable
    private ProxiedPlayer getPlayer(UUID playerId) {
        ProxyServer proxy = getProxy();
        return proxy.getPlayer(playerId);
    }

    private Configuration getRankSection(String rankId) {
        CorePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        Configuration configuration = configurationManager.get("config.yml");

        Configuration sectionRanks = configuration.getSection("ranks");
        return sectionRanks.getSection(rankId);
    }
}
