package com.github.sirblobman.api.bungeecord.core.hook;

import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.bungeecord.configuration.ConfigurationManager;
import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;
import com.github.sirblobman.api.utility.Validate;

public final class DefaultVanishHook implements IVanishHook {
    private final CorePlugin plugin;

    public DefaultVanishHook(CorePlugin plugin) {
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

    @Override
    public boolean isHidden(ProxiedPlayer player) {
        UUID playerId = player.getUniqueId();
        return isHidden(playerId);
    }

    @Override
    public boolean isHidden(UUID playerId) {
        CorePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        Configuration configuration = configurationManager.get("hidden.yml");

        String playerIdString = playerId.toString();
        return configuration.getBoolean(playerIdString, false);
    }

    @Override
    public void setHidden(ProxiedPlayer player, boolean hidden) {
        UUID playerId = player.getUniqueId();
        setHidden(playerId, hidden);
    }

    @Override
    public void setHidden(UUID playerId, boolean hidden) {
        CorePlugin plugin = getPlugin();
        ConfigurationManager configurationManager = plugin.getConfigurationManager();
        Configuration configuration = configurationManager.get("hidden.yml");

        String playerIdString = playerId.toString();
        configuration.set(playerIdString, hidden);
        configurationManager.save("hidden.yml");
    }
}
