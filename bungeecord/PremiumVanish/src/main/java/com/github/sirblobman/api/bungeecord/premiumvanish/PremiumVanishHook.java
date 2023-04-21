package com.github.sirblobman.api.bungeecord.premiumvanish;

import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import com.github.sirblobman.api.bungeecord.configuration.ConfigurablePlugin;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;

import de.myzelyam.api.vanish.BungeeVanishAPI;

public record PremiumVanishHook(Plugin plugin) implements IVanishHook {
    @Override
    public boolean isDisabled() {
        Plugin plugin = plugin();
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

    @Override
    public void setHidden(ProxiedPlayer player, boolean hidden) {
        if (hidden) {
            BungeeVanishAPI.hidePlayer(player);
        } else {
            BungeeVanishAPI.showPlayer(player);
        }
    }

    @Override
    public void setHidden(UUID playerId, boolean hidden) {
        Plugin plugin = plugin();
        ProxyServer proxy = plugin.getProxy();
        ProxiedPlayer player = proxy.getPlayer(playerId);
        setHidden(player, hidden);
    }

    @Override
    public boolean hasListener() {
        return true;
    }

    @Override
    public void registerListener() {
        Plugin plugin = plugin();
        if (!(plugin instanceof ConfigurablePlugin configurablePlugin)) {
            return;
        }

        ProxyServer proxy = plugin.getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        pluginManager.registerListener(plugin, new PremiumVanishListener(configurablePlugin));
    }
}
