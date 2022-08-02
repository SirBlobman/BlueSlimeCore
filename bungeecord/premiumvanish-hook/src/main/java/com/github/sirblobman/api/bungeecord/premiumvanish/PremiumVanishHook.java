package com.github.sirblobman.api.bungeecord.premiumvanish;

import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;
import com.github.sirblobman.api.utility.Validate;

import de.myzelyam.api.vanish.BungeeVanishAPI;

public final class PremiumVanishHook implements IVanishHook {
    private final Plugin plugin;

    public PremiumVanishHook(Plugin plugin) {
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

        Plugin luckPerms = pluginManager.getPlugin("PremiumVanish");
        return (luckPerms == null);
    }

    @Override
    public boolean isHidden(ProxiedPlayer player) {
        return BungeeVanishAPI.isInvisible(player);
    }

    @Override
    public boolean isHidden(UUID playerId) {
        List<UUID> invisiblePlayerList = BungeeVanishAPI.getInvisiblePlayers();
        return invisiblePlayerList.contains(playerId);
    }
}
