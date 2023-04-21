package com.github.sirblobman.api.bungeecord.core.hook;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.bungeecord.configuration.ConfigurationManager;
import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;

public record DefaultVanishHook(@NotNull CorePlugin plugin) implements IVanishHook {
    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public boolean isHidden(@NotNull ProxiedPlayer player) {
        UUID playerId = player.getUniqueId();
        return isHidden(playerId);
    }

    @Override
    public boolean isHidden(@NotNull UUID playerId) {
        CorePlugin plugin = plugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        Configuration configuration = configurationManager.get("hidden.yml");

        String playerIdString = playerId.toString();
        return configuration.getBoolean(playerIdString, false);
    }

    @Override
    public void setHidden(@NotNull ProxiedPlayer player, boolean hidden) {
        UUID playerId = player.getUniqueId();
        setHidden(playerId, hidden);
    }

    @Override
    public void setHidden(@NotNull UUID playerId, boolean hidden) {
        CorePlugin plugin = plugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        Configuration configuration = configurationManager.get("hidden.yml");

        String playerIdString = playerId.toString();
        configuration.set(playerIdString, hidden);
        configurationManager.save("hidden.yml");
    }
}
