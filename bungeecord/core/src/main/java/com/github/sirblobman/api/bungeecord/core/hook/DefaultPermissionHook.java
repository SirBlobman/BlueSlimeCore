package com.github.sirblobman.api.bungeecord.core.hook;

import com.github.sirblobman.api.bungeecord.configuration.ConfigurationManager;
import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.bungeecord.hook.permission.IPermissionHook;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public record DefaultPermissionHook(@NotNull CorePlugin plugin) implements IPermissionHook {
    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public @Nullable String getPrefix(@NotNull UUID playerId) {
        String primaryGroupName = getPrimaryGroupName(playerId);
        if (primaryGroupName == null) {
            return null;
        }

        Configuration rankSection = getRankSection(primaryGroupName);
        return rankSection.getString("prefix");
    }

    @Override
    public @Nullable String getSuffix(@NotNull UUID playerId) {
        String primaryGroupName = getPrimaryGroupName(playerId);
        if (primaryGroupName == null) {
            return null;
        }

        Configuration rankSection = getRankSection(primaryGroupName);
        return rankSection.getString("suffix");
    }

    @Override
    public @Nullable String getPrimaryGroupName(@NotNull UUID playerId) {
        ProxiedPlayer player = getPlayer(playerId);
        if (player == null) {
            return null;
        }

        CorePlugin plugin = plugin();
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
    public int getPrimaryGroupWeight(@NotNull UUID playerId, int defaultWeight) {
        String primaryGroupName = getPrimaryGroupName(playerId);
        if (primaryGroupName == null) {
            return defaultWeight;
        }

        Configuration rankSection = getRankSection(primaryGroupName);
        return rankSection.getInt("weight", defaultWeight);
    }

    private @NotNull ProxyServer getProxy() {
        CorePlugin plugin = plugin();
        return plugin.getProxy();
    }

    private @Nullable ProxiedPlayer getPlayer(@NotNull UUID playerId) {
        ProxyServer proxy = getProxy();
        return proxy.getPlayer(playerId);
    }

    private @NotNull Configuration getRankSection(String rankId) {
        CorePlugin plugin = plugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        Configuration configuration = configurationManager.get("config.yml");

        Configuration sectionRanks = configuration.getSection("ranks");
        return sectionRanks.getSection(rankId);
    }
}
